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
import com.example.paseasistencia.model.Mallas;

import java.util.ArrayList;
import java.util.List;

public class ActividadesResalizadasAdapter extends ArrayAdapter<ListaActividades> {
    private List<ListaActividades> listaMallas;
    private List<ActividadesRealizadas> actividadesRealizadas;
    private Context context;
    private Controlador controlador;

    public ActividadesResalizadasAdapter(@NonNull Context context, List<ListaActividades> listaMallas, List<ActividadesRealizadas> actividadesRealizadas) {
        super(context, R.layout.item_actividades_mallas, listaMallas);
        this.listaMallas = listaMallas;
        this.actividadesRealizadas = actividadesRealizadas;
        this.controlador = Controlador.getInstance(context);
        this.context = context;

        actualizarRegistros();
    }

    public List<ActividadesRealizadas> getActividadesRealizadas() {
        actividadesRealizadas = new ArrayList<>();
        for (ListaActividades la : listaMallas) {
            for (ActividadesRealizadas ar : la.getListaActividadesRealizadas()) {
                actividadesRealizadas.add(ar);
            }
        }

        return this.actividadesRealizadas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_actividades_mallas,parent,false);
        TextView tv_actividad = convertView.findViewById(R.id.tv_actividad);
        TextView tv_tipoActividad = convertView.findViewById(R.id.tv_tipo_actividad);
        TextView tv_mallas = convertView.findViewById(R.id.tv_mallas);

        ListaActividades listaActividades = listaMallas.get(position);
        tv_actividad.setText(listaActividades.getActividad().getNombre());
        tv_tipoActividad.setText(Controlador.TIPOS_ACTIVIDADES[listaActividades.getTipoActividad()]);
        tv_mallas.setText(listaActividades.getMallas());

        return convertView;
    }


    public int add(Integer cuadrilla, Actividades actividad, Integer tipoActividad, String sector, ArrayList<ActividadesRealizadas> lar, ListaActividades listaActividadesAnterior) {
        Integer index = -1;

        if (listaActividadesAnterior != null) {
            index = getIndex(actividad.getNombre(), tipoActividad, sector);
            if (index == -1) {
                index = getIndex(listaActividadesAnterior.getActividad().getNombre(), listaActividadesAnterior.getTipoActividad(), listaActividadesAnterior.getSector());
            } else {
                index = -2;
            }
        } else {

            index = getIndex(actividad.getNombre(), tipoActividad, sector);
        }

        //actualizar mallas

        ListaActividades la = null;
        if (index == -1) {
            la = new ListaActividades(cuadrilla, actividad, tipoActividad, sector, lar);
            this.add(la);
        } else if (index > -1) {
            la = listaMallas.get(index);
            la.setActividad(actividad);
            la.setTipoActividad(tipoActividad);
            la.setSector(sector);
            la.setListaActividadesRealizadas(lar);

            if (listaActividadesAnterior != null) {
                int len = actividadesRealizadas.size();
                int i = 0;
                while (i < len) {
                    ActividadesRealizadas ar = actividadesRealizadas.get(i);
                    if (ar.getActividad().getId() != listaActividadesAnterior.getActividad().getId() || ar.getTipoActividad() != listaActividadesAnterior.getTipoActividad() || !ar.getSector().equals(listaActividadesAnterior.getSector())) {
                        actividadesRealizadas.remove(ar);
                        len--;
                    } else {
                        i++;
                    }
                }

                actualizarRegistros();
            }
            this.notifyDataSetChanged();
        }

        return index;
    }

    @Override
    public void add(@Nullable ListaActividades ar) {
        super.add(ar);
        super.notifyDataSetChanged();
        actualizarRegistros();
    }

    private void actualizarRegistros(){

        Log.v("actividades", actividadesRealizadas.size() + "");

        for (ActividadesRealizadas ar : actividadesRealizadas) {
            //ActividadesRealizadas ar = actividadesRealizadas.get(i);
            Integer index = getIndex(ar.getActividad().getNombre(), ar.getTipoActividad(), ar.getSector());

            if (index != -1) {
                //actualiza la lista de las mallas
                Log.v("actividades", "actualiza la lista de las mallas");
                ListaActividades lar = listaMallas.get(index);
                lar.addActividadRealizada(ar);
                //break;
            }else{
                //insertar nueva lista de malla
                Log.v("actividades", "insertar nueva lista de malla");
                ArrayList<ActividadesRealizadas> lar = new ArrayList<>();
                lar.add(ar);
                ListaActividades la = new ListaActividades(ar.getCuadrlla(), ar.getActividad(), ar.getTipoActividad(), ar.getSector(), lar);
                listaMallas.add(la);
            }
        }
    }

    public List<Mallas> getMallasList(String actividad, Integer tipoActividad, String sector) {

        int index = getIndex(actividad, tipoActividad, sector);
        if (index == -1) {
            return new ArrayList<Mallas>();
        } else {
            ArrayList<Mallas> listaMallas = this.listaMallas.get(index).getListaMallas();
            return listaMallas;
        }


    }

    private Integer getIndex(String actividad, Integer tipoActividad, String sector) {
        for (int i = 0; i < listaMallas.size(); i++) {

            if (listaMallas.get(i).getActividad().getNombre().equals(actividad)
                    && listaMallas.get(i).getTipoActividad() == tipoActividad
                    && listaMallas.get(i).getSector().equals(sector)) {
                return i;
            }
        }

        return -1;
    }

}
