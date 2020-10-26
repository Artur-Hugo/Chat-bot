package com.devoligastudio.chatbot.model;

import com.devoligastudio.chatbot.Config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;


    public Usuario(){

    }

    public void salvar(){
        //referencia para salvar dados no nosso banco de dados;
        DatabaseReference referencefirebase = ConfiguracaoFirebase.getFirebase();
        referencefirebase.child("usuario").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
