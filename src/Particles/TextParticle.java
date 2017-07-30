package Particles;

import core.FontManager;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

public class TextParticle extends Particle {
    
    private int timer;
    private double xx, yy, zz;
    private double xa, ya, za;
    private FontManager fontManager;
    private String txt;
    
    public TextParticle(String txt, int x, int y, Color color ){
        super();
        this.txt = txt;
        this.color = color;
        this.x = x;
        this.y = y;
        this.xx = x;
        this.yy = y;
        this.zz = 2;
        this.xa = 0;
        this.ya = -Math.abs((this.rnd.nextGaussian() * 2 + 3) * 1.2);
        this.za = this.rnd.nextGaussian() * 0.7 + 2;
        this.timer = 0;
        this.fontManager = FontManager.getInstance();
    }
    
    @Override
    public void update(double dt) {
        timer++;
        if(this.timer > 60){
            this.dead = true;
        }
        this.xx += this.xa;
        this.yy += this.ya;
        this.zz += this.za;
        if(this.zz < 0){
            this.zz = 0;
            this.za *= -0.5;
            this.xa *= 0.6;
            this.ya *= 0.6;
        }
        this.za -= 0.15;
        this.x = (int)this.xx;
        this.y = (int)this.yy;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(this.color);
        g.setFont(this.fontManager.font19Bold);
        g.drawString(this.txt, this.x - 1, this.y);
    }
    
}
