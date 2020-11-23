package com.thinkingahead.stockges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.adapter.AdapterRecyclerProdutos;
import com.thinkingahead.stockges.config.Configs;
import com.thinkingahead.stockges.config.ConfiguracaoFireBase;

public class RemoverFragment extends Fragment {

    private AdapterRecyclerProdutos adapterRecyclerProdutos;
    private DatabaseReference produtosRef;
    private ValueEventListener valueEventListenerProdutos;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_listar, container, false);

        // Configurações Básicas
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewProdutos);
        DatabaseReference firebaseRef = ConfiguracaoFireBase.getDataBaseReference();
        produtosRef = ConfiguracaoFireBase.getDataBaseReference().child(Configs.userID).child("produtos");
        adapterRecyclerProdutos = new AdapterRecyclerProdutos(ListarFragment.listaProdutos);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayoutProdutos);
        textStatus = rootView.findViewById(R.id.textStatus);

        // Configuração e Listagem de Produtos no RecyclerView e SwipeRefreshLayout
        Configs.configuracaoRecyclerViewProdutos(recyclerView, adapterRecyclerProdutos, getContext());

        // Configuração do LongItemClick (Clique Longo) do RecyclerView e da Remoção do Produto
        Configs.recyclerRemover(getContext(), recyclerView, firebaseRef, adapterRecyclerProdutos);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Configuração do SwipeRefreshLayout e da atualização dos Produtos ao usar o mesmo
        Configs.refreshRecyclerProdutos(getContext(), swipeRefreshLayout, adapterRecyclerProdutos, produtosRef, rootView, textStatus);

        // Configurar TextStatus e o Seu Valor
        Configs.recyclerStatus(textStatus);

        // Recuperar Todos os Produtos
        valueEventListenerProdutos = Configs.recuperarProdutos(valueEventListenerProdutos, produtosRef, ListarFragment.listaProdutos, rootView, adapterRecyclerProdutos, getContext(), textStatus);

    }

    @Override
    public void onStop() {
        super.onStop();

        // Remover o EventListener dos Produtos
        Configs.removerValueEventListener(produtosRef, valueEventListenerProdutos, getContext());

    }


}
