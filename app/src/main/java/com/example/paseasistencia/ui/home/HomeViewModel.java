package com.example.paseasistencia.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.Vehicle;

import java.util.ArrayList;
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

        loadAllVehicles();
        return mCuadrillasData;
    }


    private void loadAllVehicles() {
        if(controlador.validarSesion() == Controlador.STATUS_SESION.SESION_ACTIVA)
            mCuadrillasData.postValue(this.controlador.getCuadrillas());
    }

}