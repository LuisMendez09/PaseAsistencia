package com.example.paseasistencia.ui.importacion;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;

import com.example.paseasistencia.R;
import com.example.paseasistencia.controlador.ImportarCatalogoTrabajadoresWork;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.controlador.ImportarCatalogos;
import com.example.paseasistencia.controlador.Controlador;
import com.example.paseasistencia.controlador.IactualizacionDatos;
import com.example.paseasistencia.controlador.ImportarCatalogosWork;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ImportacionFragment extends Fragment /*implements IactualizacionDatos*/ {
    private static final String TAG = "importar";

    private ImportacionViewModel importacionViewModel;
    private TextView tvMensajes;
    private ProgressBar progressBar;
    private Button btnActualizar;
    private Button btnActualziarTrabajadores;
    private Controlador controlador = null;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        FileLog.d(TAG, "iniciar ImportacionFragment");

        controlador = Controlador.getInstance(this.getContext());
        View root = inflater.inflate(R.layout.fragment_importacion, container, false);

        importacionViewModel = ViewModelProviders.of(this).get(ImportacionViewModel.class);
        tvMensajes = root.findViewById(R.id.tv_mensajes);
        btnActualizar = (Button) root.findViewById(R.id.btn_guardar);
        btnActualziarTrabajadores = (Button) root.findViewById(R.id.btn_actualizarCatalogoTrabajadores);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.i(TAG, "inicia la importacion de catalogos");
                //new ImportarCatalogos(ImportacionFragment.this, controlador, ImportarCatalogos.TIPO_CATALOGO.OTROS).execute();

                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ImportarCatalogosWork.class)
                        .addTag(getString(R.string.channel_id_catalogos))
                        .build();
                Log.d(TAG,"id "+request.getId());
                WorkManager.getInstance(getContext())
                        .enqueueUniqueWork(getString(R.string.channel_id_catalogos), ExistingWorkPolicy.KEEP, request);

                observadorServicio(request.getId());
            }
        });

        btnActualziarTrabajadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileLog.d(TAG, "inicia la importacion de catalogos de trabajadores");
                Log.d(TAG, "inicia la importacion de catalogos de trabajadores");

                //ImportarCatalogos importarCatalogos = new ImportarCatalogos(ImportacionFragment.this, controlador, ImportarCatalogos.TIPO_CATALOGO.TRABAJADORES);
                //importarCatalogos.execute();

                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ImportarCatalogoTrabajadoresWork.class)
                        .addTag(getString(R.string.channel_id_trabajadores))
                        //.setInputData(new Data.Builder().putString("KEY_INPUT_URL",servidor).build())
                        .build();
                Log.d(TAG,"id "+request.getId());
                WorkManager.getInstance(getContext())
                        .enqueueUniqueWork(getString(R.string.channel_id_trabajadores), ExistingWorkPolicy.KEEP, request);

                observadorServicio(request.getId());
            }
        });

        consultaStatusServicio();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!controlador.configuracionValida()) {
            Navigation.findNavController(view).navigate(R.id.nav_configuracion);
        }
    }

     private void observadorServicio(UUID id){
         LifecycleOwner lifecycleOwner = getViewLifecycleOwner();

         WorkManager.getInstance(getContext())
                .getWorkInfoByIdLiveData(id)
                .observe(lifecycleOwner, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(@Nullable  WorkInfo workInfo) {
                        if(workInfo!=null){
                            Data progress = workInfo.getProgress();
                            String msn =progress.getString("PROGRESS");
                            String idServicio = workInfo.getTags().toArray()[0].toString();


                            if(msn!=null){
                                tvMensajes.setText(msn);
                            }

                            if(workInfo.getState() == WorkInfo.State.SUCCEEDED){
                                Snackbar.make(requireView(), R.string.notification_finalizada, Snackbar.LENGTH_LONG).show();
                                unSetLoadingAnimation();
                                irAHome(msn,idServicio);
                            }else   if(workInfo.getState() == WorkInfo.State.ENQUEUED){
                                setLoadingAnimation();
                            }else if (workInfo.getState() == WorkInfo.State.FAILED){
                                Snackbar.make(requireView(), R.string.notification_fallido, Snackbar.LENGTH_LONG).show();
                                unSetLoadingAnimation();
                            }
                        }
                    }
        });
     }

     private static final  ListenableFuture<List<WorkInfo>> consultaServisio(String[] channelID, Context context){
         WorkQuery workQuery = WorkQuery.Builder
                 .fromTags(Arrays.asList(channelID))
                 .addStates(Arrays.asList(WorkInfo.State.RUNNING))
                 .addUniqueWorkNames(Arrays.asList(channelID))
                 .build();

         return WorkManager.getInstance(context).getWorkInfos(workQuery);
     }

     private void consultaStatusServicio(){
        String ids [] = {getString(R.string.channel_id_trabajadores),getString(R.string.channel_id_catalogos)};
         ListenableFuture<List<WorkInfo>> workInfos = ImportacionFragment.consultaServisio(ids,getContext());

         try {
             List<WorkInfo> list = workInfos.get();
             for (WorkInfo workInfo :list) {
                 if(workInfo!=null){
                     if(workInfo.getState()== WorkInfo.State.RUNNING){
                         setLoadingAnimation();
                         observadorServicio(workInfo.getId());
                     }else {
                         unSetLoadingAnimation();
                     }
                 }
             }
         } catch (ExecutionException e) {
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
     }

    public static WorkInfo.State consultaStatusServicio(Context context){
        String ids [] = {context.getString(R.string.channel_id_trabajadores),context.getString(R.string.channel_id_catalogos)};
        ListenableFuture<List<WorkInfo>> workInfos = ImportacionFragment.consultaServisio(ids,context);

        try {
            List<WorkInfo> list = workInfos.get();
            for (WorkInfo workInfo :list) {
                if (workInfo != null) {
                    Log.d(TAG,workInfo.getState().name());
                    return workInfo.getState();
                }else{
                    return WorkInfo.State.FAILED;
                }
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
            return WorkInfo.State.FAILED;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return WorkInfo.State.FAILED;
        }

        return WorkInfo.State.SUCCEEDED;
    }

    public void setLoadingAnimation(){
        progressBar.setVisibility(View.VISIBLE);
        btnActualizar.setEnabled(false);
        btnActualziarTrabajadores.setEnabled(false);
        tvMensajes.setText("");
    }

    public void unSetLoadingAnimation(){
        progressBar.setVisibility(View.GONE);
        btnActualizar.setEnabled(true);
        btnActualziarTrabajadores.setEnabled(true);
       // tvMensajes.setText("");
    }

    private void irAHome(String msn,String idservicio){
        if(!idservicio.equals(getContext().getString(R.string.channel_id_catalogos))){
            Log.d(msn + " servicio","ir a home");
            if (tvMensajes.getText().equals(Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores))) {
                NavHostFragment.findNavController(ImportacionFragment.this).popBackStack(R.id.nav_homeFragmen, false);
            }
        }
    }
/*
    @Override
    public void actualizacionMensajes(String mensaje) {
        tvMensajes.setText(mensaje);
    }

    @Override
    public void actualizacionMensajesEnvio(Object value, Controlador.STATUS_CONEXION status_conexion) {

    }

    @Override
    public void iniciarAnimacion(Integer min, Integer max) {
        setLoadingAnimation();
    }

    @Override
    public void finalizarAnimacion(String mensaje) {
        unSetLoadingAnimation();
        if (mensaje.equals(Controlador.getCONTEXT().getString(R.string.msn_fin_trabajadores))) {
            NavHostFragment.findNavController(ImportacionFragment.this).popBackStack(R.id.nav_homeFragmen, false);
        }

    }

    @Override
    public void incrementar(Integer incremento) {
        NavHostFragment.findNavController(ImportacionFragment.this).navigate(R.id.nav_configuracion);
    }*/
}
