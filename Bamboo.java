package com.example.hungrypanda;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class Bamboo implements GameObject {
    private int x, top, bottom;
    private Drawable image;
    private Rect boundingBox;
    private Tree tree;
    private int width;
    private int height;

    public Bamboo(Context context, Tree tree){
        this.image = context.getResources().getDrawable(R.drawable.bamboo);
        this.tree = tree;
        width = tree.getWidth() / 3;
        height =  tree.getGapSize() / 10;
        update();
    }

    @Override
    public void draw(Canvas canvas) {
        this.boundingBox = new Rect(x, top,  x + width, bottom);
        this.image.setBounds(boundingBox);

        image.draw(canvas);
    }

    @Override
    public void update() {
        x = tree.getX() + ((tree.getWidth() / 2) - (width / 2));
        top = tree.getMiddleOfGap() - height /2;
        bottom = tree.getMiddleOfGap() + height /2;
    }

    /**
     * Checks if Bamboos bounding box intersects with Panda's bounding box
     * @param pandaRect - Panda's bounding box
     * @return
     */
    public boolean gotEaten(Rect pandaRect){
        if(pandaRect == null || this.boundingBox == null){
            return false;
        }
        return pandaRect.intersect(boundingBox);
    }
}
