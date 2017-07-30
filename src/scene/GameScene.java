package scene;

import core.AppDefines;
import core.Camera;
import core.Game;
import core.GraphicsManager;
import entities.Player;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import level.Level;

public class GameScene extends Scene {

    private Player player;
    private Level level;
    private Camera camera;
    private int deathAlpha, timer, winAlpha;
    
    public GameScene(int width, int height, Game game){
        super(width, height, game);
        
        this.level = new Level(1);
        this.player = new Player(this.level);
        this.player.addInputsManager(this.game.getInputsManager());
        this.player.addGame(game);
        this.level.addPlayer(this.player);
        this.camera = new Camera(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT, this.level);
        this.player.setCamera(camera);
        this.deathAlpha = this.timer = this.winAlpha = 0;
        this.gm = GraphicsManager.getInstance();
    }
    
    @Override
    public void update(double dt){
        if(!this.player.isDead() && !this.player.hasCristal){
            int startX = this.player.getPosX() / AppDefines.TILE_SIZE - (AppDefines.SCREEN_WIDTH / (2 * AppDefines.TILE_SIZE));
            int startY = this.player.getPosY() / AppDefines.TILE_SIZE - (AppDefines.SCREEN_HEIGHT / (2 * AppDefines.TILE_SIZE));
            this.level.update(dt, startX, startY);
            this.player.update(dt);
            this.camera.update(this.player);
        }
        else if(this.player.hasCristal){
            this.winAlpha += dt * 3;
            this.winAlpha = Math.min(this.winAlpha, 210);
        }
        else{
            this.deathAlpha += dt * 3;
            this.deathAlpha = Math.min(this.deathAlpha, 210);
        }
    }
    
    @Override
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.translate(-this.camera.x, -this.camera.y);

        int startX = (this.player.getPosX() / AppDefines.TILE_SIZE) - (AppDefines.NB_TILES_X / 2);
        int startY = (this.player.getPosY() / AppDefines.TILE_SIZE) - (AppDefines.NB_TILES_Y / 2);
        if(startX > this.level.getWidth() - AppDefines.NB_TILES_X)startX = this.level.getWidth() - AppDefines.NB_TILES_X;
        if(startY > this.level.getHeight() - AppDefines.NB_TILES_Y)startY = this.level.getHeight() - AppDefines.NB_TILES_Y;
        if(startX < 0)startX = 0;
        if(startY < 0)startY = 0;
        
        g.setColor(new Color(44, 65, 71));
        g.fillRect(this.camera.x, this.camera.y, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
        this.level.renderFirstLayer(g, startX, startY);
        this.player.render(g);

        g2d.translate(this.camera.x, this.camera.y);

        this.renderGUI(g);
        
        if(this.player.isDead()){
            g.setColor(new Color(89, 0, 14, this.deathAlpha));
            g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
            g.setFont(this.fm.font48);
            FontMetrics fontMetrics = g.getFontMetrics(this.fm.font48);
            g.setColor(Color.black);
            int strw = fontMetrics.stringWidth(this.player.deathMsg);
            g.drawString(this.player.deathMsg, AppDefines.SCREEN_WIDTH/2 - strw/2, AppDefines.SCREEN_HEIGHT/2 - fontMetrics.getAscent()/2);
        }

        if(this.player.hasCristal){
            g.setColor(new Color(17, 184, 255, this.winAlpha));
            g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
            g.setFont(this.fm.font48);
            FontMetrics fontMetrics = g.getFontMetrics(this.fm.font48);
            g.setColor(Color.black);
            String msg = "You Win !";
            int strw = fontMetrics.stringWidth(msg);
            g.drawString(msg, AppDefines.SCREEN_WIDTH/2 - strw/2, 150);
            msg = "Thanks for playing.";
            strw = fontMetrics.stringWidth(msg);
            g.drawString(msg, AppDefines.SCREEN_WIDTH/2 - strw/2, AppDefines.SCREEN_HEIGHT/2 - fontMetrics.getAscent()/2);
            g.setFont(this.fm.font24);
            fontMetrics = g.getFontMetrics(this.fm.font24);
            msg = "Game made in 48h by Afendar.";
            strw = fontMetrics.stringWidth(msg);
            g.drawString(msg, AppDefines.SCREEN_WIDTH/2 - strw/2, 500);
        }
    }
    
    public void renderGUI(Graphics g){
        
        g.drawImage(gm.spritesheetsGUI.getSubimage(76, 0, 250, 160), 5, 5, null);
        
        g.setColor(new Color(17, 184, 255));
        for(int i=0;i<this.player.getBattery();i++){
            g.fillRect(i + 105 + ((i%10 == 0)?5:0), 58, 1, 28);
        }
        
        for(int i=0;i<this.player.getHealth();i++){
            g.fillRect(i + 105 + ((i%10 == 0)?5:0), 104, 1, 28);
        }
        
        g.drawImage(gm.spritesheetsGUI.getSubimage(326, 0, 250, 99), AppDefines.SCREEN_WIDTH - 255, 5, null);
        
        FontMetrics metrics = g.getFontMetrics(this.fm.font24);
        g.setFont(this.fm.font24);
        String score = "" + this.player.getScore();
        int scoreLength = metrics.stringWidth(score);
        g.drawString(score, AppDefines.SCREEN_WIDTH - 45 - scoreLength, 43 + metrics.getAscent());
    }
}
