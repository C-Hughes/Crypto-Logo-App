package com.example.camnc.cryptologogame.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.camnc.cryptologogame.Common.Common;
import com.example.camnc.cryptologogame.guessLogoActivity;

/**
 * Created by camnc on 08/04/2018.
 */

public class GridViewAnswerAdapter extends BaseAdapter {

    private Context context;
    private char[] answerCharacter;
    private guessLogoActivity mainActivity;

    public GridViewAnswerAdapter(char[] answerCharacter, Context context, guessLogoActivity mainActivity){
        this.answerCharacter = answerCharacter;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return answerCharacter.length;
    }

    @Override
    public Object getItem(int position) {
        return answerCharacter[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Button button;
        if (view == null) {
            if (answerCharacter[position] == 1) {
                //Create Button
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(85, 85));
                button.setPadding(8, 8, 8, 8);
                button.setBackgroundColor(Color.DKGRAY);
            } else {
                //Create Button
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(85, 85));
                button.setPadding(8, 8, 8, 8);
                button.setBackgroundColor(Color.DKGRAY);
                button.setTextColor(Color.WHITE);
                button.setText(String.valueOf(answerCharacter[position]));

                //Click to remove letter.
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean answerFull = false;

                        //When answer character is removed, add to suggest source.
                        char selected = answerCharacter[position];
                        for (int i = 0; i < Common.suggest_source.length; i++) {
                            //If character selected is a letter.
                            if (!Character.isAlphabetic(Common.suggest_source[i])) {
                                Common.suggest_source[i] = selected;
                                break;
                            }
                            if(i == Common.suggest_source.length-1) {
                                answerFull = true;
                            }
                        }
                        //If the suggest source is not full
                        if (!answerFull){
                            //Update user answer
                            Common.user_submit_answer[position] = 1;//Remove from answer
                            GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(Common.user_submit_answer, context, mainActivity);
                            mainActivity.gridViewAnswer.setAdapter(answerAdapter);
                            answerAdapter.notifyDataSetChanged();
                        }
                        //Update suggestion words
                        mainActivity.suggestAdapter = new GridViewSuggestAdapter(Common.suggest_source, context, mainActivity);
                        mainActivity.gridViewSuggest.setAdapter(mainActivity.suggestAdapter);
                        mainActivity.suggestAdapter.notifyDataSetChanged();
                    }
                });
            }
        } else {
            button = (Button) view;
        }
        return button;
    }
}