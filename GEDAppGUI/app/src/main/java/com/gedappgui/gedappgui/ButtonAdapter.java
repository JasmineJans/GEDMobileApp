/*
 * ButonAdapter.java
 *
 * Buton Adapter
 *
 * Adapter to fill gridview with buttons
 *
 * Worked on by:
 * Myanna Harris
 * Kristina Spring
 * Jasmine Jans
 * Jimmy Sherman
 *
 * Last Edit: 2-17-17
 *
 */

package com.gedappgui.gedappgui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.VIBRATOR_SERVICE;

public class ButtonAdapter extends BaseAdapter {

    // Hold context of activity that called adapter
    private Context mContext;
    // Hold the ids of the images from the drawable folder
    private String[] buttonNames;
    private ImageView changer;
    private String[] answers;
    private TextView statement;
    private int cur = 0;
    private int pictureindex = 0;
    private int[] pictures;
    private TextView resulter;
    private int lesson;
    private int concept;
    private int nextActivity;

    // For Haptic Feedback
    private Vibrator myVib;

    /**
     * Constructor for PictureGameView buttons
     * Gets the variables from PictureGameView to use in creating the buttons and events
     * @param c the activity's current context
     * @param buttonNamesp an array of button names to add
     * @param change the initial picture
     * @param splitter the split data to run the game
     * @param state the initial equation at the top
     * @param pics the set of pictures to go through
     * @param result the Textview that tells the user if they are right or wrong
     * @param lessonID the current lesson index
     * @param conceptID the current concept index
     * @param nextAct the GameEnd activity to be launched when the game is completed. 1 if from arcade 0 if from lessons
     */
    public ButtonAdapter(Context c, String[] buttonNamesp, ImageView change, String splitter,
                         TextView state, int[] pics, TextView result, int lessonID,
                         int conceptID, int nextAct) {

        mContext = c;
        buttonNames = buttonNamesp;
        changer = change;
        answers = splitter.split(";");
        statement = state;
        pictures = pics;
        resulter = result;
        lesson = lessonID;
        nextActivity = nextAct;
        concept = conceptID;

        // Set up vibrator service
        myVib = (Vibrator) c.getSystemService(VIBRATOR_SERVICE);
    }

    /**
     * Deafult constructor for just putting buttons in gridview
     * @param c the current activity's context
     * @param buttonNamesp the names of the button being put into the gridlayout
     */
    public ButtonAdapter(Context c, String[] buttonNamesp){
        mContext = c;
        buttonNames = buttonNamesp;
    }

    /**
     *  Gets the number of itams to put in the view
     * @return number of buttons to put in
     */
    @Override
    public int getCount() {
        return buttonNames.length;
    }

    /**
     * Does not do anything but needed to implement BaseAdapter
     * returns null
     * @param position null
     * @return null
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * Does not do anything but needed to implement BaseAdapter
     * returns the position sent to the method
     * @param position the position of the button
     * @return position of the button
     */
    @Override
    public long getItemId(int position) {
        return position;
    }


    //credit: radhoo, StackOverflow
    //Helper function for animating the picture change in PictureGameView.java

    /**
     * credit: radhoo, StackOverflow
     * Helper function for animating the picture change in PictureGameView.java
     * @param c the current activities context
     * @param v the current imageview
     * @param new_image the new imageview
     */
    private static void ImageViewAnimatedChange(Context c, final ImageView v, final int new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }



