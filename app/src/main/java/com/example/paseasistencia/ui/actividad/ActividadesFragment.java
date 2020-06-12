package com.example.paseasistencia.ui.actividad;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActividadesFragment extends Fragment {
    private ActividadesViewModel viewModel;
    private Cuadrillas cuadrillas;
    private ListaAsistencia listaAsistencia[];
    private ActividadesResalizadasAdapter actividadesResalizadasAdapter;
    private static final String TAG = "ActividadesFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FileLog.i(TAG, "iniciar ActividadesFragment");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_actividades, container, false);
        final TextView tvCuadrilla = view.findViewById(R.id.tv_cuadrilla);
        final TextView tvMayordomo = view.findViewById(R.id.tv_mayordomo);
        final TextView tvAsistencia = view.findViewById(R.id.tv_asistencia);
        final ListView lvActividades = view.findViewById(R.id.lv_actividades);

        Button btnGuardar = view.findViewById(R.id.btn_guardar);
        Drawable rightDrawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rightDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_save_24px);
        } else {
            rightDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_save_24px, null);
        }
        btnGuardar.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRegistros();
            }
        });

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
                actividadesResalizadasAdapter = new ActividadesResalizadasAdapter(ActividadesFragment.this.getContext(), new ArrayList<ListaActividades>(), c.getActividadesResalizadas(c.getSettings().getFecha(), cuadrillas));
                lvActividades.setAdapter(actividadesResalizadasAdapter);
                lvActividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //editar
                        new DialogAddActividad(actividadesResalizadasAdapter/*,item.getActividad().getNombre(),item.getTipoActividad(),item.getSector(),ActividadesFragment.*/, position, ActividadesFragment.this.cuadrillas)
                                .show(getActivity().getSupportFragmentManager(), "nuevaActividad");
                    }
                });

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


        return view;
    }


    private void agregarActividad(){
        FileLog.i(TAG, "agregar nueva actividad");
        new DialogAddActividad(this.actividadesResalizadasAdapter, -1/*"",-1,""*/, this.cuadrillas).show(getActivity().getSupportFragmentManager(), "nuevaActividad");
    }

    private void guardarRegistros(){
        boolean b = false;
        if (actividadesResalizadasAdapter.getCount() > 0) {
            FileLog.i(TAG, "iniciar guardado de los cambio");
            Controlador c = Controlador.getInstance(this.getContext());

            List<ListaAsistencia> t = Arrays.asList(listaAsistencia);
            b = c.setNuevasAsistencias(cuadrillas, t, actividadesResalizadasAdapter.getActividadesRealizadas(), Complementos.obtenerHoraString(cuadrillas.getFechaInicio()), cuadrillas.getFecha());

            if (b)
                NavHostFragment.findNavController(ActividadesFragment.this).popBackStack(R.id.nav_homeFragmen, false);
        } else {
            FileLog.i(TAG, "sin actividades agregadas");
        }

    }
}
