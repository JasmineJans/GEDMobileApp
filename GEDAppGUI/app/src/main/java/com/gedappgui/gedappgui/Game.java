/*
 * Game.java
 *
 * Game page activity
 *
 * View that hosts the game that was chosen or that corresponds with the current lesson
 * Helps students solidify their knowledge of the lesson
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Created by jasminejans on 10/29/16.
 *
 * Last Edit: 1-29-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import java.util.ArrayList;

public class Game extends AppCompatActivity {
    private int conceptID;
    private int lessonID;
    private int redo;
    // int to hold whether to go to questions or play next
    // 0 = questions, 1 = play
    private int nextActivity;

    // Game name to load correct game
    private String gameName;
    private BucketGameView bucketGameView;

    /*
     * Starts the activity and shows corresponding view on screen
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        Intent mIntent = getIntent();
        conceptID = mIntent.getIntExtra("conceptID", 0);
        lessonID = mIntent.getIntExtra("lessonID", 0);
        redo = mIntent.getIntExtra("redoComplete", 0);

        // Allow user to control audio with volume buttons on phone
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Get next_activity value from intent to decide next activity after game
        nextActivity = mIntent.getIntExtra("next_activity", 1);

        if (lessonID == 1) {

            //Getting display object
            Display display = getWindowManager().getDefaultDisplay();

            //Getting the screen resolution into point object
            Point size = new Point();
            display.getSize(size);

            //Initializing game view object
            //this time we are also passing the screen size to the GameView constructor
            ArrayList<ArrayList<String>> gameQuestions = new ArrayList<ArrayList<String>>();

            ArrayList<String> texts = new ArrayList<String>();
            texts.add("1");
            texts.add("2");
            texts.add("3");
            texts.add("4");
            texts.add("5");
            ArrayList<String> answers = new ArrayList<String>();
            String question = "_ * _ = 15";
            answers.add(question);
            answers.add("3");
            answers.add("5");

            gameQuestions.add(texts);
            gameQuestions.add(answers);

            texts = new ArrayList<String>();
            texts.add("11");
            texts.add("7");
            texts.add("8");
            texts.add("9");
            texts.add("10");
            answers = new ArrayList<String>();
            question = "_ * _ = 80";
            answers.add(question);
            answers.add("8");
            answers.add("10");

            gameQuestions.add(texts);
            gameQuestions.add(answers);

            bucketGameView = new BucketGameView(this, size.x, size.y, gameQuestions,
                    conceptID, lessonID, nextActivity);
            setContentView(bucketGameView);
        } else if (lessonID == 2) {

            //Getting display object
            Display display = getWindowManager().getDefaultDisplay();

            //Getting the screen resolution into point object
            Point size = new Point();
            display.getSize(size);

            //Initializing game view object
            //this time we are also passing the screen size to the GameView constructor
            ArrayList<ArrayList<String>> gameQuestions = new ArrayList<ArrayList<String>>();

            ArrayList<String> texts = new ArrayList<String>();
            texts.add("1");
            texts.add("2");
            texts.add("3");
            texts.add("4");
            texts.add("5");
            ArrayList<String> answers = new ArrayList<String>();
            String question = "_ * _ = 15";
            answers.add(question);
            answers.add("3");
            answers.add("5");

            gameQuestions.add(texts);
            gameQuestions.add(answers);

            bucketGameView = new BucketGameView(this, size.x, size.y, gameQuestions,
                    conceptID, lessonID, nextActivity);
            setContentView(bucketGameView);
        }
    }

    /*
     * hides bottom navigation bar
     * Called after onCreate on first creation
     * Called every time this activity gets the focus
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}

        if (bucketGameView != null)
            bucketGameView.resume();
    }

    /* 
     * Shows and hides the bottom navigation bar when user swipes at it on screen
     * Called when the focus of the window changes to this activity
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (bucketGameView != null)
            bucketGameView.pause();
    }

    /*
     * Goes to the GameEnd activity
     * Called when the game is over
     * intent.putExtra("next_activity", nextActivity);
     *   = sends nextActivity to tell game whether to go to question or play next
     */
    public void goToGameEnd(View view) {
        Intent intent = new Intent(this, GameEnd.class);
        intent.putExtra("next_activity", nextActivity);
        intent.putExtra("conceptID",conceptID);
        intent.putExtra("lessonID",lessonID);
        intent.putExtra("redoComplete", redo);
        startActivity(intent);
    }
}
