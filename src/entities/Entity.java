package entities;

import Particles.TextParticle;
import audio.Sound;
import core.AppDefines;
import core.GraphicsManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import level.Level;

public abstract class Entity {
    
    protected int posX, posY, velX, velY, dir, health;
    protected Level level;
    protected boolean dead;
    protected Random rnd;
    protected static int LEFT = 0;
    protected static int RIGHT = 1;
    protected ArrayList<TextParticle> textParticles;
    protected GraphicsManager gm;
    
    public Entity(int posX, int posY, Level level){
        this.posX = posX;
        this.posY = posY;
        this.velX = this.velY = 0;
        this.level = level;
        this.dead = false;
        this.dir = RIGHT;
        this.rnd = new Random();
        this.textParticles = new ArrayList<>();
        this.gm = GraphicsManager.getInstance();
    }
    
    public int getPosX(){
        return this.posX;
    }
    
    public int getPosY(){
        return this.posY;
    }
    
    public void setPosX(int posX){
        this.posX = posX;
    }
    
    public void setPosY(int posY){
        this.posY = posY;
    }
    
    public boolean isDead(){
        return this.dead;
    }
    
    public void die(){
        this.dead = true;
    }
    
    public void hit(int dammages){
        this.health -= dammages;
        
        this.textParticles.add(new TextParticle(""+dammages, this.posX + AppDefines.TILE_SIZE/2, this.posY + AppDefines.TILE_SIZE/2, new Color(128, 0, 0)));
        
        if(this.health <= 0){
            new Thread(Sound.explosion::play).start();
            this.die();
        }
        else{
            new Thread(Sound.hit[(int)Math.round(Math.random() * 1)]::play).start();
        }
    }
    
    public abstract Rectangle getBounds();
    public abstract void update(double dt);
    public abstract void render(Graphics g);
}
