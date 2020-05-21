package com.example.paseasistencia.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paseasistencia.MainActivity;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.Trabajadores;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private HomeViewModel viewModel;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        mRecyclerView = view.findViewById(R.id.vehicle_recycler_view);


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.setControlador(Controlador.getInstance(this.getContext()));
        viewModel.getmCuadrillasData().observe(getViewLifecycleOwner(), new Observer<List<Cuadrillas>>() {
            @Override
            public void onChanged(List<Cuadrillas> cuadrillas) {
                mHomeAdapter = new HomeAdapter(getContext(),cuadrillas, new VehicleListener());
                mRecyclerView.setAdapter(mHomeAdapter);
            }
        });


    }

    private void crearNuevaCuadrilla(final View view){
        Log.i("inicio","agregar caudrilla");
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
                            Log.i("inicio","guardar cuadrilla");
                            try {
                                Date dateInicio = new Date(Complementos.convertirStringAlong(viewModel.getControlador().getSettings().getFecha(),horaInicio.getText().toString()));
                                Cuadrillas nuevaCuadrilla = new Cuadrillas(cuadrilla.getText().toString(),"",dateInicio,new Date(0));
                                Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(nuevaCuadrilla));
                            } catch (ParseException e) {
                                Log.i("inicio","error");
                                e.printStackTrace();
                                Snackbar.make(view, "Hora de inicio no valida", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        }else{
                            Log.i("inicio","error 1");
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

    private class VehicleListener implements HomeAdapter.VehicleAdapterListener {

        @Override
        public void onVehicleSelected(final Cuadrillas cuadrillas, final View view) {
            // pending implementation
            if(Controlador.getInstance(HomeFragment.this.getContext()).validarSesion()==Controlador.STATUS_SESION.SESION_ACTIVA){
                if(!Controlador.getInstance(HomeFragment.this.getContext()).validarCuadrilla(cuadrillas.getCuadrilla())){
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
                                        //ArrayList<Trabajadores> asistencia = Controlador.getInstance(HomeFragment.this.getContext()).getTrabajadoresXcuadrilla(cuadrillas.getCuadrilla());
                                        //if(Controlador.getInstance(HomeFragment.this.getContext()).setNuevaAsistencias(cuadrillas,asistencia,hora.getText().toString(),Complementos.getDateActualToString())){
                                        try {
                                            Date dateInicio = new Date(Complementos.convertirStringAlong(viewModel.getControlador().getSettings().getFecha(), hora.getText().toString()));
                                            cuadrillas.setFechaInicio(dateInicio);
                                            cuadrillas.setFechaFin(new Date(0));
                                            Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(cuadrillas));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        //}
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
                }else{
                    Log.i("inicio","con asistencia");
                    Navigation.findNavController(view).navigate(HomeFragmentDirections.actionNavHomeToVehicleDetailFragment(cuadrillas));
                }
            }else{
                Log.i("inicio","sesion ya finalizada o no iniciada");
            }
        }
    }
}
