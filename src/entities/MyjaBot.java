/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import core.AppDefines;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import level.Level;

public class MyjaBot extends Mob {

    private BufferedImage sprite;
    private double timeAnim;
    private int offset;
    
    public MyjaBot(int posX, int posY, Level level) {
        super(posX, posY, level);
        this.health = 50;
        this.offset = 0;
        this.timeAnim = 0;
        this.sprite = this.gm.enemyTileset.getSubimage(0, 0, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
    }
    
    @Override
    public void update(double dt){
        super.update(dt);
        if(this.velX != 0){
            this.timeAnim += dt;
            if(this.timeAnim >= 10){
                this.timeAnim = 0;
                this.offset++;
                if(this.offset > 3)
                    this.offset = 0;
                this.sprite = this.gm.enemyTileset.getSubimage(this.offset * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
                if(this.dir == LEFT){
                    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                    tx.translate(-this.sprite.getWidth(null), 0);
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    this.sprite = op.filter(this.sprite, null);
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g){
        
        this.bullets.stream().forEach((bullet) -> {
            bullet.render(g);
        });
        super.render(g);
        
        g.drawImage(this.sprite, this.posX, this.posY, null);
    }
}
