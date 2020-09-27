package com.devoligastudio.chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.devoligastudio.chatbot.Adapter.ChatMessageAdapter;
import com.devoligastudio.chatbot.model.ChatMessage;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.PCAIMLProcessorExtension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   ListView listView;
   ImageButton btnsend;
   EditText editTextMsg;
   ImageView imageView;


   private Bot bot;
   public static Chat chat;
   private ChatMessageAdapter adapter;

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
            default:
                chatMessage =  new  ChatMessage(false,false, "Não entendi");
                adapter.add(chatMessage); break;
        }
  if(message.equals("oi")){
        ChatMessage chatMessage = new ChatMessage(false,false, "Oi o que deseja?\n-amarelo\n-Ave");
        adapter.add(chatMessage);
    }
    }

}
