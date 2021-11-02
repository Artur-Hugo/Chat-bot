package com.devoligastudio.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.devoligastudio.chatbot.helper.Base64Custom;
import com.devoligastudio.chatbot.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class fazerlogin extends AppCompatActivity {

    EditText senha, email;
    private FirebaseUser user;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private     String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazerlogin);

email = findViewById(R.id.emailid);
senha = findViewById(R.id.senhaid);

        //verificarUsuarioLogado();

    }

    private void verificarUsuarioLogado() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if( autenticacao.getCurrentUser() != null ) {
            String emaili = user.getEmail();;
            if(emaili.equals("atendentemaster@hotmail.com")){
                abrirTelaAtendente();
            }else {

                abrirTelaPrincipal();
            }}
       /* if(user != null){



         if( autenticacao.getCurrentUser() != null ) {
             String emaili = user.getEmail();;
             if(emaili.equals("atendentemaster@hotmail.com")){
                abrirTelaAtendente();
            }else {

                abrirTelaPrincipal();
           }}


        }*/

         }


    private void abrirTelaAtendente() {
        Intent intent = new Intent(this,ListaDeConversa.class);
        startActivity(intent);
    }

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(this,MeioPrincipal.class);
        startActivity(intent);
    }

    public void abrircadastro(View view){

        Intent intent = new Intent(this, cadastro.class);
        startActivity(intent);
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuario")
                            .child(identificadorUsuarioLogado);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario usuariorecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencia preferencias = new Preferencia(fazerlogin.this);

//Terá o Usuario salvo no aparelho
                         //   preferencias.salvarDados( identificadorUsuarioLogado, usuariorecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent( valueEventListener);

                    if( usuario.getEmail().equals("atendentemaster@hotmail.com" )){
                        abrirTelaAtendente();
                        Toast.makeText(fazerlogin.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                    }else{
                        //Toast.makeText(fazerlogin.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();






                    abrirTelaPrincipal();
                    Toast.makeText(fazerlogin.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();}
                }else{
                    Toast.makeText(fazerlogin.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }
    public void logaragora(View view){

        if(email.getText().toString().trim().equals("") || senha.getText().toString().trim().equals("")){//como o tamanho é zero é nulla aresposta

            Toast.makeText(fazerlogin.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();

        }else{

        usuario = new Usuario();
        usuario.setEmail( email.getText().toString() );
        usuario.setSenha( senha.getText().toString() );
        validarLogin();
    }}

}
