package com.example.paseasistencia.ui.actividad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.Mallas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActividadesResalizadasAdapter extends ArrayAdapter<ActividadesRealizadas> {
    private List<ActividadesRealizadas> actividades;
    private List<ActividadesRealizadas> listaActividades = new ArrayList<>();
    private HashMap<String,List<Mallas>> mallasRealizadas = new HashMap<>();
    private HashMap<String,String> totalActividades = new HashMap<>();
    private Context context;
    private Controlador controlador;

    public ActividadesResalizadasAdapter(@NonNull Context context,List<ActividadesRealizadas> actividades) {
        super(context, R.layout.item_actividades_mallas,actividades);
        controlador = Controlador.getInstance(context);
        this.actividades = actividades;
        this.context = context;

        actualizarRegistros();
    }

    public List<ActividadesRealizadas> getActividades() {
        return listaActividades;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_actividades_mallas,parent,false);
        TextView tv_actividad = convertView.findViewById(R.id.tv_actividad);
        TextView tv_tipoActividad = convertView.findViewById(R.id.tv_tipo_actividad);
        TextView tv_mallas = convertView.findViewById(R.id.tv_mallas);


        ActividadesRealizadas actividadesRealizadas = actividades.get(position);
        tv_actividad.setText(actividadesRealizadas.getActividad().getNombre());
        tv_tipoActividad.setText(Controlador.TIPOS_ACTIVIDADES[actividadesRealizadas.getTipoActividad()]);
        tv_mallas.setText(totalActividades.get(actividadesRealizadas.getActividad().getNombre()));

        return convertView;
    }


    @Override
    public void  add(@Nullable ActividadesRealizadas object) {

        Boolean aBoolean = addActividadRealizada(object);
        if(aBoolean)
            super.add(object);
        else
            super.notifyDataSetChanged();
    }

    private Boolean addActividadRealizada(ActividadesRealizadas ar){
        this.listaActividades.add(ar);
        if(!totalActividades.containsKey(ar.getActividad().getNombre())){
            totalActividades.put(ar.getActividad().getNombre(),ar.getMalla().getId());
            return true;
        }else{
            String s = totalActividades.get(ar.getActividad().getNombre());
            totalActividades.put(ar.getActividad().getNombre(),s+", "+ar.getMalla().getId());
            return false;
        }
    }

    private void actualizarRegistros(){

        for (ActividadesRealizadas ar :actividades) {

            if(!totalActividades.containsKey(ar.getActividad().getNombre())){
                totalActividades.put(ar.getActividad().getNombre(),ar.getMalla().getId());
                Log.i("actividades","add");
            }else{

                String s = totalActividades.get(ar.getActividad().getNombre());
                totalActividades.put(ar.getActividad().getNombre(),s+", "+ar.getMalla().getId());
                Log.i("actividades","update");
                actividades.remove(ar);
            }

            if(!mallasRealizadas.containsKey(ar.getActividad().getNombre())){
                ArrayList<Mallas> m = new ArrayList<>();
                m.add(ar.getMalla());
                mallasRealizadas.put(ar.getActividad().getNombre(), m);
            }else{
                mallasRealizadas.get(ar.getActividad().getNombre()).add(ar.getMalla());
            }
        }
    }

    public List<Mallas> getMallasList(String actividad){
        if(actividad.equals(""))
            return new ArrayList<Mallas>();
        else
            return this.mallasRealizadas.get(actividad);
    }
}
