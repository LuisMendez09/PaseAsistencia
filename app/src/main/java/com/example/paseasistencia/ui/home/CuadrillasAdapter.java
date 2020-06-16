package com.example.paseasistencia.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paseasistencia.R;
import com.example.paseasistencia.model.Cuadrillas;

import java.util.ArrayList;
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
        void onDeleteCuadrilla(Cuadrillas cuadrillas);
    }

    class CuadrillaViewHolder extends RecyclerView.ViewHolder {
        private TextView mNumCuadrillaView;
        private TextView mMayordomoTextView;
        private CardView view;


        public CuadrillaViewHolder(@NonNull final View itemView) {
            super(itemView);
            mNumCuadrillaView = itemView.findViewById(R.id.detail_cuadrilla_name_text_view);
            mMayordomoTextView = itemView.findViewById(R.id.trabajador);
            view = itemView.findViewById(R.id.cv_cuadrillas);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("clic", "clic en la view ");
                    mListener.onCuadrillaSelected(mCuadrillasList.get(getAdapterPosition()), itemView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.v("clic", "clic largo en la view ");
                    Cuadrillas cuadrillas = mCuadrillasList.get(getAdapterPosition());
                    if (cuadrillas != null) {
                        if (cuadrillas.getId() != null) {
                            mListener.onDeleteCuadrilla(cuadrillas);
                        }
                    }
                    return true;
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
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CuadrillaViewHolder holder, int position) {

        Cuadrillas cuadrillas = mCuadrillasList.get(position);
        holder.mNumCuadrillaView.setText(cuadrillas.getCuadrilla().toString());
        holder.mMayordomoTextView.setText(cuadrillas.getMayordomo());

        if (!cuadrillas.getHoraInicio().equals("") && cuadrillas.getHoraFinal().equals("")) {
            holder.itemView.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorActivo));
        } else if (!cuadrillas.getHoraFinal().equals("")) {
            holder.view.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorFinalizado));
        } else {
            holder.view.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorDefault));
        }

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
