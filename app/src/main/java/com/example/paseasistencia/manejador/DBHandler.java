package com.example.paseasistencia.manejador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.paseasistencia.complementos.KeyValues;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.ActividadesRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.Reportes;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.TiposPermisos;
import com.example.paseasistencia.model.Trabajadores;

import java.util.ArrayList;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper {

    // Contacts table name
    private static final String TABLE_ACTIVIDADES_REALIZADAS = "ActividadesResalizadas";
    private static final String TABLE_ASISTENCIA = "Asistencia";
    private static final String TABLE_REPORTES = "Permisos";
    private static final String TABLE_TRABAJADORES = "Trabajadores";
    private static final String TABLE_ACTIVIDADES = "Actividades";
    private static final String TABLE_PUESTOS = "Puestos";
    private static final String TABLE_MALLAS = "Mallas";
    private static final String TABLE_TIPOS_PERMISOS = "TiposPermisos";
    private static final String TABLE_SETTINGS = "Settings";
    private static final String TABLE_CONFIGURACION = "Configuracion";
    private static final String TABLE_CUADRILLAS_REVISADAS = "CuadrillasRevisadas";

    // Configuracion table columnas name
    private static final String KEY_ID_CONFIGURACION = "id";
    private static final String KEY_URL_CONFIGURACION = "URL";

    //settings table columnas name
    private static final String KEY_ID_SETTINGS = "Id";
    private static final String KEY_DATEINICIO_SETTINGS = "DateInicio";
    private static final String KEY_DATEFIN_SETTINGS = "DateFin";
    private static final String KEY_FECHASTRING_SETTINGS = "FechaString";
    private static final String KEY_HORAINICIOSTRING_SETTINGS = "HoraIncioString";
    private static final String KEY_HORAFINSTRING_SETTINGS = "HoraFinString";
    private static final String KEY_JORNADAFINALIZADA_SETTINGS = "FinJornada";//INDICADOR DE FIN DE JORNADA
    private static final String KEY_JORNADAINICIADA_SETTINGS = "InicioJornada";//INDICADOR DE INICIO DE JORNADA
    private static final String KEY_ENVIODATOS_SETTINGS = "EnviarInformacion";//INDICADOR DE INFORMACION PENDIENTE

    //cuadrillas
    private static final String KEY_ID_CUADRILLASREVISADAS = "id";
    private static final String KEY_CUADRILLA_CUADRILLASREVISADAS = "Cuadrilla";
    private static final String KEY_RESPONSABLE_CUADRILLASREVISADAS = "Responsable";
    private static final String KEY_DATEINICIO_CUADRILLASREVISADAS = "DateInicio";
    private static final String KEY_FECHA_CUADRILLASREVISADAS = "Fecha";
    private static final String KEY_DATEFIN_CUADRILLASREVISADAS = "DateFin";

    // catalogo actividades Table Columns names
    private static final String KEY_ID_ACTIVIDADES = "id";
    private static final String KEY_DESCRIPCION_ACTIVIDADES = "Descripcion";

    // catalogo Puestos Table Columns names
    private static final String KEY_ID_PUESTOS = "id";
    private static final String KEY_DESCRIPCION_PUESTOS= "Descripcion";

    // catalogo Tipos Permisos Table Columns names
    private static final String KEY_ID_TIPOSPERMISOS = "id";
    private static final String KEY_DESCRIPCION_TIPOSPERMISOS= "Descripcion";

    // catalogo mallas Table Columns names
    private static final String KEY_ID_MALLAS = "id";
    private static final String KEY_SECTOR_MALLAS= "Sector";
    private static final String KEY_MALLAS_MALLAS= "Malla";

    // Trabajadores Table Columns names
    private static final String KEY_ID_TRABAJADORES = "Id";
    private static final String KEY_CONSECUTIVO_TRABAJADORES = "Consecutivo";
    private static final String KEY_NOMBRE_TRABAJADORES = "Nombre";
    private static final String KEY_NUMERO_TRABAJADORES = "Numero";
    private static final String KEY_CUADRILLA_TRABAJADORES = "Cuadrilla";
    private static final String KEY_IDPUESTO_TRABAJADORES = "idPuesto";
    private static final String KEY_SENDED_TRABAJADORES = "Nuevo";


    //Reportes Table columns names
    private static final String KEY_ID_REPORTE = "Id";
    private static final String KEY_IDTRABAJADOR_REPORTE = "Id_Trabajador";
    private static final String KEY_IDTIPOPERMISO_REPORTE = "Id_Tipo_Permiso";
    private static final String KEY_FECHA_REPORTE = "Fecha";
    private static final String KEY_HORAINICIO_REPORTE = "Hora_Inicial";
    private static final String KEY_HORAFIN_REPORTE = "Hora_Final";
    private static final String KEY_SENDED_REPORTE = "Enviado";

    //Asistencia Table columns names
    private static final String KEY_ID_ASISTECNIA = "Id";
    private static final String KEY_IDTRABAJADOR_ASISTECNIA = "Id_Trabajador";
    private static final String KEY_IDPUESTO_ASISTECNIA = "Id_Puesto";
    private static final String KEY_DATEINICIO_ASISTECNIA = "DateInicio";
    private static final String KEY_DATEFIN_ASISTECNIA = "DateFin";
    private static final String KEY_FECHA_ASISTECNIA = "Fecha";
    private static final String KEY_HORAINICIO_ASISTECNIA = "Hora_Inicial";
    private static final String KEY_HORAFIN_ASISTECNIA = "Hora_Final";
    private static final String KEY_SENDED_ASISTECNIA = "Enviado";

    //Actividades realizadas table columns names
    private static final String KEY_ID_ACTIVIDADESREALIZADAS = "Id";
    private static final String KEY_CUADRILLA_ACTIVIDADESREALIZADAS = "Cuadrilla";
    private static final String KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS = "IdActividad";
    private static final String KEY_IDMALLA_ACTIVIDADESREALIZADAS = "IdMalla";
    private static final String KEY_FECHA_ACTIVIDADESREALIZADAS = "Fecha";
    private static final String KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS = "TipoActividad";
    private static final String KEY_SENDED_ACTIVIDADESREALIZADAS = "Enviado";

    //private SQLiteDatabase db;

    public DBHandler(final Context context) {

        super(new DatabaseContext(context), KeyValues.MY_DATABASE_NAME+KeyValues.EXTENCIO_DATABASE, null, KeyValues.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONFIGURACION_TABLE = "CREATE TABLE " + TABLE_CONFIGURACION + "("
                +KEY_ID_CONFIGURACION + " INTEGER PRIMARY KEY,"
                +KEY_URL_CONFIGURACION+ " TEXT"
                +")";
        db.execSQL(CREATE_CONFIGURACION_TABLE);

        String CREATE_SETTINGS_TABLE = "CREATE TABLE "+ TABLE_SETTINGS + " ("
                +KEY_ID_SETTINGS + " INTEGER PRIMARY KEY,"
                +KEY_DATEINICIO_SETTINGS + " INTEGER,"
                +KEY_DATEFIN_SETTINGS + " INTEGER,"
                +KEY_FECHASTRING_SETTINGS + " TEXT,"
                +KEY_HORAINICIOSTRING_SETTINGS + " TEXT,"
                +KEY_HORAFINSTRING_SETTINGS + " TEXT,"
                +KEY_JORNADAFINALIZADA_SETTINGS + " INTEGER,"
                +KEY_JORNADAINICIADA_SETTINGS + " INTEGER,"
                +KEY_ENVIODATOS_SETTINGS + " INTEGER"
                +")";
        db.execSQL(CREATE_SETTINGS_TABLE);

        String CREATE_TABLE_CATOLOGO_MALLAS = "CREATE TABLE "+ TABLE_MALLAS + " ("
                +KEY_ID_MALLAS + " TEXT PRIMARY KEY,"
                +KEY_SECTOR_MALLAS + " TEXT,"
                +KEY_MALLAS_MALLAS + " TEXT"
                +")";

        db.execSQL(CREATE_TABLE_CATOLOGO_MALLAS);

        String CREATE_TABLE_CUADRILLASREVISADAS = "CREATE TABLE "+ TABLE_CUADRILLAS_REVISADAS + " ("
                +KEY_ID_CUADRILLASREVISADAS + " INTEGER PRIMARY KEY,"
                +KEY_CUADRILLA_CUADRILLASREVISADAS + " Integer,"
                +KEY_RESPONSABLE_CUADRILLASREVISADAS+" TEXT,"
                +KEY_DATEINICIO_CUADRILLASREVISADAS + " Integer,"
                +KEY_FECHA_CUADRILLASREVISADAS+" TEXT,"
                +KEY_DATEFIN_CUADRILLASREVISADAS + " Integer"
                +")";

        db.execSQL(CREATE_TABLE_CUADRILLASREVISADAS);

        String CREATE_TABLE_CATOLOGO_PUESTOS = " CREATE TABLE "+ TABLE_PUESTOS + " ("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_PUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_PUESTOS);

        String CREATE_TABLE_TIPOS_PERMISOS= " CREATE TABLE "+ TABLE_TIPOS_PERMISOS + " ("
                + KEY_ID_TIPOSPERMISOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TIPOSPERMISOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TIPOS_PERMISOS);

        String CREATE_TABLE_CATOLOGO_ACTIVIDADES = " CREATE TABLE "+ TABLE_ACTIVIDADES + " ("
                + KEY_ID_ACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_ACTIVIDADES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_ACTIVIDADES);

        String CREATE_TABLE_CUADRILLAS = " CREATE TABLE "+ TABLE_TRABAJADORES + " ("
                + KEY_ID_TRABAJADORES + " INTEGER PRIMARY KEY,"
                +KEY_CUADRILLA_TRABAJADORES + " INTEGER,"
                +KEY_CONSECUTIVO_TRABAJADORES + " INTEGER,"
                +KEY_NOMBRE_TRABAJADORES + " TEXT,"
                +KEY_NUMERO_TRABAJADORES + " INTEGER,"
                +KEY_IDPUESTO_TRABAJADORES + " INTEGER,"
                +KEY_SENDED_TRABAJADORES + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_CUADRILLAS);

        String CREATE_TABLE_REPORTES = " CREATE TABLE "+ TABLE_REPORTES + " ("
                +KEY_ID_REPORTE + " INTEGER PRIMARY KEY,"
                +KEY_IDTRABAJADOR_REPORTE + " INTEGER,"
                +KEY_IDTIPOPERMISO_REPORTE + " INTEGER,"
                +KEY_FECHA_REPORTE + " TEXT,"
                +KEY_HORAINICIO_REPORTE + " TEXT,"
                +KEY_HORAFIN_REPORTE+" TEXT,"
                +KEY_SENDED_REPORTE+" INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_REPORTES);

        String CREATE_TABLE_ASISTENCIA = " CREATE TABLE "+ TABLE_ASISTENCIA + " ("
                +KEY_ID_ASISTECNIA + " INTEGER PRIMARY KEY,"
                +KEY_IDTRABAJADOR_ASISTECNIA + " INTEGER,"
                +KEY_IDPUESTO_ASISTECNIA + " INTEGER,"
                +KEY_DATEINICIO_ASISTECNIA+" INTEGER,"
                +KEY_DATEFIN_ASISTECNIA+" INTEGER,"
                +KEY_FECHA_ASISTECNIA + " TEXT,"
                +KEY_HORAINICIO_ASISTECNIA + " TEXT,"
                +KEY_HORAFIN_ASISTECNIA +" TEXT,"
                +KEY_SENDED_ASISTECNIA + " INTEGER"
                +")";
        db.execSQL(CREATE_TABLE_ASISTENCIA);

        String CREATE_TABLE_ACTIVIDADESREALIZADAS = " CREATE TABLE "+ TABLE_ACTIVIDADES_REALIZADAS + " ("
                +KEY_ID_ACTIVIDADESREALIZADAS + " INTEGER PRIMARY KEY,"
                +KEY_CUADRILLA_ACTIVIDADESREALIZADAS + " INTEGER,"
                +KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS + " INTEGER,"
                +KEY_IDMALLA_ACTIVIDADESREALIZADAS + " TEXT,"
                +KEY_FECHA_ACTIVIDADESREALIZADAS + " TEXT,"
                +KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS+" INTEGER,"
                +KEY_SENDED_ACTIVIDADESREALIZADAS + " INTEGER"
                +")";
        db.execSQL(CREATE_TABLE_ACTIVIDADESREALIZADAS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURACION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALLAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUESTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_PERMISOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES_REALIZADAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJADORES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASISTENCIA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUADRILLAS_REVISADAS);

        onCreate(db);
    }

    public void recrearTablaListaPuestos(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA ListaPuestos");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUESTOS);

        String CREATE_TABLE_CATOLOGO_PUESTOS = " CREATE TABLE "+ TABLE_PUESTOS + " ("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_PUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_PUESTOS);
    }

    public void recrearTablaListaActividades(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA Actividades");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);

        String CREATE_TABLE_CATOLOGO_ACTIVIDADES = " CREATE TABLE "+ TABLE_ACTIVIDADES + " ("
                + KEY_ID_ACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_ACTIVIDADES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_ACTIVIDADES);
    }

    public void recrearTablaListaMallas(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA mallas");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALLAS);

        String CREATE_TABLE_CATOLOGO_MALLAS = "CREATE TABLE "+ TABLE_MALLAS + " ("
                +KEY_ID_MALLAS + " TEXT PRIMARY KEY,"
                +KEY_SECTOR_MALLAS + " TEXT,"
                +KEY_MALLAS_MALLAS + " TEXT"
                +")";

        db.execSQL(CREATE_TABLE_CATOLOGO_MALLAS);
    }

    public void recrearTablacuadrillasRevisadas(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA mallas");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUADRILLAS_REVISADAS);

        String CREATE_TABLE_CUADRILLASREVISADAS = "CREATE TABLE "+ TABLE_CUADRILLAS_REVISADAS + " ("
                +KEY_ID_CUADRILLASREVISADAS + " INTEGER PRIMARY KEY,"
                +KEY_CUADRILLA_CUADRILLASREVISADAS + " Integer,"
                +KEY_RESPONSABLE_CUADRILLASREVISADAS + " TEXT,"
                +KEY_DATEINICIO_CUADRILLASREVISADAS + " Integer,"
                +KEY_FECHA_CUADRILLASREVISADAS+" TEXT,"
                +KEY_DATEFIN_CUADRILLASREVISADAS + " Integer"
                +")";

        db.execSQL(CREATE_TABLE_CUADRILLASREVISADAS);
    }

    public void recrearTablaListaPermisos(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA mallas");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_PERMISOS);

        String CREATE_TABLE_TIPOS_PERMISOS= " CREATE TABLE "+ TABLE_TIPOS_PERMISOS + " ("
                + KEY_ID_TIPOSPERMISOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TIPOSPERMISOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TIPOS_PERMISOS);
    }

    public void recrearTablaListaTrabajadores(){
        Log.v("RecrearPuestos","REINICIAR LA TABLA trabajadores");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRABAJADORES);

        String CREATE_TABLE_CUADRILLAS = " CREATE TABLE "+ TABLE_TRABAJADORES + " ("
                + KEY_ID_TRABAJADORES + " INTEGER PRIMARY KEY,"
                +KEY_CUADRILLA_TRABAJADORES + " INTEGER,"
                +KEY_CONSECUTIVO_TRABAJADORES + " INTEGER,"
                +KEY_NOMBRE_TRABAJADORES + " TEXT,"
                +KEY_NUMERO_TRABAJADORES + " INTEGER,"
                +KEY_IDPUESTO_TRABAJADORES + " INTEGER,"
                +KEY_SENDED_TRABAJADORES + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_CUADRILLAS);
    }

    /*******************************ADD*******************************************************/
    /*private void iniciarConexion(){
        db = this.getWritableDatabase();
    }*/

    /*private void cerrarConexion(){
        db.close(); // Closing database connection
    }*/

    /***
     * inserta configuracion
     * @param configuracion datos de tipo {@link Configuracion}
     * @return return -1 si corrio un error de lo contrara retortar un valor mayor que 0
     */
    public Long addConfiguracion(Configuracion configuracion){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_CONFIGURACION,configuracion.getId());
        values.put(KEY_URL_CONFIGURACION,configuracion.getUrl());

        Long insert = db.insert(TABLE_CONFIGURACION, null, values);
        if(insert !=-1)
            configuracion.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_CONFIGURACION);

        db.close();

        return insert;
    }

    public Long addSettings(Settings settings){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_SETTINGS ,settings.getId());
        values.put(KEY_DATEINICIO_SETTINGS , settings.getInicio().getTime());
        values.put(KEY_DATEFIN_SETTINGS ,settings.getFin().getTime());
        values.put(KEY_FECHASTRING_SETTINGS ,settings.getFecha());
        values.put(KEY_HORAINICIOSTRING_SETTINGS , settings.getHorainicio());
        values.put(KEY_HORAFINSTRING_SETTINGS , settings.getHoraFinal());
        values.put(KEY_JORNADAFINALIZADA_SETTINGS ,settings.getJornadaFinalizada());
        values.put(KEY_JORNADAINICIADA_SETTINGS,+ settings.getJornadaIniciada());
        values.put(KEY_ENVIODATOS_SETTINGS,settings.getEnvioDatos());

        Long insert = db.insert(TABLE_SETTINGS, null, values);
        if(insert !=-1)
            settings.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_SETTINGS);

        db.close();

        return insert;
    }

    public Long addMallas(Mallas mallas){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_MALLAS,mallas.getId());
        values.put(KEY_SECTOR_MALLAS,mallas.getSector());
        values.put(KEY_MALLAS_MALLAS,mallas.getMallas());

        Long insert = db.insert(TABLE_MALLAS, null, values);

        if(insert == -1)
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_MALLAS);

        db.close();

        return insert;
    }

    public Long addPuestos(Puestos puestos){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_PUESTOS,puestos.getId());
        values.put(KEY_DESCRIPCION_PUESTOS,puestos.getNombre());

        Long insert = db.insert(TABLE_PUESTOS, null, values);

        if(insert !=-1)
            puestos.setId(Integer.valueOf(insert.toString()));
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_PUESTOS);

        db.close();

        return insert;
    }

    public Long addTiposPermisos(TiposPermisos tiposPermisos){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_TIPOSPERMISOS,tiposPermisos.getId());
        values.put(KEY_DESCRIPCION_TIPOSPERMISOS,tiposPermisos.getNombre());

        Long insert = db.insert(TABLE_TIPOS_PERMISOS, null, values);

        if(insert !=-1)
            tiposPermisos.setId(Integer.parseInt(insert.toString()));
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_TIPOS_PERMISOS);

        db.close();

        return insert;
    }

    public Long addActividad(Actividades actividades){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_ACTIVIDADES,actividades.getId());
        values.put(KEY_DESCRIPCION_ACTIVIDADES,actividades.getNombre());

        Long insert = db.insert(TABLE_ACTIVIDADES, null, values);

        if(insert !=-1)
            actividades.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_ACTIVIDADES);

        db.close();

        return insert;
    }

    public Long addTrabajador(Trabajadores trabajadores){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(KEY_ID_TRABAJADORES,trabajadores.getId());
        values.put(KEY_CONSECUTIVO_TRABAJADORES,trabajadores.getConsecutivo());
        values.put(KEY_CUADRILLA_TRABAJADORES,trabajadores.getCuadrilla());
        values.put(KEY_NOMBRE_TRABAJADORES,trabajadores.getNombre());
        values.put(KEY_NUMERO_TRABAJADORES,trabajadores.getNumero());
        values.put(KEY_IDPUESTO_TRABAJADORES,trabajadores.getPuesto().getId());
        values.put(KEY_SENDED_TRABAJADORES,trabajadores.getSended());

        Long insert = db.insert(TABLE_TRABAJADORES, null, values);

        if(insert !=-1)
            trabajadores.setId(Integer.parseInt(insert.toString()));
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_TRABAJADORES);

        db.close();

        return insert;
    }

    public Long addReportes(Reportes reportes){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_REPORTE,reportes.getId());
        values.put(KEY_IDTRABAJADOR_REPORTE,reportes.getTrabajadores().getId());
        values.put(KEY_IDTIPOPERMISO_REPORTE,reportes.getTiposPermisos().getId());
        values.put(KEY_FECHA_REPORTE,reportes.getFecha());
        values.put(KEY_HORAINICIO_REPORTE,reportes.getHoraInicial());
        values.put(KEY_HORAFIN_REPORTE,reportes.getHoraFinal());
        values.put(KEY_SENDED_REPORTE,reportes.getSended());

        Long insert = db.insert(TABLE_REPORTES, null, values);

        if(insert !=-1)
            reportes.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_REPORTES);

        db.close();

        return insert;
    }

    public Long addAsistencia(Asistencia asistencia){

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_ASISTECNIA,asistencia.getId());
        values.put(KEY_IDTRABAJADOR_ASISTECNIA,asistencia.getTrabajador().getId());
        values.put(KEY_IDPUESTO_ASISTECNIA,asistencia.getPuesto().getId());
        values.put(KEY_DATEINICIO_ASISTECNIA,asistencia.getDateInicio().getTime());
        values.put(KEY_DATEFIN_ASISTECNIA,asistencia.getDateFin()!=null?asistencia.getDateFin().getTime():0);
        values.put(KEY_FECHA_ASISTECNIA,asistencia.getFecha());
        values.put(KEY_HORAINICIO_ASISTECNIA,asistencia.getHoraInicio());
        values.put(KEY_HORAFIN_ASISTECNIA,asistencia.getHoraFinal());
        values.put(KEY_SENDED_ASISTECNIA,asistencia.getSended());

        Long insert = db.insert(TABLE_ASISTENCIA, null, values);

        if(insert !=-1)
            asistencia.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_ASISTENCIA);

        db.close();

        return insert;
    }

    public Long addActividadesRealizadas(ActividadesRealizadas actividadesRealizadas){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_ACTIVIDADESREALIZADAS,actividadesRealizadas.getId());
        values.put(KEY_CUADRILLA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getCuadrlla());
        values.put(KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS,actividadesRealizadas.getActividad().getId());
        values.put(KEY_IDMALLA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getMalla().getId());
        values.put(KEY_FECHA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getFecha());
        values.put(KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS,actividadesRealizadas.getTipoActividad());
        values.put(KEY_SENDED_ACTIVIDADESREALIZADAS,actividadesRealizadas.getSended());

        Long insert = db.insert(TABLE_ACTIVIDADES_REALIZADAS, null, values);

        if(insert !=-1)
            actividadesRealizadas.setId(insert);
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_ACTIVIDADES_REALIZADAS);

        db.close();

        return insert;
    }

    public Long addCuadrillaRevisada(Cuadrillas cuadrillas){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(KEY_CUADRILLA_CUADRILLASREVISADAS,cuadrillas.getCuadrilla());
        values.put(KEY_RESPONSABLE_CUADRILLASREVISADAS,cuadrillas.getMayordomo());
        values.put(KEY_DATEINICIO_CUADRILLASREVISADAS,cuadrillas.getFechaInicio().getTime());
        values.put(KEY_FECHA_CUADRILLASREVISADAS,cuadrillas.getFecha());
        values.put(KEY_DATEFIN_CUADRILLASREVISADAS,cuadrillas.getFechaFin().getTime());

        Long insert = db.insert(TABLE_CUADRILLAS_REVISADAS, null, values);

        if(insert !=-1)
            cuadrillas.setId(Integer.valueOf(insert.toString()));
        else
            Log.e("ERROR_DB","error en la inserion de datos en la tabla"+ TABLE_CUADRILLAS_REVISADAS);

        db.close();

        return insert;
    }
    /*******************************GET*******************************************************/
    public Cuadrillas getCuadrilla(String c) {

        Cuadrillas cuadrillas = null;

        String selectQuery = "SELECT * FROM " + TABLE_CUADRILLAS_REVISADAS +" WHERE "+KEY_CUADRILLA_CUADRILLASREVISADAS+" = '"+c+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cuadrillas = new Cuadrillas(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(cuadrillas != null)
            Log.v("RESULTADO_DB",cuadrillas.toString());
        return cuadrillas;
    }

    public Configuracion getConfiguracion(Long id) {

        Configuracion configuracion = null;

        String selectQuery = "SELECT * FROM " + TABLE_CONFIGURACION +" WHERE "+KEY_ID_CONFIGURACION+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                configuracion = new Configuracion(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(configuracion != null)
            Log.v("RESULTADO_DB",configuracion.toString());
        return configuracion;
    }

    public Settings getSetting(Long id) {

        Settings settings = null;

        String selectQuery = "SELECT * FROM " + TABLE_SETTINGS +" WHERE "+KEY_ID_SETTINGS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                settings = new Settings(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(settings != null)
            Log.v("RESULTADO_DB" ,settings.toString());
        return settings;
    }

    public Mallas getMallas(String id) {

        Mallas mallas = null;

        String selectQuery = "SELECT * FROM " + TABLE_MALLAS +" WHERE "+KEY_ID_MALLAS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mallas = new Mallas(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(mallas != null)
            Log.v("RESULTADO_DB" ,mallas.toString());

        return mallas;
    }

    public ArrayList<Mallas> getMallas() {

        ArrayList<Mallas> mallas = new ArrayList<Mallas>() ;

        String selectQuery = "SELECT * FROM " + TABLE_MALLAS +" ORDER BY "+KEY_ID_MALLAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mallas.add(new Mallas(cursor)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(mallas.size() !=  0)
            Log.v("RESULTADO_DB" ,""+mallas.size());

        return mallas;
    }

    public ArrayList<String> getSectores() {

        ArrayList<String> sectores = new ArrayList<String>() ;

        String selectQuery = "SELECT "+KEY_SECTOR_MALLAS+" FROM " + TABLE_MALLAS +" GROUP BY "+KEY_SECTOR_MALLAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                sectores.add(cursor.getString(0)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(sectores.size() !=  0)
            Log.v("RESULTADO_DB" ,""+sectores.size());

        return sectores;
    }

    public ArrayList<Mallas> getMallasXsector(String sector) {

        ArrayList<Mallas> mallas = new ArrayList<Mallas>() ;

        String selectQuery = "SELECT * FROM " + TABLE_MALLAS +" WHERE "+KEY_SECTOR_MALLAS+" = '"+sector+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mallas.add(new Mallas(cursor)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(mallas.size() !=  0)
            Log.v("RESULTADO_DB" ,""+mallas.size());

        return mallas;
    }

    public Puestos getPuestos(Integer id) {

        Puestos puesto = null;

        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS +" WHERE "+KEY_ID_PUESTOS+" = '"+id+"' ";


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puesto = new Puestos(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(puesto != null)
            Log.v("RESULTADO_DB" ,puesto.toString());

        return puesto;
    }

    public Puestos getPuestos(String descripcion) {

        Puestos puesto = null;

        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS +" WHERE "+KEY_DESCRIPCION_PUESTOS+" = '"+descripcion+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puesto = new Puestos(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        if(puesto==null){
            puesto = getPuestos(2);
        }

        if(puesto != null)
            Log.v("RESULTADO_DB" ,puesto.toString());

        return puesto;
    }

    public ArrayList<Puestos> getPuestos() {

        ArrayList<Puestos> puestos = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_PUESTOS +" ORDER BY "+KEY_DESCRIPCION_PUESTOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                puestos.add(new Puestos(cursor)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(puestos.size() ==  0)
            Log.v("RESULTADO_DB" ,""+puestos.size());

        return puestos;
    }

    public TiposPermisos getTiposPermisos(Long id) {

        TiposPermisos tiposPermiso = null;

        String selectQuery = "SELECT * FROM " + TABLE_TIPOS_PERMISOS +" WHERE "+KEY_ID_TIPOSPERMISOS+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tiposPermiso = new TiposPermisos(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(tiposPermiso != null)
            Log.v("RESULTADO_DB" ,tiposPermiso.toString());

        return tiposPermiso;
    }

    public ArrayList<TiposPermisos> getTiposPermisos() {

        ArrayList<TiposPermisos> tiposPermisos = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_TIPOS_PERMISOS +" ORDER BY "+KEY_DESCRIPCION_TIPOSPERMISOS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tiposPermisos.add(new TiposPermisos(cursor)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(tiposPermisos.size() ==  0)
            Log.v("RESULTADO_DB" ,""+tiposPermisos.size());

        return tiposPermisos;
    }
    public Actividades getActividades(Long id) {

        Actividades actividad = null;

        String selectQuery = "SELECT * FROM " + TABLE_ACTIVIDADES +" WHERE "+KEY_ID_ACTIVIDADES+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actividad = new Actividades(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(actividad != null)
            Log.v("RESULTADO_DB" ,actividad.toString());

        return actividad;
    }

    public ArrayList<Actividades> getActividades() {

        ArrayList<Actividades> actividades = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_ACTIVIDADES +" ORDER BY "+KEY_DESCRIPCION_ACTIVIDADES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actividades.add(new Actividades(cursor)) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(actividades.size() ==  0)
            Log.v("RESULTADO_DB" ,""+actividades.size());

        return actividades;
    }

    public Trabajadores getTrabajadores(Long id) {

        Trabajadores trabajador = null;

        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_ID_TRABAJADORES+" = '"+id+"' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajador = new Trabajadores(cursor,getPuestos(cursor.getInt(5)));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(trabajador != null)
            Log.v("RESULTADO_DB" ,trabajador.toString());

        return trabajador;
    }

    public Trabajadores getTrabajadores(Integer cuadrilla,Integer consecutivo) {

        Trabajadores trabajador = null;

        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_CUADRILLA_TRABAJADORES+" = '"+cuadrilla+"' AND "+KEY_CONSECUTIVO_TRABAJADORES+" = "+consecutivo;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("sql",selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajador = new Trabajadores(cursor,getPuestos(cursor.getInt(5)));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(trabajador != null)
            Log.v("RESULTADO_DB" ,trabajador.toString());

        return trabajador;
    }

    public Integer getConsecutivo(Integer cuadrilla) {

        Integer consecutivo=0;

        String selectQuery = "SELECT "+KEY_CONSECUTIVO_TRABAJADORES+" FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_CUADRILLA_TRABAJADORES+" = '"+cuadrilla+"' ORDER BY "+KEY_CONSECUTIVO_TRABAJADORES +" desc limit 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                consecutivo = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        return consecutivo+1;
    }

    public ArrayList<Trabajadores> getTrabajadoresCuadrilla(Integer cuadrilla) {

        ArrayList<Trabajadores> trabajadores = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_CUADRILLA_TRABAJADORES+" = "+cuadrilla +" ORDER BY "+KEY_CONSECUTIVO_TRABAJADORES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajadores.add(new Trabajadores(cursor,getPuestos(cursor.getInt(5)))) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(trabajadores.size() ==  0)
            Log.v("RESULTADO_DB" ,""+trabajadores.size());

        return trabajadores;
    }

    public ArrayList<Cuadrillas> getCuadrillas() {

        ArrayList<Cuadrillas> cuadrillas = new ArrayList<>() ;

        String selectQuery = "SELECT "+KEY_CUADRILLA_TRABAJADORES+" FROM " + TABLE_TRABAJADORES +" GROUP BY "+KEY_CUADRILLA_TRABAJADORES+" ORDER BY "+KEY_CUADRILLA_TRABAJADORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Log.i("sql",selectQuery);
        if (cursor.moveToFirst()) {
            do {
                Integer c = cursor.getInt(0);
                //Cuadrillas cuadrillas1 = new Cuadrillas(c, getTrabajadores(c, 1).getNombre());
                Cuadrillas  cuadrillas1 = getCuadrilla(c.toString());
                if(cuadrillas1==null){
                    cuadrillas1 = new Cuadrillas(c, getTrabajadores(c, 1).getNombre());
                }
                cuadrillas.add(cuadrillas1);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(cuadrillas.size() ==  0)
            Log.v("RESULTADO_DB" ,""+cuadrillas.size());

        return cuadrillas;
    }

    public Cuadrillas getCuadrillaActiva(String fecha,Integer c){
        Cuadrillas cuadrilla = null;

        String selectQuery = "SELECT * FROM " + TABLE_CUADRILLAS_REVISADAS +" WHERE "+KEY_FECHA_CUADRILLASREVISADAS+" = '"+fecha+"' AND "+KEY_CUADRILLA_CUADRILLASREVISADAS+" = "+c;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               cuadrilla = new Cuadrillas(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(cuadrilla !=  null)
            Log.v("RESULTADO_DB" ," id cuadrilla"+cuadrilla.getId());

        return cuadrilla;
    }

    public ArrayList<Reportes> getReportesTrabajador(String fecha ,Long idTrabajaor) {

        ArrayList<Reportes> reportesTrabajador = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_REPORTES +" WHERE "+ KEY_IDTRABAJADOR_REPORTE +" = '"+idTrabajaor+"' AND "+KEY_FECHA_REPORTE+" = "+fecha;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reportesTrabajador.add(new Reportes(cursor,getTrabajadores(idTrabajaor),getTiposPermisos(cursor.getLong(2))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(reportesTrabajador != null)
            Log.v("RESULTADO_DB" ,reportesTrabajador.toString());

        return reportesTrabajador;
    }

    public  ArrayList<Reportes> getReportesTrabajador(String fecha) {

        ArrayList<Reportes> reportes = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_REPORTES +" WHERE "+KEY_FECHA_REPORTE+" = "+fecha;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reportes.add(new Reportes(cursor,getTrabajadores(cursor.getLong(1)),getTiposPermisos(cursor.getLong(2)))) ;
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(reportes.size() ==  0)
            Log.v("RESULTADO_DB" ,""+reportes.size());

        return reportes;
    }

    public ArrayList<Asistencia> getAsistencia(String fecha) {

        ArrayList<Asistencia> asistencias = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_ASISTENCIA +" WHERE "+KEY_FECHA_ASISTECNIA+" = '"+fecha+"' ORDER BY "+KEY_IDTRABAJADOR_ASISTECNIA;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                asistencias.add(new Asistencia(cursor,getTrabajadores(cursor.getLong(1)),getPuestos(cursor.getInt(2))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(asistencias.size() ==  0)
            Log.v("RESULTADO_DB" ,""+asistencias.size());

        return asistencias;
    }

    public ArrayList<Asistencia> getAsistencia(String fecha,Trabajadores trabajador) {

        ArrayList<Asistencia> asistencias = new ArrayList<>() ;

        String selectQuery = "SELECT * FROM " + TABLE_ASISTENCIA +" WHERE "+KEY_IDTRABAJADOR_ASISTECNIA+" = "+trabajador.getId() +" AND "+KEY_FECHA_ASISTECNIA+" = '"+fecha+"' ORDER BY "+KEY_HORAINICIO_ASISTECNIA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                asistencias.add(new Asistencia(cursor,trabajador,getPuestos(cursor.getInt(2))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(asistencias.size() ==  0)
            Log.v("RESULTADO_DB" ,""+asistencias.size());

        return asistencias;
    }

    public ArrayList<ListaAsistencia> getAsistencia(String fecha, Cuadrillas cuadrilla) {

        ArrayList<ListaAsistencia> listaAsistencias = new ArrayList<>() ;

        ArrayList<Trabajadores> trabajadoresCuadrilla = getTrabajadoresCuadrilla(cuadrilla.getCuadrilla());
        for (Trabajadores t : trabajadoresCuadrilla) {
            ArrayList<Asistencia> asistencia = getAsistencia(fecha, t);

            if(asistencia.size()!=0){
                for (Asistencia a : asistencia) {
                    listaAsistencias.add(new ListaAsistencia(t,a));
                }
            }else{
                listaAsistencias.add(new ListaAsistencia(t, new Asistencia(t, t.getPuesto(), cuadrilla.getFechaInicio(), new Date(0), 0)));
            }

        }

        if(listaAsistencias.size() ==  0)
            Log.v("RESULTADO_DB" ,""+listaAsistencias.size());

        return listaAsistencias;
    }

    public ArrayList<ListaAsistencia> getAsistenciaTrabajador(String fecha,Integer idTrabajador) {

        ArrayList<ListaAsistencia> listaAsistencias = new ArrayList<>() ;

        Trabajadores t = getTrabajadores(Long.valueOf(idTrabajador));

        ArrayList<Asistencia> asistencia = getAsistencia(fecha, t);

        if(asistencia.size()!=0){
            for (Asistencia a : asistencia) {
                listaAsistencias.add(new ListaAsistencia(t,a));
            }
        }else{
            listaAsistencias.add(new ListaAsistencia(t,null));
        }

        if(listaAsistencias.size() ==  0)
            Log.v("RESULTADO_DB" ,""+listaAsistencias.size());

        return listaAsistencias;
    }

    public ArrayList<Cuadrillas> getAsistenciaCuadrillas(String fecha) {

        ArrayList<Cuadrillas> cuadrillas = new ArrayList<>() ;

        String selectQuery = "SELECT t."+KEY_CUADRILLA_TRABAJADORES+", t."+KEY_NOMBRE_TRABAJADORES
                +" FROM " + TABLE_ASISTENCIA +"as a "
                +" INNER JOIN "+TABLE_TRABAJADORES+" as t on t."+KEY_ID_TRABAJADORES+" = a."+KEY_IDTRABAJADOR_ASISTECNIA
                +" WHERE a."+KEY_FECHA_ASISTECNIA+" = "+fecha+" AND a."+KEY_IDPUESTO_ASISTECNIA+"=3"
                +" ORDER BY t."+KEY_CUADRILLA_TRABAJADORES
                +" GROUP BY t."+KEY_CUADRILLA_TRABAJADORES+",t."+KEY_NOMBRE_TRABAJADORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cuadrillas.add(new Cuadrillas(cursor.getInt(0),cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(cuadrillas.size() ==  0)
            Log.v("RESULTADO_DB" ,""+cuadrillas.size());

        return cuadrillas;
    }


    public  ArrayList<ActividadesRealizadas> getActividadesRealizadas(String fecha, Cuadrillas cuadrilla) {

        ArrayList<ActividadesRealizadas> actividadesRealizadas = new ArrayList<>() ;

        String selectQuery = "SELECT *  FROM " + TABLE_ACTIVIDADES_REALIZADAS +" WHERE "+KEY_FECHA_REPORTE+" = '"+fecha+"' AND "+KEY_CUADRILLA_ACTIVIDADESREALIZADAS+" = "+cuadrilla.getCuadrilla();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                actividadesRealizadas.add(new ActividadesRealizadas(cursor,getActividades(cursor.getLong(2)),getMallas(cursor.getString(3))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if(actividadesRealizadas.size() ==  0)
            Log.v("RESULTADO_DB" ,""+actividadesRealizadas.size());

        return actividadesRealizadas;
    }

    /*******************************UPDATE*******************************************************/
    public int updateConfiguracion(Configuracion configuracion){
        Log.v("UPDATE DB",configuracion.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_URL_CONFIGURACION,configuracion.getUrl());

            i = db.update(TABLE_CONFIGURACION, values, KEY_ID_CONFIGURACION + " = ?",
                    new String[]{String.valueOf(configuracion.getId().toString())});
            db.close();
        }catch (Exception e){
            Log.v("ERROR DB","error "+e.getMessage());
        }
        return i;
    }

    public int updateSetting(Settings settings){
        Log.v("UPDATE DB",settings.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_DATEINICIO_SETTINGS, settings.getInicio().getTime());
            values.put(KEY_DATEFIN_SETTINGS, settings.getFin().getTime());
            values.put(KEY_FECHASTRING_SETTINGS,settings.getFecha());
            values.put(KEY_HORAINICIOSTRING_SETTINGS, settings.getHorainicio());
            values.put(KEY_HORAFINSTRING_SETTINGS, settings.getHoraFinal());
            values.put(KEY_JORNADAFINALIZADA_SETTINGS, settings.getJornadaFinalizada());
            values.put(KEY_JORNADAINICIADA_SETTINGS, settings.getJornadaIniciada());
            values.put(KEY_ENVIODATOS_SETTINGS, settings.getEnvioDatos());

            i = db.update(TABLE_SETTINGS, values, KEY_ID_SETTINGS + " = ?",
                    new String[]{String.valueOf(settings.getId().toString())});
            db.close();
        }catch (Exception e){
            Log.v("ERROR DB",e.getMessage());
        }
        return i;
    }

    public int UpdateTrabajador(Trabajadores trabajadores){
        Log.v("UPDATE DB",trabajadores.toString());
        int i = -1;
        try{
            SQLiteDatabase  db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CONSECUTIVO_TRABAJADORES,trabajadores.getConsecutivo());
            values.put(KEY_CUADRILLA_TRABAJADORES,trabajadores.getCuadrilla());
            values.put(KEY_NOMBRE_TRABAJADORES,trabajadores.getNombre());
            values.put(KEY_NUMERO_TRABAJADORES,trabajadores.getNumero());
            values.put(KEY_IDPUESTO_TRABAJADORES,trabajadores.getPuesto().getId());
            values.put(KEY_SENDED_TRABAJADORES,trabajadores.getSended());

            i = db.update(TABLE_TRABAJADORES, values, KEY_ID_TRABAJADORES + " = ?",
                    new String[]{String.valueOf(trabajadores.getId().toString())});

            db.close();
        }catch (Exception e){
            Log.v("ERROR DB",e.getMessage());
        }

        return i;


    }

    public int updateReportes(Reportes reportes){
        Log.v("UPDATE DB",reportes.toString());
        int i = -1;
        try{
            SQLiteDatabase  db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

        values.put(KEY_IDTRABAJADOR_REPORTE,reportes.getTrabajadores().getId());
        values.put(KEY_IDTIPOPERMISO_REPORTE,reportes.getTiposPermisos().getId());
        values.put(KEY_FECHA_REPORTE,reportes.getFecha());
        values.put(KEY_HORAINICIO_REPORTE,reportes.getHoraInicial());
        values.put(KEY_HORAFIN_REPORTE,reportes.getHoraFinal());
        values.put(KEY_SENDED_REPORTE,reportes.getSended());


        i = db.update(TABLE_REPORTES, values, KEY_ID_REPORTE + " = ?",
                new String[]{String.valueOf(reportes.getId().toString())});

            db.close();
        }catch (Exception e){
            Log.v("ERROR DB",e.getMessage());
        }

        return i;
    }

    public int updateAsistencia(Asistencia asistencia){
        Log.v("UPDATE DB",asistencia.toString());
        int i = -1;
        try{
            SQLiteDatabase  db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_IDTRABAJADOR_ASISTECNIA,asistencia.getTrabajador().getId());
            values.put(KEY_IDPUESTO_ASISTECNIA,asistencia.getPuesto().getId());
            values.put(KEY_DATEINICIO_ASISTECNIA,asistencia.getDateInicio().getTime());
            values.put(KEY_DATEFIN_ASISTECNIA,asistencia.getDateFin().getTime());
            values.put(KEY_FECHA_ASISTECNIA,asistencia.getFecha());
            values.put(KEY_HORAINICIO_ASISTECNIA,asistencia.getHoraInicio());
            values.put(KEY_HORAFIN_ASISTECNIA,asistencia.getHoraFinal());
            values.put(KEY_SENDED_ASISTECNIA,asistencia.getSended());


            i = db.update(TABLE_ASISTENCIA, values, KEY_ID_ASISTECNIA + " = ?",
                    new String[]{String.valueOf(asistencia.getId().toString())});

            db.close();
        }catch (Exception e){
            Log.v("ERROR DB",e.getMessage());
        }

        return i;
    }

    public int updateActividadesRealizadas(ActividadesRealizadas actividadesRealizadas){
        Log.v("UPDATE DB",actividadesRealizadas.toString());
        int i = -1;
        try{
            SQLiteDatabase  db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CUADRILLA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getCuadrlla());
            values.put(KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS,actividadesRealizadas.getActividad().getId());
            values.put(KEY_IDMALLA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getMalla().getId());
            values.put(KEY_FECHA_ACTIVIDADESREALIZADAS,actividadesRealizadas.getFecha());
            values.put(KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS,actividadesRealizadas.getTipoActividad());
            values.put(KEY_SENDED_ACTIVIDADESREALIZADAS,actividadesRealizadas.getSended());


            i = db.update(TABLE_ACTIVIDADES_REALIZADAS, values, KEY_ID_ACTIVIDADESREALIZADAS + " = ?",
                    new String[]{String.valueOf(actividadesRealizadas.getId().toString())});

            db.close();
        }catch (Exception e){
            Log.v("ERROR DB",e.getMessage()+"");
        }

        return i;
    }

    /************************DELETES*******************************************************/

    public void deleteAsistencias(String fecha,String idTrabajador){
        SQLiteDatabase  db = this.getWritableDatabase();
        int delete = db.delete(TABLE_ASISTENCIA, KEY_FECHA_ASISTECNIA + " =? AND " + KEY_IDTRABAJADOR_ASISTECNIA + " =? ", new String[]{fecha, idTrabajador});
        db.close();
    }

}
