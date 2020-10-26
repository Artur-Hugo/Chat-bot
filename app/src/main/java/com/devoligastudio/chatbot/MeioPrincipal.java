package com.devoligastudio.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MeioPrincipal extends AppCompatActivity {

    Button chatbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meio_principal);

        chatbutton = findViewById(R.id.chatinclickid);


        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MeioPrincipal.this, MainActivity.class);
                startActivity(it);
            }
        });
    }
}