    /**
     * Used for making true and false buttons in PictureGameView.java
     * @param position the position of the view in the gridlayout
     * @param convertView the view going into the layout
     * @param parent the gridlayout
     * @return new button to be put into the gridview
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Button button;
        if (convertView == null) {
             //if it's not recycled, initialize some attributes
            button = new Button(mContext);
            button.setLayoutParams(
                    new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }

        button.setText(buttonNames[position]);
        if (button.getText().equals("TRUE")){
            //set true color to green
            button.getBackground().setColorFilter(0xFF23c438, PorterDuff.Mode.MULTIPLY);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (answers[cur + 1].equals("t")){
                        // vibrate when correct
                        myVib.vibrate(150);
                        //questions onneven indexes, answers on odd indexes
                        //Gets a random even number to put in the current statement
                        int rand = (int) (Math.random() * 40);
                        if ((rand % 2) == 1)
                            rand = rand - 1;
                        cur = rand;
                        statement.setText(toHTML(answers[cur]));
                        resulter.setText("Correct!");
                        Runnable r = new Runnable(){
                            @Override
                            public void run(){
                                ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                            }
                        };
                        Handler h = new Handler();
                        //Delay picture change by .75 secs
                        h.postDelayed(r,750);
                        pictureindex++;
                        //once 5 statements are answered correctly
                        if (pictureindex > 4) {
                            button.setEnabled(false);
                            statement.setText("Congratulations!");

                            if (nextActivity != 1) {
                                Runnable r2 = new Runnable() {
                                    @Override
                                    public void run() {
                                        //starts GameEnd activity
                                        Context context = mContext;
                                        Intent intent = new Intent(context, GameEnd.class);
                                        intent.putExtra("next_activity", nextActivity);
                                        intent.putExtra("conceptID", concept);
                                        intent.putExtra("lessonID", lesson);
                                        context.startActivity(intent);
                                    }
                                };
                                Handler h2 = new Handler();
                                //Delay transition to game end by 2.75 secs
                                h2.postDelayed(r2, 2750);
                            } else {
                                button.setEnabled(true);
                                pictureindex = 0;
                                //randomly pulled from the queue
                                int rand2 = (int) (Math.random() * 40);
                                if ((rand2 % 2) == 1)
                                    rand2 = rand2 - 1;
                                cur = rand2;
                                statement.setText(toHTML(answers[cur]));
                                Runnable r2 = new Runnable(){
                                    @Override
                                    public void run(){
                                        ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                                    }
                                };
                                Handler h2 = new Handler();
                                //Delay picture change by .75 secs
                                h.postDelayed(r,750);
                            }
                        }

                    }
                    else{
                        // incorrect vibrate
                        long[] incorrectBuzz = {0,55,40,55};
                        myVib.vibrate(incorrectBuzz, -1); // vibrate

                        resulter.setText("Incorrect! Try again");
                        //randomly pulled from the queue
                        int rand2 = (int) (Math.random() * 40);
                        if (rand2 % 2 == 1)
                            rand2 = rand2 - 1;
                        cur = rand2;
                        statement.setText(toHTML(answers[cur]));
                    }
                }
            });
        }
        else if (button.getText().equals("FALSE")){
            //set false color to red
            button.getBackground().setColorFilter(0xFFce4257, PorterDuff.Mode.MULTIPLY);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (answers[cur + 1].equals("f")){
                        // vibrate when correct
                        myVib.vibrate(150);
                        //randomly pulled from the queue
                        int rand = (int) (Math.random() * 40);
                        if ((rand % 2) == 1)
                            rand = rand - 1;
                        cur = rand;
                        statement.setText(toHTML(answers[cur]));
                        resulter.setText("Correct!");
                        //changes picture
                        Runnable r = new Runnable(){
                            @Override
                            public void run(){
                                ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                            }
                        };
                        Handler h = new Handler();
                        //Delay picture change by .75 secs
                        h.postDelayed(r,750);
                        pictureindex++;
                        //once 5 statements are answered correctly
                        if (pictureindex > 4){
                            statement.setText("Congratulations!");
                            button.setEnabled(false);
                            if (nextActivity != 1) {
                                Runnable r2 = new Runnable() {
                                    @Override
                                    public void run() {
                                        //starts GameEnd activity
                                        Context context = mContext;
                                        Intent intent = new Intent(context, GameEnd.class);
                                        intent.putExtra("next_activity", nextActivity);
                                        intent.putExtra("conceptID", concept);
                                        intent.putExtra("lessonID", lesson);
                                        context.startActivity(intent);
                                    }
                                };
                                Handler h2 = new Handler();
                                //Delay transition to game end by 2.75 secs
                                h2.postDelayed(r2, 2750);
                            } else {
                                button.setEnabled(true);
                                pictureindex = 0;
                                //randomly pulled from the queue
                                int rand2 = (int) (Math.random() * 41);
                                if ((rand2 % 2) == 1)
                                    rand2 = rand2 - 1;
                                cur = rand2;
                                statement.setText(toHTML(answers[cur]));
                                Runnable r2 = new Runnable(){
                                    @Override
                                    public void run(){
                                        ImageViewAnimatedChange(mContext,changer,pictures[pictureindex]);
                                    }
                                };
                                Handler h2 = new Handler();
                                //Delay picture change by .75 secs
                                h.postDelayed(r,750);
                            }
                        }

                    }
                    else{
                        // incorrect vibrate
                        long[] incorrectBuzz = {0,55,40,55};
                        myVib.vibrate(incorrectBuzz, -1); // vibrate

                        resulter.setText(toHTML("Incorrect! Try again"));
                        //randomly pulled from the queue
                        int rand2 = (int) (Math.random() * 41);
                        if ((rand2 % 2) == 1)
                            rand2 = rand2 - 1;
                        cur = rand2;
                        statement.setText(toHTML(answers[cur]));

                    }
                }
            });
        }
        button.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite));

        return button;
    }

    /**
     * Converts database strings to HTML to support superscripts
     * @param input the string to be converted
     * @return Spanned object to be passed into the setText method
     */
    public Spanned toHTML(String input) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(input);
        }
    }


}
