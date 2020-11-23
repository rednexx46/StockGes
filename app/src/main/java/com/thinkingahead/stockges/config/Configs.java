package com.thinkingahead.stockges.config;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.thinkingahead.stockges.R;
import com.thinkingahead.stockges.activity.*;
import com.thinkingahead.stockges.adapter.AdapterRecyclerFrases;
import com.thinkingahead.stockges.adapter.AdapterRecyclerProdutos;
import com.thinkingahead.stockges.helper.RecyclerItemClickListener;
import com.thinkingahead.stockges.model.Frases;
import com.thinkingahead.stockges.model.Produtos;
import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.aboutpage.AboutPage;

import java.io.ByteArrayOutputStream;
import java.util.*;

import static com.thinkingahead.stockges.fragments.ListarFragment.listaProdutos;


public class Configs {


    public static Boolean isResultadoFull = false;
    private static Boolean mostrarPassword = false;
    public static Boolean erroFrase = false;
    public static String resultado = null;
    public static int childrenCount = 0;
    public static int frasesCount = 0;
    public static String userID;
    public static String produtoID;
    public static String produtoFoto;
    public static Boolean produtoImagem;
    public static byte[] dadosImagem;
    private static ValueEventListener valueEventListener;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_FOTOGRAFIAS = 200;

    // Geral - Begin

