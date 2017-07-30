package level.tiles;

import core.AppDefines;
import java.awt.Color;
import java.awt.Graphics;
import level.Level;

public class Floor extends Tile {

    public Floor(int imgX, int imgY){
        super(imgX, imgY, 1);
    }
    
    @Override
    public boolean canPass(Level level, int x, int y){
        return false;
    }
    
    @Override
    public void update(Level level, int x, int y, double dt){
        
    }
    
    @Override
    public void render(Graphics g, int x, int y){
        g.drawImage(gm.spritesheetsTerrain.getSubimage(4 * AppDefines.TILE_SIZE, 0, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
    }
    
    public void render(Graphics g, int x, int y, boolean left, boolean right, boolean top, boolean bottom){
        /*g.setColor(Color.black);
        if(right && !left){
            if(bottom && !top){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(192, 128, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else if(top && !bottom){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(192, 192, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else{
                g.drawImage(gm.spritesheetsTerrain.getSubimage(4 * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
        }
        else if(left && !right){
            
            if(bottom && !top){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(256, 128, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else if(top && !bottom){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(256, 192, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else{
                g.drawImage(gm.spritesheetsTerrain.getSubimage(3 * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
        }
        else{
            if(left && right && !top && !bottom){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(256, 0, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else if(bottom && left && right && !top){
                g.drawImage(gm.spritesheetsTerrain.getSubimage(192, 0, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            }
            else if(top && bottom && (!left || !right)){
                g.fillRect(x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
            }
            else if(top && bottom && left && right){*/
                g.drawImage(gm.spritesheetsTerrain.getSubimage(224, 161, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE), x * AppDefines.TILE_SIZE, y * AppDefines.TILE_SIZE, null);
            /*}
            else{
                
            }
        }*/
    }
}
