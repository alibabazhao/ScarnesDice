package com.example.ali.scarnesdice;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;

import java.util.Random;

public class ScarnesDice extends AppCompatActivity {
    //Four global variables to store:
    private static int userTotalScore=0;
    private static int userTurnScore=0;
    private static int computerTotalScore=0;
    private static int computerTurnScore=0;
    private TextView scores;
    private TextView message;
    private ImageView diceFace;

    private Drawable d;
    private boolean computerTurn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scarnes_dice);

        scores=findViewById(R.id.scores);
        message=findViewById(R.id.message);
        diceFace=findViewById(R.id.diceFace);
        Button btnRoll=findViewById(R.id.btnRoll);
        Button btnHold=findViewById(R.id.btnHold);
        Button btnReset=findViewById(R.id.btnReset);

        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random r = new Random();
                int diceValue = 1 + r.nextInt(6);
                if(!computerTurn){
                    diceCheck(diceValue);

                    if(diceValue!=1) {
                        userTurnScore = diceValue;
                        userTotalScore += userTurnScore;
                        message.setText("Your turn score: "+userTurnScore);
                    }
                    else{
                        holdEvent();
                    }
                }
            }
        });

        //updating the user's overall score, reset the user round score and update the label
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                holdEvent();
            }
        });

        //reset the 4 global variables to 0 and update the label text
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                userTotalScore=0;
                userTurnScore=0;
                computerTotalScore=0;
                computerTurnScore=0;
                scores.setText("Your score: "+userTotalScore+"\t\t\t\t\t\t\t\t\t\tComputer score: "+computerTotalScore);
                message.setText("Your turn score: "+userTurnScore);
            }
        });
    }

    //a help method for checking the dice value
    private void diceCheck(int diceValue){
        switch(diceValue){
            case 1:
                diceValue=1;
                d = getResources().getDrawable(R.drawable.dice1);
                break;
            case 2:
                diceValue=2;
                d = getResources().getDrawable(R.drawable.dice2);
                break;
            case 3:
                diceValue=3;
                d = getResources().getDrawable(R.drawable.dice3);
                break;
            case 4:
                diceValue=4;
                d = getResources().getDrawable(R.drawable.dice4);
                break;
            case 5:
                diceValue=5;
                d = getResources().getDrawable(R.drawable.dice5);
                break;
            case 6:
                diceValue=6;
                d = getResources().getDrawable(R.drawable.dice6);
                break;
        }
        diceFace.setImageDrawable(d);
    }

    //a help method for the hold button's event
    private void holdEvent(){
        //Disable the roll and hold buttons when computer turn
        if(!computerTurn) {
            userTurnScore=0;
            scores.setText("Your score: " + userTotalScore + "\t\t\t\t\t\t\t\t\t\tComputer score: " + computerTotalScore);
            message.setText("Your turn score: "+userTurnScore);
            computerTurn=true;
            computerTurn();
        }
    }

    private void computerTurn(){
        //Disable the roll and hold buttons
        //Create a while loop that loops over each of the computer's turn. During each iteration of the loop:
//        while(computerTurn) {
            //pick a random die value and display it (hopefully using the helper you created earlier)
            Random r=new Random();
            int diceValue = 1 + r.nextInt(6);
            //follow the game rules depending on the value of the roll.
            diceCheck(diceValue);
            //Be sure to update the label with the computer's round score or "Computer holds" or "Computer rolled a one" as appropriate.
            if (diceValue != 1) {
                computerTurnScore = diceValue;
                computerTotalScore+=computerTurnScore;
                message.setText("Computer turn score: "+computerTurnScore);
                if(computerTotalScore<=(userTotalScore+10)){
                    computerReRoll();
                }
                else{
                    Random x=new Random();
                    int computerDecide=x.nextInt(2);
                    if(computerDecide==0){  //0 as hold
                        computerHold();
                    }
                    //computer decide to roll again, create a timed event that will do so after an appropriate delay (say 500 ms).
                    else {  //1 as reroll
                        computerReRoll();
                    }
                }
            } else {
                computerHold();
            }
//        } //Get rid of the while loop (but not its contents!) and make the computerTurn method handle a single roll of the computer's turn
    }

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            computerTurn();
        }
    };

    //a help method for computer re-roll
    private void computerReRoll(){
        message.setText("Computer rolled again!");
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    //a help method for computer hold
    private void computerHold(){
        computerTurnScore = 0;
        scores.setText("Your score: " + userTotalScore + "\t\t\t\t\t\t\t\t\t\tComputer score: " + computerTotalScore);
        message.setText("Computer turn score: "+computerTurnScore+"\t\t\t\t\tComputer holds! Your Turn.");
        computerTurn=false;
    }

}