    public static void configuracaoRecyclerViewProdutos(RecyclerView recyclerView, AdapterRecyclerProdutos adapterRecyclerProdutos, Context context) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecyclerProdutos);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayout.VERTICAL));

    }

    public static void refreshRecyclerProdutos(final Context context, final SwipeRefreshLayout swipeRefreshLayout, final AdapterRecyclerProdutos adapterRecyclerProdutos, final DatabaseReference produtosRef, final View rootView, final TextView textStatus) {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ValueEventListener valueEventListenerProdutos;
                    valueEventListenerProdutos = recuperarProdutos(null, produtosRef, listaProdutos, rootView, adapterRecyclerProdutos, context, textStatus);

                    swipeRefreshLayout.setRefreshing(false);
                    produtosRef.removeEventListener(valueEventListenerProdutos);
                    Toast.makeText(context, "Lista Atualizada com Sucesso!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public static ValueEventListener recuperarProdutos(ValueEventListener valueEventListenerProdutos, DatabaseReference produtosRef, final List<Produtos> listaProdutos, final View rootView, final AdapterRecyclerProdutos adapterRecyclerProdutos, final Context context, final TextView textStatus) {
        try {
            valueEventListenerProdutos = produtosRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    listaProdutos.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Produtos produto = dados.getValue(Produtos.class);

                        listaProdutos.add(produto);
                    }

                    rootView.findViewById(R.id.carregamentoProdutos).setVisibility(View.GONE);
                    setChildrenCount((int) dataSnapshot.getChildrenCount());
                    recyclerStatus(textStatus);

                    adapterRecyclerProdutos.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return valueEventListenerProdutos;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

        return valueEventListenerProdutos;

    }

    public static void removerValueEventListener(DatabaseReference databaseReference, ValueEventListener valueEventListener, Context context) {
        try {
            databaseReference.removeEventListener(valueEventListener);
        } catch (Exception e) {
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void esconderMostrarPassword(final ImageButton imageButtonPassword, final EditText editPassword) {
        imageButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mostrarPassword) {

                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageButtonPassword.setImageResource(R.drawable.ic_mostrar_password_24dp);
                    mostrarPassword = false;

                } else {

                    editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageButtonPassword.setImageResource(R.drawable.ic_esconder_password_24dp);
                    mostrarPassword = true;

                }

            }
        });
    }

    public static void esconderMostrarPassword(final ImageButton imageButtonPassword, final EditText editPassword, final EditText editConfirmarPassword) {
        imageButtonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mostrarPassword) {

                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editConfirmarPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageButtonPassword.setImageResource(R.drawable.ic_mostrar_password_24dp);
                    mostrarPassword = false;

                } else {

                    editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    editConfirmarPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageButtonPassword.setImageResource(R.drawable.ic_esconder_password_24dp);
                    mostrarPassword = true;

                }

            }
        });
    }

    public static void getProfilePhotoUrl(Context context, CircleImageView imageFoto) {
        Uri url = Objects.requireNonNull(ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser()).getPhotoUrl();

        if (url != null) {

            Glide.with(context).load(url).into(imageFoto);

        } else {
            imageFoto.setImageResource(R.drawable.foto_perfil_padrao);
        }
    }


    public static void recyclerStatus(TextView textStatus) {

        int recyclerSize = getChildrenCount();

        if (recyclerSize > 0) {
            textStatus.setVisibility(View.GONE);
        } else {
            textStatus.setVisibility(View.VISIBLE);
        }

    }

    public static int getFrasesCount() {
        return frasesCount;
    }

    public static void setFrasesCount(int frasesCount) {
        Configs.frasesCount = frasesCount;
    }

    public static Boolean getProdutoImagem() {
        return produtoImagem;
    }

    public static void setProdutoImagem(Boolean produtoImagem) {
        Configs.produtoImagem = produtoImagem;
    }

    public static byte[] getDadosImagem() {
        return dadosImagem;
    }

    public static void setDadosImagem(byte[] dadosImagem) {
        Configs.dadosImagem = dadosImagem;
    }

    public static String getProdutoFoto() {
        return produtoFoto;
    }

    public static void setProdutoFoto(String produtoFoto) {
        Configs.produtoFoto = produtoFoto;
    }

    public static String getProdutoID() {
        return produtoID;
    }

    public static void setProdutoID(String produtoID) {
        Configs.produtoID = produtoID;
    }

    public static String getResultado() {
        return resultado;
    }

    public static void setResultado(String resultado) {
        Configs.resultado = resultado;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        Configs.userID = userID;
    }

    // Geral - End

    // InicioFragment - Begin

    public static ValueEventListener fraseInspiradora(final DatabaseReference databaseReference, final TextView textFrase, final Context context) {

        final Random random = new Random();

        valueEventListener = databaseReference.child("frases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {

                    Frases frase;

                    setChildrenCount((int) dataSnapshot.getChildrenCount());
                    int rand = random.nextInt(childrenCount);

                    Iterator itr = dataSnapshot.getChildren().iterator();

                    for (int i = 0; i < rand; i++) {
                        itr.next();
                    }

                    DataSnapshot childSnapshot = (DataSnapshot) itr.next();
                    frase = childSnapshot.getValue(Frases.class);
                    if (frase != null) {
                        textFrase.setText('"' + frase.getFrase() + '"' + "\nAutor: " + frase.getAutor());
                        databaseReference.removeEventListener(valueEventListener);
                    } else {
                        textFrase.setText("Nenhuma Frase Encontrada :(");
                    }


                } catch (Exception e) {

                    if (getErroFrase() != null) {

                        if (getErroFrase()) {
                            Toast.makeText(context, "Erro, Experimente Adicionar uma Frase", Toast.LENGTH_SHORT).show();
                            setErroFrase(false);
                        }

                        textFrase.setText("Nenhuma Frase Encontrada :(");

                    }

                    e.printStackTrace();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return valueEventListener;

    }

    public static void definirNomeUtilizador(TextView textIntro) {

        String nome = Objects.requireNonNull(ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser()).getDisplayName();

        if (nome != null) {

            try {

                textIntro.setText(nome);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static Boolean getErroFrase() {
        return erroFrase;
    }

    public static void setErroFrase(Boolean erroFrase) {
        Configs.erroFrase = erroFrase;
    }

    public static int getChildrenCount() {
        return childrenCount;
    }

    public static void setChildrenCount(int childrenCount) {
        Configs.childrenCount = childrenCount;
    }

    // InicioFragment - End

    // InserirFragment - Begin

    public static void cliqueDoBotaoFab(FloatingActionButton fab, final TextView textCodigo, final TextView textDescricao, final TextView textQuantidade, final Fragment fragment, final CircleImageView imageProduto) {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    Configs.setProdutoID(UUID.randomUUID().toString());
                    final String codigo = textCodigo.getText().toString();
                    final String descricao = textDescricao.getText().toString();
                    final String quantidade = textQuantidade.getText().toString();
                    boolean verificarCampos = !codigo.isEmpty() || !descricao.isEmpty() || !quantidade.isEmpty();


                    if (verificarCampos && getProdutoImagem()) {

                        enviarProdutoInserir(codigo, descricao, quantidade, fragment.getContext());

                        Toast.makeText(fragment.getContext(), "Produto Enviado com Sucesso!", Toast.LENGTH_SHORT).show();
                        limparCamposInserir(textCodigo, textQuantidade, textDescricao);

                        imageProduto.setImageResource(R.drawable.padrao_produto);
                        setProdutoFoto(null);
                        setProdutoImagem(false);
                        setIsResultadoFull(false);

                    } else if (verificarCampos) {

                        Produtos produto = new Produtos();
                        produto.setUniqueID(getProdutoID());
                        produto.setCodigo(codigo);
                        produto.setDescricao(descricao);
                        produto.setQuantidade(quantidade);
                        produto.guardarDados();

                        Toast.makeText(fragment.getContext(), "Produto Enviado com Sucesso!", Toast.LENGTH_SHORT).show();
                        limparCamposInserir(textCodigo, textQuantidade, textDescricao);

                        imageProduto.setImageResource(R.drawable.padrao_produto);
                        setProdutoFoto(null);
                        setProdutoImagem(false);
                        setIsResultadoFull(false);

                    } else {

                        Toast.makeText(fragment.getContext(), "Preencha pelo menos um Campo!", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {

                    Toast.makeText(fragment.getContext(), "Falha ao Enviar Produto: " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private static void enviarProdutoInserir(final String codigo, final String descricao, final String quantidade, Context context) {

        try {

            StorageReference storageReference = ConfiguracaoFireBase.getStorageReference();
            StorageReference imagemRef = storageReference.child("imagens").child("produtos").child(getProdutoID() + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(getDadosImagem());

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            setProdutoFoto(uri.toString());

                            Produtos produto = new Produtos();
                            produto.setUniqueID(getProdutoID());
                            produto.setCodigo(codigo);
                            produto.setDescricao(descricao);
                            produto.setQuantidade(quantidade);
                            produto.setFoto(getProdutoFoto());
                            produto.guardarDados();

                        }
                    });

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    public static void abrirLeitorCamaraFotografias(ImageButton imagemQrCode, ImageButton imagemCamara, ImageButton imagemFotografias, final Fragment fragment) {
        imagemQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment.startActivity(new Intent(fragment.getContext(), QrCodeActivity.class));
                setIsResultadoFull(true);
            }
        });

        imagemCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (i.resolveActivity(Objects.requireNonNull(fragment.getActivity()).getPackageManager()) != null) {
                    fragment.startActivityForResult(i, SELECAO_CAMERA);
                }

            }
        });

        imagemFotografias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(Objects.requireNonNull(fragment.getActivity()).getPackageManager()) != null) {
                    fragment.startActivityForResult(i, SELECAO_FOTOGRAFIAS);
                }

            }
        });

    }

    public static void recuperarDadosImagem(int requestCode, Intent data, Bitmap imagem, CircleImageView imageProduto, final Fragment fragment) {

        try {

            switch (requestCode) {
                case SELECAO_CAMERA:
                    assert data != null;
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;

                case SELECAO_FOTOGRAFIAS:
                    assert data != null;
                    Uri localImagemSelecionada = data.getData();
                    ImageDecoder.Source source = ImageDecoder.createSource(Objects.requireNonNull(fragment.getActivity()).getContentResolver(), localImagemSelecionada);
                    imagem = ImageDecoder.decodeBitmap(source);

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(fragment.getContext(), "Erro ao Recuperar Imagem!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // COMECA TUDO AQUI
        if (imagem != null) {

            imageProduto.setImageBitmap(imagem);

            // Recuperar Dados da Imagem para o Firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();
            setDadosImagem(dadosImagem);
            setProdutoImagem(true);

        }

    }

    public static void inserirCodigoProduto(TextView textCodigo) {

        resultado = Configs.getResultado();

        if (isResultadoFull) {
            textCodigo.setText(resultado);
        }

        if (!isResultadoFull) {
            textCodigo.setText("");
        }

    }

    private static void limparCamposInserir(TextView textCodigo, TextView textQuantidade, TextView textDescricao) {
        textCodigo.setText("");
        textQuantidade.setText("");
        textDescricao.setText("");
    }

    // InserirFragment - End

    // RemoverFragment - Begin

    public static void recyclerRemover(final Context c, RecyclerView recyclerView, final DatabaseReference firebaseRef, final AdapterRecyclerProdutos adapterRecyclerProdutos) {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(c, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, final int position) {
                final Produtos produto = listaProdutos.get(position);
                final DatabaseReference produtosRef = firebaseRef.child(userID).child("produtos");

                AlertDialog.Builder dialog = new AlertDialog.Builder(c);

                // Configurar Título e Mensagem
                dialog.setTitle("Confirmar Exclusão");
                dialog.setCancelable(false);
                dialog.setMessage("Deseja remover o produto: " + produto.getCodigo());
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            produtosRef.child(produto.getUniqueID()).removeValue();
                            adapterRecyclerProdutos.notifyItemRemoved(position);
                            Toast.makeText(c, "Eliminado com Sucesso!", Toast.LENGTH_SHORT).show();
                            adapterRecyclerProdutos.notifyDataSetChanged();

                        } catch (Exception e) {
                            Toast.makeText(c, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                dialog.setNegativeButton("Não", null);
                dialog.show();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    // RemoverFragment - End

    // AtualizarFragment - Begin

    public static void recyclerAtualizar(final Context c, RecyclerView recyclerView, final AdapterRecyclerProdutos adapterRecyclerProdutos) {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(c, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {


            }

            @Override
            public void onLongItemClick(View view, final int position) {

                final Produtos produto = listaProdutos.get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(c);

                // Configurar Título e Produto
                dialog.setTitle("Confirmar Atualização");
                dialog.setCancelable(false);
                dialog.setMessage("Deseja atualizar o produto: " + produto.getCodigo());
                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent i = new Intent(c, AtualizarProdutoActivity.class);
                            i.putExtra("posicao", position);
                            c.startActivity(i);
                            adapterRecyclerProdutos.notifyDataSetChanged();
                        } catch (Exception e) {
                            Toast.makeText(c, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                dialog.setNegativeButton("Não", null);
                dialog.show();

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    // AtualizarFragment - End

    // SobreFragment - Begin

    public static View verificaoDoTema(Context context) {
        View aboutPage = null;

        try {
            int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    aboutPage = new AboutPage(context)
                            .isRTL(false)
                            .setImage(R.drawable.tautau_mascote_oculos)
                            .setDescription("Somos especializados, focados, divertidos e diretos. Vamos vestir a camisola da tua marca, empresa ou projeto com o objetivo de te proporcionar os melhores resultados. Esta é a nossa garantia.")
                            .addGroup("Ótimos Serviços, Profissionais Experientes")
                            .addEmail("geral@tautau.pt")
                            .addWebsite("https://tautau.pt/")
                            .addFacebook("tautauagency")
                            .addInstagram("tautauagency/")
                            .enableDarkMode(false)
                            .create();
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    aboutPage = new AboutPage(context)
                            .isRTL(false)
                            .setImage(R.drawable.tautau_mascote_oculos)
                            .setDescription("Somos especializados, focados, divertidos e diretos. Vamos vestir a camisola da tua marca, empresa ou projeto com o objetivo de te proporcionar os melhores resultados. Esta é a nossa garantia.")
                            .addGroup("Ótimos Serviços, Profissionais Experientes")
                            .addEmail("geral@tautau.pt")
                            .addWebsite("https://tautau.pt/")
                            .addFacebook("tautauagency")
                            .addInstagram("tautauagency/")
                            .enableDarkMode(true)
                            .create();
                    break;
                default:
                    Toast.makeText(context, "Erro: " + currentNightMode, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return aboutPage;

    }

    public static View definicaoDoLayoutSobre(View aboutPage, View view) {
        if (aboutPage != null) {
            return aboutPage;
        } else {
            aboutPage = view;
        }
        return aboutPage;
    }

    // SobreFragment - End

    // FraseActivity - Begin

    public static void adicionarFrase(Button buttonConfirmar, final EditText editAutor, final EditText editFrase, final Context c, final Activity activity) {

        String textoAutor = editAutor.getText().toString();

        if (editAutor.getText().toString().isEmpty()) {
            textoAutor = "Desconhecido";
        }

        final String finalTextoAutor = textoAutor;

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoFraseInspiradora = editFrase.getText().toString();

                if (textoFraseInspiradora.isEmpty()) {
                    Toast.makeText(c, "Introduza uma Frase!", Toast.LENGTH_SHORT).show();
                } else {

                    String childLength = String.valueOf(getChildrenCount() + 1);

                    try {
                        Frases frase = new Frases();
                        frase.setFrase(textoFraseInspiradora);
                        frase.setId(childLength);
                        frase.setAutor(finalTextoAutor);
                        frase.guardar();

                        limparCamposFrase(editAutor, editFrase);
                        Toast.makeText(c, "Frase adicionada com Sucesso!", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(c, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });


    }

    private static void limparCamposFrase(EditText editAutor, EditText editFrase) {

        editAutor.setText("");
        editFrase.setText("");

    }

    // FraseActivity - End

    // AtualizarProdutoActivity - Begin

    public static void atualizarProduto(FloatingActionButton fab, final int position, final TextView textCodigo, final TextView textDescricao, final TextView textQuantidade, final ImageView imageProduto, final Activity activity) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    final String codigo = textCodigo.getText().toString();
                    final String descricao = textDescricao.getText().toString();
                    final String quantidade = textQuantidade.getText().toString();

                    boolean verificarCampos = !codigo.isEmpty() || !descricao.isEmpty() || !quantidade.isEmpty();


                    if (verificarCampos && getProdutoImagem()) {

                        enviarProdutoAtualizar(codigo, descricao, quantidade, activity.getApplicationContext(), position);

                        Toast.makeText(activity.getApplicationContext(), "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        limparCamposAtualizar(textCodigo, textQuantidade, textDescricao);

                        imageProduto.setImageResource(R.drawable.padrao_produto);
                        setProdutoFoto(null);
                        setProdutoImagem(false);
                        setIsResultadoFull(false);

                    } else if (verificarCampos) {

                        Produtos produto = new Produtos();
                        produto.setUniqueID(listaProdutos.get(position).getUniqueID());
                        produto.setCodigo(codigo);
                        produto.setDescricao(descricao);
                        produto.setQuantidade(quantidade);
                        produto.setFoto(listaProdutos.get(position).getFoto());
                        produto.guardarDados();

                        Toast.makeText(activity.getApplicationContext(), "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        limparCamposInserir(textCodigo, textQuantidade, textDescricao);

                        imageProduto.setImageResource(R.drawable.padrao_produto);
                        setProdutoFoto(null);
                        setProdutoImagem(false);
                        setIsResultadoFull(false);

                    } else {

                        Toast.makeText(activity.getApplicationContext(), "Preencha pelo menos um Campo!", Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(activity.getApplicationContext(), "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                    limparCamposAtualizar(textCodigo, textQuantidade, textDescricao);
                    setIsResultadoFull(false);
                    activity.finish();

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });

        Log.i("msg", "resultado:" + getIsResultadoFull());

    }

    private static void enviarProdutoAtualizar(final String codigo, final String descricao, final String quantidade, Context context, final int position) {

        try {

            StorageReference storageReference = ConfiguracaoFireBase.getStorageReference();
            StorageReference imagemRef = storageReference.child("imagens").child("produtos").child(listaProdutos.get(position).getUniqueID() + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(getDadosImagem());

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            setProdutoFoto(uri.toString());

                            Produtos produto = new Produtos();
                            produto.setUniqueID(listaProdutos.get(position).getUniqueID());
                            produto.setCodigo(codigo);
                            produto.setDescricao(descricao);
                            produto.setQuantidade(quantidade);
                            produto.setFoto(getProdutoFoto());
                            produto.guardarDados();

                        }
                    });

                }
            });

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

    public static void recuperarDadosImagem(int requestCode, Intent data, Bitmap imagem, ImageView imageProduto, Activity activity) {

        try {

            switch (requestCode) {
                case SELECAO_CAMERA:
                    assert data != null;
                    imagem = (Bitmap) data.getExtras().get("data");
                    imageProduto.setImageBitmap(imagem);
                    break;

                case SELECAO_FOTOGRAFIAS:
                    assert data != null;
                    Uri localImagemSelecionada = data.getData();
                    ImageDecoder.Source source = ImageDecoder.createSource(Objects.requireNonNull(activity).getContentResolver(), localImagemSelecionada);
                    imagem = ImageDecoder.decodeBitmap(source);
                    imageProduto.setImageBitmap(imagem);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), "Erro ao Recuperar Imagem!", Toast.LENGTH_SHORT).show();
        }

        // COMECA TUDO AQUI
        if (imagem != null) {

            // Recuperar Dados da Imagem para o Firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();
            setDadosImagem(dadosImagem);
            setProdutoImagem(true);

        }

    }

    public static void definirCamposAtualizar(TextView textQuantidade, TextView textDescricao, ImageView imageProduto, Produtos produto) {

        // Definição de Campos dos Produtos
        textQuantidade.setText(produto.getQuantidade());
        textDescricao.setText(produto.getDescricao());
        Picasso.get().load(produto.getFoto()).into(imageProduto);

    }

    public static void definirCodigoQr(TextView textCodigo, Produtos produto, Context context) {

        String resultado = Configs.getResultado();

        try {
            if (Configs.getIsResultadoFull()) {
                textCodigo.setText(resultado);
            } else {
                textCodigo.setText(produto.getCodigo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public static void abrirLeitorCamaraFotografias(ImageButton imagemQrCode, ImageButton imagemCamara, ImageButton imagemFotografias, final Activity activity) {
        imagemQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity.getApplicationContext(), QrCodeActivity.class));
                setIsResultadoFull(true);
            }
        });

        imagemCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (i.resolveActivity(Objects.requireNonNull(activity).getPackageManager()) != null) {
                    activity.startActivityForResult(i, SELECAO_CAMERA);
                }

            }
        });

        imagemFotografias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(Objects.requireNonNull(activity).getPackageManager()) != null) {
                    activity.startActivityForResult(i, SELECAO_FOTOGRAFIAS);
                }

            }
        });

    }

    private static void limparCamposAtualizar(TextView textCodigo, TextView textQuantidade, TextView textDescricao) {
        textCodigo.setText("");
        textQuantidade.setText("");
        textDescricao.setText("");
    }

    public static Boolean getIsResultadoFull() {
        return isResultadoFull;
    }

    public static void setIsResultadoFull(Boolean isResultadoFull) {
        Configs.isResultadoFull = isResultadoFull;
    }

    // AtualizarProdutoActivity - End

    // HomeActivity - Begin

    public static void configuracaoMenuHome(MenuItem item, Activity activity) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                ConfiguracaoFireBase.getFirebaseAuth().signOut();
                activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
                activity.finish();
                break;
            case R.id.menuAdicionarFrase:
                activity.startActivity(new Intent(activity.getApplicationContext(), AdicionarFraseActivity.class));
                break;
            case R.id.menuRemoverFrase:
                activity.startActivity(new Intent(activity.getApplicationContext(), RemoverFraseActivity.class));
                break;
            case R.id.menuConta:
                activity.startActivity(new Intent(activity.getApplicationContext(), DefinicoesContaActivity.class));
                break;
        }
    }

    public static void definicaoMenuHome(Menu menu, Activity activity) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
    }

    public static void definirPerfil(TextView textUserName, TextView textEmail, ImageView imageProfile, Context context) {

        try {

            textUserName.setText(Objects.requireNonNull(ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser()).getDisplayName());
            textEmail.setText(ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser().getEmail());

            Uri url = Objects.requireNonNull(ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser()).getPhotoUrl();

            if (url != null) {

                Glide.with(context).load(url).into(imageProfile);

            } else {
                imageProfile.setImageResource(R.drawable.foto_perfil_padrao);
            }


        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(context, "ERRO: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    // HomeActivity - End

    // LoadingActivity - Begin

    public static void configuracaoDoLoadingInicial(final Activity activity, final Intent loginActivity) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        activity.startActivity(loginActivity);
                        activity.finish();
                    }
                },
                3000
        );
    }

    // LoadingActivity - End

    // LoginActivity - Begin

    public static void loginUtilizador(final FirebaseAuth mAuth, final EditText textEmail, final EditText textPassword, final Activity activity) {

        try {
            mAuth.signInWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        verificarUtilizador(mAuth, activity);
                        limparCamposLogin(textEmail, textPassword);
                        Intent intent = new Intent(activity.getApplicationContext(), HomeActivity.class);
                        activity.startActivity(intent);
                        setErroFrase(true);
                    } else {
                        String excecao;

                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidUserException e) {
                            excecao = "Utilizador não Registado";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Credenciais Incorretas";
                        } catch (Exception e) {
                            excecao = "Erro ao fazer Login: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(activity.getApplicationContext(), excecao, Toast.LENGTH_SHORT).show();

                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void verificarUtilizador(final FirebaseAuth mAuth, final Activity activity) {

        if (mAuth != null) {

            FirebaseUser utilizador = mAuth.getCurrentUser();

            if (utilizador != null) {
                DatabaseReference databaseReference = ConfiguracaoFireBase.getDataBaseReference();
                setUserID(mAuth.getCurrentUser().getUid());
                recuperarFrases(databaseReference, utilizador);

                Toast.makeText(activity, "Bem Vindo, " + utilizador.getDisplayName() + "!", Toast.LENGTH_SHORT).show();
                activity.startActivity(new Intent(activity.getApplicationContext(), HomeActivity.class));
                activity.finish();

            }

        }

    }

    public static void botaoLogin(final Button buttonLogin, final EditText textEmail, final EditText textPassword, final Activity activity, final FirebaseAuth mAuth) {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!textEmail.getText().toString().equals("")) {
                        if (!textPassword.getText().toString().equals("")) {
                            loginUtilizador(mAuth, textEmail, textPassword, activity);
                        } else {
                            Toast.makeText(activity.getApplicationContext(), "Introduza a Password!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Introduza o Email!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "Tente Novamente!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private static void limparCamposLogin(EditText textEmail, EditText textPassword) {
        textEmail.setText("");
        textPassword.setText("");
    }

    // LoginActivity - End

    // SobreActivity - Begin

    public static void configuracaoMenuLogin(MenuItem item, Activity activity) {
        switch (item.getItemId()) {
            case R.id.menuSobre:
                ConfiguracaoFireBase.getFirebaseAuth().signOut();
                activity.startActivity(new Intent(activity.getApplicationContext(), SobreActivity.class));
                break;
            case R.id.menuEsquecerPassword:
                activity.startActivity(new Intent(activity.getApplicationContext(), EsquecerPasswordActivity.class));
                break;
            case R.id.menuCriarConta:
                activity.startActivity(new Intent(activity.getApplicationContext(), CriarContaActivity.class));
                break;
        }
    }

    public static void definicaoMenuMain(Menu menu, Activity activity) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
    }

    // SobreActivity - End

    // QrCodeActivity - Begin

    public static void scanCode(IntentIntegrator integrator, Activity activity) {

        try {
            integrator.setCaptureActivity(CapturaActivity.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Procurando Código de Barras...");
            integrator.initiateScan();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public static void lerCodigoDeBarras(int requestCode, int resultCode, Intent data, final Activity activity, final IntentIntegrator integrator) {

        try {

            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            setResultado(result.getContents());

            if (result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false);
                builder.setMessage(result.getContents());
                builder.setTitle("Resultado:");
                builder.setPositiveButton("Novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Configs.scanCode(integrator, activity);
                    }
                }).setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(activity.getApplicationContext(), "Sem Resultados", Toast.LENGTH_SHORT).show();
                activity.finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    // QrCodeActivity - End

    // CriarContaActivity - Begin

    public static void criarConta(Button buttonCriarConta, final EditText editNome, final EditText editEmail, final EditText editPassword, final EditText editConfirmarPassword, final FirebaseAuth mAuth, final Activity activity) {
        buttonCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String confirmarPassword = editConfirmarPassword.getText().toString();
                final String nome = editNome.getText().toString();

                try {
                    if (!nome.isEmpty()) {


                        if (!email.isEmpty()) {
                            if (!password.isEmpty()) {
                                if (!confirmarPassword.isEmpty()) {
                                    if (password.equals(confirmarPassword)) {

                                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(activity.getApplicationContext(), "Conta Criada com Sucesso!", Toast.LENGTH_SHORT).show();
                                                    definirNomePerfil(mAuth, activity.getApplicationContext(), nome);
                                                    activity.finish();
                                                    mAuth.signOut();
                                                    setErroFrase(true);

                                                } else {

                                                    String excecao;

                                                    try {
                                                        throw Objects.requireNonNull(task.getException());
                                                    } catch (FirebaseAuthWeakPasswordException e) {
                                                        excecao = "Introduza uma Password mais Forte!";
                                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                                        excecao = "Introduza um E-mail Válido!";
                                                    } catch (FirebaseAuthUserCollisionException e) {
                                                        excecao = "Esta conta já se encontra Registrada!";
                                                    } catch (Exception e) {
                                                        excecao = "Erro ao Registrar Utilizador: " + e.getMessage();
                                                        e.printStackTrace();
                                                    }

                                                    Toast.makeText(activity.getApplicationContext(), excecao, Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        });

                                    } else {
                                        Toast.makeText(activity.getApplicationContext(), "As Passwords não Correspondem!", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(activity.getApplicationContext(), "Confirme a sua Password!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(activity.getApplicationContext(), "Introduza uma Password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity.getApplicationContext(), "Introduza um Email!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity.getApplicationContext(), "Introduza o seu Nome!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public static void definirNomePerfil(FirebaseAuth mAuth, Context context, String nome) {
        try {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome).build();

            Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdates);

        } catch (Exception e) {

            Toast.makeText(context, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }

    // CriarContaActivity - End

    // DefinicoesContaActivity - Begin

    public static void cliqueImagensNomeFotografiaCamaraConta(final Activity activity, ImageButton imageCamara, ImageButton imageFotografias, ImageButton imageNome, TextView textRemoverFrase, final int SELECAO_FOTOGRAFIAS, final int SELECAO_CAMERA, final EditText textNome, final FirebaseUser utilizador) {

        textRemoverFrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final FirebaseUser utilizador = ConfiguracaoFireBase.getFirebaseAuth().getCurrentUser();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

                    // Configurar Título e Mensagem
                    dialog.setTitle("Confirmar Exclusão");
                    dialog.setCancelable(false);
                    assert utilizador != null;
                    dialog.setMessage("Deseja remover a sua Conta: " + utilizador.getDisplayName());
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                activity.startActivity(new Intent(activity.getApplicationContext(), RemoverContaActivity.class));

                            } catch (Exception e) {
                                Toast.makeText(activity.getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                    dialog.setNegativeButton("Não", null);
                    dialog.show();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }
        });

        imageFotografias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(i, SELECAO_FOTOGRAFIAS);
                }

            }
        });

        imageCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(activity.getPackageManager()) != null) {
                    activity.startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imageNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nome = textNome.getText().toString();
                String userName = utilizador.getDisplayName();

                if (!nome.isEmpty() && !textNome.getText().toString().equals(userName)) {

                    try {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(nome).build();

                        utilizador.updateProfile(profileUpdates);

                        Toast.makeText(activity.getApplicationContext(), "Nome Atualizado com Sucesso", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                        Toast.makeText(activity.getApplicationContext(), "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();

                    }
                } else {

                    Toast.makeText(activity.getApplicationContext(), "Introduza um Nome!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public static void resultadoDaActivity(int requestCode, Intent data, Bitmap imagem, final Activity activity, final CircleImageView imageFoto, final FirebaseUser utilizador, StorageReference storageReference) {

        try {

            switch (requestCode) {
                case SELECAO_CAMERA:
                    assert data != null;
                    imagem = (Bitmap) data.getExtras().get("data");
                    break;

                case SELECAO_FOTOGRAFIAS:
                    assert data != null;
                    Uri localImagemSelecionada = data.getData();
                    ImageDecoder.Source source = ImageDecoder.createSource(activity.getContentResolver(), localImagemSelecionada);
                    imagem = ImageDecoder.decodeBitmap(source);

                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), "Erro ao Recuperar Imagem!", Toast.LENGTH_SHORT).show();

        }

        // COMECA TUDO AQUI
        if (imagem != null) {

            imageFoto.setImageBitmap(imagem);
            String userID = utilizador.getUid();

            // Recuperar Dados da Imagem para o Firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            // Guardar Imagem no Firebase
            final StorageReference imagemRef = storageReference.child("imagens").child("perfil").child(userID + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(activity.getApplicationContext(), "Erro ao fazer Upload da Imagem!", Toast.LENGTH_SHORT).show();

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(activity.getApplicationContext(), "Sucesso ao fazer Upload da Imagem!", Toast.LENGTH_SHORT).show();

                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            final Uri url = task.getResult();

                            UserProfileChangeRequest profileRequest = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

                            Objects.requireNonNull(utilizador).updateProfile(profileRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        getProfilePhotoUrl(activity.getApplicationContext(), imageFoto);

                                    } else {

                                        Toast.makeText(activity.getApplicationContext(), "Erro ao definir a Foto de Perfil!", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                        }
                    });

                }
            });


        }
    }

    // DefinicoesContaActivity - End

    // RemoverFraseActivity - Begin

    public static void configuracaoRecyclerViewFrases(final RecyclerView recyclerView, final AdapterRecyclerFrases adapterRecyclerProdutos, final Activity activity, final FirebaseUser utilizador, final TextView textFrase, final RelativeLayout carregamentoFrases) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRecyclerProdutos);
        recyclerView.addItemDecoration(new DividerItemDecoration(activity.getApplicationContext(), LinearLayout.VERTICAL));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity.getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, final int position) {
                try {   

                    final Frases frase = RemoverFraseActivity.listaFrases.get(position);
                    final DatabaseReference databaseReference = ConfiguracaoFireBase.getDataBaseReference().child(utilizador.getUid()).child("frases");

                    AlertDialog.Builder dialog;
                    dialog = new AlertDialog.Builder(activity);

                    // Configurar Título e Mensagem
                    dialog.setTitle("Confirmar Exclusão");
                    dialog.setMessage("Deseja remover a Frase: " + frase.getFrase());
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                databaseReference.child(frase.getId()).removeValue();
                                adapterRecyclerProdutos.notifyItemRemoved(position);
                                Toast.makeText(activity.getApplicationContext(), "Eliminada com Sucesso!", Toast.LENGTH_SHORT).show();
                                recuperarFrases(databaseReference, utilizador, carregamentoFrases, adapterRecyclerProdutos, textFrase);
                            } catch (Exception e) {
                                Toast.makeText(activity.getApplicationContext(), "Erro ao Eliminar Frase!" , Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                    dialog.setNegativeButton("Não", null);
                    dialog.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));

    }

    public static void recuperarFrases(DatabaseReference databaseReference, FirebaseUser utilizador) {
        try {

            databaseReference.child(utilizador.getUid()).child("frases").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    RemoverFraseActivity.listaFrases.clear();

                    for (DataSnapshot dados : snapshot.getChildren()) {

                        Frases frases = dados.getValue(Frases.class);
                        RemoverFraseActivity.listaFrases.add(frases);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void recuperarFrases(DatabaseReference databaseReference, FirebaseUser utilizador, final RelativeLayout carregamentoFrases, final AdapterRecyclerFrases adapterRecyclerFrases, final TextView textFrase) {
        try {

            databaseReference.child(utilizador.getUid()).child("frases").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    RemoverFraseActivity.listaFrases.clear();

                    for (DataSnapshot dados : snapshot.getChildren()) {

                        Frases frases = dados.getValue(Frases.class);
                        RemoverFraseActivity.listaFrases.add(frases);

                        setFrasesCount((int) snapshot.getChildrenCount());

                        Log.i("msg", "teste: " + getFrasesCount());

                        if (getFrasesCount() > 0) {
                            textFrase.setVisibility(View.GONE);
                        } if (getFrasesCount() == 0) {
                            textFrase.setVisibility(View.VISIBLE);
                        }

                    }

                    carregamentoFrases.setVisibility(View.GONE);

                    adapterRecyclerFrases.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void swipeRefreshLayoutFrases(final SwipeRefreshLayout swipeRefreshLayoutFrases, final DatabaseReference databaseReference, final FirebaseUser utilizador, final AdapterRecyclerFrases adapterRecyclerFrases) {
        swipeRefreshLayoutFrases.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayoutFrases.setRefreshing(false);
                recuperarFrases(databaseReference, utilizador);
                adapterRecyclerFrases.notifyDataSetChanged();
            }
        });
    }

    // RemoverFraseActivity - End

    // RemoverContaActivity - Begin

    public static void confirmarRemoverConta(Button buttonRemoverConta, final EditText editPassword, final FirebaseUser utilizador, final FirebaseAuth mAuth, final Activity activity) {

        buttonRemoverConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = editPassword.getText().toString();

                if (!password.isEmpty()) {

                    AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(utilizador.getEmail()), password);

                    utilizador.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                utilizador.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(activity, "Conta: " + '"' + utilizador.getDisplayName() + '"' + " Removida com Sucesso!", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            HomeActivity homeActivity = new HomeActivity();
                                            homeActivity.finish();
                                            DefinicoesContaActivity definicoesContaActivity = new DefinicoesContaActivity();
                                            definicoesContaActivity.finish();
                                            activity.finish();
                                            activity.startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));

                                        } else {
                                            Toast.makeText(activity, "Erro ao Remover a Conta: " + '"' + utilizador.getDisplayName() + '"' + "!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, "Password Incorreta!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    Toast.makeText(activity, "Introduza a Password!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    // RemoverContaActivity - End

    // Adapter - Begin

    // NENHUM CODIGO AQUI
    // PARA FACILITAR DECIDI DEIXAR O CÓDIGO NO ADAPTER

    // Adapter - End


}
