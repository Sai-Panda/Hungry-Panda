package com.example.hungrypanda;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Panda implements GameObject {
    private Bitmap panda;
    private final int X = 80;
    private int y;

    private int WIDTH = 100;
    private int HEIGHT = 100;

    /**
     * Panda object class
     * @param map - image of the panda
     */
    public Panda(Bitmap map){
        panda = Bitmap.createScaledBitmap(map, WIDTH, HEIGHT, true);
        this.y = MainActivity.SCREEN_HEIGHT / 2;
    }

    /**
     * @return x value of top left corner of the panda
     */
    public int getX(){
        return this.X;
    }

    /**
     * @return y value of top side of the panda
     */
    public int getY(){
        return this.y;
    }

    /**
     * @return the width of the panda
     */
    public int getWidth(){
        return this.WIDTH;
    }

    /**
     * @return the height of the panda
     */
    public int getHeight(){ return this.HEIGHT; }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(panda, X, y, null);
    }

    @Override
    public void update() {

    }

    /**
     * New update method overloading the default update method
     * @param dy
     */
    public void update(int dy){
        if(dy < 0){
            y = Math.max(0 + this.getHeight() / 2, y + dy);
        } else {
            y = Math.min(MainActivity.SCREEN_HEIGHT - (this.getHeight() + this.getHeight() / 2), y + dy);
        }
    }
}
