package com.example.paseasistencia.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paseasistencia.MainActivity;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.ui.finalizarCuadrilla.FinalizarCuadrillaModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private RecyclerView mRecyclerView;
    private CuadrillasAdapter mCuadrillasAdapter;
    private HomeViewModel viewModel;
    private Controlador controlador;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FileLog.i(TAG, "iniciar HomeFragment");
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        controlador = Controlador.getInstance(getContext());
        mRecyclerView = view.findViewById(R.id.cuadrillas_recycler_view);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //crear nueva cuadrilla
                crearNuevaCuadrilla(view);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (controlador.sesionNoFinalizada()) {
            if (controlador.getCuadrillasPendientesPorFinalizar() > 0) {
                Navigation.findNavController(view).navigate(R.id.nav_finalizarCuadrilla);
            } else {
                controlador.iniciarSession();
                inicializarListadoCuadrillas();
            }
        } else {
            controlador.iniciarSession();
            inicializarListadoCuadrillas();
        }

    }

    private void inicializarListadoCuadrillas() {
        FileLog.i(TAG, "inicializar lista de cuadrillas");
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.setControlador(Controlador.getInstance(this.getContext()));
        viewModel.getmCuadrillasData().observe(getViewLifecycleOwner(), new Observer<List<Cuadrillas>>() {
            @Override
            public void onChanged(List<Cuadrillas> cuadrillas) {

                mCuadrillasAdapter = new CuadrillasAdapter(getContext(), cuadrillas, new HomeListener());
                mRecyclerView.setAdapter(mCuadrillasAdapter);

            }
        });
    }

    private void crearNuevaCuadrilla(final View view){
        FileLog.i(TAG, "crear nueva cuadrilla");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_cuadrilla,null);
        final TextView cuadrilla = viewDialog.findViewById(R.id.tv_cuadrilla);
        final TextView horaInicio = viewDialog.findViewById(R.id.tv_horaInicio);

        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getTimePiker(horaInicio,HomeFragment.this.getContext());
            }
        });


        builder.setView(viewDialog)
                .setTitle("agregar cuadrilla")
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar los cambios en el adapter
                        if(viewModel.getControlador().validarNumeroCuadrilla(cuadrilla.getText().toString(),horaInicio.getText().toString())){
                            try {
                                FileLog.i(TAG, "continuar al detalle de la cuadrilla");
                                Date dateInicio = new Date(Complementos.convertirStringAlong(viewModel.getControlador().getSettings().getFecha(),horaInicio.getText().toString()));
                                Cuadrillas nuevaCuadrilla = new Cuadrillas(cuadrilla.getText().toString(), "", dateInicio, new Date(0), 0);
                                Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(nuevaCuadrilla));
                            } catch (ParseException e) {
                                e.printStackTrace();
                                FileLog.i(TAG, "Hora de inicio no valida " + e.getMessage());
                                Snackbar.make(view, "Hora de inicio no valida", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }else{
                            FileLog.i(TAG, "campos no validos");
                            Snackbar.make(view, "Valores no validos", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                })
                .create()
                .show();
    }

    private class HomeListener implements CuadrillasAdapter.HomeAdapterListener {

        @Override
        public void onCuadrillaSelected(final Cuadrillas cuadrillas, final View view) {
            // pending implementation
            FileLog.i(TAG, "Se selecciono la cuadrilla " + cuadrillas.getCuadrilla());
            if(Controlador.getInstance(HomeFragment.this.getContext()).validarSesion()==Controlador.STATUS_SESION.SESION_ACTIVA){
                if(!Controlador.getInstance(HomeFragment.this.getContext()).validarCuadrilla(cuadrillas.getCuadrilla())){
                    FileLog.i(TAG, "cuadrilla no inicializada");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(HomeFragment.this.getContext());

                    LayoutInflater layoutInflater =HomeFragment.this.getActivity().getLayoutInflater();
                    View viewDialog = layoutInflater.inflate(R.layout.dialog_add_hora_inicio_cuadrilla,null);
                    final TextView hora = viewDialog.findViewById(R.id.tv_horaInicio);

                    hora.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MainActivity.getTimePiker(hora, HomeFragment.this.getContext());
                        }
                    });

                    builder.setView(viewDialog)
                            .setTitle("Iniciar cuadrilla "+cuadrillas.getCuadrilla())
                            .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //guardar los cambios en el adapter

                                    if(!hora.getText().toString().equals("")){
                                        FileLog.i(TAG, "inicializar cuadrilla");
                                        try {
                                            Date dateInicio = new Date(Complementos.convertirStringAlong(viewModel.getControlador().getSettings().getFecha(), hora.getText().toString()));
                                            cuadrillas.setFechaInicio(dateInicio);
                                            cuadrillas.setFechaFin(new Date(0));
                                            Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(cuadrillas));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            FileLog.i(TAG, "error " + e.getMessage());
                                        }
                                    } else {
                                        FileLog.i(TAG, "campos no validos");
                                    }

                                }
                            })
                            .create()
                            .show();
                }else{
                    if (cuadrillas.getFechaFin().getTime() == Long.valueOf(0)) {
                        FileLog.i(TAG, "cuadrilla ya inicializada ir al detalle");
                        Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(cuadrillas));
                    } else {
                        FileLog.i(TAG, "cuadrilla finalizada");
                        Snackbar.make(view, "cuadrilla finalizada", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }else{
                FileLog.i(TAG, "sesion ya finalizada o no iniciada");
            }
        }


        @Override
        public void onDeleteCuadrilla(Cuadrillas cuadrillas) {
            //no implementar
        }
    }
}
