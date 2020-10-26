package com.devoligastudio.chatbot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devoligastudio.chatbot.Preferencia;
import com.devoligastudio.chatbot.R;
import com.devoligastudio.chatbot.model.Mensagem;

import java.util.ArrayList;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagems;


    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagems = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(mensagems != null){
//Recupera dados do usuario remetente
            Preferencia preferencia = new Preferencia(context);
            String idUsuarioRementente = preferencia.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recupera mensagem
            Mensagem mensagem = mensagems.get(position);

            if(idUsuarioRementente.equalsIgnoreCase(mensagem.getIdUsuario())){
                view = inflater.inflate(R.layout.layout_mensagem_esquerda, parent, false);

            }else {
//Monta view a partir do xml
                view = inflater.inflate(R.layout.layout_mensagem_direita, parent, false);}

            //Recupera elemento para exibição
            TextView textomensagem = (TextView) view.findViewById(R.id.tv_mensagem);
            textomensagem.setText(mensagem.getMensagem());
        }

        return view;

    }
}
