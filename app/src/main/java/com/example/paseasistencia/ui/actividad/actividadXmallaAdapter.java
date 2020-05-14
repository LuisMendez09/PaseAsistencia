package com.example.paseasistencia.ui.actividad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Mallas;

import java.util.HashMap;
import java.util.List;

public class actividadXmallaAdapter extends ArrayAdapter<Mallas> {

    private List<Mallas> mallasSeleccionadas;
    //private HashMap<String,String> totalActividades = new HashMap<>();
    private List<String> sectores;
    private HashMap<String,List<Mallas>> mallas = new HashMap<>();
    private Context context;
    private Controlador controlador;

    public actividadXmallaAdapter(@NonNull Context context, List<Mallas> mallasActivas) {
        super(context, R.layout.item_mallas,mallasActivas);
        controlador = Controlador.getInstance(context);
        this.mallasSeleccionadas = mallasActivas;
        this.context = context;

        sectores = controlador.getSectores();
        for (String s : sectores) {
            mallas.put(s, controlador.getMallas(s));
        }
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_mallas,parent,false);
        final Spinner spSector = convertView.findViewById(R.id.sp_sector);
        final Spinner spMalla = convertView.findViewById(R.id.sp_malla);


        final Mallas m = this.mallasSeleccionadas.get(position);

        ArrayAdapter<String> sectoresAdapter = new ArrayAdapter<>(this.getContext(),R.layout.support_simple_spinner_dropdown_item,this.sectores);
        spSector.setAdapter(sectoresAdapter);
        spSector.setSelection(Complementos.getIndex(spSector,m.getSector()));

        spSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<Mallas> mallasAdapter = new ArrayAdapter<>(actividadXmallaAdapter.this.getContext(),R.layout.support_simple_spinner_dropdown_item,mallas.get(spSector.getSelectedItem()));
                spMalla.setAdapter(mallasAdapter);
                spMalla.setSelection(Complementos.getIndex(spMalla,m.getMallas()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMalla.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                Mallas mallaNueva = (Mallas) spMalla.getSelectedItem();

                m.setId(mallaNueva.getId());
                m.setSector(mallaNueva.getSector());
                m.setMallas(mallaNueva.getMallas());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }

    @Override
    public void add(@Nullable Mallas object) {
        super.add(object);
        //notifyDataSetChanged();

    }

    public List<Mallas> getMallasSeleccionadas(){
        return mallasSeleccionadas;
    }
}
