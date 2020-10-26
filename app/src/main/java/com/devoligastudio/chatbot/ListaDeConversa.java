package com.devoligastudio.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devoligastudio.chatbot.Adapter.ConversaAdapter;
import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.devoligastudio.chatbot.helper.Base64Custom;
import com.devoligastudio.chatbot.model.Conversas_mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaDeConversa extends AppCompatActivity {

    private ListView listView;
    private ConversaAdapter adapter;
    private ArrayList<Conversas_mensagem> conversas;

    private DatabaseReference firebase;

    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_conversa);

        listView = findViewById(R.id.listaconversa);

        conversas = new ArrayList<>();
        adapter = new ConversaAdapter(getApplication(),0,conversas);

        listView.setAdapter(adapter);

        Preferencia preferencia = new Preferencia(this);
        String idUsuarioLogado = preferencia.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas").child(idUsuarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conversas.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Conversas_mensagem conversasMensagem = dados.getValue(Conversas_mensagem.class);
                    conversas.add(conversasMensagem);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplication(), atendente.class);

                Conversas_mensagem conversasMensagem = conversas.get(position);

                intent.putExtra("nome", conversasMensagem.getNome() );
                String email = Base64Custom.decodificarBase64( conversasMensagem.getIdUsuario() );
                intent.putExtra("email", email );

                startActivity(intent);
            }
        });



    }
    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }
}
