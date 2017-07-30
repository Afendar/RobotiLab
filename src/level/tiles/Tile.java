package level.tiles;

import core.GraphicsManager;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import level.Level;

public abstract class Tile {
    
    public int imgX, imgY;
    public final int ID;
    public BufferedImage tileset, tile;
    public int bonus = 0;
    
    protected GraphicsManager gm = GraphicsManager.getInstance();
    
    public Tile(int imgX, int imgY, int ID){
        this.imgX = imgX;
        this.imgY = imgY;
        this.ID = ID;
        TileAtlas.atlas.add(this);
    }
    
    public abstract boolean canPass(Level level, int x, int y);
    
    public abstract void update(Level level, int x, int y, double dt);
    
    public void render(Graphics g, int x, int y){
    }
    
    public void renderTop(Graphics g, int x, int y){
    }
}