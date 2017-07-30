package Particles;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public abstract class Particle {
    
    protected boolean dead;
    public int x;
    public int y;
    public int dx;
    public int dy;
    public int health;
    protected Color color;
    protected Random rnd;
    
    public Particle(){
        this.dead = false;
        this.rnd = new Random();
    }
    
    public abstract void update(double dt);
    public abstract void render(Graphics g);
    
    public boolean isDead(){
        return this.dead;
    }
}
