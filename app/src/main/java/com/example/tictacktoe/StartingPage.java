package com.example.tictacktoe;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class StartingPage extends AppCompatActivity {

    Button humanBtn, aiBtn;
//    private boolean player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_page);

        humanBtn = findViewById(R.id.human_btn);
        aiBtn = findViewById(R.id.ai_btn);

        humanBtn.setOnClickListener(view -> onClick(false));
        aiBtn.setOnClickListener(view -> onClick(true));



    }//onCreate

    public void onClick(boolean player){
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.putExtra("boolean_key", player);
        startActivity(i);
    }
}