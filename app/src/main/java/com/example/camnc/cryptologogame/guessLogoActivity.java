package com.example.camnc.cryptologogame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.camnc.cryptologogame.Adapter.GridViewAnswerAdapter;
import com.example.camnc.cryptologogame.Adapter.GridViewSuggestAdapter;
import com.example.camnc.cryptologogame.Common.Common;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class guessLogoActivity extends AppCompatActivity {

    int position;
    int offset = 0;
    char[] answer;
    String correct_answer;
    Button btnSubmit;
    ImageView imgViewQuestion;
    ProgressDatabaseHelper progressDatabaseHelper;
    public GridView gridViewAnswer, gridViewSuggest;
    public GridViewAnswerAdapter answerAdapter;
    public GridViewSuggestAdapter suggestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_logo);

        progressDatabaseHelper = new ProgressDatabaseHelper(this);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        //If data has been passed, set logo.
        if (data != null) {
            position = data.getInt("ImageID");
            int levelSelected = data.getInt("LevelSelected");
            offset = (levelSelected-1) * 15;
            position += offset;
        }
        initView();
    }

    private void initView(){
        gridViewAnswer = (GridView)findViewById(R.id.gridViewAnswer);
        gridViewSuggest = (GridView)findViewById(R.id.gridViewSuggestion);
        imgViewQuestion = (ImageView)findViewById(R.id.imgLogo);

        //Sets the characters for guessing logo
        //If logo has been guessed, correct answer is displayed.
        setupList();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Convert char to string.
            String result = "";
            for(int i = 0; i < Common.user_submit_answer.length; i++) {
                result += String.valueOf(Common.user_submit_answer[i]);
            }

            if(result.equals(correct_answer)){
                Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
                //Update Database. Display message if leveled up.
                if(progressDatabaseHelper.updateProgress(correct_answer, position) == "levelUp"){
                    Toast.makeText(getApplicationContext(),"Level Up!",Toast.LENGTH_SHORT).show();
                }

                //Update Image array so check mark appears.
                Common.thumbs[position] = Common.thumbsCorrect[position];

                //Set Adapters
                GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(setupNullList(), getApplicationContext(), guessLogoActivity.this);
                gridViewAnswer.setAdapter(answerAdapter);
                answerAdapter.notifyDataSetChanged();

                GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter(Common.suggest_source, getApplicationContext(), guessLogoActivity.this);
                gridViewSuggest.setAdapter(suggestAdapter);
                suggestAdapter.notifyDataSetChanged();

                position++; //Move to next logo
                setupList(); //Setup next logo
            } else {
                Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void setupList(){
        Random random = new Random();

        //Get new image and update UI
        int imageSelected = Common.thumbs[position];
        imgViewQuestion.setImageResource(imageSelected);

        //Get correct answer from name
        correct_answer = getResources().getResourceName(imageSelected);
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/")+1);
        //If answer has 't' at end of string, then remove it. t = tick
        if(correct_answer.charAt(correct_answer.length() - 1) == 't'){
            correct_answer = correct_answer.substring(0, correct_answer.length() - 1);
        }

        //Set correct_answer, initialise new userAnswer.
        answer = correct_answer.toCharArray();
        Common.user_submit_answer = new char[answer.length];

        /////// Seed suggestion character //////
        Common.suggest_source = new char[16];
        String suggestSourceString = String.valueOf(answer);
        for(int i = answer.length; i < 16; i++){
            suggestSourceString += Common.alphabet_character[random.nextInt(Common.alphabet_character.length)];
        }
        suggestSourceString = shuffle(suggestSourceString); //Shuffle letters
        Common.suggest_source = suggestSourceString.toCharArray();

        //Check if the logo has been guessed before, set the correct answer & disable submit button.
        if(progressDatabaseHelper.checkGuessed(correct_answer)){
            answerAdapter = new GridViewAnswerAdapter(setGuessedList(), this, this);
            btnSubmit.setEnabled(false);
        } else {
            answerAdapter = new GridViewAnswerAdapter(setupNullList(), this, this);
            btnSubmit.setEnabled(true);
        }

        //set for GridView
        suggestAdapter = new GridViewSuggestAdapter(Common.suggest_source, this, this);
        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gridViewSuggest.setAdapter(suggestAdapter);
        gridViewAnswer.setAdapter(answerAdapter);
    }

    //Setup blank answer
    private char[] setupNullList(){
        char result[] = new char[answer.length];
        for(int i = 0; i < answer.length; i++){
            result[i]=' ';
        }
        return result;
    }

    //setup if logo has been guessed before
    private char[] setGuessedList(){
        char result[] = new char[answer.length];
        for(int i = 0; i < answer.length; i++){
            result[i]= answer[i];
            Common.user_submit_answer[i] = answer[i];
        }
        return result;
    }

    //Shuffle strings https://stackoverflow.com/questions/3316674/how-to-shuffle-characters-in-a-string
    public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }
}
