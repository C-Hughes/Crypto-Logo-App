package com.example.camnc.cryptologogame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private float x1,x2;
    static final float MIN_DISTANCE = 150;
    TextView text_progress, text_percent, text_noCorrect, text_json;
    ProgressDialog pd;
    ProgressDatabaseHelper progressDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Initialise the database our SQLiteOpenHelper object*/
        progressDatabaseHelper = new ProgressDatabaseHelper(this);
        text_progress = (TextView)findViewById(R.id.text_progress);
        text_percent = (TextView)findViewById(R.id.text_percent);
        text_noCorrect = (TextView)findViewById(R.id.text_noCorrect);
        text_json = (TextView) findViewById(R.id.text_json);

        //Initialise gridview images for correct answers
        progressDatabaseHelper.initThumbs();
        checkProgress();
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Initialise gridview images for correct answers
        progressDatabaseHelper.initThumbs();
        checkProgress();
        //Update BTC price
        new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/bitcoin/?convert=GBP");
    }

    private void checkProgress(){
        int result[] = progressDatabaseHelper.getProgress();

        if(result[0] == -1){
            text_progress.setText("1/5 Levels Unlocked");
            text_percent.setText("0% Completed");
            text_noCorrect.setText("0 Logos Guessed");
        } else {
            text_progress.setText(result[0] + "/5 Levels Unlocked");
            text_percent.setText(result[1] + "% Complete");
            text_noCorrect.setText(result[2] + " Logos Guessed");
        }
    }

    //Swipe to Play
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                //When swiping right
                if ((Math.abs(x2 - x1) > MIN_DISTANCE) && (x2 < x1)){
                    Intent startIntent = new Intent(MainActivity.this, logoLevelsActivity.class);
                    startActivity(startIntent);
                } 
                break;
        }
        return super.onTouchEvent(event);
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();

            } catch (MalformedURLException e) {e.printStackTrace();}
            catch (IOException e) {e.printStackTrace();}
            finally {
                if (connection != null) {connection.disconnect();}
                try {
                    if (reader != null) {reader.close();}
                }
                catch (IOException e) {e.printStackTrace();}
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            //Cut all JSON data except GBP price
            String GBPPrice = result.substring(result.indexOf("price_gbp") + 13, result.indexOf("price_gbp") + 20);
            result = "BTC: £"+GBPPrice;
            text_json.setText(result);
        }
    }
}