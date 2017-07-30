package core;

import scene.Scene;
import scene.GameScene;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import scene.SplashScene;

public class Game extends Canvas implements Runnable {

    private static Game _instance = null;
    private int screenW, screenH, lastTime, pauseTime;
    private String title;
    private boolean running;
    private Thread gameThread;
    private ArrayList<Scene> scenes;
    private InputsManager inputManager;
    
    private Game(){
        this.running = false;
        
        this.inputManager = InputsManager.getInstance();
        this.inputManager.setGame(this);
        
        this.scenes = new ArrayList<>();
        SplashScene gs = new SplashScene(this.screenW, this.screenH, this);
        this.scenes.add(gs);
    }

    public static Game getInstance(){
        if(_instance == null)
            _instance = new Game();
        
        return _instance;
    }
    
    public void setScreenSize(int width, int height){
        this.screenW = width;
        this.screenH = height;
        this.setMinimumSize(new Dimension(width, height));
        this.setMaximumSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.setSize(new Dimension(width, height));
    }
    
    public void start(){
        if(this.running)
            return;
        this.running = true;
        this.gameThread = new Thread(this, "gameThread");
        this.gameThread.start();
    }
    
    public void stop(){
        this.running = false;
    }
    
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double nsms = 1000000000 / 60;
        int frameCpt = 0;
        
        boolean needUpdate;
        this.lastTime = TimerThread.MILLI;
        this.pauseTime= 0;
        
        while(this.running){
            long current = System.nanoTime();
            try{
                Thread.sleep(2);
            }
            catch(InterruptedException e){}
            
            needUpdate = false;
            double delta = (current - lastTime) / nsms;
            
            if((current - lastTime) / nsms  >= 1)
            {         
                frameCpt++;
                lastTime = current;
                needUpdate = true;
            }
            
            this.render();
            
            if(needUpdate)
            {
                this.update(delta);
            }
            
            if(System.currentTimeMillis() - startTime >= 1000)
            {
                frameCpt = 0;
                startTime = System.currentTimeMillis();
            }
        }
    }
    
    public void update(double dt){
        
        this.inputManager.update();
        this.scenes.get(this.scenes.size() - 1).update(dt);
    }
    
    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(2);
            requestFocus();
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        //clear screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
        
        this.scenes.stream().forEach((scene) -> {
            scene.render(g);
        });
        
        g.dispose();
        bs.show();
    }
    
    public InputsManager getInputsManager(){
        return this.inputManager;
    }
    
    public void addScene(Scene scene){
        this.scenes.add(scene);
    }
    
    public void shiftScene(){
        this.scenes.remove(this.scenes.size() - 1);
    }
}
