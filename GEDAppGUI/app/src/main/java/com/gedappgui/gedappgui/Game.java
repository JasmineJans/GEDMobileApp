package com.gedappgui.gedappgui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by jasminejans on 10/29/16.
 * comment
 */

public class Game extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    public void goToGameEnd(View view) {
        Intent intent = new Intent(this, GameEnd.class);
        startActivity(intent);
    }

    public void goBackToPlay(View view) {
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }
}
