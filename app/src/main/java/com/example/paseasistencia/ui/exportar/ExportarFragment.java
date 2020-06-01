package com.example.paseasistencia.ui.exportar;

import android.os.Build;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.EnviarDatos;
import com.example.paseasistencia.controlador.IactualizacionDatos;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.ui.finalizarCuadrilla.FinalizarCuadrillaModel;

import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExportarFragment extends Fragment implements IactualizacionDatos {
    private FinalizarCuadrillaModel viewModel;
    private Button btnAceptar;
    private TextView tvMensaje;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exportar, container, false);

        btnAceptar = root.findViewById(R.id.btn_guardar);
        tvMensaje = root.findViewById(R.id.tv_mensajes);
        progressBar = root.findViewById(R.id.progressBar);

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("finalizar", "view create " + view.toString());

        viewModel = ViewModelProviders.of(requireActivity()).get(FinalizarCuadrillaModel.class);

        final NavController navController = Navigation.findNavController(view);

        viewModel.getmCuadrillasData(getContext()).observe(getViewLifecycleOwner(), new Observer<List<Cuadrillas>>() {
            @Override
            public void onChanged(List<Cuadrillas> cuadrillas) {
                Log.i("finalizar", "view create -----");
                if (cuadrillas.size() > 0) {
                    navController.navigate(R.id.nav_finalizarCuadrilla);
                }
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iniciar el envio
                EnviarDatos ed = new EnviarDatos(ExportarFragment.this, ExportarFragment.this.getContext());
                ed.execute();
            }
        });

        inicializarEnvio(0, 0);
    }

    private void inicializarEnvio(Integer min, Integer max) {
        tvMensaje.setText("");
        progressBar.setProgress(0);
        progressBar.setMax(max);
    }

    @Override
    public void actualizacionMensajes(String mensaje) {

    }

    @Override
    public void actualizacionMensajesEnvio(Object value, Controlador.STATUS_CONEXION status_conexion) {
        Integer i = (Integer) value;

        this.tvMensaje.setText(status_conexion.toString());
    }

    @Override
    public void incrementar(Integer incremento) {
        progressBar.setProgress(incremento);
    }

    @Override
    public void iniciarAnimacion(Integer min, Integer max) {
        inicializarEnvio(min, max);
    }

    @Override
    public void finalizarAnimacion() {
    }
}
