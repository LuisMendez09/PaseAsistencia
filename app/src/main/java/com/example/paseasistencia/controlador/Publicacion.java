package com.example.paseasistencia.controlador;

import android.content.Context;
import android.util.Log;

import com.example.paseasistencia.complementos.Complementos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Publicacion {
    private Context context;
    private String sLogFilePath;
    private File theFile;
    private static final String TAG = "Publicacion";
    //private static BufferedWriter sBufferedWriter;
    //private static FileWriter fileWriter;


    public Publicacion(Context context) {
        this.context = context;
    }

    public Long leerArchivoPublicacion() {
        Long fechaSesion = Long.valueOf(0);

        try {
            open();

            FileReader fileReader = new FileReader(theFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String linea = "";
            while ((linea = bufferedReader.readLine()) != null) {
                Log.v(TAG, "Fecha inicio de aplicacion: " + linea);
                fechaSesion = Long.parseLong(linea);
            }

            if (bufferedReader != null) {
                bufferedReader.close();
                fileReader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            fechaSesion = Long.valueOf(-1);
        }
        return fechaSesion;
    }

    public void escribir(Long fechaIniciosSesion) {
        try {
            open();

            FileWriter fileWriter = new FileWriter(theFile);
            BufferedWriter sBufferedWriter = new BufferedWriter(fileWriter);
            sBufferedWriter.write(fechaIniciosSesion.toString());

            if (sBufferedWriter != null) {
                //sBufferedWriter.newLine();
                sBufferedWriter.flush();
                sBufferedWriter.close();
                fileWriter.close();
            }

        } catch (IOException e) {
            Log.e("FileLog", Log.getStackTraceString(e));
        }
    }

    private void open() {
        sLogFilePath = Complementos.rutaAlmacenamiento(context) + File.separator + "sesion.txt";

        theFile = new File(sLogFilePath);
        if (!theFile.getParentFile().exists()) {
            theFile.getParentFile().mkdirs();
        }

        if (!theFile.exists()) {
            try {
                theFile.createNewFile();
            } catch (IOException e) {
                Log.e("FileLog", Log.getStackTraceString(e));
            }
        }
    }

    /*private void close() {
        try {
            if (sBufferedWriter != null) {
                //sBufferedWriter.newLine();
                sBufferedWriter.flush();
                sBufferedWriter.close();
                fileWriter.close();
            }
        } catch (IOException e) {
            Log.e("FileLog", Log.getStackTraceString(e));
        }
    }*/
}
