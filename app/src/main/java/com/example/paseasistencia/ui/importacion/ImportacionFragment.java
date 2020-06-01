package com.example.paseasistencia.ui.importacion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.BuscarListaActividades;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.IactualizacionDatos;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.Puestos;

import java.util.List;

public class ImportacionFragment extends Fragment implements IactualizacionDatos {

    private ImportacionViewModel importacionViewModel;
    private TextView tvMensajes;
    private ProgressBar progressBar;
    private Button btnActualziarl;
    private Controlador controlador = null;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        importacionViewModel =  ViewModelProviders.of(this).get(ImportacionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_importacion, container, false);
        tvMensajes = root.findViewById(R.id.tv_mensajes);
        btnActualziarl = (Button) root.findViewById(R.id.btn_guardar);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        controlador = Controlador.getInstance(this.getContext());
        /*importacionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        btnActualziarl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BuscarListaActividades(ImportacionFragment.this,controlador).execute();
            }
        });

        return root;
    }

    public void setLoadingAnimation(){
        progressBar.setVisibility(View.VISIBLE);
        btnActualziarl.setEnabled(false);
        tvMensajes.setText("");
    }

    public void unSetLoadingAnimation(){
        progressBar.setVisibility(View.GONE);
        btnActualziarl.setEnabled(true);
       // tvMensajes.setText("");
    }

    @Override
    public void actualizacionMensajes(String mensaje) {
        tvMensajes.setText(mensaje);
    }

    @Override
    public void actualizacionMensajesEnvio(Object value, Controlador.STATUS_CONEXION status_conexion) {

    }

    @Override
    public void iniciarAnimacion(Integer min, Integer max) {
        setLoadingAnimation();
    }

    @Override
    public void finalizarAnimacion() {
        unSetLoadingAnimation();
    }

    @Override
    public void incrementar(Integer incremento) {

    }
}
