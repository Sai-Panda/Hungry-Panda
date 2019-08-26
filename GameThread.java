package com.example.hungrypanda;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Threading to allow for multiple actions to occur at once
 */
public class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private HungryPandaPanel gamePanel;
    private boolean running;
    private boolean paused;
    public Canvas canvas;

    public GameThread(SurfaceHolder surfaceHolder, HungryPandaPanel panel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = panel;
    }

    /**
     * Updates thread running status
     * @param running
     */
    public void setRunning(boolean running){
        this.running = running;
    }

    /**
     * @return whether the thread is running or not
     */
    public boolean isRunning(){
        return this.running;
    }

    /**
     * @return whether the thread is paused or not (i.e minimised)
     */
    public boolean isPaused(){
        return paused;
    }

    /**
     * Updates game status (is game minimised, etc)
     * @param paused
     */
    public void setPause(boolean paused){
        this.paused = paused;
    }

    /**
     * Main run loop for the thread.
     */
    @Override
    public void run(){
        while(this.isRunning()){
            canvas = null;
            try{
                canvas = this.surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder){
                    if(!this.isPaused()) { //Only update if the game isn't paused or minimised
                        this.gamePanel.update();
                    }
                    this.gamePanel.draw(canvas);
                }
            } finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas); //Put the changes onto the canvas
                }
            }
        }

    }
}
