package com.example.paseasistencia.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Cuadrillas>> mCuadrillasData;
    private Controlador controlador;


    public HomeViewModel() {
    }

    public void setControlador(Controlador controlador){
        this.controlador = controlador;
    }

    public Controlador getControlador() {
        return controlador;
    }

    MutableLiveData<List<Cuadrillas>> getmCuadrillasData(){
        if(mCuadrillasData == null){
            mCuadrillasData = new MutableLiveData<List<Cuadrillas>>();
        }

        loadAllCuadrillas();
        return mCuadrillasData;
    }

    private void loadAllCuadrillas() {
        String s = controlador.getSettings() == null ? "" : controlador.getSettings().toString();
        if(controlador.validarSesion() == Controlador.STATUS_SESION.SESION_ACTIVA)
            mCuadrillasData.postValue(this.controlador.getCuadrillas());
    }

}