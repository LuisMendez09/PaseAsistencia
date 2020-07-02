package com.example.paseasistencia.ui.detail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                String num = etNumeroTrabajador.getText().toString();
                int respuesta = adapter.asistencia(num);

                if (respuesta == 1)
                    etNumeroTrabajador.setText("");
                else
                    Snackbar.make(v, "el numero " + num + " no es valido", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }
        });

        //Personalizamos
        //Resources res = getResources();

        //Buttons
        // Button positive_button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        //positive_button.setBackground(res.getDrawable(R.drawable.buttom_redondo));

        //Button negative_button =  dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        //negative_button.setBackground(res.getDrawable(R.drawable.buttom_redondo));

        //int color = Color.parseColor("#304f5a");

        //Title
        //int titleId = res.getIdentifier("alertTitle", "id", "Android");
        //View title = dialog.findViewById(titleId);
        /*if (title != null) {
            ((TextView) title).setTextColor(color);
        }*/

        //Title divider
        //int titleDividerId = res.getIdentifier("titleDivider", "id", "Android");
        //View titleDivider = dialog.findViewById(titleDividerId);
        //if (titleDivider != null) {
        //    titleDivider.setBackgroundColor(res.getColor(R.color.design_default_color_error));
        //}
    }
}
