package com.example.paseasistencia.ui.actividad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Mallas;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DialogAddActividad extends DialogFragment {

    private Controlador controlador;
    private Cuadrillas cuadrilla;
    private CharSequence[] items;
    private ActividadesResalizadasAdapter actividadesResalizadasAdapter;
    private List<Mallas> Catalogomallas = new ArrayList<>();
    private List<Mallas> mallasSeleccionadas = new ArrayList<>();
    private ListaActividades listaActividad;

    private Button btnSector;
    private TextView tvSector;
    private Button btnMallas;
    private TextView tvMallas;
    private Spinner sp_actividad;
    private Spinner sp_tipoActividad;

    public DialogAddActividad(ActividadesResalizadasAdapter actividadesResalizadasAdapter, Integer index/*String actividad,Integer tipoActiviad,String sector*/, Cuadrillas cuadrilla) {
        this.controlador = Controlador.getInstance(getContext());
        this.cuadrilla = cuadrilla;

        this.actividadesResalizadasAdapter = actividadesResalizadasAdapter;
        if (index != -1)
            this.listaActividad = this.actividadesResalizadasAdapter.getItem(index);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = this.getActivity().getLayoutInflater();
        final View viewDialog = layoutInflater.inflate(R.layout.dialog_seleccion_mallas, null);

        sp_actividad = viewDialog.findViewById(R.id.sp_actividad);
        sp_tipoActividad = viewDialog.findViewById(R.id.sp_tipoActividad);
        btnSector = viewDialog.findViewById(R.id.btn_sector);
        tvSector = viewDialog.findViewById(R.id.tv_sector);

        btnMallas = viewDialog.findViewById(R.id.btn_mallas);
        tvMallas = viewDialog.findViewById(R.id.tv_mallas);

        ArrayAdapter<Actividades> actividadesAdapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, Controlador.getInstance(this.getContext()).getActividades());
        sp_actividad.setAdapter(actividadesAdapter);


        ArrayAdapter<String> tipoActividadAdapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item, Controlador.TIPOS_ACTIVIDADES);
        sp_tipoActividad.setAdapter(tipoActividadAdapter);

        if (this.listaActividad != null) {
            this.sp_actividad.setSelection(Complementos.getIndex(sp_actividad, this.listaActividad.getActividad().getNombre()));
            this.sp_tipoActividad.setSelection(listaActividad.getTipoActividad());
            this.tvSector.setText(this.listaActividad.getSector());
            this.mallasSeleccionadas = this.actividadesResalizadasAdapter.getMallasList(this.listaActividad.getActividad().getNombre(), this.listaActividad.getTipoActividad(), this.listaActividad.getSector());
        }


        ArrayList<String> sectores = this.controlador.getSectores();
        this.items = sectores.toArray(new CharSequence[sectores.size()]);

        SeleccionSector();
        seleccionarMallas();

        builder.setView(viewDialog)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guardar(viewDialog);
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    private void guardar(View v) {
        ArrayList<ActividadesRealizadas> listaActividades = new ArrayList<>();
        if (sp_tipoActividad.getSelectedItemPosition() != 0 && !tvMallas.getText().equals("")) {
            for (Mallas m : mallasSeleccionadas) {
                listaActividades.add(new ActividadesRealizadas(Integer.parseInt(cuadrilla.getCuadrilla().toString())
                        , (Actividades) sp_actividad.getSelectedItem(), tvSector.getText().toString(), m, controlador.getSettings().getFecha(), sp_tipoActividad.getSelectedItemPosition(), 0));
            }
            int respuesta = actividadesResalizadasAdapter.add(cuadrilla.getCuadrilla(), (Actividades) sp_actividad.getSelectedItem(), sp_tipoActividad.getSelectedItemPosition(), tvSector.getText().toString(), listaActividades, listaActividad);

        }

    }

    private void SeleccionSector() {

        btnSector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Seleccione un sector")
                        .setSingleChoiceItems(items, getIndex(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                agregarSector(items[which].toString());
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

    }

    private void seleccionarMallas() {

        this.Catalogomallas = controlador.getMallas(tvSector.getText().toString());

        final CharSequence m[] = new CharSequence[this.Catalogomallas.size()];
        for (int i = 0; i < this.Catalogomallas.size(); i++) {
            m[i] = this.Catalogomallas.get(i).getMallas();
        }

        final boolean[] Checked = agregarMallas();

        btnMallas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Seleccione mallas")
                        .setMultiChoiceItems(m, Checked, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    mallasSeleccionadas.add(DialogAddActividad.this.Catalogomallas.get(which));
                                } else {
                                    remover(DialogAddActividad.this.Catalogomallas.get(which));
                                }

                                agregarMallas();
                            }
                        })
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        });
    }

    private void agregarSector(String sector) {
        if (!sector.equals(tvSector.getText().toString())) {
            tvSector.setText(sector);

            tvMallas.setText("");
            mallasSeleccionadas.clear();

            Catalogomallas.clear();
            Catalogomallas = controlador.getMallas(tvSector.getText().toString());
            seleccionarMallas();
        }
    }

    private boolean[] agregarMallas() {
        tvMallas.setText("");
        boolean[] seleccionadas = new boolean[Catalogomallas.size()];
        llenarArreglo(seleccionadas);

        for (int i = 0; i < mallasSeleccionadas.size(); i++) {

            tvMallas.setText(tvMallas.getText().toString() + ", " + mallasSeleccionadas.get(i).getMallas());

            for (int j = 0; j < Catalogomallas.size(); j++) {

                if (mallasSeleccionadas.get(i).getId().equals(Catalogomallas.get(j).getId())) {
                    seleccionadas[j] = true;
                    break;
                }
            }
        }
        return seleccionadas;
    }

    private void remover(Mallas m) {
        for (int i = 0; i < mallasSeleccionadas.size(); i++) {
            if (mallasSeleccionadas.get(i).getId().equals(m.getId())) {
                mallasSeleccionadas.remove(i);
            }
        }
    }

    private void llenarArreglo(boolean[] seleccionadas) {
        for (int i = 0; i < seleccionadas.length; i++) {
            seleccionadas[i] = false;
        }
    }

    private int getIndex() {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(tvSector.getText().toString())) {
                return i;
            }
        }

        return 0;
    }
}
