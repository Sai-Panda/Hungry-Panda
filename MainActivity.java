package com.example.hungrypanda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends Activity {
    static final int SCREEN_HEIGHT = Resources.getSystem().getDisplayMetrics().heightPixels;
    static final int SCREEN_WIDTH = Resources.getSystem().getDisplayMetrics().widthPixels;
    static HungryPandaPanel hPandaPanel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hPandaPanel = null;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        displayScore();
    }

    /**
     * Displays the users high score on the menu, will default to 0
     * if no high score is availible.
     */
    public void displayScore(){
        TextView scoreValue = findViewById(R.id.scoreValue);
        SharedPreferences prefs = this.getSharedPreferences("HighScores", Context.MODE_PRIVATE);
        int highScore = prefs.getInt("HighScore", 0);
        scoreValue.setText(String.valueOf(highScore));
    }

    /**
     * Creates a new HungryPandaPanel instance and switches the content view
     * @param view - in this case it would be the playButton view
     */
    public void launchGame(View view){
        hPandaPanel = new HungryPandaPanel(this);
        setContentView(hPandaPanel);
    }
}
