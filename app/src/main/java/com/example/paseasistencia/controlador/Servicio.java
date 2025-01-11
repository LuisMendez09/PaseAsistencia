package com.example.paseasistencia.controlador;

import android.os.Process;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.example.paseasistencia.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Servicio extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            buscarTrabajadores("http://192.100.1.13:8005/api/TRABAJADOREs?fechaInicioSem=2020-10-23");

            /*try {
                System.out.println("inicio servicio");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }*/


            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {

        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();


        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void buscarTrabajadores(String url) {
        String respuesta = "";
        Controlador controlador = Controlador.getInstance(getApplicationContext());
        try {
            Request request = Bridge.get(url).throwIfNotSuccess().request();
            Response response = request.response();

            JSONArray jsonArray = response.asJsonArray();
            //List<TiposPermisos>listaPermisos = response.asClassList(TiposPermisos.class);

            if (jsonArray.length() == 0) {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                Log.v("servicio", Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
            } else if (jsonArray.length() > 0) {
                controlador.reiniciarListaTrabajadores();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer clave = (Integer) jsonObject.get("Clave");
                    Integer consecutivo = (Integer) jsonObject.get("Consecutivo");
                    String nombre = (String) jsonObject.get("Nombre");
                    Integer numero = (Integer) jsonObject.get("Numero");
                    String puesto = (String) jsonObject.get("Puesto");

                    controlador.setTrabajadores(clave, consecutivo, nombre, numero, puesto);
                    Log.v("servicio", jsonObject.toString());
                }
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores);
            } else {
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                Log.v("servicio", Controlador.getCONTEXT().getString(R.string.msn_enesperado));
            }
        } catch (BridgeException e) {
            Log.v("servicio", e.getMessage());
        } catch (JSONException e) {
            Log.v("servicio", e.getMessage());
        }

    }
}
