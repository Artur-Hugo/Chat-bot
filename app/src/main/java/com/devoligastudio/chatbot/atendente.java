package com.devoligastudio.chatbot;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.devoligastudio.chatbot.Adapter.MensagemAdapter;
import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.devoligastudio.chatbot.helper.Base64Custom;
import com.devoligastudio.chatbot.model.Conversas_mensagem;
import com.devoligastudio.chatbot.model.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class atendente extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;

    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;

    private String idUsuarioRemetente;
    private String nomeUsuarioremetente;

    private DatabaseReference firebase;

    private ListView listView;
    // private ArrayList<Mensagem> mensagens;
    // private ArrayAdapter<Mensagem> adapter;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter adapter;

    private ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendente);

        toolbar = findViewById(R.id.tb_conversa);
        editMensagem = findViewById(R.id.edit_mensagemid);
        btMensagem = findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_conversas);

//dados do usuario logado
        Preferencia preferencia = new Preferencia(atendente.this);
        idUsuarioRemetente = preferencia.getIdentificador();
        nomeUsuarioremetente = preferencia.getNOME();

        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            nomeUsuarioDestinatario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);

        }
// Configura toolbar
        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoMensagem = editMensagem.getText().toString();

                if(textoMensagem.isEmpty()){
                    Toast.makeText(atendente.this, "Digite uma mensagem para enviar", Toast.LENGTH_LONG).show();

                }else{
                    //mandar dados ao firebase
                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRemetente);
                    mensagem.setMensagem(textoMensagem);
                    editMensagem.setText("");



                    //salvamos mensagem para o remetente
                    Boolean retornomensagemremetente = salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);
                    if(!retornomensagemremetente){
                        Toast.makeText(
                                atendente.this,
                                "Problema ao salvar mensagem, tente novamente!",
                                Toast.LENGTH_LONG
                        ).show();
                    } else{
                        //salvar mensagem para o destinatario
                        Boolean retornomensagemdestinatario = salvarMensagem(idUsuarioDestinatario, idUsuarioRemetente, mensagem);

                    }



                    Conversas_mensagem conversasMensagem = new Conversas_mensagem();
                    conversasMensagem.setIdUsuario(idUsuarioDestinatario);
                    conversasMensagem.setNome(nomeUsuarioDestinatario);
                    conversasMensagem.setMensagem(textoMensagem);
                    Boolean retornoconversaremetente = salvarConversa(idUsuarioRemetente,idUsuarioDestinatario,conversasMensagem);
                    Boolean retornoconversadestinatario = salvarConversa(idUsuarioDestinatario,idUsuarioRemetente,conversasMensagem);
                    if (!retornoconversadestinatario){
                        Toast.makeText(
                                atendente.this,
                                "Problema ao salvar conversa, tente novamente!",
                                Toast.LENGTH_LONG
                        ).show();
                    }


                    //salvar conversa para o remetente
                    if (!retornoconversaremetente){
                        Toast.makeText(
                                atendente.this,
                                "Problema ao salvar conversa, tente novamente!",
                                Toast.LENGTH_LONG
                        ).show();
                    }else {
                        //salvamos a Conversa para o Destinatario
                        conversasMensagem = new Conversas_mensagem();
                        conversasMensagem.setIdUsuario(idUsuarioRemetente);
                        conversasMensagem.setNome(nomeUsuarioremetente);
                        conversasMensagem.setMensagem(textoMensagem);

                        salvarConversa(idUsuarioDestinatario,idUsuarioRemetente,conversasMensagem);
                    }

                }
            }
        });

        // Monta listview e adapter
        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(atendente.this, mensagens);
        listView.setAdapter( adapter );

        adapter.notifyDataSetChanged();

        // Recuperar mensagens do Firebase
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child( idUsuarioRemetente )
                .child( idUsuarioDestinatario );

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpar mensagens
                mensagens.clear();

                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Mensagem mensagem = dados.getValue( Mensagem.class );
                    mensagens.add( mensagem);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener( valueEventListenerMensagem );



    }




    private boolean salvarMensagem(String idRemetente, String idDestinatario, Mensagem mensagem){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente).
                    child(idDestinatario)
                    .push()
                    .setValue(mensagem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }


    }

    private boolean salvarConversa(String idUsuarioRemetente, String idUsuarioDestinatario, Conversas_mensagem conversa){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idUsuarioRemetente)
                    .child(idUsuarioDestinatario)
                    .setValue(conversa);

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}

