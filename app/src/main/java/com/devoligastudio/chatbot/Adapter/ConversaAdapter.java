package com.devoligastudio.chatbot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devoligastudio.chatbot.R;
import com.devoligastudio.chatbot.model.Conversas_mensagem;

import java.util.ArrayList;

public class ConversaAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Conversas_mensagem> conversasM;

    public ConversaAdapter(@NonNull Context c, int resource, @NonNull ArrayList<Conversas_mensagem> objects) {
        super(c,0, objects);
        this.context = c;
        this.conversasM = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if(conversasM != null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.lista_conversa,parent,false);

            TextView nome = (TextView) view.findViewById(R.id.tvtitulo);
            TextView ultimamensagem = (TextView) view.findViewById(R.id.tvsubtitulo);

            Conversas_mensagem conversasMensagem = conversasM.get(position);
            nome.setText(conversasMensagem.getNome());
            ultimamensagem.setText(conversasMensagem.getMensagem());

        }

        return view;
    }
}
