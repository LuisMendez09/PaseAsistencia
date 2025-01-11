package com.example.paseasistencia.controlador;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImportarCatalogoTrabajadoresWork extends Worker {
    private NotificationManager notificationManager;
    public static final String PROGRESS = "PROGRESS";
    private int PROGRESS_MAX = 100;
    private int PROGRESS_CURRENT = 0;
    private NotificationCompat.Builder builder;
    private static final String TAG = "";

    public ImportarCatalogoTrabajadoresWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setProgressAsync(new Data.Builder().putString(PROGRESS, "Iniciando").build());
    }

    @NonNull
    @Override
    public Result doWork() {
        FileLog.i(TAG, "inicia servicio doWork");
        try {
            ForegroundInfo foregroundInfo = createForegroundInfo();

            actualizarNotificacion(getApplicationContext().getString(R.string.notification_iniciar));
            setForegroundAsync(foregroundInfo);
            String s = buscarTrabajadores();

            setProgressAsync(new Data.Builder().putString(PROGRESS, s).build());
            FileLog.i("importar", "fin servicio doWork");
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            FileLog.i(TAG, e.getMessage());
        }
        return Result.success();
    }


    private void actualizarNotificacion(String mensaje) {
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
        builder.setContentText(mensaje);
        notificationManager.notify(Integer.parseInt(getApplicationContext().getString(R.string.channel_id_trabajadores)), builder.build());
    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {

        Context context = getApplicationContext();
        String id = context.getString(R.string.channel_id_trabajadores);
        String title = context.getString(R.string.notification_title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(context);
        }

        builder = new NotificationCompat.Builder(context, id);

        builder.setContentTitle(title)
                .setTicker(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setOngoing(true);

        Notification notification = builder.build();

        return new ForegroundInfo(Integer.parseInt(id), notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(Context context) {
        // Create a Notification channel
        String id = context.getString(R.string.channel_id_trabajadores);
        CharSequence name = context.getString(R.string.notification_title);
        String description = context.getString(R.string.notification_title);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(channel);
    }

    private String buscarTrabajadores() {
        String respuesta = "";
        Controlador controlador = Controlador.getInstance(getApplicationContext());

        if (controlador.catalogosActualizados()) {
            String url = controlador.getConfiguracion().getUrl();
            if (!url.endsWith("/"))
                url = url + "/";
            url = url + "TRABAJADOREs?fechaInicioSem=" + Complementos.getDateActualToStringServidor();


            try {
                setProgressAsync(new Data.Builder().putString(PROGRESS, "BUSCANDO").build());
                actualizarNotificacion(getApplicationContext().getString(R.string.notification_conectaxion));

                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();

                JSONArray jsonArray = response.asJsonArray();

                if (jsonArray.length() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.v("servicio", Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (jsonArray.length() > 0) {
                    setProgressAsync(new Data.Builder().putString(PROGRESS, "GUARDANDO DATOS").build());

                    controlador.reiniciarListaTrabajadores();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Integer clave = (Integer) jsonObject.get("Clave");
                        Integer consecutivo = (Integer) jsonObject.get("Consecutivo");
                        String nombre = (String) jsonObject.get("Nombre");
                        Integer numero = (Integer) jsonObject.get("Numero");
                        String puesto = (String) jsonObject.get("Puesto");

                        controlador.setTrabajadores(clave, consecutivo, nombre, numero, puesto);

                        PROGRESS_CURRENT = (i * 100) / jsonArray.length();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_en_progreso));

                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores);
                } else {
                    respuesta = getApplicationContext().getString(R.string.msn_enesperado);
                    Log.v("servicio", Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }
            } catch (BridgeException e) {
                respuesta = getApplicationContext().getString(R.string.msn_sinConexion);
                Log.v("servicio", e.getMessage());
            } catch (JSONException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                Log.v("servicio", e.getMessage());
            }

            Log.v("servicio", "finalizar");
        } else {
            respuesta = "CATALOGOS SIN ACTUALIZAR";
            Log.d("servicio", "catalogos sin actualizar----***");
        }


        if (respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores))) {
            controlador.actualizarFechaActualziacion();
        }

        return respuesta;
    }

}
