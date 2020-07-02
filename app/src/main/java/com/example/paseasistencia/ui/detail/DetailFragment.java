package com.example.paseasistencia.ui.detail;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.paseasistencia.MainActivity;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Puestos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

/**action_nav_home_to_vehicleDetailFragment
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements IDetallesAsistencia{
    private DetailFragmentViewModel viewModel;
    private DetailAdapter mDetailAdapter;
    private TextView tvAsistencia;
    private ListView lvTrabajadores;


    private String items[] = null;
    private Cuadrillas cuadrillas;
    private static final String TAG = "DetailFragment";
    private static final String PUESTO_BASE = "PERSONAL CAMPO";

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FileLog.i(TAG, "iniciar DetailFragment");
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);

        final TextView numCuadrillaTextView = view.findViewById(R.id.detail_cuadrilla_name_text_view);
        final TextView mayordomoTextView = view.findViewById(R.id.trabajador);
        lvTrabajadores = view.findViewById(R.id.lv_lista_cuadrilla);
        tvAsistencia = view.findViewById(R.id.tv_asistencia);
        Button btnAguardar = view.findViewById(R.id.btn_guardar);
        Button btnCapturaAsistencia = view.findViewById(R.id.btn_paseLista);

        Drawable rightDrawable = null;
        Drawable leftDrawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rightDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_arrow_forward_ios_24px);
            leftDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_list_alt_24px);
        } else {
            rightDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_forward_ios_24px, null);
            leftDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_list_alt_24px);
        }

        btnAguardar.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        btnCapturaAsistencia.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);

        Application application = Objects.requireNonNull(getActivity()).getApplication();

        cuadrillas = DetailFragmentArgs.fromBundle(Objects.requireNonNull(getArguments())).getDetailFragmentArgs();

        DetailFragmentViewModelFactory factory = new DetailFragmentViewModelFactory(application,cuadrillas);
        viewModel = ViewModelProviders.of(this,factory).get(DetailFragmentViewModel.class);

        viewModel.getSelectedCuadrilla().observe(getViewLifecycleOwner(), new Observer<Cuadrillas>() {
            @Override
            public void onChanged(Cuadrillas cuadrillas) {
                numCuadrillaTextView.setText(cuadrillas.getCuadrilla().toString());
                mayordomoTextView.setText(cuadrillas.getMayordomo());

                mDetailAdapter = new DetailAdapter(getContext(), viewModel.getSelectedTrabajadores(),DetailFragment.this);
                lvTrabajadores.setAdapter(mDetailAdapter);

            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (viewModel.getStatusSesion()==Controlador.STATUS_SESION.SESION_ACTIVA)
                        agregarTrabajador();
            }
        });

        btnAguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.getStatusSesion()==Controlador.STATUS_SESION.SESION_ACTIVA)
                    guardarCambios(v);
            }
        });

        btnCapturaAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogCapturaAsistencia(mDetailAdapter).show(getActivity().getSupportFragmentManager(), "capturaAsistencia");
            }
        });

        return view;
    }

    private void guardarCambios(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Esta seguro de guardar los cambios")
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar

                        if(mDetailAdapter.getTrabajadores().size()>0){
                            FileLog.i(TAG, "avanzar a ActividadesFragment");
                            cuadrillas.setMayordomo(mDetailAdapter.getResponsable());
                            ListaAsistencia arrray [] = new ListaAsistencia[mDetailAdapter.getTrabajadores().size()];
                            ListaAsistencia[] listaAsistencias = mDetailAdapter.getTrabajadores().toArray(arrray);
                            Navigation.findNavController(view).navigate(DetailFragmentDirections.actionDetailFragmentToActividadesFragment(cuadrillas,listaAsistencias,mDetailAdapter.getTotalAsistencia()));

                        }else{
                            Snackbar.make(view, "sin trabajadores", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    @Override
    public void actualziarAsistencia(String totalAsistencia) {
        tvAsistencia.setText(totalAsistencia);
    }

    @Override
    public void menuSeleccion(final ListaAsistencia asistencia, final int fila) {

        AlertDialog.Builder menu = new AlertDialog.Builder(this.getContext());
        ListView itemsMenu = new ListView(this.getContext());

        if(asistencia.getAsistencia().getDateFin().getTime()!=new Date(0).getTime())
            items = new String[]{"Editar trabajador","Editar puesto"};
        else
            items = new String[]{"Editar trabajador","Editar puesto","Agregar puesto"};


        ArrayAdapter<String> adapterMenu = new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,items);
        itemsMenu.setAdapter(adapterMenu);
        menu.setView(itemsMenu);
        menu.setTitle("Seleccione una opcion");

        final AlertDialog dialog = menu.create();
        dialog.show();


        itemsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(items[position]=="Editar trabajador"){
                FileLog.i(TAG, "editar nombre Trabajador");
               editarnombre(asistencia);
               dialog.dismiss();
            }else if (items[position]=="Editar puesto"){
                FileLog.i(TAG, "editar hora inicio y final puesto");
                editarPuesto(asistencia,fila);
                dialog.dismiss();
            }else{
                FileLog.i(TAG, "agregar un nuevo puesto");
                agregarPuesto(asistencia);
                dialog.dismiss();
            }
            }
        });
    }

    private void editarnombre(final ListaAsistencia asistencia) {
        FileLog.v(TAG, "inicia edicion del nombre del trabajador");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_edicion_trabajador, null);
        final TextView nombre = viewDialog.findViewById(R.id.et_nombre);
        nombre.setText(asistencia.getTrabajadores().getNombre());

        builder.setView(viewDialog)
                .setTitle("Editando a " + asistencia.getTrabajadores().getNombre())
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar los cambios en el adapter
                        if (!nombre.getText().toString().equals("")) {
                            mDetailAdapter.actualizarTrabajador(asistencia.getTrabajadores().getNombre(), nombre.getText().toString());
                            asistencia.getTrabajadores().setNombre(nombre.getText().toString().toUpperCase());
                            asistencia.setEdicionTrabajador(1);

                            mDetailAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .create()
                .show();

    }

    private void editarPuesto(final ListaAsistencia a, final int fila) {
        FileLog.v(TAG, "inicia edicion de de puesto " + a.toString());
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_edicion_asistencia,null);
        final TextView horaInicio = viewDialog.findViewById(R.id.tv_horaInicio);
        final TextView horaFin = viewDialog.findViewById(R.id.tv_horaFin);

        horaInicio.setText(a.getAsistencia().getHoraInicio());
        horaFin.setText(a.getAsistencia().getHoraFinal());

        horaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getTimePiker(horaInicio,DetailFragment.this.getContext());
            }
        });

        horaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!horaFin.getText().equals(""))
                    MainActivity.getTimePiker(horaFin,DetailFragment.this.getContext());
            }
        });

        builder.setView(viewDialog)
                .setTitle("cambio de puesto a "+a.getTrabajadores().getNombre())
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar los cambios en el adapter
                        try {
                            Date ai = new Date(Complementos.convertirStringAlong(a.getAsistencia().getFecha(), horaInicio.getText().toString()));
                            Date af =  horaFin.getText().toString().equals("")?new Date(0):new Date(Complementos.convertirStringAlong(a.getAsistencia().getFecha(), horaFin.getText().toString()));

                            Asistencia editar = new Asistencia(a.getTrabajadores(),a.getAsistencia().getPuesto(),ai,af,0);

                            if(mDetailAdapter.validarHoras(editar,fila,cuadrillas)){
                                Log.i("edit","actualizar");
                                a.getAsistencia().setDateInicio(ai);
                                a.getAsistencia().setDateFin(af);
                                mDetailAdapter.notifyDataSetChanged();
                                FileLog.v(TAG, "aplicar cambio de edicion de puesto");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            FileLog.v(TAG, e.getMessage());
                        }
                    }
                })
                .create()
                .show();
    }

    private void agregarPuesto(final ListaAsistencia a) {
        FileLog.v(TAG, "agregar nuevo puesto " + a.toString());
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_puesto,null);
        final TextView hora = viewDialog.findViewById(R.id.tv_horaInicio);
        final Spinner spPuesto = viewDialog.findViewById(R.id.sp_puesto);

        ArrayAdapter<Puestos> puestosAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,Controlador.getInstance(this.getContext()).getPuestos());
        spPuesto.setAdapter(puestosAdapter);

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getTimePiker(hora,DetailFragment.this.getContext());
            }
        });

        builder.setView(viewDialog)
                .setTitle("cambio de puesto a "+a.getTrabajadores().getNombre())
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //guardar los cambios en el adapter

                        if(a.getAsistencia().getDateFin().getTime()==new Date(0).getTime()) {
                            try {
                                FileLog.v(TAG, "guardar los cambios");
                                Long time = Complementos.convertirStringAlong(Complementos.getDateActualToString(), hora.getText().toString());

                                Asistencia nuevaAsistencia = new Asistencia(a.getTrabajadores(),(Puestos) spPuesto.getSelectedItem(),new Date(time),new Date(0),0);

                                if(mDetailAdapter.validarHoras(nuevaAsistencia,-1,cuadrillas)){
                                    a.getAsistencia().setDateFin(new Date(time));
                                    mDetailAdapter.add(new ListaAsistencia(a.getTrabajadores(),nuevaAsistencia));
                                } else {
                                    FileLog.v(TAG, "horas no validas");
                                }

                            }catch (Exception e){
                                FileLog.v(TAG, "ERROR " + e.getMessage());
                            }

                        } else {
                            FileLog.v(TAG, "campos no validos");
                        }

                        mDetailAdapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
    }

    private void agregarTrabajador() {
        FileLog.v(TAG, "agregar nuevo trabajador");
        FileLog.v(TAG, "sadasdasdasd");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        View viewDialog = layoutInflater.inflate(R.layout.dialog_add_trabajador,null);
        final TextView tvNombre = viewDialog.findViewById(R.id.et_nombre);
        final TextView tvConsecutivo = viewDialog.findViewById(R.id.tv_consecutivo);
        final Spinner spPuesto = viewDialog.findViewById(R.id.sp_puesto);
        final TextView tvHoraInicio = viewDialog.findViewById(R.id.et_horaInicio);

        tvConsecutivo.setText(mDetailAdapter.getConsecutivo().toString());
        tvHoraInicio.setText(Complementos.obtenerHoraString(cuadrillas.getFechaInicio()));

        ArrayAdapter<Puestos> puestosAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,Controlador.getInstance(this.getContext()).getPuestos());
        spPuesto.setAdapter(puestosAdapter);
        spPuesto.setSelection(Complementos.getIndex(spPuesto, PUESTO_BASE));

        tvHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getTimePiker(tvHoraInicio,DetailFragment.this.getContext());
            }
        });

        builder.setView(viewDialog)
                .setTitle("agregar nuevo trabajador")
                .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //crear trabajador
                        FileLog.v(TAG, "guardar cambios");
                        mDetailAdapter.addTrabajador(cuadrillas,tvConsecutivo.getText().toString(),tvNombre.getText().toString(),(Puestos) spPuesto.getSelectedItem());
                    }
                })
                .create()
                .show();
    }


}
