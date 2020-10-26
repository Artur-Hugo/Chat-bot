package com.devoligastudio.chatbot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.devoligastudio.chatbot.Adapter.ChatMessageAdapter;
import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.devoligastudio.chatbot.helper.Base64Custom;
import com.devoligastudio.chatbot.model.ChatMessage;
import com.devoligastudio.chatbot.model.Contato;
import com.devoligastudio.chatbot.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   ListView listView;
   ImageButton btnsend;
   EditText editTextMsg;
   ImageView imageView;


   private Bot bot;
   public static Chat chat;
   private ChatMessageAdapter adapter;
    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnsend = findViewById(R.id.btnsendid);
        editTextMsg = findViewById(R.id.editTextMsg);
        imageView = findViewById(R.id.imageview);

        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView.setAdapter(adapter);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String message = editTextMsg.getText().toString();

                String response = chat.multisentenceRespond(editTextMsg.getText().toString());

                if(TextUtils.isEmpty(message)){
                    Toast.makeText(MainActivity.this, "Please enter a query", Toast.LENGTH_SHORT).show();
                    return;

                }

                sendMessage(message);
                BotaReply(message);

                editTextMsg.setText("");
                listView.setSelection(adapter.getCount() );
            }
        });
/*
        boolean available = isSDCartAvailable();

        AssetManager assets = getResources().getAssets();
        File fileName = new File(Environment.getExternalStorageDirectory().toString() + "/TBC/bots/TBC");

        boolean makefile = fileName.mkdirs();

        if(fileName.exists()){

            try{
                for (String dir : assets.list("TBC")){
                    File subdir = new File(fileName.getPath() + "/" + dir);
                    boolean subdir_Check = subdir.mkdirs();

                    for(String file : assets.list("TBC/" + dir )){
                        File newFile = new File(fileName.getPath() + "/" + dir +"/"+ file);

                        if(newFile.exists()) {
                            continue;
                        }

                        InputStream in;
                        OutputStream out;
                        String str;
                        in = assets.open("TBC/" + dir + "/" + file );
                        out = new FileOutputStream(fileName.getPath() + "/" + dir + file);

                        copyFile(in,out);
                        in.close();
                        out.flush();
                        out.close();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MagicStrings.root_path = Environment.getExternalStorageDirectory().toString() + "/TBC";
        AIMLProcessor.extension = new PCAIMLProcessorExtension();

        bot = new Bot ("TBC",MagicStrings.root_path, "chat");*/
        chat = new Chat(bot);
    }/*

    private void copyFile(InputStream in, OutputStream out) throws IOException{
        byte[] buffer = new byte[1024];
        int read;

        while ((read = in.read(buffer)) != -1){
    out.write(buffer, 0, read);
        }

    }

    public static boolean isSDCartAvailable() {
return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)? true : false;
    } */

    private void sendMessage(String message){
ChatMessage chatMessage = new ChatMessage(false,true, message);
adapter.add(chatMessage);
    }
    private void BotaReply(String message){

        switch (message){
            case "oi":
                ChatMessage chatMessage = new ChatMessage(false,false, "Oi o que deseja?\n-amarelo\n-Ave");
                adapter.add(chatMessage); break;
            case "amarelo":
                chatMessage = new ChatMessage(false,false, "amarelo é uma cor");
                adapter.add(chatMessage); break;
            case "ave":
                chatMessage =  new  ChatMessage(false,false, "ave é um animal");
                adapter.add(chatMessage); break;
            case "Quero falar com a atendente":
                chatMessage =  new  ChatMessage(false,false, "Ok, quer falar com a atendente.");

                adapter.add(chatMessage);
                Toast.makeText(MainActivity.this, "Direcionando para o atendimento", Toast.LENGTH_LONG).show();
                abrirCadastroContato();
                break;
            default:
                chatMessage =  new  ChatMessage(false,false, "Não entendi");
                adapter.add(chatMessage); break;
        }
  if(message.equals("oi")){
        ChatMessage chatMessage = new ChatMessage(false,false, "Oi o que deseja?\n-amarelo\n-Ave");
        adapter.add(chatMessage);
    }
    }

    private void abrirCadastroContato() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        //alertDialog.setView( editText );

        //Configura botões
        alertDialog.setPositiveButton("Falar com o atendente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = "atendentemaster@hotmail.com";

                //Valida se o e-mail foi digitado
                if( emailContato.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                }else{

                    //Verificar se o usuário já está cadastrado no nosso App
                    //
                    // converte o email em base64
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //Recuperar instância Firebase


                    //Definir uma referencia para o nó do usuario que é o identificador
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(identificadorContato);


                    //Consultar os dados do firebase apenas em uma unica vez
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Caso temos dados recuperados no datasnapshot
                            if(dataSnapshot.getValue() !=null){
//para conseguir recuperar os dados do usuario que está logado atualmente
                                Usuario usuariocontato = dataSnapshot.getValue(Usuario.class);

                                Preferencia preferencia = new Preferencia(MainActivity.this);
                                String identificadorUsuarioLogado = preferencia.getIdentificador();


                                // usuarioauth.getCurrentUser().getEmail();

                                //Criamos o nó contatos e listamos no fragmento contato

                                //pegamos uma refencia no e verificamos se essa pessoa existe no banco de dados
                                firebase = ConfiguracaoFirebase.getFirebase();
                                //referencia para salvar os dados no nó contatos
                                firebase = firebase.child("contatos")
                                        .child(identificadorUsuarioLogado).child(identificadorContato)
                                ;

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuariocontato.getEmail());
                                contato.setNome(usuariocontato.getNome());

                                firebase.setValue(contato);
                                Toast.makeText(MainActivity.this, "Usuário possui cadastro.", Toast.LENGTH_LONG)
                                        .show();

                            }else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro.", Toast.LENGTH_LONG)
                                        .show();
                            }
                            IrAoAtendimento();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }


        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void IrAoAtendimento() {
        Intent intent = new Intent(getApplication(), atendente.class);

String Nome = "Atendente";

        intent.putExtra("nome", Nome );
        String email = "atendentemaster@hotmail.com";
        intent.putExtra("email", email );

        startActivity(intent);
    }

}
