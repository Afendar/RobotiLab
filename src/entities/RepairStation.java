package entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import level.Level;

public class RepairStation extends Entity {

    private BufferedImage sprite;
    
    public RepairStation(int posX, int posY, Level level){
        super(posX - 32, posY - 32, level);
        this.sprite = this.gm.spritesheetsTerrain.getSubimage(0, 192, 128, 96);
    }

    @Override
    public void update(double dt) {
        
    }
    
    @Override
    public void render(Graphics g){
        g.drawImage(this.sprite, this.posX, this.posY, null);
    }    
    
    @Override
    public Rectangle getBounds(){
        return new Rectangle(this.posX, this.posY, 128, 96);
    }
}
