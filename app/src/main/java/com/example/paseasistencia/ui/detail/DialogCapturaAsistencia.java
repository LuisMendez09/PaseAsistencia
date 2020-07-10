package com.example.paseasistencia.ui.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.paseasistencia.R;
import com.google.android.material.snackbar.Snackbar;

public class DialogCapturaAsistencia extends DialogFragment {
    private EditText etNumeroTrabajador;
    private DetailAdapter adapter;

    public DialogCapturaAsistencia(DetailAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.dialog_captura_asistencias, null);
        etNumeroTrabajador = (EditText) v.findViewById(R.id.et_numeroTrabajador);

        etNumeroTrabajador.requestFocus();
        etNumeroTrabajador.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    actualizar(v);
                    return true;
                }
                return false;
            }
        });

        builder.setTitle("Capturar asistencia ")
                .setView(v)
                .setPositiveButton("OK", null)
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        //Obtenemos el AlertDialog
        AlertDialog dialog = (AlertDialog) getDialog();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);//Al presionar atras no desaparece

        //Implementamos el listener del boton OK para mostrar el toast
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar(v);
            }
        });
    }

    private void actualizar(View v) {
        String num = etNumeroTrabajador.getText().toString();
        int respuesta = adapter.asistencia(num);

        if (respuesta == DetailAdapter.GUARDADO_EXITOSO)
            etNumeroTrabajador.setText("");
        else
            Snackbar.make(v, "el numero " + num + " no es valido", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
    }
}
