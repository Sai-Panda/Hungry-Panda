package com.example.hungrypanda;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * A tree object that represents the two trunks that are on the same x value.
 */
public class Tree implements GameObject {
    private int bottomTrunkTop;

    private final int START_TRUNK_X = MainActivity.SCREEN_WIDTH;
    private final int START_TRUNK_Y = MainActivity.SCREEN_HEIGHT;

    private int x = START_TRUNK_X;
    private int y = START_TRUNK_Y;

    private final int TRUNK_WIDTH = 100;

    private static final int DEFAULT_TREE_SPEED = 10;

    private Bamboo bamboo;

    private Context context;

    private Rect topTrunk;
    private Rect bottomTrunk;

    private Drawable topImg;
    private Drawable bottomImg;

    private final int HEIGHT_RANGE = ((MainActivity.SCREEN_HEIGHT - (MainActivity.SCREEN_HEIGHT / 5))
                                    - (MainActivity.SCREEN_HEIGHT / 6));
    private int topTrunkHeight = (int) (Math.random() * HEIGHT_RANGE) + MainActivity.SCREEN_HEIGHT / 6;

    private static int speed;

    private boolean alreadyPassed = false;

    private final int MAX_SPEED = 80;

    public Tree(Context context){
        this.context = context;

        topTrunk = new Rect(x, 0, x + TRUNK_WIDTH, topTrunkHeight);

        bottomTrunkTop = (int) (topTrunkHeight + (HungryPandaPanel.getPanda().getHeight() + 20) + (Math.random() * 400 + 30));
        bottomTrunk = new Rect(x, y, x + TRUNK_WIDTH, y - bottomTrunkTop);

        topImg = context.getResources().getDrawable(R.drawable.tree);
        bottomImg = context.getResources().getDrawable(R.drawable.tree);

        x = topTrunk.left;
        y = topTrunk.top;

        bamboo = new Bamboo(context, this);
        if(HungryPandaPanel.getNumTreesPassed() == 0){
            speed = DEFAULT_TREE_SPEED;
        } else {
            speed = speed;
        }

    }

    /**
     * @return the x value of the tree
     */
    public int getX(){
        return x;
    }

    @Override
    public void draw(Canvas canvas) {
        topTrunk = new Rect(x, 0, x + TRUNK_WIDTH, topTrunkHeight);
        bottomTrunk = new Rect(x, bottomTrunkTop, x + TRUNK_WIDTH, MainActivity.SCREEN_HEIGHT);

        topImg.setBounds(topTrunk);
        bottomImg.setBounds(bottomTrunk);

        topImg.draw(canvas);
        bottomImg.draw(canvas);
        if(bamboo != null) {
            bamboo.draw(canvas);
        }
    }

    @Override
    public void update() {
        this.x-= speed;
        if(bamboo != null){
            bamboo.update();
        }
    }

    /**
     * @return the width of the tree
     */
    public int getWidth(){
        return this.TRUNK_WIDTH;
    }

    /**
     * Checks if the Panda's bounding box and the trees bounding box intersect
     * @param pandaRect - Panda's bounding box
     * @return whether the two Boxes intersect
     */
    public boolean collisionDetected(Rect pandaRect){
        return pandaRect.intersect(topTrunk) || pandaRect.intersect(bottomTrunk);
    }

    /**
     * Gets the y value in the middle of the tree gap, used for drawing the bamboo
     */
    public int getMiddleOfGap(){
        return topTrunk.bottom + getGapSize() / 2;
    }

    /**
     * @return size of the gap between the two trunks
     */
    public int getGapSize(){
        return bottomTrunk.top - topTrunk.bottom;
    }

    /**
     * Checks if the Panda's bounding box and the bamboo's bounding box intersect
     * @param pandaRect - Panda's bounding box
     * @return
     */
    public boolean ateBamboo(Rect pandaRect){
        if(bamboo == null){
            return false;
        } else if(bamboo.gotEaten(pandaRect)){
            bamboo = null;
            updateSpeed();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the speed (if need be)
     * @return whether the score was updated or not
     */
    private void updateSpeed(){
        if (HungryPandaPanel.getNumTreesPassed() % 2 == 0){ //Updates speed of all trees every second tree passed
            speed++;
            if(speed > MAX_SPEED){
                speed = MAX_SPEED;
            }
        }
    }

    /**
     * Checks if the Panda has passed this tree,
     * uses respective x values to check.
     * @param pandaRect - Panda's bounding box
     * @return whether the Panda has passed the tree or not
     */
    public boolean passedTree(Rect pandaRect){
        if(alreadyPassed){
            return false;
        }
        if(pandaRect.left > this.getX() + this.getWidth()){
            updateSpeed();
            alreadyPassed = true;
            return true;
        }
        return false;
    }
    /**
     * Set the speed of the trees
     * @param newSpeed
     */
    public static void setSpeed(int newSpeed){
        speed = newSpeed;
    }
}
