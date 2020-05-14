package com.example.paseasistencia.ui.configuracion;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Configuracion;

public class ConfiguracionViewModel extends ViewModel {
    private MutableLiveData<Configuracion> mConfiguracion;

    public ConfiguracionViewModel() {
        mConfiguracion = new MutableLiveData<>();
    }

    public void setLiveDate(Context c){
        mConfiguracion.setValue(Controlador.getInstance(c).getConfiguracion());
    }

    public LiveData<Configuracion> getText() {
        return mConfiguracion;
    }
}
