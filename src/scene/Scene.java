package scene;

import core.FontManager;
import core.Game;
import core.GraphicsManager;
import core.InputsManager;
import java.awt.Graphics;

public abstract class Scene {
    
    protected int width, height;
    protected Game game;
    protected InputsManager inputsManager;
    protected GraphicsManager gm;
    protected FontManager fm;
    
    public Scene(int width, int height, Game game){
        this.width = width;
        this.height = height;
        this.game = game;
        this.inputsManager = this.game.getInputsManager();
        this.gm = GraphicsManager.getInstance();
        this.fm = FontManager.getInstance();
        
    }
    public abstract void update(double dt);
    public abstract void render(Graphics g);
}
