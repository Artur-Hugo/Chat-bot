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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class cadastro extends AppCompatActivity {
    private EditText nome;private EditText email;private EditText senha;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = (EditText) findViewById(R.id.aname);
        email = (EditText) findViewById(R.id.amail2id);
        senha = (EditText) findViewById(R.id.asenhaid);

        autenticacao = FirebaseAuth.getInstance();

    }
    public void botaocadastrar(View view){
        usuario = new Usuario();
        usuario.setNome(nome.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setSenha(senha.getText().toString());
        cadastrarUsuario();

  /*  String iemail = email.getText().toString().trim();
    String isenha = senha.getText().toString().trim();
    criarUser(iemail, isenha); */


    }
    private  void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
        autenticacao.createUserWithEmailAndPassword(

                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(cadastro.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(cadastro.this,"Cadastrado", Toast.LENGTH_LONG ).show();

                   /* FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvar(); */

                    String identificadorUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setId( identificadorUsuario );
                    usuario.salvar();



                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(identificadorUsuario);

                    Preferencia preferencias = new Preferencia(cadastro.this);
                    preferencias.salvarDados( identificadorUsuario, usuario.getNome() );

                    abrirLoginUsuario();

                }
                else{

                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está em uso no App!";
                    } catch (FirebaseAuthWebException e) {
                        erroExcecao = "Erro na operação WEB";
                    } catch (FirebaseAuthEmailException e) {
                        erroExcecao = "Erro na tentativa de enviar um email por meio do Firebase Auth";
                    } catch (FirebaseAuthRecentLoginRequiredException e) {
                        erroExcecao = "Erro relacionada à autenticação do Firebase.";
                    } catch (Exception e) {
                        erroExcecao = "Ao cadastrar usuário!";
                        e.printStackTrace();
                    }


                    Toast.makeText(cadastro.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG ).show();


                }

            }
        });
    }
    public void abrirLoginUsuario(){
        Intent intent = new Intent(cadastro.this, fazerlogin.class);
        startActivity(intent);
        finish();
    }

}
