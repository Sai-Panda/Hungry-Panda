package com.example.hungrypanda;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.*;

/**
 * For having a centralised place to manage all the trees in the game.
 */
public class IncomingTrees {
    private List<Tree> trees;
    private Context context;

    private Tree closestToPanda;

    public IncomingTrees(Context context){
        trees = new ArrayList<>();
        this.context = context;
    }

    /**
     * Creates a new tree if it is needed
     */
    public void generateTree(){
        if(this.needATree()){
            trees.add(new Tree(context));
            closestToPanda = trees.get(0);
        }
    }

    /**
     * Checks if the closestToPanda Tree is off the screen or if there is
     * less than one tree in the game.
     * @return whether a tree needs to be generates
     */
    private boolean needATree(){
        if(trees.size() < 1){
            return true;
        }

        if(closestToPanda.getX() + closestToPanda.getWidth() < 0){
            trees.remove(closestToPanda);
            return true;
        }
        return false;
    }

    /**
     * Draws all the trees onto the canvas.
     * @param canvas
     */
    public void drawTrees(Canvas canvas){
        for(Tree t : this.trees){
            t.draw(canvas);
        }
    }

    /**
     * Updates all the trees
     */
    public void updateTrees(){
        for(Tree t : this.trees){
            t.update();
        }
    }

    /**
     * Checks if panda collided with the tree
     * @param pandaRect
     * @return
     */
    public boolean collisionDetected(Rect pandaRect){
        return closestToPanda.collisionDetected(pandaRect);
    }

    /**
     * Checks if the Panda has passed the closest tree,
     * uses respective x values to check.
     * @param pandaRect - Panda's bounding box
     * @return whether the Panda has passed the tree or not
     */
    public boolean passedTree(Rect pandaRect){
        return closestToPanda.passedTree(pandaRect);
    }

    /**
     * Checks if panda collided with the bamboo rect
     * @param pandaRect
     * @return
     */
    public boolean ateBamboo(Rect pandaRect){
        return closestToPanda.ateBamboo(pandaRect);
    }
}
