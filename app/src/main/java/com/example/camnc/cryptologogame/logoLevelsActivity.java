package com.example.camnc.cryptologogame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class logoLevelsActivity extends AppCompatActivity implements View.OnClickListener {

    private float x1, x2;
    static final float MIN_DISTANCE = 150;
    Button btn_level1, btn_level2, btn_level3, btn_level4, btn_level5;
    ProgressDialog pd;
    ProgressDatabaseHelper progressDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_levels);

        /* Initialise the database our SQLiteOpenHelper object*/
        progressDatabaseHelper = new ProgressDatabaseHelper(this);
        btn_level1 = (Button) findViewById(R.id.level1Btn);
        btn_level2 = (Button) findViewById(R.id.level2Btn);
        btn_level3 = (Button) findViewById(R.id.level3Btn);
        btn_level4 = (Button) findViewById(R.id.level4Btn);
        btn_level5 = (Button) findViewById(R.id.level5Btn);
        btn_level1.setOnClickListener(this);
        btn_level2.setOnClickListener(this);
        btn_level3.setOnClickListener(this);
        btn_level4.setOnClickListener(this);
        btn_level5.setOnClickListener(this);

        //Locks the levels that aren't unlocked yet.
        lockLevels();
    }

    @Override
    public void onResume() {
        super.onResume();
        lockLevels();
    }

    @Override
    public void onClick(View v) {
        int result[] = progressDatabaseHelper.getProgress();
        int levelUnlocked = result[0];
        int levelSelected = 1;

        int id = v.getId();
        if (id == R.id.level1Btn) {
            levelSelected = 1;
        } else if (id == R.id.level2Btn) {
            levelSelected = 2;
        } else if (id == R.id.level3Btn) {
            levelSelected = 3;
        } else if (id == R.id.level4Btn) {
            levelSelected = 4;
        } else if (id == R.id.level5Btn) {
            levelSelected = 5;
        }
        if (levelSelected <= levelUnlocked) {
            Intent startIntent = new Intent(getApplicationContext(), logoGridActivity.class);
            startIntent.putExtra("LevelSelected", levelSelected);
            startActivity(startIntent);
        }
    }

    private void lockLevels() {
        //Get level progress
        int result[] = progressDatabaseHelper.getProgress();
        int levelUnlocked = result[0];

        //Set all buttons to be enabled.
        btn_level2.setEnabled(true);
        btn_level3.setEnabled(true);
        btn_level4.setEnabled(true);
        btn_level5.setEnabled(true);

        //Lock levels which haven't been unlocked yet.
        if (levelUnlocked < 2) btn_level2.setEnabled(false);
        if (levelUnlocked < 3) btn_level3.setEnabled(false);
        if (levelUnlocked < 4) btn_level4.setEnabled(false);
        if (levelUnlocked < 5) btn_level5.setEnabled(false);
    }

    //Swipe to go back
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                //When swiping right
                if ((Math.abs(x2 - x1) > MIN_DISTANCE) && (x2 > x1)) {
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
