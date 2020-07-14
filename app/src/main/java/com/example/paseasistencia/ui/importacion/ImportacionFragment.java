package com.example.paseasistencia.ui.importacion;

import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.controlador.ImportarCatalogos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.IactualizacionDatos;
import com.example.paseasistencia.ui.configuracion.ConfiguracionFragment;

public class ImportacionFragment extends Fragment implements IactualizacionDatos {
    private static final String TAG = "";

    private ImportacionViewModel importacionViewModel;
    private TextView tvMensajes;
    private ProgressBar progressBar;
    private Button btnActualizar;
    private Button btnActualziarTrabajadores;
    private Controlador controlador = null;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        FileLog.i(TAG, "iniciar ImportacionFragment");

        controlador = Controlador.getInstance(this.getContext());
        View root = inflater.inflate(R.layout.fragment_importacion, container, false);

        importacionViewModel = ViewModelProviders.of(this).get(ImportacionViewModel.class);
        tvMensajes = root.findViewById(R.id.tv_mensajes);
        btnActualizar = (Button) root.findViewById(R.id.btn_guardar);
        btnActualziarTrabajadores = (Button) root.findViewById(R.id.btn_actualizarCatalogoTrabajadores);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.i(TAG, "inicia la importacion de catalogos");
                new ImportarCatalogos(ImportacionFragment.this, controlador, ImportarCatalogos.TIPO_CATALOGO.OTROS).execute();
            }
        });

        btnActualziarTrabajadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.i(TAG, "inicia la importacion de catalogos de trabajadores");
                ImportarCatalogos importarCatalogos = new ImportarCatalogos(ImportacionFragment.this, controlador, ImportarCatalogos.TIPO_CATALOGO.TRABAJADORES);
                importarCatalogos.execute();
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!controlador.configuracionValida()) {
            Navigation.findNavController(view).navigate(R.id.nav_configuracion);
        }
    }

    public void setLoadingAnimation(){
        progressBar.setVisibility(View.VISIBLE);
        btnActualizar.setEnabled(false);
        btnActualziarTrabajadores.setEnabled(false);
        tvMensajes.setText("");
    }

    public void unSetLoadingAnimation(){
        progressBar.setVisibility(View.GONE);
        btnActualizar.setEnabled(true);
        btnActualziarTrabajadores.setEnabled(true);
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
    public void finalizarAnimacion(String mensaje) {
        unSetLoadingAnimation();
        if (mensaje.equals(Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores))) {
            NavHostFragment.findNavController(ImportacionFragment.this).popBackStack(R.id.nav_homeFragmen, false);
        }

    }

    @Override
    public void incrementar(Integer incremento) {
        NavHostFragment.findNavController(ImportacionFragment.this).navigate(R.id.nav_configuracion);
    }
}
