package com.example.paseasistencia.ui.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DetailAdapter extends ArrayAdapter<ListaAsistencia> {
    private Controlador controlador;

    private IDetallesAsistencia iDetallesAsistencia;

    private Context context;
    private List<ListaAsistencia> trabajadores;
    private ArrayList<Puestos> puestos;
    private CharSequence[] items;

    private int PUESTO_MAYORDOMO = 3;
    private int PUESTO_NO_TRABAJO = 11;
    private int PUESTO_PERSONAL_CAMPO = 2;
    public static final int GUARDADO_EXITOSO = 1;
    public static final int ERROR_GUARDAR = 0;


    public DetailAdapter(@NonNull Context context, List<ListaAsistencia> trabajadores,IDetallesAsistencia iDetallesAsistencia) {
        super(context, R.layout.item_lista_cuadrilla,trabajadores);
        this.trabajadores = trabajadores;
        this.context = context;
        this.controlador = Controlador.getInstance(context);
        this.puestos = Controlador.getInstance(context).getPuestos();
        this.iDetallesAsistencia=iDetallesAsistencia;

        items = new CharSequence[this.puestos.size()];
        for (int i = 0; i < this.puestos.size(); i++) {
            items[i] = this.puestos.get(i).toString();
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_lista_cuadrilla,parent,false);
        TextView tvConsecutivo = convertView.findViewById(R.id.tv_consecutivo);
        TextView tvNombre = convertView.findViewById(R.id.tv_trabajador);
        TextView tvTotalHoras = convertView.findViewById(R.id.tv_totalHoras);
        LinearLayout fila = convertView.findViewById(R.id.ly_itemCuadralla);

        final TextView tvPuesto = convertView.findViewById(R.id.tv_puesto);

        final ListaAsistencia trabajadorSeleccionado = trabajadores.get(position);

        tvConsecutivo.setText(trabajadorSeleccionado.getTrabajadores().getConsecutivo().toString());
        tvNombre.setText(trabajadorSeleccionado.getTrabajadores().getNombre());
        tvPuesto.setText(trabajadorSeleccionado.getAsistencia().getPuesto().getNombre());

        colorearFila(position, fila);

        String horas = Complementos.getTotalHoras(trabajadorSeleccionado.getAsistencia().getDateInicio(), trabajadorSeleccionado.getAsistencia().getDateFin());
        tvTotalHoras.setText(horas.equals("00:00") ? "" : horas);

        tvNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlador.validarSesion() == Controlador.STATUS_SESION.SESION_ACTIVA)
                    iDetallesAsistencia.menuSeleccion(trabajadorSeleccionado,position);
            }
        });

        View.OnClickListener c = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPuestos(trabajadorSeleccionado);
            }
        };

        tvConsecutivo.setOnClickListener(c);
        tvPuesto.setOnClickListener(c);

        iDetallesAsistencia.actualziarAsistencia(getTotalAsistencia());

        return convertView;
    }


    private void dialogPuestos(final ListaAsistencia trabajadorSeleccionado) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        int seleccion = Complementos.getIndex(items, trabajadorSeleccionado.getAsistencia().getPuesto().getNombre());
        builder.setTitle("Seleccione un puesto")
                .setSingleChoiceItems(items, seleccion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //agregarSector(items[which].toString());
                        trabajadorSeleccionado.getAsistencia().setPuesto(puestos.get(which));
                        trabajadorSeleccionado.getTrabajadores().setPuesto(puestos.get(which));
                        DetailAdapter.this.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
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

    private ListaAsistencia getTrabajador(Integer numero) {
        for (ListaAsistencia la : trabajadores) {
            if (la.getTrabajadores().getConsecutivo() == numero)
                return la;
        }

        return null;
    }

    public int asistencia(String numero) {
        if (!numero.equals("")) {
            Integer num = Integer.parseInt(numero);
            ListaAsistencia trabajador = getTrabajador(num);

            if (trabajador != null) {
                Puestos p = controlador.buscarPuestos(PUESTO_PERSONAL_CAMPO, puestos);

                trabajador.getAsistencia().setPuesto(p);
                trabajador.getTrabajadores().setPuesto(p);

                notifyDataSetChanged();
                iDetallesAsistencia.actualziarAsistencia(getTotalAsistencia());

                return GUARDADO_EXITOSO;
            }
        }

        return ERROR_GUARDAR;
    }

    private void colorearFila(int position, LinearLayout fila) {
        ListaAsistencia trabajadorSeleccionado = trabajadores.get(position);

        for (int i = 0; i < fila.getChildCount(); i++) {
            ((TextView) fila.getChildAt(i)).setTextColor(getContext().getResources().getColor(R.color.colorText));
        }


        if (trabajadorSeleccionado.getAsistencia().getPuesto().getId() == this.PUESTO_MAYORDOMO)
            fila.setBackgroundColor(getContext().getResources().getColor(R.color.colorMayordomo));
        else if (trabajadorSeleccionado.getAsistencia().getPuesto().getId() == this.PUESTO_NO_TRABAJO)
            fila.setBackgroundColor(getContext().getResources().getColor(R.color.colorNoTrabajo2));
        else if (trabajadorSeleccionado.getAsistencia().getPuesto().getId() == this.PUESTO_PERSONAL_CAMPO) {
            fila.setBackgroundColor(getContext().getResources().getColor(R.color.colorPersonalCampo2));
        } else if (trabajadorSeleccionado.getAsistencia().getPuesto().getId() != this.PUESTO_PERSONAL_CAMPO)
            fila.setBackgroundColor(getContext().getResources().getColor(R.color.colorOtros2));


    }
}
