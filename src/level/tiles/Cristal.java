package level.tiles;

import core.AppDefines;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import level.Level;

public class Cristal extends Tile {

    private BufferedImage sprite;
    
    public Cristal(int imgX, int imgY){
        super(imgX, imgY, 3);
        this.sprite = this.gm.spritesheetsTerrain.getSubimage(128, 192, 64, 64);
        
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
        g.drawImage(this.sprite, x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
    }
    
}
