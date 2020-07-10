package com.example.paseasistencia.complementos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Spinner;

import java.io.File;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Complementos {
    public  static File rutaAlmacenamiento(Context context){
        File storageDir = null;
        File[] temp2 = null;
        File temp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            temp2 = context.getExternalMediaDirs();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                temp2 = context.getExternalFilesDirs("");
            } else{
                temp = context.getExternalFilesDir("");
                if(temp == null)
                    temp = context.getDatabasePath("");
            }
        }

        if(temp != null){
            storageDir = temp;
        }

        if(temp2 != null){
            if(temp2[temp2.length-1]==null){
                storageDir = temp2[0];
            }else{
                storageDir = temp2[temp2.length - 1];
            }
        }

        return storageDir;
    }

    public static int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public static int getIndex(CharSequence[] array, String myString) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(myString)) {
                return i;
            }
        }

        return 0;
    }

    public static Long getDateTimeActual(){
        return new Date().getTime();
    }

    public static String getTotalHoras(Date timeI, Date timeF) {
        if(timeF.getTime()!= Long.valueOf(0)){
            Long dif = timeF.getTime() - timeI.getTime();
            Long horas,minutos,segundos;

            segundos = dif/1000;
            minutos = segundos / 60;
            segundos = segundos % 60;
            horas = minutos / 60;
            minutos = minutos % 60;

            return String.format("%02d", horas)+":"+String.format("%02d", minutos);
        }
        return "00:00";
    }

    public static Date getDateActual(){
        return new Date();
    }

    public static String getDateActualToString(){
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(new Date());
    }

    public static String getDateActualToStringServidor(){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        return formatter.format(new Date());
    }

    public static Long convertirStringAlong(String fecha,String hora) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return (sdf.parse(fecha+" "+hora)).getTime();
    }

    /***
     * obtener la fecha en formato dd/MM/yyyy
     * @param fecha tipo date
     * @return string con el ofrmato de fecha
     */
    public static String obtenerFechaString(Date fecha){
        if (fecha != null) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        }

        return "";

    }

    public static String fechaInicioSemana() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);


        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            c.add(Calendar.DAY_OF_YEAR, -1);
        }
        return obtenerFechaString(c.getTime());
    }

    public static Date fechaInicioSemanaDate() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);


        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            c.add(Calendar.DAY_OF_YEAR, -1);
        }
        return c.getTime();
    }
    public static String fechaFinSemana() {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        return obtenerFechaServidor(c.getTime());
    }

    public static String obtenerFechaServidor(Date fecha) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(fecha);
    }

    public static String obtenerHoraString(Date hora){
        if (hora != null) {
            if (hora.getTime() != Long.valueOf(0)) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                return sdf.format(hora);
            }
        }

        return "";
    }

    public static final int getNombreDia(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);

        return c.get(Calendar.DAY_OF_WEEK);
    }
}
