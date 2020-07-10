package com.example.paseasistencia.controlador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.MallasRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Trabajadores;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnviarDatos extends AsyncTask<Void, Integer, Controlador.STATUS_CONEXION> {
    private IactualizacionDatos iactualizacionDatos;
    private Controlador controlador;
    private String servidor;
    private Context context;
    private static final String TAG = "EnviarDatos";

    public EnviarDatos(IactualizacionDatos iactualizacionDatos, Context context) {
        this.iactualizacionDatos = iactualizacionDatos;
        this.controlador = Controlador.getInstance(context);
        this.context = context;

        this.servidor = this.controlador.getConfiguracion().getUrl();

        if (!this.servidor.endsWith("/"))
            this.servidor = this.servidor + "/";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        FileLog.i(TAG, "inicia el envio de informacion");

        Integer max = controlador.totalRegistrosPendientesPorEnviar();
        iactualizacionDatos.iniciarAnimacion(0, max);
        FileLog.i(TAG, max + " registros por enviar");
        iactualizacionDatos.actualizacionMensajesEnvio(max, Controlador.STATUS_CONEXION.INICIO_ENVIO);
    }

    @SuppressLint("WrongThread")
    @Override
    protected Controlador.STATUS_CONEXION doInBackground(Void... voids) {
        Controlador.STATUS_CONEXION status_conexion = Controlador.STATUS_CONEXION.ENVIO_EXITOSO;
        Integer increment = 0;
        ArrayList<Trabajadores> trabajadoresPendientesPorEnviar = controlador.getTrabajadoresPendientesPorEnviar();
        ArrayList<MallasRealizadas> mallasRealizadasPendientesPorEnviar = controlador.getActividadesRealizadasPendientesPorEnviar();
        ArrayList<Asistencia> asistenciasPendientesPorEnviar = controlador.getAsistenciasPendientesPorEnviar();
        ArrayList<Cuadrillas> cuadrillasPendientesPorEnviar = controlador.getCuadrillasPendientesPorEnviar();

        FileLog.i(TAG, "inicia el envio de trabajadores ");
        try {
            String url = this.servidor + "TRABAJADOREs";
            for (Trabajadores t : trabajadoresPendientesPorEnviar) {
                status_conexion = peticionEnvio(url, t.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    t.setSended(1);
                    controlador.updateTrabajador(t);
                    increment++;
                    onProgressUpdate(increment);
                } else {
                    FileLog.i(TAG, "finalizo el envio de trabajadores " + status_conexion.name());
                    return status_conexion;
                }
            }

            FileLog.i(TAG, "inicia el envio de actividades realizadas");
            url = this.servidor + "actividadesRealizadas_asistencia";
            for (MallasRealizadas ar : mallasRealizadasPendientesPorEnviar) {

                status_conexion = peticionEnvio(url, ar.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    ar.setSended(1);
                    controlador.updateActividadesRealizadas(ar);

                    increment++;
                    onProgressUpdate(increment);
                } else {
                    FileLog.i(TAG, "termino el envio de actividades realizadas " + status_conexion);
                    return status_conexion;
                }
            }

            FileLog.i(TAG, "inicia el envio de asistencia ");
            url = this.servidor + "asistencias";
            FileLog.i("enviar", url + " " + asistenciasPendientesPorEnviar.size());
            for (Asistencia a : asistenciasPendientesPorEnviar) {

                status_conexion = peticionEnvio(url, a.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    a.setSended(1);
                    controlador.updateAsistencias(a);

                    increment++;
                    onProgressUpdate(increment);
                } else {
                    FileLog.i(TAG, "termino el envio de actividades realizadas asistencia " + a.getId() + " status" + status_conexion);
                    return status_conexion;
                }
            }
            FileLog.i(TAG, "inicia el envio cuadrillas revisadas");
            url = this.servidor + "asistenciaCuadrillas";

            String mac = getMacAddress();
            Log.v("MacAddress", mac);
            for (Cuadrillas c : cuadrillasPendientesPorEnviar) {
                JSONObject jsonObject = c.toJson();
                jsonObject.put("capturado", mac);
                Log.v("MacAddress", jsonObject.toString());
                status_conexion = peticionEnvio(url, jsonObject);

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    c.setSended(1);
                    controlador.updateCuadrilla(c);
                    increment++;
                    onProgressUpdate(increment);
                } else {
                    FileLog.i(TAG, "finalizo el envio de cuadrillas revisadas cuadrilla" + c.getCuadrilla() + " status" + status_conexion);
                    return status_conexion;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            FileLog.i(TAG, "error " + Controlador.STATUS_CONEXION.ERROR_JSON + " mensaje " + e.getMessage());
            return Controlador.STATUS_CONEXION.ERROR_JSON;
        } catch (Exception e) {
            e.printStackTrace();
            FileLog.i(TAG, "error " + Controlador.STATUS_CONEXION.ERROR_INESPERADO + " mensaje " + e.getMessage());
            return Controlador.STATUS_CONEXION.ERROR_INESPERADO;
        }

        CompresorZip.comprimir(context, 0);

        return Controlador.STATUS_CONEXION.ENVIO_EXITOSO;
    }

    @Override
    protected void onPostExecute(Controlador.STATUS_CONEXION status_conexion) {
        super.onPostExecute(status_conexion);
        iactualizacionDatos.actualizacionMensajesEnvio(0, status_conexion);
        FileLog.i(TAG, "termino el envio de informacion");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        iactualizacionDatos.incrementar(values[0]);
    }

    public Controlador.STATUS_CONEXION peticionEnvio(String url, JSONObject json) {
        Controlador.STATUS_CONEXION r = null;
        Response response = null;

        try {
            Request request = Bridge.post(url).body(json).request();
            response = request.response();
            if (response.isSuccess()) {
                r = Controlador.STATUS_CONEXION.ENVIO_EXITOSO;
            } else {
                if (response.code() == 409) {
                    r = Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO;
                } else {
                    FileLog.i(TAG, "ERROR DE ENVIO " + json.toString());
                    r = Controlador.STATUS_CONEXION.ERROR_ENVIO;
                }
            }
        } catch (BridgeException e) {
            e.printStackTrace();
            FileLog.i(TAG, e.getMessage());
            r = Controlador.STATUS_CONEXION.ERROR_ENVIO;
        }
        return r;
    }

    private static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String s = Integer.toHexString(b & 0xFF);

                    if (s.length() == 1)
                        s = "0" + s;

                    res1.append(s + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }
        return "";
    }

}
