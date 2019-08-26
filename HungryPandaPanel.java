package com.example.hungrypanda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Canvas;

/**
 * The drawing and updating of the sprites are all done in this class. The thread is also
 * created and stopped/started here.
 */
public class HungryPandaPanel extends SurfaceView implements SurfaceHolder.Callback {
    public GameThread thread;

    private static Panda panda;

    private int yBeforeDrag = 0; //Previous y value before the user dragged
    private int dy = 0; //Difference in y between the previous position and the current position of users input

    private IncomingTrees incomingTrees;

    private boolean hasLost;

    private static int numTreesPassed;

    private static int score = 0; //Amount of bamboo eaten

    public HungryPandaPanel(Context context) {
        super(context);

        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this);

        thread.setPause(false);

        panda = new Panda(BitmapFactory.decodeResource(getResources(),
                R.drawable.squarepanda));
        incomingTrees = new IncomingTrees(context);
        incomingTrees.generateTree();
        hasLost = false;
        numTreesPassed = 0;
        score = 0;
        setFocusable(true);
    }

    /**
     * Called whenever the game first starts, or if the user opens the app after it's minimised.
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new GameThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
        thread.setPause(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

    /**
     * Called when the app is minimised
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        thread.setRunning(false);
    }

    /**
     * Called when user touches screen
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        //If they pressed down, then remember the y position
        if (action == MotionEvent.ACTION_DOWN) {
            yBeforeDrag = (int) event.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) { //If it was a drag
            //Used screenHeight in determining how far the Panda should move, this should
            //make it more robust to different phones and the difference in height can affect
            //gameplay.
            int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
            double multiplier = screenHeight * 0.0006; //0.0006 was found through trial and error to be a good multiplier
            dy = (int) (((int) event.getRawY() - yBeforeDrag) * multiplier);
            yBeforeDrag = (int) event.getRawY();
        }
        return true;
    }

    /**
     * Updates the position of all the sprites, generates trees if needed and checks for
     * collision between Panda and Tree or Panda and Bamboo
     */
    public void update() {
        if(!hasLost) {
            incomingTrees.generateTree();
            incomingTrees.updateTrees();
            panda.update(dy);
            dy = 0;
            if(incomingTrees.collisionDetected(getPandaRect())){
                hasLost = true;
                return;
            }
        }

        if (hasLost) {
            long startTime = System.nanoTime();
            while(System.nanoTime() - startTime < 2000000000){

            }

            thread.setRunning(false);
            thread.setPause(true);
            numTreesPassed = 0;

            SharedPreferences prefs = getContext().getSharedPreferences("HighScores",
                                      Context.MODE_PRIVATE); //Get stored data from shared preferences
            if(prefs.contains("HighScore")){ //If a previous high score exists, then update it
                int prevScore = prefs.getInt("HighScore", 0);
                if(score > prevScore){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("HighScore", score);
                    editor.commit(); //Put the changes into SharedPreferences
                }
            } else {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("HighScore", score);
                editor.commit();
            }

            //Open the MainActivity menu again.
            Intent i = new Intent().setClass(getContext(), MainActivity.class);
            getContext().startActivity(i);
        }

        if(incomingTrees.passedTree(getPandaRect())){
            numTreesPassed++;
            score++;
        }

        if(incomingTrees.ateBamboo(getPandaRect())){
            score+=2;
        }
    }

    /**
     * Used for collision checking
     * @return the bounding box of the panda
     */
    private Rect getPandaRect() {
        return new Rect(panda.getX(), panda.getY(),
                panda.getX() + panda.getWidth() - 10,
                panda.getY() + panda.getHeight() - 10);
    }

    /**
     * Draws all the sprites and text onto the canvas
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if(canvas != null) {
            super.draw(canvas);
            canvas.drawColor(Color.GRAY);
            panda.draw(canvas);
            incomingTrees.drawTrees(canvas);
            if(hasLost){
                drawLoss(canvas);
            } else {
                drawScore(canvas);
            }
        }

    }

    /**
     * Draws the users score after they have lost
     */
    private void drawLoss(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);

        String scoreValue;
        SharedPreferences prefs = getContext().getSharedPreferences("HighScores",
                Context.MODE_PRIVATE); //Get stored data from shared preferences
        if(prefs.contains("HighScore")) { //If a previous high score exists, then update it
            int prevScore = prefs.getInt("HighScore", 0);
            if (score > prevScore) {
                scoreValue = "New High Score:\n" + score;
            } else {
                scoreValue = "Ouch!\nScore: " + score;
            }
        } else {
            scoreValue = "New High Score:\n" + score;
        }
        int width = (int) textPaint.measureText(scoreValue);
        StaticLayout scoreLayout = new StaticLayout(scoreValue, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        canvas.translate((canvas.getWidth() / 2) - (scoreLayout.getWidth() / 2), (canvas.getHeight() / 2) - (scoreLayout.getHeight() / 2));
        scoreLayout.draw(canvas);
    }

    /**
     * Draws the score onto the screen, x value is centered
     */
    private void drawScore(Canvas canvas){
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40 * getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.WHITE);

        String scoreValue = String.valueOf(score);
        int width = (int) textPaint.measureText(scoreValue);
        StaticLayout scoreLayout = new StaticLayout(scoreValue, textPaint, width, Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        canvas.translate((canvas.getWidth() / 2) - (scoreLayout.getWidth() / 2), 0);
        scoreLayout.draw(canvas);
    }

    /**
     * @return the panda
     */
    public static Panda getPanda() {
        return panda;
    }

    /**
     * Get number of trees passed
     */
    public static int getNumTreesPassed(){
        return numTreesPassed;
    }
    /**
     * @return current score
     */
    public static int getScore(){
        return score;
    }
}
