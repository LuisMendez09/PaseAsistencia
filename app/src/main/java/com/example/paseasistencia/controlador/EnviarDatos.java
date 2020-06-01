package com.example.paseasistencia.controlador;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.RequestBuilder;
import com.afollestad.bridge.Response;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Trabajadores;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class EnviarDatos extends AsyncTask<Void, Integer, Controlador.STATUS_CONEXION> {
    private IactualizacionDatos iactualizacionDatos;
    private Controlador controlador;
    private String servidor;

    public EnviarDatos(IactualizacionDatos iactualizacionDatos, Context context) {
        this.iactualizacionDatos = iactualizacionDatos;
        this.controlador = Controlador.getInstance(context);

        this.servidor = this.controlador.getConfiguracion().getUrl();
        if (!this.servidor.endsWith("/"))
            this.servidor = this.servidor + "/";
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("enviar", "Pre envio");

        Integer max = controlador.totalRegistrosPendientesPorEnviar();
        iactualizacionDatos.iniciarAnimacion(0, max);
        Log.i("enviar", max + "");
        iactualizacionDatos.actualizacionMensajesEnvio(max, Controlador.STATUS_CONEXION.INICIO_ENVIO);
    }

    @SuppressLint("WrongThread")
    @Override
    protected Controlador.STATUS_CONEXION doInBackground(Void... voids) {
        Controlador.STATUS_CONEXION status_conexion;
        Integer increment = 0;
        ArrayList<Trabajadores> trabajadoresPendientesPorEnviar = controlador.getTrabajadoresPendientesPorEnviar();
        ArrayList<ActividadesRealizadas> actividadesRealizadasPendientesPorEnviar = controlador.getActividadesRealizadasPendientesPorEnviar();
        ArrayList<Asistencia> asistenciasPendientesPorEnviar = controlador.getAsistenciasPendientesPorEnviar();
        ArrayList<Cuadrillas> cuadrillasPendientesPorEnviar = controlador.getCuadrillasPendientesPorEnviar();

        try {
            String url = this.servidor + "TRABAJADOREs";
            Log.i("enviar", url + " " + trabajadoresPendientesPorEnviar.size());
            for (Trabajadores t : trabajadoresPendientesPorEnviar) {
                status_conexion = peticionEnvio(url, t.toJson());
                Log.i("enviar", status_conexion.name());
                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    t.setSended(1);
                    controlador.updateTrabajador(t);
                    increment++;
                    onProgressUpdate(increment);
                } else {
                    return status_conexion;
                }
            }
            Log.i("enviar", "fin envio");

            url = this.servidor + "actividadesRealizadas_asistencia";
            Log.i("enviar", url + " " + actividadesRealizadasPendientesPorEnviar.size());
            for (ActividadesRealizadas ar : actividadesRealizadasPendientesPorEnviar) {
                status_conexion = peticionEnvio(url, ar.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    ar.setSended(1);
                    controlador.updateActividadesRealizadas(ar);

                    increment++;
                    onProgressUpdate(increment);
                } else {
                    return status_conexion;
                }
            }

            url = this.servidor + "asistencias";
            Log.i("enviar", url + " " + asistenciasPendientesPorEnviar.size());
            for (Asistencia a : asistenciasPendientesPorEnviar) {
                Log.i("enviar", a.toJson().toString());
                status_conexion = peticionEnvio(url, a.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    a.setSended(1);
                    controlador.updateAsistencias(a);

                    increment++;
                    onProgressUpdate(increment);
                } else {
                    return status_conexion;
                }
            }

            url = this.servidor + "asistenciaCuadrillas";
            Log.i("enviar", url + " " + cuadrillasPendientesPorEnviar.size());
            for (Cuadrillas c : cuadrillasPendientesPorEnviar) {
                Log.i("enviar", c.toJson().toString());
                status_conexion = peticionEnvio(url, c.toJson());

                if (status_conexion == Controlador.STATUS_CONEXION.ENVIO_EXITOSO || status_conexion == Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO) {
                    c.setSended(1);
                    controlador.updateCuadrilla(c);
                    increment++;
                    onProgressUpdate(increment);
                } else {
                    return status_conexion;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return Controlador.STATUS_CONEXION.ERROR_JSON;

        } catch (Exception e) {
            e.printStackTrace();
            return Controlador.STATUS_CONEXION.ERROR_INESPERADO;
        }
        return Controlador.STATUS_CONEXION.ENVIO_EXITOSO;
    }

    @Override
    protected void onPostExecute(Controlador.STATUS_CONEXION status_conexion) {
        super.onPostExecute(status_conexion);
        iactualizacionDatos.actualizacionMensajesEnvio(0, status_conexion);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        iactualizacionDatos.incrementar(values[0]);
    }

    public void prueba(String mensaje) {
        Log.i("enviar", mensaje);
    }

    public Controlador.STATUS_CONEXION peticionEnvio(String url, JSONObject json) {
        Controlador.STATUS_CONEXION r = null;
        Log.i("enviar", "inicia peticino" + json);

        Response response = null;

        try {
            Request request = Bridge.post(url).body(json).request();
            response = request.response();

            Log.i("enviar", "inicia peticino" + response.code());
            if (response.isSuccess()) {
                Log.i("enviar", "Operacion exitosa");
                r = Controlador.STATUS_CONEXION.ENVIO_EXITOSO;
            } else {
                if (response.code() == 409) {
                    Log.i("enviar", "ya existe el registro en la db ");
                    r = Controlador.STATUS_CONEXION.REGISTRO_DUPLICADO;
                } else {
                    Log.i("enviar", "error inesperado " + response.code());

                    r = Controlador.STATUS_CONEXION.ERROR_ENVIO;
                }
            }
        } catch (BridgeException e) {
            e.printStackTrace();
            Log.i("enviar", response.asString());
            r = Controlador.STATUS_CONEXION.ERROR_ENVIO;
        }


        return r;
    }

}
