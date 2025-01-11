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
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.TiposActividades;
import com.example.paseasistencia.model.TiposPermisos;

import java.util.List;

public class ImportarCatalogosWork extends Worker {
    private String servidor = "";
    private Controlador controlador = null;
    private static final String TAG = "ImportarCatalogosWork";

    private NotificationManager notificationManager;
    public static final String PROGRESS = "PROGRESS";
    private int PROGRESS_MAX = 100;
    private int PROGRESS_CURRENT = 0;
    private NotificationCompat.Builder builder;


    public ImportarCatalogosWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        setProgressAsync(new Data.Builder().putString(PROGRESS, "Iniciando").build());

        controlador = Controlador.getInstance(getApplicationContext());
        this.servidor = this.controlador.getConfiguracion().getUrl();
        if (!this.servidor.endsWith("/"))
            this.servidor = this.servidor + "/";
    }

    @NonNull
    @Override
    public Result doWork() {
        FileLog.i(TAG, "inicia servicio doWork");
        try {
            ForegroundInfo foregroundInfo = createForegroundInfo();

            actualizarNotificacion(getApplicationContext().getString(R.string.notification_iniciar));
            setForegroundAsync(foregroundInfo);
            String s = actualiza();

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
        notificationManager.notify(Integer.parseInt(getApplicationContext().getString(R.string.channel_id_catalogos)), builder.build());
    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {

        Context context = getApplicationContext();
        String id = context.getString(R.string.channel_id_catalogos);
        String title = context.getString(R.string.notification_title_catalogos);

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
        String id = context.getString(R.string.channel_id_catalogos);
        CharSequence name = context.getString(R.string.notification_title_catalogos);
        String description = context.getString(R.string.notification_title_catalogos);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        notificationManager.createNotificationChannel(channel);
    }

    private String actualiza() {
        String respuesta = "";

        setProgressAsync(new Data.Builder().putString(PROGRESS, "ACTUALIZANDO PUESTOS").build());
        respuesta = buscarListaPuestos();
        if (!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin)) || !respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))) {
            setProgressAsync(new Data.Builder().putString(PROGRESS, "ACTUALIZANDO ACTIVIDADES").build());
            respuesta = buscarListaActividades();
            if (!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin)) || !respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))) {
                setProgressAsync(new Data.Builder().putString(PROGRESS, "ACTUALIZANDO MALLAS").build());
                respuesta = buscarListaMallas();
                if (!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin)) || !respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))) {
                    setProgressAsync(new Data.Builder().putString(PROGRESS, "ACTUALIZANDO TIPOS DE ACTIVIDADES").build());
                    respuesta = buscarTiposActividades();
                }
            }
        }

        return respuesta;
    }


    private String buscarListaPuestos() {
        String respuesta = null;

        PROGRESS_CURRENT = 0;
        try {
            String url = this.servidor + "ListaPuestoes";
            FileLog.i(TAG, "Inicia peticion de puestos ");


            try {
                actualizarNotificacion(getApplicationContext().getString(R.string.notification_conectaxion));

                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                Log.v("puestos", response.asJsonArray().toString());
                List<Puestos> listaPuestos = response.asClassList(Puestos.class);

                if (listaPuestos.size() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (listaPuestos.size() > 0) {
                    controlador.reiniciarListaPuestos();
                    for (int i = 0; i < listaPuestos.size(); i++/*Puestos puesto : listaPuestos*/) {

                        controlador.setPuetos(/*puesto*/listaPuestos.get(i));
                        PROGRESS_CURRENT = (i * 100) / listaPuestos.size();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_puestos));
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                } else {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                FileLog.i(TAG, "termina peticion de puestos, Total puestos " + listaPuestos.size());
            } catch (BridgeException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                FileLog.i(TAG, "Error al conectar con el servidor " + e.getMessage());
            }
        } catch (RuntimeException ee) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, ee.getMessage());

        } catch (Exception e) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, e.getMessage());
        }

        return respuesta;
    }

    private String buscarListaActividades() {
        PROGRESS_CURRENT = 0;

        String respuesta = null;
        try {
            String url = this.servidor + "ACTIVIDADEs";

            FileLog.i(TAG, "Inicia peticion de Actividades ");

            try {
                actualizarNotificacion(getApplicationContext().getString(R.string.notification_conectaxion));
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<Actividades> listaActividades = response.asClassList(Actividades.class);

                if (listaActividades.size() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (listaActividades.size() > 0) {
                    controlador.reiniciarListaActividades();
                    for (/*Actividades actividad : listaActividades*/int i = 0; i < listaActividades.size(); i++) {
                        controlador.setActividad(/*actividad*/listaActividades.get(i));

                        PROGRESS_CURRENT = (i * 100) / listaActividades.size();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_Actividades));
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                } else {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                FileLog.i(TAG, "termina peticion de actividades, Total actividades " + listaActividades.size());
            } catch (BridgeException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                FileLog.i(TAG, "Error al conectar con el servidor " + e.getMessage());
            }
        } catch (RuntimeException ee) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, ee.getMessage());

        } catch (Exception e) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, e.getMessage());
        }

        return respuesta;
    }

    private String buscarListaMallas() {
        PROGRESS_CURRENT = 0;
        String respuesta = null;
        try {
            String url = this.servidor + "mallas";
            FileLog.i(TAG, "Inicia peticion de mallas ");

            try {
                actualizarNotificacion(getApplicationContext().getString(R.string.notification_conectaxion));
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<Mallas> listaMallas = response.asClassList(Mallas.class);

                if (listaMallas.size() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (listaMallas.size() > 0) {
                    controlador.reiniciarListaMallas();
                    for (/*Mallas mallas : listaMallas*/int i = 0; i < listaMallas.size(); i++) {
                        controlador.setMallas(/*mallas*/listaMallas.get(i));

                        PROGRESS_CURRENT = (i * 100) / listaMallas.size();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_mallas));
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                } else {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                FileLog.i(TAG, "termina peticion de mallas, Total mallas " + listaMallas.size());
            } catch (BridgeException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                FileLog.i(TAG, "Error al conectar con el servidor " + e.getMessage());
            }
        } catch (RuntimeException ee) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, ee.getMessage());

        } catch (Exception e) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, e.getMessage());
        }

        return respuesta;
    }

    private String buscarListaPermisos() {
        PROGRESS_CURRENT = 0;
        String respuesta = null;
        try {
            String url = this.servidor + "tiposPermisoes";
            FileLog.i(TAG, "Inicia peticion de tipos de permisos ");

            try {
                actualizarNotificacion(getApplicationContext().getString(R.string.notification_conectaxion));
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<TiposPermisos> listaPermisos = response.asClassList(TiposPermisos.class);

                if (listaPermisos.size() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (listaPermisos.size() > 0) {
                    controlador.reiniciarListaTiposPermisos();
                    for (/*TiposPermisos permisos : listaPermisos*/ int i = 0; i < listaPermisos.size(); i++) {
                        controlador.setTipoPermisos(/*permisos*/listaPermisos.get(i));

                        PROGRESS_CURRENT = (i * 100) / listaPermisos.size();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_permisos));
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                } else {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                FileLog.i(TAG, "termina peticion de permisos, Total permisos " + listaPermisos.size());
            } catch (BridgeException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                FileLog.i(TAG, "Error al conectar con el servidor " + e.getMessage());
            }
        } catch (RuntimeException ee) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, ee.getMessage());

        } catch (Exception e) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, e.getMessage());
        }

        return respuesta;
    }

    private String buscarTiposActividades() {
        PROGRESS_CURRENT = 0;
        String respuesta = null;
        try {
            String url = this.servidor + "tipos_actividad";

            FileLog.i(TAG, "Inicia peticion de tipos de actividad ");

            try {
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();

                List<TiposActividades> tiposActividades = response.asClassList(TiposActividades.class);

                if (tiposActividades.size() == 0) {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                } else if (tiposActividades.size() > 0) {
                    controlador.reiniciarTiposActividades();
                    for (/*TiposActividades ta : tiposActividades*/int i = 0; i < tiposActividades.size(); i++) {
                        controlador.setTiposActividad(/*ta*/tiposActividades.get(i));

                        PROGRESS_CURRENT = (i * 100) / tiposActividades.size();
                        actualizarNotificacion(getApplicationContext().getString(R.string.notification_tiposActividades));
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                } else {
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    FileLog.i(TAG, Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                FileLog.i(TAG, "termina peticion de puestos, Total tipos de actividades " + tiposActividades.size());
            } catch (BridgeException e) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                FileLog.i(TAG, "Error al conectar con el servidor " + e.getMessage());
            }
        } catch (RuntimeException ee) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, ee.getMessage() + "");

        } catch (Exception e) {
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            FileLog.i(TAG, e.getMessage());
        }

        return respuesta;
    }
}
