package com.devoligastudio.chatbot;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencia {

    private SharedPreferences preferences;
    private Context context;
    private final String NOME_ARQUIVO = "chatbot.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOMEUSUARIO = "UsuarioLogado";

    public Preferencia(Context contextoParametro){

        context = contextoParametro;
        preferences = context.getSharedPreferences(NOME_ARQUIVO,MODE);
        editor = preferences.edit();

    }

    //salvar usuario direto do aparelho do usuario
    public void salvarDados( String identificadorUsuario, String nomeusuario ){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOMEUSUARIO, nomeusuario);
        editor.commit();

    }
    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }
    public String getNOME(){
        return preferences.getString(CHAVE_NOMEUSUARIO, null);
    }



}
