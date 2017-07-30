package core;

import entities.Player;
import level.Level;

public class Camera {
    
    public int x, y;
    public int w, h;
    public Level level;
    
    public Camera(int x, int y, int w, int h, Level level){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.level = level;
    }
    
    public void update(Player p){
        this.x = (p.getPosX() + (p.getBounds().width / 2)) - this.w / 2;
        this.y = (p.getPosY() + (p.getBounds().height / 2)) - this.h / 2;
        
        if(this.x < 0)
            this.x = 0;
        if(this.y < 0)
            this.y = 0;
        if(this.y + this.h > this.level.getHeight())
            this.y = this.level.getHeight() - this.h;
        if(this.x + this.w > this.level.getWidth())
            this.x = this.level.getWidth() - this.w;
    }
}
