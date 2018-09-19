package com.example.camnc.cryptologogame.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.camnc.cryptologogame.guessLogoActivity;
import com.example.camnc.cryptologogame.Common.Common;

import java.util.List;

/**
 * Created by camnc on 08/04/2018.
 */

public class GridViewSuggestAdapter extends BaseAdapter {

    private char[] suggestSource;
    private Context context;
    private guessLogoActivity mainActivity;

    public GridViewSuggestAdapter(char[] suggestSource, Context context, guessLogoActivity mainActivity){
        this.suggestSource = suggestSource;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public int getCount() {
        return suggestSource.length;
    }

    @Override
    public Object getItem(int position) {
        return suggestSource[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        Button button;
        if (view == null) {
            if (suggestSource[position] == 1) {
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
                button.setText(String.valueOf(suggestSource[position]));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Boolean answerFull = false;
                        //When suggest character is removed, add to user answer.
                        char selected = suggestSource[position];
                        for (int i = 0; i < Common.user_submit_answer.length; i++) {
                            //If character selected is a letter.
                            if (!Character.isAlphabetic(Common.user_submit_answer[i])) {
                                Common.user_submit_answer[i] = selected;
                                break;
                            }
                            //If user answer is full
                            if(i == Common.user_submit_answer.length-1) {
                                answerFull = true;
                            }
                        }
                        //If the user answer is not full
                        if (!answerFull){
                            //Update suggestion words
                            Common.suggest_source[position] = 1;//Remove from suggestion words
                            mainActivity.suggestAdapter = new GridViewSuggestAdapter(Common.suggest_source, context, mainActivity);
                            mainActivity.gridViewSuggest.setAdapter(mainActivity.suggestAdapter);
                            mainActivity.suggestAdapter.notifyDataSetChanged();
                        }

                        //Update user answer
                        GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(Common.user_submit_answer, context, mainActivity);
                        mainActivity.gridViewAnswer.setAdapter(answerAdapter);
                        answerAdapter.notifyDataSetChanged();
                    }
                });
            }
        } else {
            button = (Button) view;
        }
        return button;
    }
}