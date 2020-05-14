package com.example.paseasistencia.ui.actividad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.ui.detail.DetailFragmentViewModel;

import java.util.List;

public class ActividadesFragmentViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplicacion;
    private Cuadrillas mCuadrillas;
    private ListaAsistencia [] mlistaTrabajadores;
    private String mtotalAsistencia;

    public ActividadesFragmentViewModelFactory(Application mApplicacion, Cuadrillas cuadrillas, ListaAsistencia [] listaTrabajadores, String totalAsistencia) {
        this.mApplicacion = mApplicacion;
        this.mCuadrillas = cuadrillas;
        this.mtotalAsistencia = totalAsistencia;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ActividadesViewModel.class)){
            return (T) new ActividadesViewModel(mApplicacion, mCuadrillas,mlistaTrabajadores,mtotalAsistencia);
        }

        throw new IllegalArgumentException("no se puede crear una instacia de esta clase");
    }
}
