package com.example.paseasistencia.ui.finalizarCuadrilla;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;

import java.util.List;

public class FinalizarCuadrillaModel extends ViewModel {
    private MutableLiveData<List<Cuadrillas>> mCuadrillasData;
    private Controlador controlador;

    public FinalizarCuadrillaModel() {
    }


    public MutableLiveData<List<Cuadrillas>> getmCuadrillasData(Context context) {
        if (mCuadrillasData == null) {
            mCuadrillasData = new MutableLiveData<List<Cuadrillas>>();
        }

        controlador = Controlador.getInstance(context);
        loadAllCuadrillasPendientes();
        return mCuadrillasData;
    }

    public Integer getcuadrillasPendientes() {
        return controlador.getCuadrillasPendientesPorFinalizar();
    }

    private void loadAllCuadrillasPendientes() {

        mCuadrillasData.postValue(this.controlador.getCuadrillasActivas());
    }

    public boolean finalizarCuadrilla(Cuadrillas cuadrilla, String hora) {
        if (!hora.equals("")) {
            controlador.finalizarCuadrilla(cuadrilla, hora);
            loadAllCuadrillasPendientes();
            return true;
        } else {
            return false;
        }
    }

    public boolean eliminarCuadrilla(Cuadrillas c) {
        controlador.deleteListaAsistencia(c);
        loadAllCuadrillasPendientes();
        return true;
    }
}