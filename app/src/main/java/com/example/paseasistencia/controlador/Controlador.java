package com.example.paseasistencia.controlador;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.manejador.DBHandler;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.TiposPermisos;
import com.example.paseasistencia.model.Trabajadores;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Controlador {

    private static Controlador INSTANCIA = null;
    private static Context CONTEXT = null;
    private static DBHandler CONEXION = null;
    public static  final String TIPOS_ACTIVIDADES [] = new String [] {"Slecciones tipo Actividad","RECOLECCION","HORAS","TAREA"};
    public static enum STATUS_SESION{
        SESION_ACTIVA,
        SESION_NO_INICIADA,
        SESION_NO_FINALIZADA,
        SESION_FINALIZADA
    }

    private Controlador(Context context){
        Controlador.CONEXION = new DBHandler(context);
    }

    private synchronized static void createInstance(Context context){
        if(Controlador.INSTANCIA == null){
            Controlador.INSTANCIA = new Controlador(context);
        }

        Controlador.CONTEXT = context;
    }

    public static Controlador getInstance(Context context){
        if(Controlador.INSTANCIA == null)
            createInstance(context);

        return Controlador.INSTANCIA;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    public static Context getCONTEXT() {
        return CONTEXT;
    }

    /******************************Reinicios*****************************************************/
    public void reiniciarListaPuestos(){
        Controlador.CONEXION.recrearTablaListaPuestos();
    }
    public void reiniciarListaActividades(){
        Controlador.CONEXION.recrearTablaListaActividades();
    }
    public void reiniciarListaMallas(){
        Controlador.CONEXION.recrearTablaListaMallas();
    }

    public void reiniciarListaTiposPermisos(){
        Controlador.CONEXION.recrearTablaListaPermisos();
    }

    public void reiniciarListaTrabajadores(){
        Controlador.CONEXION.recrearTablaListaTrabajadores();
    }

    public void reiniciarListaCuadrillasRevisadas(){
        Controlador.CONEXION.recrearTablacuadrillasRevisadas();
    }

    /********************************Configuracion*****************************************************/
    public Configuracion getConfiguracion(){
        return Controlador.CONEXION.getConfiguracion(Long.valueOf(1));
    }

    public Boolean setConfiguracion(String url,String id){
        Long i = Long.valueOf(-1);
        if(id.equals("")){
            if(!url.equals("")){
                Configuracion c = new Configuracion(url);
                i = Controlador.CONEXION.addConfiguracion(c);
            }
        }else {
            return updateConfiguracion(id,url);
        }

        return i==-1? false : true;

    }

    public Boolean updateConfiguracion(String id,String url){
        Integer i = -1;
        if(!url.equals("")){
            Configuracion c = new Configuracion(Long.parseLong(id),url);
            i = Controlador.CONEXION.updateConfiguracion(c);
        }


        return i==-1? false : true;
    }
    /********************************Settings*****************************************************/

    public Settings getSettings(){
        return Controlador.CONEXION.getSetting(Long.valueOf(1));
    }

    private Settings setSetting(Date dateInicio,Date dateFin,Integer jornadaFinalizada,Integer jornadaInicia,Integer envioDatos){

        Settings s = new Settings(dateInicio,dateFin,jornadaFinalizada,jornadaInicia,envioDatos);
        Long aLong = Controlador.CONEXION.addSettings(s);
        s.setId(aLong);
        Log.i("sesion",s.toString());
        return s;
    }

    private boolean actualizarSetting(Settings settings){
        int i = Controlador.CONEXION.updateSetting(settings);
        if(i==-1)
            return false;
        else
            return true;
    }

    private void inicarFinalizarSettings(int iniciarJorjana, int finalizarJorjana, int enviarDatos, Date horaInicio, Date horaFin, Settings settings) {

        settings.setJornadaIniciada(iniciarJorjana);
        settings.setJornadaFinalizada(finalizarJorjana);
        settings.setEnvioDatos(enviarDatos);
        settings.setInicio(horaInicio);
        settings.setFin(horaFin);
    }

    public STATUS_SESION validarSesion(){
        Settings settings = getSettings();
        STATUS_SESION sesion = null;

        if(settings==null){
            sesion = STATUS_SESION.SESION_NO_INICIADA;
        }else{
            Log.i("sesion",settings.toString());
            String fecha = Complementos.getDateActualToString();

            if(!settings.getFecha().equals(fecha)){
                if(settings.getJornadaFinalizada()==0){
                    sesion = STATUS_SESION.SESION_NO_FINALIZADA;
                }else{
                    sesion = STATUS_SESION.SESION_FINALIZADA;
                }
            }else{
                if(settings.getJornadaFinalizada()==1){
                    sesion = STATUS_SESION.SESION_FINALIZADA;
                }else{
                    sesion = STATUS_SESION.SESION_ACTIVA;
                }
            }
        }

        Log.i("sesion",sesion.name());
        return sesion;
    }

    public STATUS_SESION iniciarSession(){
        Settings settings = getSettings();

        if(settings==null){
            setSetting(new Date(),new Date(0),0,1,0);
            Controlador.CONEXION.recrearTablacuadrillasRevisadas();
            return STATUS_SESION.SESION_ACTIVA;
        }else{
            String fecha = Complementos.getDateActualToString();
            Log.i("inicio", settings.getFecha() + " -- " + fecha);
            if(!settings.getFecha().equals(fecha)){
                if(settings.getJornadaFinalizada()==0){
                    //finalizar asistencias
                    ArrayList<Asistencia> asistencias = getAsistencias(settings.getFecha());
                    try {
                        Date fin = new Date(Complementos.convertirStringAlong(settings.getFecha(), "16:00"));

                        for (Asistencia a : asistencias) {
                            if(a.getHoraFinal().equals("")){
                                finalizarAsistencias(a,fin);
                            }
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        return STATUS_SESION.SESION_NO_INICIADA;
                    }
                }

                inicarFinalizarSettings(1, 0, 0, new Date(), new Date(0), settings);

                boolean b = actualizarSetting(settings);
                if(b){
                    Controlador.CONEXION.recrearTablacuadrillasRevisadas();
                    return STATUS_SESION.SESION_ACTIVA;
                }else{
                    return STATUS_SESION.SESION_NO_INICIADA;
                }
            }else{
                if(settings.getJornadaFinalizada()==1){
                    return STATUS_SESION.SESION_FINALIZADA;
                }else{
                    return STATUS_SESION.SESION_ACTIVA;
                }
            }
        }
    }

    public STATUS_SESION finalizarSesion() {

        if (validarSesion() == STATUS_SESION.SESION_ACTIVA) {
            Settings settings = getSettings();
            Date fin = new Date();
            inicarFinalizarSettings(0, 1, 0, settings.getInicio(), fin, settings);
            return STATUS_SESION.SESION_FINALIZADA;
        } else {
            return STATUS_SESION.SESION_FINALIZADA;
        }
    }

    /********************************Catalogo de puestos*****************************************************/
    public ArrayList<Puestos> getPuestos(){
        return Controlador.CONEXION.getPuestos();
    }

    public Puestos getPuestos(String descripcion){
        if(descripcion.equals("A MAYORDOMOS CAMPO"))
            descripcion = "MAYORDOMO";
        return Controlador.CONEXION.getPuestos(descripcion);
    }

    public Boolean setPuetos(Puestos p){
        Long i = Long.valueOf(-1);

        if(p!=null){
            i = Controlador.CONEXION.addPuestos(p);
        }

        return i==-1? false : true;

    }

    /******************************Catalogo de Actividades*****************************************************/
    public ArrayList<Actividades> getActividades(){
        return Controlador.CONEXION.getActividades();
    }

    public Boolean setActividad(Actividades a){
        Long i = Long.valueOf(-1);

        if(a!=null){
            i = Controlador.CONEXION.addActividad(a);
        }

        return i==-1? false : true;

    }

    /******************************Catalogo de mallas*****************************************************/
    public ArrayList<Mallas> getMallas(){
        return Controlador.CONEXION.getMallas();
    }

    public Boolean setMallas(Mallas m){
        Long i = Long.valueOf(-1);

        if(m!=null){
            i = Controlador.CONEXION.addMallas(m);
        }

        return i==-1? false : true;

    }

    public ArrayList<String> getSectores(){
        Log.i("actividad","get sectores");
        return Controlador.CONEXION.getSectores();
    }

    public ArrayList<Mallas> getMallas(String sector){
       return Controlador.CONEXION.getMallasXsector(sector);
    }
    /******************************Catalogo de tipos Permisos*****************************************************/
    public ArrayList<TiposPermisos> getTiposPermisos(){
        return Controlador.CONEXION.getTiposPermisos();
    }

    public Boolean setTipoPermisos(TiposPermisos tp){
        Long i = Long.valueOf(-1);

        if(tp!=null){
            i = Controlador.CONEXION.addTiposPermisos(tp);
        }

        return i==-1? false : true;
    }
    /******************************Catalogo de Trabajadores*****************************************************/
    public Trabajadores getTrabajadores(Integer id){
        return Controlador.CONEXION.getTrabajadores(Long.valueOf(id));
    }

    public ArrayList<Trabajadores> getTrabajadoresXcuadrilla(Integer cuadrilla){
        return Controlador.CONEXION.getTrabajadoresCuadrilla(cuadrilla);
    }

    public Boolean setTrabajadores(Integer cuadrilla,Integer consecutivo,String nombre,Integer numero,String puesto){
        Long i = Long.valueOf(-1);

        if(cuadrilla!=null && consecutivo!=null && !nombre.equals("") && numero!=null && !puesto.equals("")){

            i = Controlador.CONEXION.addTrabajador(new Trabajadores(cuadrilla,consecutivo,nombre,numero,getPuestos(puesto),0));
        }

        return i==-1? false : true;
    }
    /******************************Catalogo Cuadrillas*****************************************************/


    public ArrayList<Cuadrillas> getCuadrillas(){
        return Controlador.CONEXION.getCuadrillas();
    }

    public boolean setCuadrilla(Cuadrillas cuadrillas){



        Long aLong = Controlador.CONEXION.addCuadrillaRevisada(cuadrillas);
        if(aLong!=-1)
            return true;
        else
            return false;
    }

    public boolean validarNumeroCuadrilla(String cuadrilla,String horaInicio){
        try{
            int i = Integer.parseInt(cuadrilla);
            Integer consecutivo = Controlador.CONEXION.getConsecutivo(i);
            Log.i("inicio","error "+consecutivo+" -- "+horaInicio);
            if(consecutivo==1 && !horaInicio.equals(""))
                return true;
            else
                return false;
        }catch (NumberFormatException ex){
            return false;
        }

    }

    public boolean validarCuadrilla(Integer cuadrilla){
        Cuadrillas c = Controlador.CONEXION.getCuadrillaActiva(getSettings().getFecha(),cuadrilla);
        if(c==null)
            return false;
        else
            return true;
    }

    public Integer getConsecutivoSiguiente(Integer cuadrilla){
        return Controlador.CONEXION.getConsecutivo(cuadrilla);
    }
    /******************************Catalogo Asistencias*****************************************************/
    public ArrayList<Asistencia> getAsistencias(String fecha){
        return Controlador.CONEXION.getAsistencia(fecha);
    }

    public ArrayList<ListaAsistencia> getAsistencia(String fecha, Cuadrillas cuadrilla) {
        return Controlador.CONEXION.getAsistencia(fecha,cuadrilla);
    }

    public ArrayList<Asistencia> getAsistencia(String fecha, Trabajadores trabajador){
        return Controlador.CONEXION.getAsistencia(fecha,trabajador);
    }

    public boolean iniciarCuadrilla(String fecha, Cuadrillas cuadrilla) {

        ArrayList<ListaAsistencia> a = getAsistencia(fecha, cuadrilla);

        for (ListaAsistencia la : a) {

            if(la.getAsistencia()==null){
                return false;
            }
        }

        return true;
    }

    public void finalizarCuadrilla(String fecha, Cuadrillas cuadrilla, String fin) {
        try {
            Settings settings = getSettings();
            Date horaFin = new Date(Complementos.convertirStringAlong(settings.getFecha(), fin));
            ArrayList<ListaAsistencia> asistencia = getAsistencia(fecha, cuadrilla);
            for (ListaAsistencia la : asistencia) {
                finalizarAsistencias(la.getAsistencia(), horaFin);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean finalizarAsistencias(Asistencia asistencia,Date fin){
        Boolean r = true;

        asistencia.setDateFin(fin);

        int i = Controlador.CONEXION.updateAsistencia(asistencia);

        if(i>-1)
            return true;
        else
            return false;
    }

    public boolean setAsistencia(ListaAsistencia asistencia){
        Boolean r = true;

            Long aLong = Controlador.CONEXION.addAsistencia(new Asistencia(asistencia.getTrabajadores(), asistencia.getAsistencia().getPuesto(), asistencia.getAsistencia().getDateInicio(), asistencia.getAsistencia().getDateFin(), 0));
            if(aLong == -1)
                r = false;

        return r;
    }

    public boolean setNuevasAsistencias(Cuadrillas cuadrilla, List<ListaAsistencia> trabajadores, String hora, String fecha) {
        Long aLong = Long.valueOf(-1);
        Date horainicio = new Date();
        try {
            horainicio = new Date(Complementos.convertirStringAlong(fecha,hora));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        for (ListaAsistencia t : trabajadores) {
            /*if(t.getTrabajadores().getId()==null){
                Controlador.CONEXION.addTrabajador(t.getTrabajadores());
            }

           // Asistencia a = new Asistencia(t,t.get.getPuesto(),horainicio,new Date(0),0);
            aLong = Controlador.CONEXION.addAsistencia(t.getAsistencia());
            */

            aLong = actualizarListaAsistencia(t);
            if(aLong == -1){
                borrarAsistencias(trabajadores,fecha);
                break;
            }


        }

        if(aLong!=-1){
            if (cuadrilla.getId() == null) {
                cuadrilla.setFechaInicio(horainicio);
                cuadrilla.setFechaFin(new Date(0));
                aLong = Controlador.CONEXION.addCuadrillaRevisada(cuadrilla);
                if (aLong != -1)
                    cuadrilla.setId(Integer.parseInt(aLong.toString()));
            }
        }


        return true;
    }

    private Long actualizarListaAsistencia(ListaAsistencia listaAsistencia) {
        Long aLong = Long.valueOf(-1);
        if(listaAsistencia.getTrabajadores().getId()==null){
            aLong = Controlador.CONEXION.addTrabajador(listaAsistencia.getTrabajadores());
        }else{
            int i = Controlador.CONEXION.UpdateTrabajador(listaAsistencia.getTrabajadores());
            aLong = Long.valueOf(i);
        }

        if(listaAsistencia.getAsistencia().getId()==null){
            aLong = Controlador.CONEXION.addAsistencia(listaAsistencia.getAsistencia());
        }else{
            int i = Controlador.CONEXION.updateAsistencia(listaAsistencia.getAsistencia());
            aLong = Long.valueOf(i);
        }

        if(aLong !=-1)
            return aLong;
        else
            return Long.valueOf(0);
    }

    private void borrarAsistencias(List<ListaAsistencia> trabajadores, String fecha) {
        for (ListaAsistencia t : trabajadores) {
            Controlador.CONEXION.deleteAsistencias(fecha, t.getTrabajadores().getId().toString());
        }
    }


    public boolean validarAsistencia(Asistencia asistencia){
        if(asistencia!=null){
            ArrayList<Asistencia> puestos = Controlador.CONEXION.getAsistencia(asistencia.getFecha(), asistencia.getTrabajador());

            for (Asistencia a : puestos) {
                if(a.getDateInicio()!=null){
                    if(a.getDateInicio().getTime() >= asistencia.getDateInicio().getTime() && a.getDateFin().getTime() < asistencia.getDateInicio().getTime()){
                        return false;
                    }
                }



            /*    if(a.getDateInicio().getTime() <= asistencia.getAsistencia().getDateInicio().getTime() && a.getDateFin().getTime() > asistencia.getAsistencia().getDateFin().getTime()){
                    return false;
                }else if (a.getDateInicio().getTime() >= asistencia.getAsistencia().getDateInicio().getTime() && a.getDateFin().getTime() < asistencia.getAsistencia().getDateFin().getTime()){
                    return false;
                }else if(a.getDateInicio().getTime() > asistencia.getAsistencia().getDateFin().getTime() && a.getDateFin().getTime() > asistencia.getAsistencia().getDateFin().getTime()){
                    return false;
                }else if (a.getDateInicio().getTime() <= asistencia.getAsistencia().getDateInicio().getTime() && a.getDateFin().getTime() > asistencia.getAsistencia().getDateFin().getTime()){
                    return false;
                }*/
            }
        }



        return true;
    }
    /******************************actividades realizadas*****************************************************/
    public ArrayList<ActividadesRealizadas> getActividadesResalizadas(String fecha,Cuadrillas cuadrillas){
        return Controlador.CONEXION.getActividadesRealizadas(fecha,cuadrillas);
    }

    public boolean setActividadesRealizadas(ActividadesRealizadas actividadesRealizadas){
        Long aLong = Long.valueOf(-1);
        if(actividadesRealizadas.getId()!=null){
            int i = Controlador.CONEXION.updateActividadesRealizadas(actividadesRealizadas);
            aLong = Long.valueOf(i);
        }else{
            aLong = Controlador.CONEXION.addActividadesRealizadas(actividadesRealizadas);
        }

        if(aLong!=-1)
            return true;
        else
            return false;
    }
}
