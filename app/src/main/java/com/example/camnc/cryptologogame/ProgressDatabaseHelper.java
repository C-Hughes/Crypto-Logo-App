package com.example.camnc.cryptologogame;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.camnc.cryptologogame.Common.Common;

public class ProgressDatabaseHelper extends SQLiteOpenHelper {
    /* Initialise constants. */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DBv015.db";
    private static final String PROGRESS_TABLE_NAME = "progress";
    private static final String[] COLUMN_NAMES = {"Level", "Percent", "noCorrect", "coinGuessedName", "coinPosition"};
    /* Construct CREATE query string. */
    private static final String PROGRESS_TABLE_CREATE =
            "CREATE TABLE " + PROGRESS_TABLE_NAME + " (" +
                    COLUMN_NAMES[0] + " INTEGER, " +
                    COLUMN_NAMES[1] + " INTEGER, " +
                    COLUMN_NAMES[2] + " INTEGER, " +
                    COLUMN_NAMES[3] + " TEXT, " +
                    COLUMN_NAMES[4] + " INTEGER);";

    ProgressDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creates the database if it doesn't exist and adds the "contacts" table.
        /* Execute SQL query. */
        db.execSQL(PROGRESS_TABLE_CREATE);
        //Seed Database with default player progress.
        db.execSQL("INSERT INTO " + PROGRESS_TABLE_NAME + "(" +
                COLUMN_NAMES[0] + "," +
                COLUMN_NAMES[1] + "," +
                COLUMN_NAMES[2] + "," +
                COLUMN_NAMES[3] + "," +
                COLUMN_NAMES[4] + ") VALUES (1,0,0,'Progress',0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // can be left empty for the purposes of this tutorial
    }

    public Boolean checkGuessed(String coinName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+ PROGRESS_TABLE_NAME +" WHERE coinGuessedName = '"+coinName+"' ", null);
        if (c.moveToFirst()){
            return true;
        } else {
            return false;
        }
    }

    public String updateProgress(String coinName, int position){
        SQLiteDatabase db = this.getReadableDatabase();

        //Initialise
        int noCorrect = 1;
        int level = 1;
        //Insert correctly guessed coin into DB.
        db.execSQL("INSERT INTO " + PROGRESS_TABLE_NAME + "(" +
                COLUMN_NAMES[0] + "," +
                COLUMN_NAMES[1] + "," +
                COLUMN_NAMES[2] + "," +
                COLUMN_NAMES[3] + "," +
                COLUMN_NAMES[4] + ") VALUES (0,0,0,'"+coinName+"',"+position+")");

        //Update the number of coins guessed and % complete.
        db.execSQL("UPDATE " + PROGRESS_TABLE_NAME + " SET Percent = Percent + 1, noCorrect = noCorrect + 1 WHERE coinGuessedName = 'Progress'");
        //Get number of logos guessed
        Cursor c = db.rawQuery("SELECT noCorrect, level FROM "+ PROGRESS_TABLE_NAME +" WHERE coinGuessedName = 'Progress' ", null);

        if (c.moveToFirst()){
            noCorrect = c.getInt(0);
            level = c.getInt(1);
        }
        //If number correct is multiple of 10
        if(noCorrect % 2 == 0 && level < 6){
            //Level up
            db.execSQL("UPDATE " + PROGRESS_TABLE_NAME + " SET Level = Level + 1 WHERE coinGuessedName = 'Progress'");
            return "levelUp";
        }
        return "";
    }

    public int[] getProgress(){
        /* Query the database and check the number of rows returned. */
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.query(PROGRESS_TABLE_NAME, COLUMN_NAMES, null, null, null, null, null, null);

        /* Make sure the query returned a valid result before trying to change text on screen.*/
        if(result != null) {
            /* Display result. */
            result.moveToFirst();
            int levelComplete = result.getInt(0);
            int percentComplete = result.getInt(1);
            int noCorrect = result.getInt(2);
            db.close();
            return new int[] {levelComplete, percentComplete, noCorrect};
        } else {
            return new int[] {-1, -1, -1};
        }
    }

    public void initThumbs(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT coinPosition FROM "+ PROGRESS_TABLE_NAME +" WHERE coinGuessedName != 'Progress' ", null);
        if (c.moveToFirst()){
            do {
                int coinPosition = c.getInt(0);
                // Do something Here with values
                Common.thumbs[coinPosition] = Common.thumbsCorrect[coinPosition];
            } while(c.moveToNext());
        }
        c.close();
        db.close();
    }
}