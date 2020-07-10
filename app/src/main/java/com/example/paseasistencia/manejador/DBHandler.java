package com.example.paseasistencia.manejador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.paseasistencia.complementos.KeyValues;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.MallasRealizadas;
import com.example.paseasistencia.model.Asistencia;
import com.example.paseasistencia.model.Configuracion;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.model.ListaAsistencia;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.Reportes;
import com.example.paseasistencia.model.Settings;
import com.example.paseasistencia.model.TiposActividades;
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
    private static final String TABLE_TIPOS_ACTIVIDADES = "TiposActividades";

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
    private static final String KEY_DATEACTUALIZACIOIN_SETTINGS = "DateActualizacion";//INDICADOR DE INFORMACION PENDIENTE

    //cuadrillas
    private static final String KEY_ID_CUADRILLASREVISADAS = "id";
    private static final String KEY_CUADRILLA_CUADRILLASREVISADAS = "Cuadrilla";
    private static final String KEY_RESPONSABLE_CUADRILLASREVISADAS = "Responsable";
    private static final String KEY_DATEINICIO_CUADRILLASREVISADAS = "DateInicio";
    private static final String KEY_FECHA_CUADRILLASREVISADAS = "Fecha";
    private static final String KEY_DATEFIN_CUADRILLASREVISADAS = "DateFin";
    private static final String KEY_SENDED_CUADRILLASREVISADAS = "Sended";

    // catalogo actividades Table Columns names
    private static final String KEY_ID_ACTIVIDADES = "id";
    private static final String KEY_DESCRIPCION_ACTIVIDADES = "Descripcion";

    // catalogo actividades Table Columns names
    private static final String KEY_ID_TIPOSACTIVIDADES = "id";
    private static final String KEY_DESCRIPCION_TIPOSACTIVIDADES = "Descripcion";

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

    private static final String TAG = "DBHandler";
    //private SQLiteDatabase db;

    public DBHandler(final Context context) {

        super(new DatabaseContext(context), KeyValues.MY_DATABASE_NAME+KeyValues.EXTENCIO_DATABASE, null, KeyValues.DATABASE_VERSION);
        FileLog.i(TAG, "iniciar db");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FileLog.i(TAG, "creacion de tablas");

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
                + KEY_ENVIODATOS_SETTINGS + " INTEGER,"
                + KEY_DATEACTUALIZACIOIN_SETTINGS + " INTEGER"
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
                + KEY_DATEFIN_CUADRILLASREVISADAS + " Integer,"
                + KEY_SENDED_CUADRILLASREVISADAS + " Integer"
                +")";

        db.execSQL(CREATE_TABLE_CUADRILLASREVISADAS);

        String CREATE_TABLE_CATOLOGO_PUESTOS = " CREATE TABLE "+ TABLE_PUESTOS + " ("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_PUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_PUESTOS);

        String CREATE_TABLE_TIPOS_ACTIVIDADES = " CREATE TABLE " + TABLE_TIPOS_ACTIVIDADES + " ("
                + KEY_ID_TIPOSACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TIPOSACTIVIDADES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TIPOS_ACTIVIDADES);


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
        FileLog.i(TAG, "actualizacion de tablas");

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_ACTIVIDADES);

        onCreate(db);
    }

    public void recrearTablaListaPuestos(){
        FileLog.i(TAG, "REINICIAR LA TABLA ListaPuestos");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUESTOS);

        String CREATE_TABLE_CATOLOGO_PUESTOS = " CREATE TABLE "+ TABLE_PUESTOS + " ("
                + KEY_ID_PUESTOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_PUESTOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_PUESTOS);
    }

    public void recrearTablaTiposActividades() {
        FileLog.i(TAG, "REINICIAR LA TABLA tipos actividades");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_ACTIVIDADES);

        String CREATE_TABLE_TIPOS_ACTIVIDADES = " CREATE TABLE " + TABLE_TIPOS_ACTIVIDADES + " ("
                + KEY_ID_TIPOSACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TIPOSACTIVIDADES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TIPOS_ACTIVIDADES);
    }

    public void recrearTablaListaActividades(){
        FileLog.v(TAG, "REINICIAR LA TABLA Actividades");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);

        String CREATE_TABLE_CATOLOGO_ACTIVIDADES = " CREATE TABLE "+ TABLE_ACTIVIDADES + " ("
                + KEY_ID_ACTIVIDADES + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_ACTIVIDADES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATOLOGO_ACTIVIDADES);
    }

    public void recrearTablaListaMallas(){
        FileLog.v(TAG, "REINICIAR LA TABLA mallas");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALLAS);

        String CREATE_TABLE_CATOLOGO_MALLAS = "CREATE TABLE "+ TABLE_MALLAS + " ("
                +KEY_ID_MALLAS + " TEXT PRIMARY KEY,"
                +KEY_SECTOR_MALLAS + " TEXT,"
                +KEY_MALLAS_MALLAS + " TEXT"
                +")";

        db.execSQL(CREATE_TABLE_CATOLOGO_MALLAS);
    }

    public void recrearTablaListaPermisos(){
        FileLog.v(TAG, "REINICIAR LA TABLA mallas");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIPOS_PERMISOS);

        String CREATE_TABLE_TIPOS_PERMISOS= " CREATE TABLE "+ TABLE_TIPOS_PERMISOS + " ("
                + KEY_ID_TIPOSPERMISOS + " INTEGER PRIMARY KEY,"
                + KEY_DESCRIPCION_TIPOSPERMISOS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_TIPOS_PERMISOS);
    }

    public void recrearTablaListaTrabajadores(){
        FileLog.v(TAG, "REINICIAR LA TABLA trabajadores");
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

    /***
     * inserta configuracion
     * @param configuracion datos de tipo {@link Configuracion}
     * @return return -1 si corrio un error de lo contrara retortar un valor mayor que 0
     */
    public Long addConfiguracion(Configuracion configuracion){
        FileLog.i(TAG, "agregar configuracion " + configuracion.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_CONFIGURACION,configuracion.getId());
        values.put(KEY_URL_CONFIGURACION,configuracion.getUrl());

        Long insert = db.insert(TABLE_CONFIGURACION, null, values);
        if(insert !=-1)
            configuracion.setId(insert);
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_CONFIGURACION);

        db.close();

        return insert;
    }

    public Long addSettings(Settings settings){
        FileLog.i(TAG, "agregar settings " + settings.toString());
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
        values.put(KEY_DATEACTUALIZACIOIN_SETTINGS, settings.getFechaActualizacion().getTime());

        Long insert = db.insert(TABLE_SETTINGS, null, values);
        if(insert !=-1)
            settings.setId(insert);
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_SETTINGS);

        db.close();

        return insert;
    }

    public Long addMallas(Mallas mallas){
        FileLog.i(TAG, "agregar Mallas " + mallas.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_MALLAS,mallas.getId());
        values.put(KEY_SECTOR_MALLAS,mallas.getSector());
        values.put(KEY_MALLAS_MALLAS,mallas.getMallas());

        Long insert = db.insert(TABLE_MALLAS, null, values);

        if(insert == -1)
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_MALLAS);

        db.close();

        return insert;
    }

    public Long addPuestos(Puestos puestos){
        FileLog.i(TAG, "agregar catalogo Puestos " + puestos.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_PUESTOS,puestos.getId());
        values.put(KEY_DESCRIPCION_PUESTOS,puestos.getNombre());

        Long insert = db.insert(TABLE_PUESTOS, null, values);

        if(insert !=-1)
            puestos.setId(Integer.valueOf(insert.toString()));
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_PUESTOS);

        db.close();

        return insert;
    }

    public Long addTiposActividades(TiposActividades tiposActividades) {
        FileLog.i(TAG, "agregar catalogo Puestos " + tiposActividades.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_TIPOSACTIVIDADES, tiposActividades.getId());
        values.put(KEY_DESCRIPCION_TIPOSACTIVIDADES, tiposActividades.getDescripcion());

        Long insert = db.insert(TABLE_TIPOS_ACTIVIDADES, null, values);

        if (insert == -1)
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_TIPOS_ACTIVIDADES);

        db.close();

        return insert;
    }

    public Long addTiposPermisos(TiposPermisos tiposPermisos){
        FileLog.i(TAG, "agregar catalogo Tipos de permisos " + tiposPermisos.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_TIPOSPERMISOS,tiposPermisos.getId());
        values.put(KEY_DESCRIPCION_TIPOSPERMISOS,tiposPermisos.getNombre());

        Long insert = db.insert(TABLE_TIPOS_PERMISOS, null, values);

        if(insert !=-1)
            tiposPermisos.setId(Integer.parseInt(insert.toString()));
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_TIPOS_PERMISOS);

        db.close();

        return insert;
    }

    public Long addActividad(Actividades actividades){
        FileLog.i(TAG, "agregar catalogo actividades " + actividades.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ID_ACTIVIDADES,actividades.getId());
        values.put(KEY_DESCRIPCION_ACTIVIDADES,actividades.getNombre());

        Long insert = db.insert(TABLE_ACTIVIDADES, null, values);

        if(insert !=-1)
            actividades.setId(insert);
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_ACTIVIDADES);

        db.close();

        return insert;
    }

    public Long addTrabajador(Trabajadores trabajadores){
        FileLog.i(TAG, "agregar trabajador " + trabajadores.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

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
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_TRABAJADORES);

        db.close();

        return insert;
    }

    public Long addReportes(Reportes reportes){
        FileLog.i(TAG, "agregar reporte " + reportes.toString());
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
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_REPORTES);

        db.close();

        return insert;
    }

    public Long addAsistencia(Asistencia asistencia){
        FileLog.i(TAG, "agregar Asistencia " + asistencia.toString1());
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
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_ASISTENCIA);

        db.close();

        return insert;
    }

    public Long addActividadesRealizadas(MallasRealizadas mallasRealizadas) {
        FileLog.i(TAG, "agregar actividades realizadas " + mallasRealizadas.toString());
        SQLiteDatabase  db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(KEY_ID_ACTIVIDADESREALIZADAS,actividadesRealizadas.getId());
        values.put(KEY_CUADRILLA_ACTIVIDADESREALIZADAS, mallasRealizadas.getCuadrlla());
        values.put(KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS, mallasRealizadas.getActividad().getId());
        values.put(KEY_IDMALLA_ACTIVIDADESREALIZADAS, mallasRealizadas.getMalla().getId());
        values.put(KEY_FECHA_ACTIVIDADESREALIZADAS, mallasRealizadas.getFecha());
        values.put(KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS, mallasRealizadas.getTipoActividad().getId());
        values.put(KEY_SENDED_ACTIVIDADESREALIZADAS, mallasRealizadas.getSended());

        Long insert = db.insert(TABLE_ACTIVIDADES_REALIZADAS, null, values);

        if(insert !=-1)
            mallasRealizadas.setId(insert);
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_ACTIVIDADES_REALIZADAS);

        db.close();
        return insert;
    }

    public Long addCuadrillaRevisada(Cuadrillas cuadrillas){
        FileLog.i(TAG, "agregar actividades realizadas " + cuadrillas.toString());
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CUADRILLA_CUADRILLASREVISADAS,cuadrillas.getCuadrilla());
        values.put(KEY_RESPONSABLE_CUADRILLASREVISADAS,cuadrillas.getMayordomo());
        values.put(KEY_DATEINICIO_CUADRILLASREVISADAS,cuadrillas.getFechaInicio().getTime());
        values.put(KEY_FECHA_CUADRILLASREVISADAS,cuadrillas.getFecha());
        values.put(KEY_DATEFIN_CUADRILLASREVISADAS,cuadrillas.getFechaFin().getTime());
        values.put(KEY_SENDED_CUADRILLASREVISADAS, cuadrillas.getSended());

        Long insert = db.insert(TABLE_CUADRILLAS_REVISADAS, null, values);

        if(insert !=-1)
            cuadrillas.setId(Integer.valueOf(insert.toString()));
        else
            FileLog.e(TAG, "error en la inserion de datos en la tabla" + TABLE_CUADRILLAS_REVISADAS);

        db.close();

        return insert;
    }
    /*******************************GET*******************************************************/
    public Integer getTotalPendientePorEnviar() {
        FileLog.i(TAG, "obtener numero de registros pendientes por enviar");
        Integer total = null;

        String selectQuery1 = "SELECT Count(*) FROM " + TABLE_TRABAJADORES + " WHERE " + KEY_SENDED_TRABAJADORES + " = 0";
        String selectQuery2 = "SELECT Count(*) FROM " + TABLE_ASISTENCIA + " WHERE " + KEY_SENDED_ASISTECNIA + " = 0";
        String selectQuery3 = "SELECT Count(*) FROM " + TABLE_ACTIVIDADES_REALIZADAS + " WHERE " + KEY_SENDED_ACTIVIDADESREALIZADAS + " = 0";
        String selectQuery4 = "SELECT Count(*) FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_SENDED_CUADRILLASREVISADAS + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery1, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor = db.rawQuery(selectQuery2, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total = total + cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor = db.rawQuery(selectQuery3, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total = total + cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor = db.rawQuery(selectQuery4, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                total = total + cursor.getInt(0);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return total;
    }

    public Cuadrillas getCuadrilla(String c, String fecha) {
        FileLog.i(TAG, "obtener cuadrilla " + c + " de la fecha " + fecha);
        Cuadrillas cuadrillas = null;

        String selectQuery = "SELECT * FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_CUADRILLA_CUADRILLASREVISADAS + " = '" + c + "' AND " + KEY_FECHA_CUADRILLASREVISADAS + " = '" + fecha + "'";

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
            FileLog.v(TAG, cuadrillas.toString());
        return cuadrillas;
    }

    public Configuracion getConfiguracion(Long id) {
        FileLog.i(TAG, "obtener configuracion ");
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
            FileLog.v(TAG, configuracion.toString());
        return configuracion;
    }

    public Settings getSetting(Long id) {
        FileLog.i(TAG, "obtener settings ");
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
            FileLog.v(TAG, settings.toString());
        return settings;
    }

    public Mallas getMallas(String id) {
        FileLog.i(TAG, "obtener malla id" + id);
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
            FileLog.v(TAG, mallas.toString());

        return mallas;
    }

    public ArrayList<Mallas> getMallas() {
        FileLog.i(TAG, "obtener catalogo de mallas");
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
            FileLog.v(TAG, "" + mallas.size());

        return mallas;
    }

    public ArrayList<String> getSectores() {
        FileLog.i(TAG, "obtener catalogo de sectores");
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
            FileLog.v(TAG, "" + sectores.size());

        return sectores;
    }

    public ArrayList<Mallas> getMallasXsector(String sector) {
        FileLog.i(TAG, "obtener catalogo de mallas del sector " + sector);
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
            FileLog.v(TAG, "" + mallas.size());

        return mallas;
    }

    public Puestos getPuestos(Integer id) {
        FileLog.i(TAG, "obtener puestos id " + id);
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
            FileLog.v(TAG, puesto.toString());

        return puesto;
    }

    public Puestos getPuestos(String descripcion) {
        FileLog.i(TAG, "obtener puesto descripcion " + descripcion);
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
            FileLog.v(TAG, puesto.toString());

        return puesto;
    }

    public ArrayList<Puestos> getPuestos() {
        FileLog.i(TAG, "obtener lista de puestos");
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
        if (puestos.size() != 0)
            FileLog.v(TAG, "" + puestos.size());

        return puestos;
    }


    public TiposPermisos getTiposPermisos(Long id) {
        FileLog.i(TAG, "obtener tipo de permiso id" + id);
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
            FileLog.v(TAG, tiposPermiso.toString());

        return tiposPermiso;
    }

    public ArrayList<TiposPermisos> getTiposPermisos() {
        FileLog.i(TAG, "obtener catalogo de tipo de permisos");
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
        if (tiposPermisos.size() != 0)
            FileLog.v(TAG, "" + tiposPermisos.size());

        return tiposPermisos;
    }

    public TiposActividades getTipoActividad(Integer id) {
        FileLog.i(TAG, "obtener tipo actividad id" + id);
        TiposActividades tipoActividad = null;

        String selectQuery = "SELECT * FROM " + TABLE_TIPOS_ACTIVIDADES + " WHERE " + KEY_ID_TIPOSACTIVIDADES + " = '" + id + "' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                tipoActividad = new TiposActividades(cursor);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (tipoActividad != null)
            FileLog.v(TAG, tipoActividad.toString());

        return tipoActividad;
    }


    public ArrayList<TiposActividades> getTiposActividades() {
        FileLog.i(TAG, "obtener catalogo de tipo de actividades");
        ArrayList<TiposActividades> tiposActividades = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TIPOS_ACTIVIDADES + " WHERE " + KEY_ID_TIPOSACTIVIDADES + " <> 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        tiposActividades.add(new TiposActividades(-1, "Selecciones tipo Actividad"));
        if (cursor.moveToFirst()) {
            do {
                tiposActividades.add(new TiposActividades(cursor));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (tiposActividades.size() != 0)
            FileLog.v(TAG, "" + tiposActividades.size());

        return tiposActividades;
    }

    public Actividades getActividades(Long id) {
        FileLog.i(TAG, "obtener actividad id" + id);
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
            FileLog.v(TAG, actividad.toString());

        return actividad;
    }

    public ArrayList<Actividades> getActividades() {
        FileLog.i(TAG, "obtener catalogo de actividades");
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
        if (actividades.size() != 0)
            FileLog.v(TAG, "" + actividades.size());

        return actividades;
    }

    public Trabajadores getTrabajadores(Long id) {
        FileLog.i(TAG, "obtener trabajadore id" + id);
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
            FileLog.v(TAG, trabajador.toString());

        return trabajador;
    }

    public Trabajadores getTrabajadores(Integer cuadrilla,Integer consecutivo) {
        FileLog.i(TAG, "obtener trabajador cuadrilla " + cuadrilla + " consecutivo " + consecutivo);
        Trabajadores trabajador = null;

        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES +" WHERE "+KEY_CUADRILLA_TRABAJADORES+" = '"+cuadrilla+"' AND "+KEY_CONSECUTIVO_TRABAJADORES+" = "+consecutivo;
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
            FileLog.v(TAG, trabajador.toString());

        return trabajador;
    }

    public Integer getConsecutivo(Integer cuadrilla) {
        FileLog.i(TAG, "obtener ultimo consecutivo cuadrilla" + cuadrilla);
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
        FileLog.i(TAG, "ultimo consecutivo " + consecutivo);
        return consecutivo+1;
    }

    public ArrayList<Trabajadores> getTrabajadoresCuadrilla(Integer cuadrilla) {
        FileLog.i(TAG, "obtener lista trabajadores cuadrilla " + cuadrilla);
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
        if (trabajadores.size() != 0)
            FileLog.v(TAG, "" + trabajadores.size());

        return trabajadores;
    }

    public int getTotalTrabajadores() {
        FileLog.i(TAG, "obtener total de trabajadores");
        int total = 0;
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_TRABAJADORES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    public ArrayList<Trabajadores> getTrabajadoresPendientesPorEnviar() {
        FileLog.i(TAG, "obtener trabajadores pendientes por enviar");
        ArrayList<Trabajadores> trabajadores = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_TRABAJADORES + " WHERE " + KEY_SENDED_TRABAJADORES + " = 0 ORDER BY " + KEY_CUADRILLA_TRABAJADORES + " , " + KEY_CONSECUTIVO_TRABAJADORES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                trabajadores.add(new Trabajadores(cursor, getPuestos(cursor.getInt(5))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (trabajadores.size() != 0)
            FileLog.v(TAG, "" + trabajadores.size());

        return trabajadores;
    }

    public ArrayList<Cuadrillas> getCuadrillas(String fecha) {
        FileLog.i(TAG, "obtener catalogo de cuadrillas");
        ArrayList<Cuadrillas> cuadrillas = new ArrayList<>() ;

        String selectQuery = "SELECT "+KEY_CUADRILLA_TRABAJADORES+" FROM " + TABLE_TRABAJADORES +" GROUP BY "+KEY_CUADRILLA_TRABAJADORES+" ORDER BY "+KEY_CUADRILLA_TRABAJADORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Integer c = cursor.getInt(0);
                Cuadrillas cuadrillas1 = getCuadrilla(c.toString(), fecha);

                if(cuadrillas1==null){
                    cuadrillas1 = new Cuadrillas(c, getTrabajadores(c, 1).getNombre());
                } else if (cuadrillas1.getMayordomo().equals("")) {
                    cuadrillas1 = new Cuadrillas(c, getTrabajadores(c, 1).getNombre());
                    Log.i("cuadrilla", cuadrillas1.getMayordomo());
                }

                cuadrillas.add(cuadrillas1);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (cuadrillas.size() != 0)
            FileLog.v(TAG, "" + cuadrillas.size());

        return cuadrillas;
    }

    public Cuadrillas getCuadrillasActiva(String fecha, Integer c) {
        FileLog.i(TAG, "obtener cuadrilla activa cuadrilla" + c + " fecha " + fecha);

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
            FileLog.v(TAG, " id cuadrilla" + cuadrilla.getId());

        return cuadrilla;
    }

    public ArrayList<Cuadrillas> getCuadrillasActiva(String fecha) {
        FileLog.i(TAG, "obtener cuadrillas activas fecha" + fecha);

        ArrayList<Cuadrillas> cuadrillas = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_FECHA_CUADRILLASREVISADAS + " = '" + fecha + "' AND " + KEY_DATEFIN_CUADRILLASREVISADAS + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cuadrillas.add(new Cuadrillas(cursor));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (cuadrillas.size() != 0)
            FileLog.v(TAG, " total registros" + cuadrillas.size());

        return cuadrillas;
    }

    public Integer getCuadrillasPendientesPorFinalizar(String fecha) {
        FileLog.i(TAG, "obtener cuadrillas pendientes por finalizar fecha " + fecha);

        Integer totalCuadrillasPorFinalizar = 0;
        String selectQuery = "SELECT COUNT(" + KEY_FECHA_CUADRILLASREVISADAS + ") FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_FECHA_CUADRILLASREVISADAS + " = '" + fecha + "' AND " + KEY_DATEFIN_CUADRILLASREVISADAS + " = 0 GROUP BY " + KEY_FECHA_CUADRILLASREVISADAS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                totalCuadrillasPorFinalizar = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        FileLog.v(TAG, " total cuadrillas por finalizar " + totalCuadrillasPorFinalizar);

        return totalCuadrillasPorFinalizar;
    }

    public ArrayList<Cuadrillas> getCuadrillasPendientesPorEnviar() {
        FileLog.i(TAG, "obtener cuadrillas pendientes por enviar ");
        ArrayList<Cuadrillas> cuadrillas = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_SENDED_CUADRILLASREVISADAS + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                cuadrillas.add(new Cuadrillas(cursor));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();

        FileLog.v(TAG, " total cuadrillas por finalizar " + cuadrillas.size());

        return cuadrillas;
    }

    public ArrayList<Reportes> getReportesTrabajador(String fecha ,Long idTrabajaor) {
        FileLog.i(TAG, "obtener reportes Trabajadores fecha" + fecha + " id trabajador " + idTrabajaor);
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
            FileLog.v(TAG, reportesTrabajador.toString());

        return reportesTrabajador;
    }

    public  ArrayList<Reportes> getReportesTrabajador(String fecha) {
        FileLog.i(TAG, "obtener reporte trabajadores fecha" + fecha);
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
        if (reportes.size() != 0)
            FileLog.v(TAG, "" + reportes.size());

        return reportes;
    }

    public ArrayList<Asistencia> getAsistencia(String fecha) {
        FileLog.i(TAG, "obtener catalogo de actividades fecha " + fecha);
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
        if (asistencias.size() != 0)
            FileLog.v(TAG, "" + asistencias.size());

        return asistencias;
    }

    public ArrayList<Asistencia> getAsistencia(String fecha,Trabajadores trabajador) {
        FileLog.i(TAG, "obtener asistencias fecha " + fecha + " id trabajador " + trabajador.getId());
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
        if (asistencias.size() != 0)
            FileLog.v(TAG, "" + asistencias.size());

        return asistencias;
    }

    public ArrayList<ListaAsistencia> getAsistencia(String fecha, Cuadrillas cuadrilla) {
        FileLog.i(TAG, "obtener lista de sistencias cuadrilla " + cuadrilla + " fecha " + fecha);
        ArrayList<ListaAsistencia> listaAsistencias = new ArrayList<>() ;

        ArrayList<Trabajadores> trabajadoresCuadrilla = getTrabajadoresCuadrilla(cuadrilla.getCuadrilla());

        Puestos puestosDefault = getPuestos(11);

        for (Trabajadores t : trabajadoresCuadrilla) {
            ArrayList<Asistencia> asistencia = getAsistencia(fecha, t);

            if(asistencia.size()!=0){
                for (Asistencia a : asistencia) {
                    listaAsistencias.add(new ListaAsistencia(t,a));
                }
            }else{
                listaAsistencias.add(new ListaAsistencia(t, new Asistencia(t, puestosDefault/*t.getPuesto()*/, cuadrilla.getFechaInicio(), new Date(0), 0)));
            }

        }

        if (listaAsistencias.size() != 0)
            FileLog.v(TAG, "" + listaAsistencias.size());

        return listaAsistencias;
    }

    public ArrayList<ListaAsistencia> getAsistenciaTrabajador(String fecha,Integer idTrabajador) {
        FileLog.i(TAG, "obtener lista asistencia trabajador " + idTrabajador + " fecha " + fecha);
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

        if (listaAsistencias.size() != 0)
            FileLog.v(TAG, "" + listaAsistencias.size());

        return listaAsistencias;
    }

    public ArrayList<Cuadrillas> getAsistenciaCuadrillas(String fecha) {
        FileLog.i(TAG, "obtener asistencia cuadrilla fecha " + fecha);
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
        if (cuadrillas.size() != 0)
            FileLog.v(TAG, "" + cuadrillas.size());

        return cuadrillas;
    }

    public ArrayList<Asistencia> getAsistenciaPendientesPorEnviar() {
        FileLog.i(TAG, "obtener asistencia pendientes por enviar ");
        ArrayList<Asistencia> asistencias = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_ASISTENCIA + " WHERE " + KEY_SENDED_ASISTECNIA + "= 0  ORDER BY " + KEY_ID_ASISTECNIA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                asistencias.add(new Asistencia(cursor, getTrabajadores(cursor.getLong(1)), getPuestos(cursor.getInt(2))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (asistencias.size() != 0)
            FileLog.v(TAG, "" + asistencias.size());

        return asistencias;
    }

    public ArrayList<MallasRealizadas> getActividadesRealizadas(String fecha, Cuadrillas cuadrilla) {
        FileLog.i(TAG, "obtener actividades realizadas cuadrilla " + cuadrilla + " fecha " + fecha);
        ArrayList<MallasRealizadas> mallasRealizadas = new ArrayList<>();

        String selectQuery = "SELECT *  FROM " + TABLE_ACTIVIDADES_REALIZADAS +" WHERE "+KEY_FECHA_REPORTE+" = '"+fecha+"' AND "+KEY_CUADRILLA_ACTIVIDADESREALIZADAS+" = "+cuadrilla.getCuadrilla();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mallasRealizadas.add(new MallasRealizadas(cursor, getActividades(cursor.getLong(2)), getMallas(cursor.getString(3)), getTipoActividad(cursor.getInt(5))));
            } while (cursor.moveToNext());
        }
        // return contact list


        cursor.close();
        db.close();
        if (mallasRealizadas.size() != 0)
            FileLog.v(TAG, "actividades" + mallasRealizadas.size());

        return mallasRealizadas;
    }

    public boolean getActividadesRealizadas(String fecha, Integer cuadrilla, Long idActividad, String idMalla, Integer tipoActividad) {
        FileLog.i(TAG, "obtener actividades realizadas cuadrilla" + cuadrilla + " fecha " + fecha + " actividad " + idActividad + " malla " + idMalla + " tipoActividad " + tipoActividad);
        boolean existe = false;

        String selectQuery = "SELECT COUNT(*)  FROM " + TABLE_ACTIVIDADES_REALIZADAS + " WHERE " + KEY_FECHA_REPORTE + " = '" + fecha + "' AND " + KEY_CUADRILLA_ACTIVIDADESREALIZADAS + " = " + cuadrilla +
                " AND " + KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS + "= " + idActividad + " AND " + KEY_IDMALLA_ACTIVIDADESREALIZADAS + " = '" + idMalla + "' AND " + KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS + " = " + tipoActividad;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            existe = true;
        }
        // return contact list

        cursor.close();
        db.close();

        FileLog.v(TAG, "Existe " + existe);

        return existe;
    }

    public ArrayList<MallasRealizadas> getActividadesRealizadasPendientesPorEnviar() {
        FileLog.i(TAG, "obtener actividades pendientes por enviar");
        ArrayList<MallasRealizadas> mallasRealizadas = new ArrayList<>();

        String selectQuery = "SELECT *  FROM " + TABLE_ACTIVIDADES_REALIZADAS + " WHERE " + KEY_SENDED_ACTIVIDADESREALIZADAS + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                mallasRealizadas.add(new MallasRealizadas(cursor, getActividades(cursor.getLong(2)), getMallas(cursor.getString(3)), getTipoActividad(cursor.getInt(5))));
            } while (cursor.moveToNext());
        }
        // return contact list

        cursor.close();
        db.close();
        if (mallasRealizadas.size() != 0)
            FileLog.v("RESULTADO_DB", "" + mallasRealizadas.size());

        return mallasRealizadas;
    }

    /*******************************UPDATE*******************************************************/
    public int updateConfiguracion(Configuracion configuracion){
        FileLog.v(TAG, "update configuracion " + configuracion.toString());
        int i = -1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_URL_CONFIGURACION,configuracion.getUrl());

            i = db.update(TABLE_CONFIGURACION, values, KEY_ID_CONFIGURACION + " = ?",
                    new String[]{String.valueOf(configuracion.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(TAG, "error " + e.getMessage());
        }
        return i;
    }

    public int updateSetting(Settings settings){
        FileLog.v(TAG, "update settings" + settings.toString());
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
            values.put(KEY_DATEACTUALIZACIOIN_SETTINGS, settings.getFechaActualizacion().getTime());


            i = db.update(TABLE_SETTINGS, values, KEY_ID_SETTINGS + " = ?",
                    new String[]{String.valueOf(settings.getId().toString())});
            db.close();
        }catch (Exception e){
            FileLog.v(TAG, e.getMessage());
        }
        return i;
    }

    public int updateTrabajador(Trabajadores trabajadores) {
        FileLog.v(TAG, "update trabajador " + trabajadores.toString());
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
            FileLog.v(TAG, e.getMessage());
        }

        return i;


    }

    public int updateReportes(Reportes reportes){
        FileLog.v(TAG, "update reportes" + reportes.toString());
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
            FileLog.v(TAG, e.getMessage());
        }

        return i;
    }

    public int updateAsistencia(Asistencia asistencia){
        FileLog.v(TAG, "update asistencia" + asistencia.toString());
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
            FileLog.v(TAG, e.getMessage());
        }

        return i;
    }

    public int updateActividadesRealizadas(MallasRealizadas mallasRealizadas) {
        FileLog.v(TAG, "update actividades realizadas" + mallasRealizadas.toString());
        int i = -1;
        try{
            SQLiteDatabase  db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CUADRILLA_ACTIVIDADESREALIZADAS, mallasRealizadas.getCuadrlla());
            values.put(KEY_IDACTIVIDAD_ACTIVIDADESREALIZADAS, mallasRealizadas.getActividad().getId());
            values.put(KEY_IDMALLA_ACTIVIDADESREALIZADAS, mallasRealizadas.getMalla().getId());
            values.put(KEY_FECHA_ACTIVIDADESREALIZADAS, mallasRealizadas.getFecha());
            values.put(KEY_TIPOACTIVIDAD_ACTIVIDADESREALIZADAS, mallasRealizadas.getTipoActividad().getId());
            values.put(KEY_SENDED_ACTIVIDADESREALIZADAS, mallasRealizadas.getSended());


            i = db.update(TABLE_ACTIVIDADES_REALIZADAS, values, KEY_ID_ACTIVIDADESREALIZADAS + " = ?",
                    new String[]{String.valueOf(mallasRealizadas.getId().toString())});

            db.close();
        } catch (Exception e) {
            FileLog.v(TAG, e.getMessage() + "");
        }

        return i;
    }

    public int updateCuadrilla(Cuadrillas cuadrilla) {
        FileLog.v(TAG, "update cuadrilla" + cuadrilla.toString());
        int i = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_CUADRILLA_CUADRILLASREVISADAS, cuadrilla.getCuadrilla());
            values.put(KEY_RESPONSABLE_CUADRILLASREVISADAS, cuadrilla.getMayordomo());
            values.put(KEY_DATEINICIO_CUADRILLASREVISADAS, cuadrilla.getFechaInicio().getTime());
            values.put(KEY_FECHA_CUADRILLASREVISADAS, cuadrilla.getFecha());
            values.put(KEY_DATEFIN_CUADRILLASREVISADAS, cuadrilla.getFechaFin().getTime());
            values.put(KEY_SENDED_CUADRILLASREVISADAS, cuadrilla.getSended());


            i = db.update(TABLE_CUADRILLAS_REVISADAS, values, KEY_ID_CUADRILLASREVISADAS + " = ?",
                    new String[]{String.valueOf(cuadrilla.getId().toString())});

            db.close();
        }catch (Exception e){
            FileLog.v(TAG, e.getMessage() + "");
        }

        return i;
    }

    /************************DELETES*******************************************************/

    public void deleteAsistencias(String fecha,String idTrabajador){
        FileLog.i(TAG, "eliminar asistencia id Trabajador " + idTrabajador + " fecha " + fecha);
        SQLiteDatabase  db = this.getWritableDatabase();
        int delete = db.delete(TABLE_ASISTENCIA, KEY_FECHA_ASISTECNIA + " =? AND " + KEY_IDTRABAJADOR_ASISTECNIA + " =? ", new String[]{fecha, idTrabajador});
        db.close();
    }

    public void deleteMallasRealizadas(String fecha, String cuadrilla) {
        FileLog.i(TAG, "eliminar mallas realizadas cuadrilla " + cuadrilla + " fecha " + fecha);
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(TABLE_ACTIVIDADES_REALIZADAS, KEY_FECHA_ACTIVIDADESREALIZADAS + " =? AND " + KEY_CUADRILLA_ACTIVIDADESREALIZADAS + " =? ", new String[]{fecha, cuadrilla});
        db.close();
    }

    public void deletePaseLista(String cuadrilla, String fecha, String ids) {
        FileLog.i(TAG, "eliminar apase lista cuadrilla" + cuadrilla + " fecha " + fecha + " asistencias " + ids);
        String deleteAsistencia = "DELETE FROM " + TABLE_ASISTENCIA + " WHERE " + KEY_ID_ASISTECNIA + " in (" + ids + ") and " + KEY_FECHA_ASISTECNIA + " = '" + fecha + "'";
        String deleteActividadesRealizadas = "DELETE FROM " + TABLE_ACTIVIDADES_REALIZADAS + " WHERE " + KEY_FECHA_ACTIVIDADESREALIZADAS + " = '" + fecha + "' AND " + KEY_CUADRILLA_ACTIVIDADESREALIZADAS + " = " + cuadrilla;
        String deleteCuadrillasRevisadas = "DELETE FROM " + TABLE_CUADRILLAS_REVISADAS + " WHERE " + KEY_FECHA_CUADRILLASREVISADAS + " = '" + fecha + "' AND " + KEY_CUADRILLA_CUADRILLASREVISADAS + " = " + cuadrilla;
        String deleteTrabajadores = "DELETE FROM " + TABLE_TRABAJADORES + " WHERE " + KEY_CUADRILLA_TRABAJADORES + " = " + cuadrilla + " and " + KEY_SENDED_TRABAJADORES + "= 0";


        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            db.execSQL(deleteAsistencia);
            db.execSQL(deleteActividadesRealizadas);
            db.execSQL(deleteCuadrillasRevisadas);
            db.execSQL(deleteTrabajadores);
        } catch (Exception e) {
            FileLog.v(TAG, e.getMessage());
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
        FileLog.v(TAG, "transaccion terminada");
    }
}
