package com.example.paseasistencia.ui.actividad;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Mallas;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActividadesFragment extends Fragment {
    ActividadesViewModel viewModel;
    Cuadrillas cuadrillas;
    ListaAsistencia listaAsistencia [];
    ActividadesResalizadasAdapter actividadesResalizadasAdapter;
    actividadXmallaAdapter mallasAdapter;


    public ActividadesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);
        final TextView tvCuadrilla = view.findViewById(R.id.tv_cuadrilla);
        final TextView tvMayordomo = view.findViewById(R.id.tv_mayordomo);
        final TextView tvAsistencia = view.findViewById(R.id.tv_asistencia);
        final ListView lvActividades = view.findViewById(R.id.lv_actividades);
        Button tbnGuardar = view.findViewById(R.id.btn_guardar);

        Application application = Objects.requireNonNull(getActivity()).getApplication();


        cuadrillas = ActividadesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getCuadrilla();
        listaAsistencia  = ActividadesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getTrabajadores();
        String asistencia = ActividadesFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getAsistencia();

        ActividadesFragmentViewModelFactory factory = new ActividadesFragmentViewModelFactory(application,cuadrillas,listaAsistencia,asistencia);
        viewModel = ViewModelProviders.of(this,factory).get(ActividadesViewModel.class);

        viewModel.getSelectedCuadrilla().observe(getViewLifecycleOwner(), new Observer<Cuadrillas>() {
            @Override
            public void onChanged(Cuadrillas cuadrillas) {

                tvCuadrilla.setText(cuadrillas.getCuadrilla().toString());
                tvMayordomo.setText(cuadrillas.getMayordomo());
                tvAsistencia.setText(viewModel.getTotalAsistencia());

                Controlador c = Controlador.getInstance(ActividadesFragment.this.getContext());
                actividadesResalizadasAdapter = new ActividadesResalizadasAdapter(ActividadesFragment.this.getContext(),c.getActividadesResalizadas(c.getSettings().getFecha(),cuadrillas));
                lvActividades.setAdapter(actividadesResalizadasAdapter);

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.getStatusSesion()== Controlador.STATUS_SESION.SESION_ACTIVA)
                    agregarActividad();
            }
        });

        tbnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRegistros();
            }
        });

        return view;
    }


    private void agregarActividad(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_actividad,null);

        final Spinner sp_actividad = viewDialog.findViewById(R.id.sp_actividad);
        final Spinner sp_tipoActividad = viewDialog.findViewById(R.id.sp_tipoActividad);
        Button btn_agregar = viewDialog.findViewById(R.id.btn_agregar);
        ListView lv_actividades = viewDialog.findViewById(R.id.lv_mallas);

        ArrayAdapter<Actividades> actividadesAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,Controlador.getInstance(this.getContext()).getActividades());
        sp_actividad.setAdapter(actividadesAdapter);


        ArrayAdapter<String> tipoActividadAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,Controlador.TIPOS_ACTIVIDADES);
        sp_tipoActividad.setAdapter(tipoActividadAdapter);

        mallasAdapter = new actividadXmallaAdapter(this.getContext(),actividadesResalizadasAdapter.getMallasList(""));
        lv_actividades.setAdapter(mallasAdapter);

        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sp_tipoActividad.getSelectedItemPosition()!=0){
                    Log.i("actividad","agregar sector y malla "+mallasAdapter.getMallasSeleccionadas().size());

                    mallasAdapter.add(new Mallas("0000","No Seleccionado","No Seleccionado"));
                    //mallasAdapter.notifyDataSetChanged();
                }else{
                    Snackbar.make(v, "tipo tarea no valido", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        builder.setView(viewDialog)
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar los cambios en el adapter
                        List<Mallas>  seleccion = mallasAdapter.getMallasSeleccionadas();

                        for(Mallas m : seleccion){
                            actividadesResalizadasAdapter.add(new ActividadesRealizadas(cuadrillas.getCuadrilla(),(Actividades) sp_actividad.getSelectedItem(),m,Controlador.getInstance(ActividadesFragment.this.getContext()).getSettings().getFecha(),sp_tipoActividad.getSelectedItemPosition(),0));
                        }
                        Log.i("guardar",seleccion.size()+"---"+actividadesResalizadasAdapter.getCount());
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

    private void guardarRegistros(){
        boolean b = false;
        if (actividadesResalizadasAdapter.getCount() > 0) {
            Controlador c = Controlador.getInstance(this.getContext());

            List<ListaAsistencia> t = Arrays.asList(listaAsistencia);
            b = c.setNuevasAsistencias(cuadrillas, t, Complementos.obtenerHoraString(cuadrillas.getFechaInicio()), cuadrillas.getFecha());


            if (b) {//ingresa las actividades realizadas
                ArrayList<ActividadesRealizadas> act = new ArrayList<>();
                for (ActividadesRealizadas ar : actividadesResalizadasAdapter.getActividades()) {

                    b = c.setActividadesRealizadas(ar);
                }
            }

            if (b)
                NavHostFragment.findNavController(ActividadesFragment.this).popBackStack(R.id.nav_homeFragmen, false);
        }

    }
}
