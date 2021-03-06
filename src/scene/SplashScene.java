package scene;

import core.AppDefines;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import core.Game;

/**
 * SplashScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class SplashScene extends Scene {

    public String txt1, txt2;
    public Font font, fontSm;
    public BufferedImage logo;
    public int alpha;
    public boolean increase, first;
    
    /**
     * 
     * @param w
     * @param h
     * @param game 
     */
    public SplashScene(int w, int h, Game game){
        super(w, h, game);
        
        this.alpha = 255;
        this.increase = false;
        this.first = true;
        this.txt1 = "Afendar games";
        this.txt2 = "present";
        
        try{
            URL url = this.getClass().getResource("/gfx/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 36.0f);
            this.fontSm = this.font.deriveFont(Font.PLAIN, 22.0f);
            
            url = this.getClass().getResource("/gfx/afendar.png");
            this.logo = ImageIO.read(url);
        }catch(FontFormatException | IOException e){
            e.getMessage();
        }
    }
    
    @Override
    public void update(double dt) {
        
        if(this.first)
        {
            try{
                Thread.sleep(500);
                this.first = false;
            }catch(InterruptedException e){
            }
        }
        
        try{
            Thread.sleep(5);
            if(this.alpha > 0 && !this.increase){
                this.alpha--;
                if( this.alpha < 0 )
                    this.alpha = 0;
            }
            else{
                this.alpha+=2;
                if(this.alpha > 255)
                    this.alpha = 255;
            }
        }catch(InterruptedException e){
        }
        
        if(this.alpha == 0)
            this.increase = true;
        
        if(this.alpha == 255 && this.increase){
            this.game.shiftScene();
            this.game.addScene(new GameScene(AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT, this.game));
        }
    }

    @Override
    public void render(Graphics g) {
        
        g.setColor(Color.black);
        g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
        g.drawImage(this.logo, AppDefines.SCREEN_WIDTH/2 - this.logo.getWidth()/2, 160 , null);
        
        g.setColor(Color.WHITE);
        g.setFont(this.font);
        FontMetrics metrics = g.getFontMetrics(this.font);
        int txt1W = metrics.stringWidth(this.txt1);
        g.drawString(this.txt1, AppDefines.SCREEN_WIDTH/2 - txt1W/2, 390);
        
        g.setFont(this.fontSm);
        metrics = g.getFontMetrics(this.fontSm);
        int txt2W = metrics.stringWidth(this.txt2);
        g.drawString(this.txt2, AppDefines.SCREEN_WIDTH/2 - txt2W/2, 430);
        
        g.setColor(new Color(0, 0, 0, this.alpha));
        g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
    }
}