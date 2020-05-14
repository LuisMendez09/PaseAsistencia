package com.example.paseasistencia.ui.actividad;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;

import java.util.List;

public class ActividadesViewModel extends AndroidViewModel {
    private MutableLiveData<Cuadrillas> selectedCuadrilla = new MutableLiveData<>();
    private MutableLiveData<ListaAsistencia[]> trabajadores = new MutableLiveData<>();
    private String totalAsistencia;
    private Controlador controlador;

    public ActividadesViewModel(@NonNull Application application, Cuadrillas cuadrillas,ListaAsistencia [] listaTrabajadores,String totalAsistencia) {
        super(application);
        selectedCuadrilla.postValue(cuadrillas);
        trabajadores.postValue(listaTrabajadores);
        this.totalAsistencia = totalAsistencia;

        Context c = Controlador.getCONTEXT();
        if(c==null)
            c = application.getBaseContext();

        this.controlador = Controlador.getInstance(c);

    }


    public Controlador.STATUS_SESION getStatusSesion(){
        return this.controlador.validarSesion();
    }
    public MutableLiveData<Cuadrillas> getSelectedCuadrilla(){
        return selectedCuadrilla;
    }
    public MutableLiveData<ListaAsistencia[]> getSelectedTrabajadores(){
        return trabajadores;
    }

    public String getTotalAsistencia() {
        return totalAsistencia;
    }
}
