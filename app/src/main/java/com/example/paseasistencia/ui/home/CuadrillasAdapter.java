package com.example.paseasistencia.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paseasistencia.R;
import com.example.paseasistencia.model.Cuadrillas;

import java.util.List;

public class CuadrillasAdapter extends RecyclerView.Adapter<CuadrillasAdapter.CuadrillaViewHolder> {
    private Context mContext;
    private List<Cuadrillas> mCuadrillasList;
    private HomeAdapterListener mListener;

    public CuadrillasAdapter(Context mContext, List<Cuadrillas> mCuadrillasList, HomeAdapterListener mListener) {
        this.mContext = mContext;
        this.mCuadrillasList = mCuadrillasList;
        this.mListener = mListener;


    }

    /***
     * interfas para el envento onClick de la viewCard
     */
    public interface HomeAdapterListener {
        void onCuadrillaSelected(Cuadrillas cuadrillas, View view);
    }

    class CuadrillaViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumCuadrillaView;
        private TextView mMayordomoTextView;

        public CuadrillaViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNumCuadrillaView = itemView.findViewById(R.id.detail_cuadrilla_name_text_view);
            mMayordomoTextView = itemView.findViewById(R.id.trabajador);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCuadrillaSelected(mCuadrillasList.get(getAdapterPosition()), itemView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("enviar", "click largo");
                    return false;
                }
            });
        }
    }

    /***
     * llamado cuando el RecyclerView nesesita un nuevo {@link CuadrillaViewHolder} de los tipos que represantan los item
     * @param parent el viewGroup donde se agregara la nueva vista una ves que se vincula al adapter
     * @param viewType el tipo de la vista del nuevo view
     * @return el nuevo ViewHoder qeu contiene la nueva vista
     */
    @NonNull
    @Override
    public CuadrillaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cuadrilla,parent,false);
        return new CuadrillaViewHolder(view);
    }

    /***
     * llamdo por el recycleView  para mostrar los datos que especifica la posicion, el metodo debera de actualizar el contenido del {@link CuadrillaViewHolder#itemView} para
     * reflejar el item de la nueva posicion
     * @param holder el ViewHolder que debe de actualizarde para representar el contenido del elemento en la posicion dada en el conjunto de datos
     * @param position la posicion del item dentro de los datos del adapter
     */
    @Override
    public void onBindViewHolder(@NonNull CuadrillaViewHolder holder, int position) {
        Cuadrillas cuadrillas = mCuadrillasList.get(position);
        holder.mNumCuadrillaView.setText(cuadrillas.getCuadrilla().toString());
        holder.mMayordomoTextView.setText(cuadrillas.getMayordomo());
    }

    /**
     * Retorna el total de items en los datos del adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return mCuadrillasList.size();
    }
}
