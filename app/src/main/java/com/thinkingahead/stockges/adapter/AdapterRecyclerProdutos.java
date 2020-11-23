package com.thinkingahead.stockges.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.model.Produtos;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class AdapterRecyclerProdutos extends RecyclerView.Adapter<AdapterRecyclerProdutos.MyViewHolder> {

    private final List<Produtos> listaProdutos;

    public AdapterRecyclerProdutos(List<Produtos> lista) {
        this.listaProdutos = lista;
    }

    @Override
    public int getItemCount() {
        return listaProdutos.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_produtos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Produtos produto = listaProdutos.get(position);

        holder.descricao.setText("Descrição: " + produto.getDescricao());
        holder.codigo.setText("Código: " + produto.getCodigo());
        holder.quantidade.setText("Quantidade: " + produto.getQuantidade());
        if (produto.getFoto() != null) {
            Picasso.get().load(produto.getFoto()).into(holder.imageProduto);
        } else {
            holder.imageProduto.setImageResource(R.drawable.padrao_produto);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView descricao;
        private final TextView codigo;
        private final TextView quantidade;
        private final CircleImageView imageProduto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            codigo = itemView.findViewById(R.id.editTextAtualizarCodigo);
            descricao = itemView.findViewById(R.id.textDescricao);
            quantidade = itemView.findViewById(R.id.textQuantidade);
            imageProduto = itemView.findViewById(R.id.imageProdutoInserir);

        }
    }


}
