package level.tiles;

import core.AppDefines;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import level.Level;

public class PowerStation extends Tile {

    private BufferedImage sprite;
    
    public PowerStation(int imgX, int imgY){
        super(imgX, imgY, 2);
        this.sprite = this.gm.spritesheetsTerrain.getSubimage(0, 288, 107, 146);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y) {
        return true;
    }

    @Override
    public void update(Level level, int x, int y, double dt) {
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(this.sprite, x * AppDefines.TILE_SIZE - 41, y * AppDefines.TILE_SIZE - 82, null);
    }
}
