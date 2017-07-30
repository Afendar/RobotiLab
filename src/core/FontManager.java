package core;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;

public class FontManager {
    
    private static FontManager _instance;

    public Font font24, font19, font19Bold, font48;
    
    private FontManager(){
        try{
            URL url = this.getClass().getResource("/gfx/fonts/giantrobotarmy.ttf");
            this.font24 = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font24 = this.font24.deriveFont(Font.PLAIN, 24.0f);
            this.font19 = this.font24.deriveFont(Font.PLAIN, 19.0f);
            this.font48 = this.font24.deriveFont(Font.PLAIN, 48.0f);
            this.font19Bold = this.font24.deriveFont(Font.BOLD, 19.0f);
        }
        catch(FontFormatException | IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    public static FontManager getInstance(){
        if(_instance == null);
            _instance = new FontManager();
        return _instance;
    }
    
}
