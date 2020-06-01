package com.example.paseasistencia.ui.exportar;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.paseasistencia.controlador.Controlador;

public class ExportarViewModel extends ViewModel {

    private MutableLiveData<Integer> totalCuadrillasPendientes = new MutableLiveData<>();
    //private Controlador controlador;
    //private Context context;


    public ExportarViewModel(/*@NonNull Application application*/) {
        /*super(application);

        Context c = application.getBaseContext();
        if(c==null)
            c = application.getBaseContext();

        Controlador controlador = Controlador.getInstance(this.context);
        totalCuadrillasPendientes.postValue(controlador.getCuadrillasPendientesPorFinalizar());
 */
    }

    public MutableLiveData<Integer> getTotalCuadrillasPendientes(Context context) {
        Controlador controlador = Controlador.getInstance(context);
        totalCuadrillasPendientes.postValue(controlador.getCuadrillasPendientesPorFinalizar());
        return totalCuadrillasPendientes;
    }
}
