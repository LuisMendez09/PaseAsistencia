package com.example.paseasistencia.ui.detail;

import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.ListaAsistencia;

public interface IDetallesAsistencia {
    //public void editarnombre(ListaAsistencia asistencia);
    //public void editarPuesto(ListaAsistencia asistencia);
    //public void agregarPuesto(ListaAsistencia asistencia);
    public void actualziarAsistencia(String totalAsistencia);
    public void menuSeleccion(ListaAsistencia asistencia,int fila);
}
