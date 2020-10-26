package com.devoligastudio.chatbot.Config;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {
private static FirebaseAuth autenticacao;
private static DatabaseReference referencefirebase;
private static FirebaseUser firebaseUser;
private static FirebaseAuth.AuthStateListener authStateListener;

public static DatabaseReference getFirebase(){
    if(referencefirebase == null){
        referencefirebase = FirebaseDatabase.getInstance().getReference();
    }
    return referencefirebase;
}

public static FirebaseAuth getFirebaseAutenticacao(){

    if(autenticacao == null){
        autenticacao = FirebaseAuth.getInstance();
    }

    return autenticacao;
}
    public static FirebaseAuth getFirebaseAuth(){
        if(autenticacao == null){
            inicializarFirebaseAuth();
        }
        return autenticacao;
    }

    private static void inicializarFirebaseAuth(){
        autenticacao = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    firebaseUser = user;
                }
            }
        }; autenticacao.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

}
