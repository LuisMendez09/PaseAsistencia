package com.example.paseasistencia.ui.finalizarCuadrilla;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paseasistencia.MainActivity;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.controlador.FileLog;
import com.example.paseasistencia.model.Cuadrillas;
import com.example.paseasistencia.ui.home.CuadrillasAdapter;

import java.util.List;

public class FinalizarCuadrillaFragment extends Fragment {
    private static final String TAG = "FinalizarCuadrillaFragment";

    private FinalizarCuadrillaModel viewModel;
    private RecyclerView mRecyclerView;
    private CuadrillasAdapter mCuadrillasAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FileLog.i(TAG, "iniciar FinalizarCuadrillaFragment");
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        mRecyclerView = root.findViewById(R.id.cuadrillas_recycler_view);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(FinalizarCuadrillaModel.class);

        final NavController navController = Navigation.findNavController(view);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack(R.id.nav_homeFragmen, false);
            }
        });

        viewModel.getmCuadrillasData(getContext()).observe(getViewLifecycleOwner(), new Observer<List<Cuadrillas>>() {
            @Override
            public void onChanged(List<Cuadrillas> cuadrillas) {
                mCuadrillasAdapter = new CuadrillasAdapter(getContext(), cuadrillas, new finalizacionCuadrillaLisener());

                mRecyclerView.setAdapter(mCuadrillasAdapter);

                if (viewModel.getcuadrillasPendientes() == 0)
                    navController.popBackStack();
            }
        });
    }


    private class finalizacionCuadrillaLisener implements CuadrillasAdapter.HomeAdapterListener {
        @Override
        public void onCuadrillaSelected(final Cuadrillas cuadrillas, final View view) {
            FileLog.i(TAG, "iniciar la camtura de finalizacion de cuadrilla");
            final AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarCuadrillaFragment.this.getContext());

            LayoutInflater layoutInflater = FinalizarCuadrillaFragment.this.getActivity().getLayoutInflater();
            View viewDialog = layoutInflater.inflate(R.layout.dialog_finalizar_cuadrilla, null);
            final TextView tvMayordomo = viewDialog.findViewById(R.id.tv_mayordomo);

            final TextView horaInicio = viewDialog.findViewById(R.id.tv_horaInicio);
            final TextView horaFin = viewDialog.findViewById(R.id.tv_horaFin);

            tvMayordomo.setText(cuadrillas.getMayordomo());
            horaInicio.setText(Complementos.obtenerHoraString(cuadrillas.getFechaInicio()));

            horaFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.getTimePiker(horaFin, FinalizarCuadrillaFragment.this.getContext());
                }
            });

            builder.setView(viewDialog)
                    .setTitle("Finalizar cuadrilla " + cuadrillas.getCuadrilla())
                    .setPositiveButton(R.string.btn_guardar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FileLog.i(TAG, "guardar los cambios");
                            //guardar los cambios en el adapter
                            viewModel.finalizarCuadrilla(cuadrillas, horaFin.getText().toString());

                        }
                    })
                    .create()
                    .show();
            //}
        }

        @Override
        public void onDeleteCuadrilla(final Cuadrillas cuadrillas) {
            FileLog.i(TAG, "iniciar dialogo para eliminar una cuadrilla revisada");
            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarCuadrillaFragment.this.getContext());
            builder.setTitle("Eliminar cuadrilla " + cuadrillas.getCuadrilla())
                    .setMessage("esta seguro de eliminar las asistencias de la cuadrilla: " + cuadrillas.getCuadrilla())
                    .setPositiveButton(R.string.btn_continuar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //guardar los cambios en el adapter
                            FileLog.i(TAG, "eliminar la cuadrilla " + cuadrillas.getCuadrilla());
                            viewModel.eliminarCuadrilla(cuadrillas);

                        }
                    })
                    .setNegativeButton(R.string.btn_cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();


        }
    }
}
