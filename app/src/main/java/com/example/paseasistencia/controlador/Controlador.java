package com.example.paseasistencia.controlador;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.manejador.DBHandler;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.MallasRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.TiposActividades;
import com.example.paseasistencia.model.TiposPermisos;
import com.example.paseasistencia.model.Trabajadores;


import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Controlador {

    private static Controlador INSTANCIA = null;
    private static Context CONTEXT = null;
    private static DBHandler CONEXION = null;
    private static ArrayList<TiposActividades> TIPOS_ACTIVIDADES = null;//new String[]{"Selecciones tipo Actividad", "RECOLECCION", "HORAS", "TAREA"};
    public static final String TAG = "Controlador";

    public enum STATUS_APP {
        SESION_APP_ACTIVA,
        SESION_APP_REINICAR,
        SESION_APP_INICIAR,
        SESION_APP_FECHA_DISPOSITIVO_NO_VALIDA,
        SESION_APP_ERROR_NO_ESPERADO
    }

    public enum STATUS_SESION {
        SESION_ACTIVA,
        SESION_NO_INICIADA,
        SESION_NO_FINALIZADA,
        SESION_FINALIZADA,
        REINICIAR_APP,
        CUADRILLA_PENDIENTES_POR_FINALIZAR,
        ACTUALIZAR_CATALOGO_TRABAJADORES
    }

    public enum STATUS_CONEXION {
        SIN_CONEXION,
        ERROR_INESPERADO,
        ENVIO_EXITOSO,
        REGISTRO_DUPLICADO,
        ERROR_SERVIDOR,
        ERROR_JSON,
        ERROR_ENVIO,
        INICIO_ENVIO,
        FINALIZACION_ENVIO
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

    public void reiniciarTiposActividades() {
        Controlador.CONEXION.recrearTablaTiposActividades();
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

    public Integer totalRegistrosPendientesPorEnviar() {
        return Controlador.CONEXION.getTotalPendientePorEnviar();
    }

    /********************************Configuracion*****************************************************/
    public Configuracion getConfiguracion(){
        return Controlador.CONEXION.getConfiguracion(Long.valueOf(1));
    }

    public boolean configuracionValida() {
        return Controlador.CONEXION.getConfiguracion(Long.valueOf(1)) == null ? false : true;
    }

    public Boolean setConfiguracion(String url,String id){
        Long i = Long.valueOf(-1);
        if(id.equals("")){
            if(!url.equals("")){
                Configuracion c = new Configuracion(url);
                FileLog.i(TAG, "agregar configuraion");
                i = Controlador.CONEXION.addConfiguracion(c);
            } else {
                FileLog.i(TAG, "sin cambio");
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
            FileLog.i(TAG, "actualizar configuracion");
            i = Controlador.CONEXION.updateConfiguracion(c);
        }


        return i==-1? false : true;
    }
    /********************************Settings*****************************************************/

    public Settings getSettings(){
        return Controlador.CONEXION.getSetting(Long.valueOf(1));
    }

    private Settings setSetting(Date dateInicio,Date dateFin,Integer jornadaFinalizada,Integer jornadaInicia,Integer envioDatos){
        Settings s = new Settings(dateInicio, dateFin, jornadaFinalizada, jornadaInicia, envioDatos, Complementos.fechaInicioSemanaDate());
        FileLog.i(TAG, "agregar settings");
        Long aLong = Controlador.CONEXION.addSettings(s);
        s.setId(aLong);
        return s;
    }

    private boolean actualizarSetting(Settings settings){
        FileLog.i(TAG, "actualizar settings");
        int i = Controlador.CONEXION.updateSetting(settings);
        if(i==-1)
            return false;
        else
            return true;
    }

    public void actualizarFechaActualziacion() {
        Settings settings = getSettings();
        if (settings != null) {
            Date da = Complementos.fechaInicioSemanaDate();
            settings.setFechaActualizacion(da);

            CONEXION.updateSetting(settings);
        }


    }

    private void inicarFinalizarSettings(int iniciarJorjana, int finalizarJorjana, int enviarDatos, Date horaInicio, Date horaFin, Settings settings) {
        FileLog.i(TAG, "inicializar o finalizar settings");
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
            if (CONEXION.getTotalTrabajadores() > 0) {
                sesion = STATUS_SESION.SESION_NO_INICIADA;
            } else {
                sesion = STATUS_SESION.ACTUALIZAR_CATALOGO_TRABAJADORES;
            }

        }else{
            String fecha = Complementos.getDateActualToString();

            if(!settings.getFecha().equals(fecha)){
                if (getCuadrillasPendientesPorFinalizar() > 0) {
                    sesion = STATUS_SESION.CUADRILLA_PENDIENTES_POR_FINALIZAR;
                } else if (!settings.getFechaActualizacionString().equals(Complementos.fechaInicioSemana())) {
                    sesion = STATUS_SESION.ACTUALIZAR_CATALOGO_TRABAJADORES;
                } else {
                    sesion = STATUS_SESION.REINICIAR_APP;
                }
                //}
            }else{
                if(settings.getJornadaFinalizada()==1){
                    sesion = STATUS_SESION.SESION_FINALIZADA;
                }else{
                    sesion = STATUS_SESION.SESION_ACTIVA;
                }
            }
        }

        FileLog.i(TAG, "status settings " + sesion);
        return sesion;
    }

    public boolean sesionNoFinalizada() {
        if (validarSesion() == STATUS_SESION.SESION_NO_FINALIZADA)
            return true;
        else
            return false;
    }

    public boolean sesionActiva() {
        if (validarSesion() == Controlador.STATUS_SESION.SESION_ACTIVA)
            return true;
        else
            return false;
    }

    public STATUS_SESION iniciarSession(){
        Settings settings = getSettings();
        STATUS_SESION status = null;

        if(settings==null){
            setSetting(new Date(),new Date(0),0,1,0);
            status = STATUS_SESION.SESION_ACTIVA;
        }else{
            String fecha = Complementos.getDateActualToString();
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
                        status = STATUS_SESION.SESION_NO_INICIADA;
                    }
                }

                inicarFinalizarSettings(1, 0, 0, new Date(), new Date(0), settings);

                boolean b = actualizarSetting(settings);
                if(b){
                    status = STATUS_SESION.SESION_ACTIVA;
                }else{
                    status = STATUS_SESION.SESION_NO_INICIADA;
                }
            }else{
                if(settings.getJornadaFinalizada()==1){
                    status = STATUS_SESION.SESION_FINALIZADA;
                }else{
                    status = STATUS_SESION.SESION_ACTIVA;
                }
            }
        }

        return status;
    }

    public STATUS_APP inisiarDia() {
        Publicacion publicacion = new Publicacion(Controlador.getCONTEXT());
        Long fechaSesion = publicacion.leerArchivoPublicacion();
        try {

            Long fechaActual = Complementos.convertirStringAlong(Complementos.getDateActualToString(), "00:00");
            Settings settings = getSettings();
            if (fechaActual >= fechaSesion) {

                if (fechaSesion == 0) {
                    publicacion.escribir(fechaActual);
                    return STATUS_APP.SESION_APP_INICIAR;
                } else if (fechaActual > fechaSesion) {
                    publicacion.escribir(fechaActual);
                    return STATUS_APP.SESION_APP_REINICAR;
                } else {
                    return STATUS_APP.SESION_APP_ACTIVA;
                }
            } else {
                return STATUS_APP.SESION_APP_FECHA_DISPOSITIVO_NO_VALIDA;
            }


        } catch (ParseException e) {
            e.printStackTrace();
            return STATUS_APP.SESION_APP_ERROR_NO_ESPERADO;
        }
    }

    /********************************Catalogo de puestos*****************************************************/
    public ArrayList<Puestos> getPuestos(){
        ArrayList<Puestos> puestos = Controlador.CONEXION.getPuestos();
        Collections.sort(puestos);
        return puestos;
    }

    public Puestos buscarPuestos(int id, ArrayList<Puestos> puestos) {
        if (puestos != null) {
            for (Puestos p :
                    puestos) {
                if (p.getId() == id)
                    return p;
            }
        }

        return Controlador.CONEXION.getPuestos(id);
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

    /******************************Catalogo de tipos activdades**********************************************/
    public ArrayList<TiposActividades> getTiposActividades() {
        if (TIPOS_ACTIVIDADES == null) {
            TIPOS_ACTIVIDADES = Controlador.CONEXION.getTiposActividades();
        }
        Log.v("tiposActividades", TIPOS_ACTIVIDADES + "-----");
        return TIPOS_ACTIVIDADES;
    }

    public TiposActividades getTiposActividades(Integer id) {
        TiposActividades tiposActividades = null;

        if (TIPOS_ACTIVIDADES == null) {
            TIPOS_ACTIVIDADES = Controlador.CONEXION.getTiposActividades();
        }

        for (TiposActividades ta : this.TIPOS_ACTIVIDADES) {
            if (ta.getId() == id) {
                tiposActividades = ta;
                break;
            }
        }

        if (tiposActividades == null)
            tiposActividades = Controlador.CONEXION.getTipoActividad(id);

        return tiposActividades;
    }

    public Boolean setTiposActividad(TiposActividades ta) {
        Long i = Long.valueOf(-1);

        if (ta != null) {
            i = Controlador.CONEXION.addTiposActividades(ta);
        }

        return i == -1 ? false : true;
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

            i = Controlador.CONEXION.addTrabajador(new Trabajadores(cuadrilla, consecutivo, nombre, numero, getPuestos(puesto), 1));
        }

        return i==-1? false : true;
    }

    public ArrayList<Trabajadores> getTrabajadoresPendientesPorEnviar() {
        return Controlador.CONEXION.getTrabajadoresPendientesPorEnviar();
    }

    public int updateTrabajador(Trabajadores trabajador) {
        return Controlador.CONEXION.updateTrabajador(trabajador);
    }

    /******************************Catalogo Cuadrillas*****************************************************/


    public ArrayList<Cuadrillas> getCuadrillas(){
        ArrayList<Cuadrillas> cuadrillas = Controlador.CONEXION.getCuadrillas(getSettings().getFecha());
        Collections.sort(cuadrillas);
        return cuadrillas;
    }

    public ArrayList<Cuadrillas> getCuadrillasActivas() {
        String fecha = getSettings().getFecha();
        return Controlador.CONEXION.getCuadrillasActiva(fecha);
    }

    public boolean updateCuadrilla(Cuadrillas cuadrilla) {
        int aLong = Controlador.CONEXION.updateCuadrilla(cuadrilla);
        if (aLong != -1)
            return true;
        else
            return false;
    }

    public boolean validarNumeroCuadrilla(String cuadrilla,String horaInicio){
        try{
            int i = Integer.parseInt(cuadrilla);
            Integer consecutivo = Controlador.CONEXION.getConsecutivo(i);
            if(consecutivo==1 && !horaInicio.equals(""))
                return true;
            else
                return false;
        }catch (NumberFormatException ex){
            return false;
        }

    }

    public boolean validarCuadrilla(Integer cuadrilla){
        Cuadrillas c = Controlador.CONEXION.getCuadrillasActiva(getSettings().getFecha(), cuadrilla);
        if(c==null)
            return false;
        else
            return true;
    }

    public Integer getCuadrillasPendientesPorFinalizar() {
        return CONEXION.getCuadrillasPendientesPorFinalizar(getSettings().getFecha());
    }

    public ArrayList<Cuadrillas> getCuadrillasPendientesPorEnviar() {
        return CONEXION.getCuadrillasPendientesPorEnviar();
    }

    public void finalizarCuadrilla(Cuadrillas cuadrilla, String fin) {
        try {
            Settings settings = getSettings();
            Date date = new Date(Complementos.convertirStringAlong(settings.getFecha(), fin));
            cuadrilla.setFechaFin(date);

            ArrayList<ListaAsistencia> asistencia = getAsistencia(getSettings().getFecha(), cuadrilla);
            for (ListaAsistencia la : asistencia) {

                finalizarAsistencias(la.getAsistencia(), date);
            }

            updateCuadrilla(cuadrilla);

        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public boolean finalizarAsistencias(Asistencia asistencia,Date fin){
        if (asistencia.getHoraFinal().equals("")) {
            asistencia.setDateFin(fin);

            int i = Controlador.CONEXION.updateAsistencia(asistencia);

            if (i > -1)
                return true;
            else
                return false;
        }
        return true;
    }


    public boolean setNuevasAsistencias(Cuadrillas cuadrilla, List<ListaAsistencia> trabajadores, List<MallasRealizadas> mallasRealizadas, String hora, String fecha) {
        Long aLong = Long.valueOf(-1);
        Date horainicio = new Date();
        try {
            horainicio = new Date(Complementos.convertirStringAlong(fecha,hora));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        FileLog.i(TAG, "iniciar guardado de actividades realizadas");
        boolean b = setActividadesRealizadas(mallasRealizadas, cuadrilla.getCuadrilla());

        if (b) {
            FileLog.i(TAG, "iniciar guardado de asistencia");
            for (ListaAsistencia t : trabajadores) {
                aLong = actualizarListaAsistencia(t);
                if (aLong == -1) {
                    borrarAsistencias(trabajadores, fecha);
                    break;
                }
            }

            if (aLong != -1) {
                FileLog.i(TAG, "iniciar guardado de cuadrilla revisadas");
                if (cuadrilla.getId() == null) {
                    cuadrilla.setFechaInicio(horainicio);
                    cuadrilla.setFechaFin(new Date(0));
                    cuadrilla.setSended(0);
                    if (cuadrilla.getMayordomo().equals("")) {
                        cuadrilla.setMayordomo(getIPrimeroLista(trabajadores));
                    }

                    Controlador.CONEXION.addCuadrillaRevisada(cuadrilla);
                } else {
                    FileLog.i(TAG, "cuadrilla ya agregada");

                }
            } else {
                FileLog.i(TAG, "error al guardar las asistencias");
            }
        } else {
            FileLog.i(TAG, "error al guardar las actividades");
        }

        return b;
    }

    private String getIPrimeroLista(List<ListaAsistencia> t) {
        for (ListaAsistencia la :
                t) {
            if (la.getTrabajadores().getConsecutivo() == 1) {
                return la.getTrabajadores().getNombre();
            }
        }

        return "";
    }

    private Long actualizarListaAsistencia(ListaAsistencia listaAsistencia) {
        Long aLong = Long.valueOf(-1);
        if(listaAsistencia.getTrabajadores().getId()==null){
            aLong = Controlador.CONEXION.addTrabajador(listaAsistencia.getTrabajadores());
        }else{
            int i = Controlador.CONEXION.updateTrabajador(listaAsistencia.getTrabajadores());
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

    public ArrayList<Asistencia> getAsistenciasPendientesPorEnviar() {
        return Controlador.CONEXION.getAsistenciaPendientesPorEnviar();
    }

    public int updateAsistencias(Asistencia asistencia) {
        return Controlador.CONEXION.updateAsistencia(asistencia);
    }
    /******************************actividades realizadas*****************************************************/
    public ArrayList<MallasRealizadas> getActividadesResalizadas(String fecha, Cuadrillas cuadrillas) {
        return Controlador.CONEXION.getActividadesRealizadas(fecha,cuadrillas);
    }

    private boolean setActividadesRealizadas(List<MallasRealizadas> mallasRealizadas, Integer cuadrilla) {
        Long aLong = Long.valueOf(-1);

        Controlador.CONEXION.deleteMallasRealizadas(getSettings().getFecha(), cuadrilla.toString());

        for (MallasRealizadas lar : mallasRealizadas) {
            aLong = Controlador.CONEXION.addActividadesRealizadas(lar);
        }


        if(aLong!=-1)
            return true;
        else
            return false;
    }

    public ArrayList<MallasRealizadas> getActividadesRealizadasPendientesPorEnviar() {
        return Controlador.CONEXION.getActividadesRealizadasPendientesPorEnviar();
    }

    public int updateActividadesRealizadas(MallasRealizadas actividadRealizada) {
        return Controlador.CONEXION.updateActividadesRealizadas(actividadRealizada);
    }

    public void deleteListaAsistencia(Cuadrillas cuadrilla) {
        String fecha = getSettings().getFecha();
        ArrayList<ListaAsistencia> asistencia = getAsistencia(fecha, cuadrilla);
        String ids = "";
        for (ListaAsistencia la : asistencia) {
            ids += la.getAsistencia().getId() + ",";
        }

        ids = ids.substring(0, ids.length() - 1);

        Controlador.CONEXION.deletePaseLista(cuadrilla.getCuadrilla().toString(), fecha, ids);

    }
}
