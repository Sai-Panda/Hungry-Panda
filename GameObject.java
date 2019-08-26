package com.example.hungrypanda;

import android.graphics.Canvas;

/**
 * Interface for all the Objects in the game, i.e Panda, Tree and Bamboo
 */
public interface GameObject {
    void draw(Canvas canvas); //Draws the item onto the canvas
    void update(); //Updates it's position and checks for collision
}
