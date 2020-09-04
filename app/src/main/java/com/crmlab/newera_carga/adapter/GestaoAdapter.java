package com.crmlab.newera_carga.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crmlab.newera_carga.R;
import com.crmlab.newera_carga.models.Gestao;

import java.util.List;

public class GestaoAdapter extends RecyclerView.Adapter<GestaoAdapter.meuViewHolder> {


    private List<Gestao> listaOportunidades;

    public GestaoAdapter(List<Gestao> lista){
        this.listaOportunidades = lista;
    }

    @NonNull
    @Override

    public meuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.gestao_adapter_layout, parent, false);
        return new meuViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull meuViewHolder holder, int position) {

        Gestao gestao = listaOportunidades.get( position );
        holder.responsavel.setText(gestao.getResponsavel());
        holder.qtdVendas.setText(gestao.getTxtColuna2());
        holder.qtdTotal.setText(gestao.getTxtColuna3());
        holder.coluna4.setText(gestao.getTxtColuna4());
        holder.coluna5.setText(gestao.getTxtColuna5());
        holder.coluna6.setText(gestao.getTxtColuna6());

    }

    @Override
    public int getItemCount() {
        return listaOportunidades.size();
    }
    public class meuViewHolder extends RecyclerView.ViewHolder{

        TextView data, responsavel, qtdVendas, qtdTotal;
        TextView coluna4, coluna5, coluna6;

        public meuViewHolder(@NonNull View itemView) {
            super(itemView);

            responsavel = itemView.findViewById(R.id.textViewResponsavel);
            qtdVendas = itemView.findViewById(R.id.textViewVendas);
            qtdTotal = itemView.findViewById(R.id.textViewQtdTotal);
            coluna4 = itemView.findViewById(R.id.textViewColuna4);
            coluna5 = itemView.findViewById(R.id.textViewColuna5);
            coluna6 = itemView.findViewById(R.id.textViewColuna6);

        }
    }
}

