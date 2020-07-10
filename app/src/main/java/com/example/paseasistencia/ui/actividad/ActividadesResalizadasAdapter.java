package com.example.paseasistencia.ui.actividad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaActividades;
import com.example.paseasistencia.model.MallasRealizadas;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.TiposActividades;

import java.util.ArrayList;
import java.util.List;

public class ActividadesResalizadasAdapter extends ArrayAdapter<ListaActividades> {
    private List<ListaActividades> listaMallas;
    private Context context;
    private Controlador controlador;

    public static final String TAG = "ActividadesResalizadasAdapter";
    public static final int NUEVO = 0;
    public static final int ACTUALIZACION = 1;
    public static final int DUPLICADO = 2;

    public ActividadesResalizadasAdapter(@NonNull Context context, List<ListaActividades> listaMallas, Cuadrillas cuadrillas) {
        super(context, R.layout.item_actividades_mallas, listaMallas);
        this.listaMallas = listaMallas;
        this.controlador = Controlador.getInstance(context);
        this.context = context;

        actualizarRegistros(controlador.getActividadesResalizadas(controlador.getSettings().getFecha(), cuadrillas));
    }

    private void actualizarRegistros(List<MallasRealizadas> mallasRealizadas) {
        FileLog.i(TAG, "inicializar lista de actividades con sus respectivas mallas");
        for (MallasRealizadas ar : mallasRealizadas) {
            Integer index = getIndex(ar.getActividad().getNombre(), ar.getTipoActividad(), ar.getSector());//index del arrayAdapter

            if (index != -1) {
                //actualiza la lista de las mallas
                ListaActividades lar = listaMallas.get(index);
                lar.addActividadRealizada(ar);
            } else {
                //insertar nueva lista de malla
                ArrayList<MallasRealizadas> lar = new ArrayList<>();
                lar.add(ar);
                ListaActividades la = new ListaActividades(ar.getCuadrlla(), ar.getActividad(), ar.getTipoActividad(), ar.getSector(), lar);
                listaMallas.add(la);
            }
        }
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
        tv_tipoActividad.setText(listaActividades.getTipoActividad().getDescripcion());
        tv_mallas.setText(listaActividades.getMallas());

        return convertView;
    }

    public void update(int posicion, ArrayList<MallasRealizadas> la, Actividades actividades, TiposActividades tipoActividad, String sector) {
        ListaActividades listaActividades = listaMallas.get(posicion);
        listaActividades.setActividad(actividades);
        listaActividades.setTipoActividad(tipoActividad);
        listaActividades.setSector(sector);
        listaActividades.setListaMallasRealizadas(la);
        this.notifyDataSetChanged();
    }

    public Integer add(Integer posicion, Integer cuadrilla, Actividades actividad, TiposActividades tipoActividad, String sector, ArrayList<MallasRealizadas> lar, ListaActividades listaActividadesAnterior) {
        Integer index = getIndex(actividad.getNombre(), tipoActividad, sector);

        if (listaActividadesAnterior == null) {
            if (index == -1) {
                FileLog.i(TAG, "agregar nueva actividad");
                ListaActividades la = new ListaActividades(cuadrilla, actividad, tipoActividad, sector, lar);
                add(la);
                return NUEVO;
            } else {
                FileLog.i(TAG, "registro duplicado");
                return DUPLICADO;
            }
        } else {
            if (index == posicion || index == -1) {
                FileLog.i(TAG, "actualizar registro");
                update(posicion, lar, actividad, tipoActividad, sector);
                return ACTUALIZACION;
            } else {
                FileLog.i(TAG, "registro duplicado");
                return DUPLICADO;
            }
        }
    }

    @Override
    public void add(@Nullable ListaActividades ar) {
        super.add(ar);
        super.notifyDataSetChanged();
    }

    public List<MallasRealizadas> getMallasRealizadas() {
        List<MallasRealizadas> mallasRealizadas = new ArrayList<>();
        for (ListaActividades la : listaMallas) {
            for (MallasRealizadas ar : la.getListaMallasRealizadas()) {
                mallasRealizadas.add(ar);
            }
        }

        return mallasRealizadas;
    }

    public List<Mallas> getMallasList(String actividad, TiposActividades tipoActividad, String sector) {
        int index = getIndex(actividad, tipoActividad, sector);
        if (index == -1) {
            return new ArrayList<Mallas>();
        } else {
            ArrayList<Mallas> listaMallas = this.listaMallas.get(index).getListaMallas();
            return listaMallas;
        }
    }

    private Integer getIndex(String actividad, TiposActividades tipoActividad, String sector) {
        for (int i = 0; i < listaMallas.size(); i++) {
            if (listaMallas.get(i).getActividad().getNombre().equals(actividad)
                    && listaMallas.get(i).getTipoActividad().getDescripcion().equals(tipoActividad.getDescripcion())
                    && listaMallas.get(i).getSector().equals(sector)) {
                return i;
            }
        }

        return -1;
    }
}
