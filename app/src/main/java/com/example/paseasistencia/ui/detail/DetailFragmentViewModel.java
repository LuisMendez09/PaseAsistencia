package com.example.paseasistencia.ui.detail;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;

import java.util.ArrayList;
import java.util.List;

public class DetailFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Cuadrillas> selectedCuadrilla = new MutableLiveData<>();
    private List<ListaAsistencia> selectedTrabajadores = new ArrayList<>();
    private Controlador controlador;


    public DetailFragmentViewModel(@NonNull Application application, Cuadrillas cuadrillas) {
        super(application);
        selectedCuadrilla.postValue(cuadrillas);
        Context c = Controlador.getCONTEXT();
        if(c==null)
            c = application.getBaseContext();

        this.controlador = Controlador.getInstance(c);

        selectedTrabajadores = controlador.getAsistencia(Complementos.obtenerFechaString(Complementos.getDateActual()), cuadrillas);
    }

    public Controlador.STATUS_SESION getStatusSesion(){
        return this.controlador.validarSesion();
    }
    public MutableLiveData<Cuadrillas> getSelectedCuadrilla(){
        return selectedCuadrilla;
    }
    public  List<ListaAsistencia> getSelectedTrabajadores(){
        return selectedTrabajadores;
    }


}
