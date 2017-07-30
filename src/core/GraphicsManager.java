package core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class GraphicsManager {
    
    private static GraphicsManager _instance;
    
    public BufferedImage bpBackground, spritesheetsGUI, spritesheetsTerrain, characterTileset, enemyTileset;
    
    private GraphicsManager(){
        try{
            URL url = this.getClass().getResource("/gfx/blueprint.png");
            this.bpBackground = ImageIO.read(url);
            url = this.getClass().getResource("/gfx/spritesheet-gui.png");
            this.spritesheetsGUI = ImageIO.read(url);
            url = this.getClass().getResource("/gfx/spritesheet-terrain.png");
            this.spritesheetsTerrain = ImageIO.read(url);
            url = this.getClass().getResource("/gfx/character.png");
            this.characterTileset = ImageIO.read(url);
            url = this.getClass().getResource("/gfx/enemy1.png");
            this.enemyTileset = ImageIO.read(url);
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static GraphicsManager getInstance(){
        if(_instance == null);
            _instance = new GraphicsManager();
        return _instance;
    }
}
