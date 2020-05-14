package com.example.paseasistencia.ui.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.paseasistencia.model.Cuadrillas;

import java.util.List;


public class DetailFragmentViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplicacion;
    private Cuadrillas mCuadrillas;

    public DetailFragmentViewModelFactory(Application mApplicacion, Cuadrillas mcuadrilla) {
        this.mApplicacion = mApplicacion;
        this.mCuadrillas = mcuadrilla;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(DetailFragmentViewModel.class)){
            return (T) new DetailFragmentViewModel(mApplicacion, mCuadrillas);
        }

        throw new IllegalArgumentException("no se puede crear una instacia de esta clase");
    }
}
