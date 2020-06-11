package com.example.paseasistencia.ui.detail;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.Trabajadores;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DetailAdapter extends ArrayAdapter<ListaAsistencia> {
    private Controlador controlador;

    private IDetallesAsistencia iDetallesAsistencia;

    private Context context;
    private List<ListaAsistencia> trabajadores;
    private ArrayList<Puestos> puestos;

    public DetailAdapter(@NonNull Context context, List<ListaAsistencia> trabajadores,IDetallesAsistencia iDetallesAsistencia) {
        super(context, R.layout.item_lista_cuadrilla,trabajadores);
        this.trabajadores = trabajadores;
        this.context = context;
        this.controlador = Controlador.getInstance(context);
        this.puestos = Controlador.getInstance(context).getPuestos();
        this.iDetallesAsistencia=iDetallesAsistencia;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_lista_cuadrilla,parent,false);
        TextView tvConsecutivo = convertView.findViewById(R.id.tv_consecutivo);
        TextView tvNombre = convertView.findViewById(R.id.tv_trabajador);
        TextView tvTotalHoras = convertView.findViewById(R.id.tv_totalHoras);
        final Spinner spPuesto = convertView.findViewById(R.id.sp_puesto);

        final ListaAsistencia trabajadorSeleccionado = trabajadores.get(position);

        tvConsecutivo.setText(trabajadorSeleccionado.getTrabajadores().getConsecutivo().toString());
        tvNombre.setText(trabajadorSeleccionado.getTrabajadores().getNombre());

        ArrayAdapter<Puestos> puestosAdapter = new ArrayAdapter<>(context,R.layout.support_simple_spinner_dropdown_item,puestos);
        spPuesto.setAdapter(puestosAdapter);

        //if(trabajadorSeleccionado.getAsistencia()==null){
        //   spPuesto.setSelection(Complementos.getIndex(spPuesto,trabajadorSeleccionado.getTrabajadores().getPuesto().getNombre()));
        //    tvTotalHoras.setText("00:00");
        //}else{
            spPuesto.setSelection(Complementos.getIndex(spPuesto,trabajadorSeleccionado.getAsistencia().getPuesto().getNombre()));
            tvTotalHoras.setText(Complementos.getTotalHoras(trabajadorSeleccionado.getAsistencia().getDateInicio(),trabajadorSeleccionado.getAsistencia().getDateFin()));
        //}


        tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlador.validarSesion() == Controlador.STATUS_SESION.SESION_ACTIVA)
                    iDetallesAsistencia.menuSeleccion(trabajadorSeleccionado,position);
            }
        });


        spPuesto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    //if(trabajadorSeleccionado.getAsistencia()==null){
                    //    trabajadorSeleccionado.getTrabajadores().setPuesto((Puestos) spPuesto.getItemAtPosition(position));
                    //}
                    //else{
                        trabajadorSeleccionado.getAsistencia().setPuesto((Puestos) spPuesto.getItemAtPosition(position));
                        trabajadorSeleccionado.getTrabajadores().setPuesto((Puestos) spPuesto.getItemAtPosition(position));
                    //}

                iDetallesAsistencia.actualziarAsistencia(getTotalAsistencia());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iDetallesAsistencia.actualziarAsistencia(getTotalAsistencia());


        return convertView;
    }

    @Override
    public void add(@Nullable ListaAsistencia object) {
        super.add(object);
        this.sort(new Comparator<ListaAsistencia>() {
            @Override
            public int compare(ListaAsistencia o1, ListaAsistencia o2) {
                return o1.getTrabajadores().getConsecutivo().compareTo(o2.getTrabajadores().getConsecutivo());
            }
        });
    }

    public String getTotalAsistencia(){
        Integer i= 0;

        for (ListaAsistencia t : trabajadores) {
            if(t.getAsistencia().getDateFin().getTime()==new Date(0).getTime()){
                if (t.getAsistencia().getPuesto().getId() != 11){
                    i++;
                }
            }
        }

        return i.toString();
    }

    public boolean validarHoras(Asistencia asistencia,int fila,Cuadrillas cuadrillas){
        Log.i("horas","fila editar "+fila);
        if(cuadrillas.getFechaInicio().getTime()<= asistencia.getDateInicio().getTime()){
            for (int i=0;i<trabajadores.size();i++) {

                if(i!=fila){
                    Log.i("edit",i+"!="+fila);
                    ListaAsistencia lt = trabajadores.get(i);
                    if(lt.getTrabajadores().getNombre().equals(asistencia.getTrabajador().getNombre())){
                        if(asistencia.getHoraFinal().equals("")){
                            Log.i("edit","hora final vacia ");
                            if(!lt.getAsistencia().getHoraFinal().equals("")){

                                if(lt.getAsistencia().getDateInicio().getTime() <= asistencia.getDateInicio().getTime() && lt.getAsistencia().getDateFin().getTime() > asistencia.getDateInicio().getTime()){
                                    return false;
                                }else if(lt.getAsistencia().getDateInicio().getTime()>asistencia.getDateInicio().getTime()){
                                    return false;
                                }
                            }else if(lt.getAsistencia().getDateInicio().getTime()>= asistencia.getDateInicio().getTime()){
                                return false;
                            }
                        }else{
                            Log.i("edit","hora final con valor");
                            if(asistencia.getDateInicio().getTime()<asistencia.getDateFin().getTime()){
                                Long oi = asistencia.getDateInicio().getTime();
                                Long of = asistencia.getDateFin().getTime();

                                Long hi = lt.getAsistencia().getDateInicio().getTime();
                                Long hf = lt.getAsistencia().getDateFin().getTime();

                                if(hf==0){
                                    if(oi>hi || of>hi)
                                        return false;
                                }else if(hi<=oi && hf > oi){  //hi|------|oi|------|hf|------|of
                                    //hora final nueva interviene en otro puesto
                                    Log.i("edit","hora final nueva interviene en otro puesto "+ hi+" <= "+oi+"=="+(hi<=oi) +" YY "+ hf +" > "+ oi+"=="+(hf>oi));
                                    return false;
                                }else if(hi<of && hf>= of){   //oi|------|hi|------|of|------|hf
                                    //hora inicial nueva interviene en otro puesto
                                    Log.i("edit","hora inicial nueva interviene en otro puesto "+ hi+" < "+of+"=="+(hi<of) +" YY "+ hf +" >= "+ of+"=="+(hf>=of));
                                    return false;
                                }else if(hi>=oi && hf<=of){//oi|------|hi|------|hf|------|of
                                    //hora inicial y final nuevas intervien en otro puesto
                                    //if(hf != 0) { ////oi|------|hi|----------|0
                                        Log.i("edit",fila +" hora inicial y final nuevas intervien en otro puesto "+hi+" >= "+oi+"=="+(hi>=oi) +" YY "+hf +" <= "+ of+"=="+(hf<=of));
                                        return false;
                                    //}
                                }else if(hi<=oi && hf>=of){//hi|------|oi|------|of|------|hf
                                    //hora inicio y final nuevas afecta a otro puesto

                                        Log.i("edit","hora inicio y final nuevas afecta a otro puesto "+hi+"<="+oi+"=="+(hi<=oi)+" YY "+hf+">="+of+"=="+(hf>=of));
                                        return false;

                                }

                            }else{
                                return false;
                            }
                        }
                    }
                }
            }
        }else{
            return false;
        }
        return true;
    }

    public void actualizarTrabajador(String nombre,String nombreAnterior){
        for (ListaAsistencia lt : trabajadores){
            if(lt.getTrabajadores().getNombre().equals(nombreAnterior)){
                lt.getTrabajadores().setNombre(nombre.toUpperCase());
                lt.getAsistencia().getTrabajador().setNombre(nombre.toUpperCase());
            }
        }
    }

    public void addTrabajador(Cuadrillas cuadrilla, String consecutivo , String nombre, Puestos puestos){
        if(!nombre.equals("")){
            Trabajadores t = new Trabajadores(cuadrilla.getCuadrilla(), Integer.parseInt(consecutivo), nombre.toUpperCase(), Integer.parseInt(consecutivo), puestos, 0);
            Asistencia a = new Asistencia(t,puestos,cuadrilla.getFechaInicio(),cuadrilla.getFechaFin(),0);
            ListaAsistencia la = new ListaAsistencia(t,a);
            trabajadores.add(la);
            notifyDataSetChanged();
        }
    }

    public Integer getConsecutivo(){
        if(this.trabajadores.size()==0)
            return 1;
        else
            return  this.trabajadores.get(this.trabajadores.size()-1).getTrabajadores().getConsecutivo()+1;
    }

    public String getResponsable() {
        for (ListaAsistencia t : this.trabajadores) {
            if (t.getTrabajadores().getPuesto().getId() == 3)
                return t.getTrabajadores().getNombre();
        }

        return "";
    }

    public List<ListaAsistencia> getTrabajadores() {
        return trabajadores;
    }
}
