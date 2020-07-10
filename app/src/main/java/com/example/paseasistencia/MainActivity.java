package com.example.paseasistencia;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.FileLog;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private boolean mostrarMensajeDeCierre = false;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FileLog.open(this.getBaseContext(), Log.VERBOSE, 3000000);
        FileLog.i("MainActivity", "Log iniciado");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_homeFragmen, R.id.nav_importacion, R.id.nav_exportar, R.id.nav_configuracion
        )
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Controlador.STATUS_APP status_app = Controlador.getInstance(this.getBaseContext()).inisiarDia();
        FileLog.v("MainActivity", "iniciar dia: " + status_app.name());
        switch (status_app) {

            case SESION_APP_ACTIVA:
                break;
            case SESION_APP_REINICAR:
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case SESION_APP_INICIAR:
                break;
            case SESION_APP_FECHA_DISPOSITIVO_NO_VALIDA:
                //mostrar mensaje antes de cerrar la app
                Log.v("ciclo", "Mostrar mensaje de cierre");
                mostrarMensajeDeCierre = true;
                //mensajeDeCierre();
                finishAffinity();
                break;
        }

        //Log.v("ciclo",Controlador.getInstance(this.getBaseContext()).getSettings().toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("MainActivity", "onReatar");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static final void getTimePiker(final TextView tvHora, Context context){
        final String[] horaSeleccionada = new String[1];
        String h[] = tvHora.getText().toString().split(":");

        Calendar c = Calendar.getInstance();
        int mHour = tvHora.getText().toString().equals("")?c.get(Calendar.HOUR_OF_DAY):Integer.parseInt(h[0]);
        int mMinute = tvHora.getText().toString().equals("")?c.get(Calendar.MINUTE):Integer.parseInt(h[1]);

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //horaSeleccionada[0] = hourOfDay+":"+minute;
                tvHora.setText(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
            }
        },mHour,mMinute,true);

        timePickerDialog.show();
    }

    private void mensajeDeCierre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());

        builder.setTitle("ERROR EN LA FECHA!!!")
                .setMessage("LA FECHA DE LA TABLETA NO ES CORRECTA.\n FOVOR DE REVISAR SI LA HORA DE LA TABLETA ES CORRECTA")
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }



}
