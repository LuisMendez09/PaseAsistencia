package com.example.paseasistencia.ui.configuracion;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Configuracion;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class ConfiguracionFragment extends Fragment {
    private ConfiguracionViewModel configuracionViewModel;
    private Context mContext;
    private static final String TAG = "ConfiguracionFragment";

    public ConfiguracionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FileLog.i(TAG, "inciar ConfiguracionFragment");
        configuracionViewModel =  ViewModelProviders.of(this).get(ConfiguracionViewModel.class);
        configuracionViewModel.setLiveDate(this.getContext());
        mContext = this.getContext();
        View root = inflater.inflate(R.layout.fragment_configuracion, container, false);

        Button tbnGuardar = root.findViewById(R.id.btn_guardar);
        final TextView tvId = root.findViewById(R.id.idConfiguracion);
        final TextView textView = root.findViewById(R.id.textConfiguracion);
        configuracionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<Configuracion>() {
            @Override
            public void onChanged(Configuracion s) {
                if(s!= null){
                    textView.setText(s.getUrl());
                    tvId.setText(s.getId().toString());
                }
            }
        });

        tbnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.i(TAG, "guardar los cambio de configuracion");
                Boolean respuesta = Controlador.getInstance(mContext).setConfiguracion(textView.getText().toString(),tvId.getText().toString());
                if(respuesta){
                    NavHostFragment.findNavController(ConfiguracionFragment.this).popBackStack(R.id.nav_homeFragmen, false);
                }else{

                    Snackbar.make(v, "ERROR, CAMPO VACIO", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return root;
    }
}
