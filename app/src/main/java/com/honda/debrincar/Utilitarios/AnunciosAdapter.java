package com.honda.debrincar.Utilitarios;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.honda.debrincar.Activities.Fragments.PaginaAnuncioCampFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioDoaFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioSolFragment;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnunciosAdapter extends ArrayAdapter<Anuncio> {

    private Context atualContext;
    private Anuncio anuncioAtual;
    private String anuncioType;

    public AnunciosAdapter(@NonNull Context context, int resource, @NonNull List<Anuncio> objects) {
        super(context, resource, objects);

        atualContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anuncio_layout, parent, false);

        }

        anuncioAtual = getItem(position);
        anuncioType = anuncioAtual.getAnuncioType();
        anuncioAtual.setAnuncioType(anuncioType);

        TextView anuncioTitulo = listItemView.findViewById(R.id.item_anun_titulo);
        anuncioTitulo.setText(anuncioAtual.getTitulo());

        TextView anuncioDescricao = listItemView.findViewById(R.id.item_anun_desc);
        anuncioDescricao.setText(anuncioAtual.getDescricao());

        TextView textTipoAnuncio = listItemView.findViewById(R.id.tipo_anuncio);

        switch (anuncioAtual.getAnuncioType()){
            case "doacao":
                textTipoAnuncio.setText("DOAÇÃO");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.amareloEscuro));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azulMedio));
                setupOnclickListeners(listItemView, "DOAÇÃO", anuncioAtual);
                break;
            case "solicitacao":
                textTipoAnuncio.setText("SOLICITAÇÃO");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.azulMedio));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.amareloEscuro));
                setupOnclickListeners(listItemView, "SOLICITAÇÃO", anuncioAtual);
                break;
            case "campanha":
                textTipoAnuncio.setText("CAMPANHA");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.amareloClaro));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azulClaro));
                setupOnclickListeners(listItemView, "CAMPANHA", anuncioAtual);
                break;
            default:
                Toast.makeText(getContext(), "Erro ao carregar anuncios.", Toast.LENGTH_LONG).show();
                break;
        }




        return listItemView;
    }

    private void chamaFragmento(String anunTipo, Anuncio anuncio) {

        FragmentManager fm = ((AppCompatActivity) atualContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        switch (anunTipo){
            case "DOAÇÃO":
                PaginaAnuncioDoaFragment paginaAnuncioDoaFragment = new PaginaAnuncioDoaFragment();
                paginaAnuncioDoaFragment.setAnuncio(anuncio);
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioDoaFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case "SOLICITAÇÃO":
                PaginaAnuncioSolFragment paginaAnuncioSolFragment =  new PaginaAnuncioSolFragment();
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioSolFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case "CAMPANHA":
                PaginaAnuncioCampFragment paginaAnuncioCampFragment = new PaginaAnuncioCampFragment();
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioCampFragment)
                        .addToBackStack(null)
                        .commit();
                break;
                default:
                    Toast.makeText(getContext(), "Erro ao carregar anúncio.", Toast.LENGTH_LONG).show();
                    break;
        }
    }

    public void setupOnclickListeners(View listItemView, String anunTipo, final Anuncio anuncio){

        final String tipoAnuncio = anunTipo;

        LinearLayout layout_dados = listItemView.findViewById(R.id.item_anun_dados);
        layout_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaFragmento(tipoAnuncio, anuncio);
            }
        });

        CircleImageView imagemItem = listItemView.findViewById(R.id.item_anun_imagem);
        imagemItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaFragmento(tipoAnuncio, anuncio);
            }
        });

        final ImageView coracaoFav = listItemView.findViewById(R.id.coracao_fav);
        coracaoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coracaoFav.getDrawable().getConstantState().equals(getContext().getResources().getDrawable(R.drawable.coracao_fav_branco).getConstantState()))
                {
                    Drawable imagem = getContext().getResources().getDrawable(R.drawable.coracao_fav_amarelo);
                    coracaoFav.setImageDrawable(imagem);
                }else
                {
                    Drawable imagem = getContext().getResources().getDrawable(R.drawable.coracao_fav_branco);
                    coracaoFav.setImageDrawable(imagem);

                }
            }
        });

    }
}
