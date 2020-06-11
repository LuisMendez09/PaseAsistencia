package com.example.paseasistencia.ui.detail;

import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.ListaAsistencia;

public interface IDetallesAsistencia {
    public void actualziarAsistencia(String totalAsistencia);
    public void menuSeleccion(ListaAsistencia asistencia,int fila);
}
