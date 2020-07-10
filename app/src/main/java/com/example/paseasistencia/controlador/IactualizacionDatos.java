package com.example.paseasistencia.controlador;

import java.util.Objects;

public interface IactualizacionDatos {
    public void actualizacionMensajes(String mensaje);
    public void actualizacionMensajesEnvio(Object value, Controlador.STATUS_CONEXION status_conexion);

    public void incrementar(Integer incremento);

    public void iniciarAnimacion(Integer min, Integer max);

    public void finalizarAnimacion(String mensaje);
}
