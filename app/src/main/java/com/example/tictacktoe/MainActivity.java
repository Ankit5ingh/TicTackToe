package com.example.tictacktoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;
    ImageView img7;
    ImageView img8;
    ImageView img9;

    TextView display, rScore, yScore;
    Button clean_btn, reset_btn;

    protected boolean aiPlayer = false;
    int chance = 0;
    int yellowScore = 0;
    int redScore = 0;
    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    List<ImageView> list = new ArrayList<>();

    public void init() {
        img1 = findViewById(R.id.index00);
        img2 = findViewById(R.id.index01);
        img3 = findViewById(R.id.index02);
        img4 = findViewById(R.id.index10);
        img5 = findViewById(R.id.index11);
        img6 = findViewById(R.id.index12);
        img7 = findViewById(R.id.index20);
        img8 = findViewById(R.id.index21);
        img9 = findViewById(R.id.index22);

        list.add(img1);
        list.add(img2);
        list.add(img3);
        list.add(img4);
        list.add(img5);
        list.add(img6);
        list.add(img7);
        list.add(img8);
        list.add(img9);

        clean_btn = findViewById(R.id.clean_btn);
        reset_btn = findViewById(R.id.reset_btn);
        display = findViewById(R.id.textView);
        rScore = findViewById(R.id.rscore);
        yScore = findViewById(R.id.yscore);
    }

    MinMaxClass minMax = new MinMaxClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        reset();
        listeners();

        Intent i = getIntent();
        Bundle b = i.getExtras();
        aiPlayer = b.getBoolean("boolean_key");
    }//onCreate


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void click(ImageView view, int i) {
        if(aiPlayer)
            aiPlayerFunction(view, i);
        else
            humanPlayerFunction(view, i);
    }// click Function

    private void aiPlayerFunction(ImageView view, int i)  {
        activePlayer = 0;
        chance = chance + 2;
        if (gameState[i]==1){
            Toast toast = Toast.makeText(getApplicationContext(), "Red took it", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (gameState[i]==2) {
            gameState[i] = 0;
            view.setImageResource(R.drawable.yellow);
            activePlayer = 1;
            display.setText(R.string.redturn);
            // Ai will play;
            int index = minMax.driverCode(gameState);
            new Handler().postDelayed(() -> aiTurn(index) ,500 );
            Log.e( "aiPlayerFunction: delay","code executed" );

        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Yellow took it", Toast.LENGTH_SHORT);
            toast.show();
        }
        winningPositions();
    }
    private void aiTurn(int index){
        Log.e( "aiTurn: ", Integer.toString(index));
        if(index>=0 && gameState[index] == 2) {
            gameState[index] = 1;
            list.get(index).setImageResource(R.drawable.red);
            activePlayer = 0;
            display.setText("Yellow Turn");
        }else{
            display.setText("Game Reseting");
            new Handler().postDelayed(() -> reset(),1000 );

        }
    }

    @SuppressLint("SetTextI18n")
    private void humanPlayerFunction(ImageView view, int i) {
        chance++;
        if (gameState[i]==1){
            Toast toast = Toast.makeText(getApplicationContext(), "Red took it", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (gameState[i]==2) {
            gameState[i] = activePlayer;
            if(activePlayer==0) {
                view.setImageResource(R.drawable.yellow);
                activePlayer = 1;
                display.setText(R.string.redturn);
            }
            else {
                view.setImageResource(R.drawable.red);
                activePlayer = 0;
                display.setText("Yellow turn");
            }

        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Yellow took it", Toast.LENGTH_SHORT);
            toast.show();
        }
        winningPositions();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void winningPositions() {
        for (int[] winningPosition : winningPositions) {
            if
            (       gameState[winningPosition[0]] == gameState[winningPosition[1]]
                    && gameState[winningPosition[0]] == gameState[winningPosition[2]]
                    && gameState[winningPosition[1]] == gameState[winningPosition[2]]
                    && gameState[winningPosition[0]] != 2
            ) {
                if (gameState[winningPosition[0]] == 0) {
                    display.setText("Yellow PLayer Won");
                    yellowScore++;
                    yScore.setText(String.format("Yellow : %d", yellowScore));
                    Toast toast = Toast.makeText(getApplicationContext(), "Yellow Won", Toast.LENGTH_SHORT);
                    toast.show();
                    replay();
                } else if (gameState[winningPosition[0]] == 1){
                    display.setText("Red Player Won");
                    redScore++;
                    rScore.setText(MessageFormat.format("Red : {0}", redScore));
                    Toast toast = Toast.makeText(getApplicationContext(), "Red Won", Toast.LENGTH_SHORT);
                    toast.show();
                    replay();
                }
            }else if(chance >= 9){
                Toast toast = Toast.makeText(getApplicationContext(), "TIE !!!", Toast.LENGTH_SHORT);
                toast.show();
                replay();
            }
        }
    }


    public void replay() {
        new Handler().postDelayed(() -> display.setText(activePlayer==1? "Red turn" : "Yellow turn"),2000 );
                gameState = new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2};
                imgToBlack();
                chance = 0;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void reset() {
        imgToBlack();
        gameState = new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2};
        redScore = 0;
        yellowScore = 0;
        display.setText("Tic Tac toe");
        rScore.setText(String.format("Red : %d", redScore));
        yScore.setText(String.format("Yellow : %d", yellowScore));
        chance = 0;
        new Handler().postDelayed(() -> display.setText(activePlayer==1? "Red turn" : "Yellow turn"), 1000);
    }


    private void listeners() {

        clean_btn.setOnClickListener(view -> replay());
        reset_btn.setOnClickListener(view -> reset());

        img1.setOnClickListener(view -> click( img1, 0));
        img2.setOnClickListener(view -> click( img2, 1));
        img3.setOnClickListener(view -> click( img3, 2));
        img4.setOnClickListener(view -> click( img4, 3));
        img5.setOnClickListener(view -> click( img5, 4));
        img6.setOnClickListener(view -> click( img6, 5));
        img7.setOnClickListener(view -> click( img7, 6));
        img8.setOnClickListener(view -> click( img8, 7));
        img9.setOnClickListener(view -> click( img9, 8));
    }

    public void imgToBlack(){
        img1.setImageResource(R.drawable.black_img);
        img2.setImageResource(R.drawable.black_img);
        img3.setImageResource(R.drawable.black_img);
        img4.setImageResource(R.drawable.black_img);
        img5.setImageResource(R.drawable.black_img);
        img6.setImageResource(R.drawable.black_img);
        img7.setImageResource(R.drawable.black_img);
        img8.setImageResource(R.drawable.black_img);
        img9.setImageResource(R.drawable.black_img);
    }
}// class
