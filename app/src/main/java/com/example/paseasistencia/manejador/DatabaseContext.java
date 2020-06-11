package com.example.paseasistencia.manejador;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;


import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.complementos.KeyValues;

import java.io.File;

public class DatabaseContext extends ContextWrapper {
    private static final String DEBUG_CONTEXT = "DatabaseContext";

    public DatabaseContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String name)  {
        File storageDir = Complementos.rutaAlmacenamiento(getBaseContext());
        File result = null;

        String dbfile = storageDir.getAbsolutePath() + File.separator + KeyValues.FOLDER_DATABASE + File.separator + name;

        result = new File(dbfile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        return result;
    }

    /* this version is called for android devices >= api-11. thank to @damccull for fixing this. */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name,mode, factory);
    }

    /* this version is called for android devices < api-11 */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {

        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);
        //if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
        //}
        return result;
    }
}
