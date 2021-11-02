package com.devoligastudio.chatbot;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.devoligastudio.chatbot.Adapter.ChatMessageAdapter;
import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.devoligastudio.chatbot.helper.Base64Custom;
import com.devoligastudio.chatbot.model.ChatMessage;
import com.devoligastudio.chatbot.model.Contato;
import com.devoligastudio.chatbot.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
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
    private FirebaseAuth usuarioauth;
    private Toolbar toolbar;
   private Bot bot;
   public static Chat chat;
   private ChatMessageAdapter adapter;
    private String identificadorContato;
    private DatabaseReference firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tb_conversa);
        listView = findViewById(R.id.listView);
        btnsend = findViewById(R.id.btnsendid);
        editTextMsg = findViewById(R.id.editTextMsg);
        imageView = findViewById(R.id.imageview);

        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessage>());
        listView.setAdapter(adapter);

        toolbar.setTitle("Olá");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent( MainActivity.this, MeioPrincipal.class);
                startActivity(it);

            }
        });

        Toast.makeText(MainActivity.this, "Comece a dizer 'oi'.", Toast.LENGTH_LONG)
                .show();

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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//Exibir menu na sua tela
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sair:
                deslogarusuario();
                return true;
            case R.id.ajudaatendimento:
                IrAoAtendimento();
                return true;
            case R.id.ajuda:
                ajuda();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void ajuda() {
        Toast.makeText(MainActivity.this, "Escreva 'Oi' para começar a usar o Chatbot.", Toast.LENGTH_LONG)
                .show();

    }

    private void deslogarusuario() {

        Intent intent = new Intent(getApplicationContext(), fazerlogin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void sendMessage(String message){
ChatMessage chatMessage = new ChatMessage(false,true, message);
adapter.add(chatMessage);
    }
    private void BotaReply(String message){
        ChatMessage chatMessage;
        message.toLowerCase();
        boolean bol = true;


        if (bol == message.indexOf("oi") > -1) {/*ChatMessage chatMessage = new ChatMessage(false,false, "Oi o que deseja?\n-amarelo\n-Ave");
                adapter.add(chatMessage);*/
            Mensagens("Oi, escreva o nome de um chá para obter a informação sobre o chá.\n Se quiser falar com a atendente escreva 'atendente'.");
        } else if (bol == message.indexOf("verde") > -1) {/*
                chatMessage = new ChatMessage(false,false, "amarelo é uma cor");
                adapter.add(chatMessage); */
            Mensagens("O chá verde exerce uma importante função antioxidante, o que contribui para a prevenção de doenças cardiovasculares, diabetes e câncer, por exemplo. Os benefícios do chá verde se devem principalmente à sua composição rica em polifenóis, que são compostos naturais presente em vegetais e promovem uma defesa ao organismo.");
        } else if (bol == message.indexOf("preto") > -1) {
            chatMessage = new ChatMessage(false, false, "Assim como o chá branco e o chá verde, o chá preto é feito a partir da planta Camellia sinensis e também possui uma importante capacidade antioxidante. “O chá preto tem uma boa quantidade de cafeína, que pode manter um estado de alerta e redução do cansaço. As catequinas e cafeína presente nele podem auxiliar na redução de gordura corporal”, entrega a profissional.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("camomila") > -1) {
            chatMessage = new ChatMessage(false, false, "O chá de Camomila seca apresenta propriedades relaxantes e ligeiramente sedativas que ajudam a tratar a insônia, a relaxar e a tratar a ansiedade e o nervosismo. Além disso, este chá também pode ajudar a reduzir as cólicas. Ingredientes: 2 colheres de chá de flores secas de Camomila.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("hortelã") > -1) {
            chatMessage = new ChatMessage(false, false, "O aroma forte e agradável da hortelã ajuda a descongestionar as vias respiratórias. Essa erva também ajuda a amenizar os sintomas da asma, combate infecções de garganta e ajuda a prevenir outras doenças respiratórias. O chá de hortelã é uma bebida saborosa e indicada para quem quer emagrecer.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("hibisco") > -1) {
            chatMessage = new ChatMessage(false, false, "Por conter antioxidantes e bioflavonoides, um dos benefícios do chá de hibisco para a saúde é a regulação dos níveis de colesterol no organismo. Ou seja, essa bebida ajuda a elevar as taxas do HDL (colesterol bom) e reduzir as de LDL (colesterol ruim) e dos triglicerídeos.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("chá mate") > -1) {
            chatMessage = new ChatMessage(false, false, "O chá mate para a saúde estão relacionados com os seus constituintes como cafeína, os vários minerais e vitaminas, que fornecem diferentes tipos de propriedades para o chá, especialmente anti-oxidante, diurético, laxante suave e é um bom estimulante cerebral." );
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("erva cidreira") > -1) {
            chatMessage = new ChatMessage(false, false, "A erva cidreira possui ação benéfica em casos de gases, problemas estomacais e tem um efeito calmante que melhora casos de insônia, ansiedade, depressão e ajuda na redução do estresse.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("boldo") > -1) {
            chatMessage = new ChatMessage(false, false, "O chá de boldo é conhecido principalmente por conter ações digestivas. Tudo isso porque ele ativa a produção da bile pelo fígado, melhorando a divisão dos alimentos. Além disso, ajuda a diminuir cólicas intestinais e gases.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("alecrim") > -1) {
            chatMessage = new ChatMessage(false, false, "Apesar de mais conhecido como tempero, o alecrim pode e deve ser usado na forma de chá. Como bebida, a erva ajuda a fortalecer o sistema imunológico e também propicia um melhor funcionamento do fígado e dos rins, reduzindo os danos provocados pelos radicais livres e, assim, prevenindo contra vários tipos de câncer.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("gengibre") > -1) {
            chatMessage = new ChatMessage(false, false, "A raiz é uma verdadeira caixinha de primeiros socorros de tantas propriedades terapêuticas que tem. Por ser uma substância termogênica, ele eleva a temperatura do corpo, acelera o metabolismo e, assim, queima as gordurinhas mais rápido. Dois estudos americanos recentes mostraram ainda que o gengibre possui a capacidade de inibir o crescimento de células cancerosas no intestino e no ovário. O gengibre também impede a formação de gases e pode ser consumido por mulheres gestantes para combater o enjoo. ");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("equinacea") > -1) {
            chatMessage = new ChatMessage(false, false, "A equinácea é uma planta medicinal usada há anos. Também conhecida como flor-de-cone, púrpura e rudbéquia, é muito aplicada como remédio caseiro no tratamento de gripes e resfriados. Em princípio, suas propriedades essenciais são anti-inflamatórias, antioxidantes, desintoxicante e outras. Por isso, o chá feito a partir da equinácea é excelente para fortalecer a imunidade, uma vez que suas propriedades dificultam a ocorrência de processos inflamatórios no corpo.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("genciana") > -1) {
            chatMessage = new ChatMessage(false, false, "O chá de genciana também está entre os melhores chás para aumentar a imunidade. Por promover natural ação antimicrobiana, a planta dificulta que vírus e bactérias prejudiquem a saúde. Além disso, é ótimo para tratar a faringite e a sinusite.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("limão") > -1) {
            chatMessage = new ChatMessage(false, false, "Devido ao seu poder anti-inflamatório e sua abundância em vitamina C, o chá de limão ajuda a manter a imunidade fortalecida e eficientemente protegendo a saúde do organismo. Ainda, é usado para auxiliar no tratamento de problemas respiratórios como resfriados e a gripe. ");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("alho") > -1) {
            chatMessage = new ChatMessage(false, false, "Por ser repleto de antioxidantes, propriedades anti-inflamatórias e antivirais, o alho é um natural fortalecedor do sistema imunológico e origina um dos melhores chás para a imunidade. Portanto, é ótimo para manter a imunidade fortalecida, o que foi comprovado em um estudo de cientistas indianos, feito em 2018. O estudo comprovou o grande potencial antioxidante da bebida.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("macela") > -1) {
            chatMessage = new ChatMessage(false, false, "A macela, muito similar à camomila, é uma planta de ação anti-inflamatória e calmante que pode beneficiar a imunidade. Basicamente, o chá de macela promove diversos benefícios à saúde e um desses diz respeito à imunidade: sua grande quantidade de antioxidantes fortalecem o sistema imunológico e ajudam a melhor proteger o organismo de doenças.");
            adapter.add(chatMessage);
        }else if (bol == message.indexOf("sabugueiro") > -1) {
            chatMessage = new ChatMessage(false, false, "O sabugueiro é utilizado no tratamento de problemas respiratórios, como a gripe, e devido ao seu poder antivirótico natural, ou seja, ele é excelente para prevenir viroses de todos os tipos. Portanto, o chá de sabugueiro é ótimo para quem visa fortalecer a imunidade.");
            adapter.add(chatMessage);
        }
        else if (bol == message.indexOf("atendente") > -1) {
            chatMessage = new ChatMessage(false, false, "Ok, quer falar com a atendente.");

            adapter.add(chatMessage);
            Toast.makeText(MainActivity.this, "Direcionando para o atendimento", Toast.LENGTH_LONG).show();
            abrirCadastroContato();
        } else {
            chatMessage = new ChatMessage(false, false, "Desculpa, não entendi\n Opções ajuda: \n escreva 'atendente' para ter um atendimento. ");
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

    private void Mensagens(String mensagem){

        ChatMessage chatMessage = new ChatMessage(false,false, mensagem);
        adapter.add(chatMessage);
    }
}
