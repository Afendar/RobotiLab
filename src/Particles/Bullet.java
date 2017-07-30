package Particles;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends Particle {
    
    public Bullet(int x, int y, int dx, int dy, int health){
        super();
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.health = health;
        this.color = Color.red;
    }
    
    @Override
    public void update(double dt) {
        this.x += this.dx;
        this.y += this.dy;
        this.health--;
        if(this.health < 0){
            this.dead = true;
        }
    }

    public void setColor(Color c){
        this.color = c;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.x - 3, this.y - 1, 6, 2);
    }
    
}
