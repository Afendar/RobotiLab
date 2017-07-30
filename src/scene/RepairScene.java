package scene;

import audio.Sound;
import core.AppDefines;
import core.BezierCurve;
import core.Game;
import entities.Player;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class RepairScene extends Scene {
    
    private int backgroundPosY;
    private double timer, timer2;
    private int selectedItem;
    private boolean out, displayError;
    private BufferedImage[] foot1, foot2, body;
    private int[] damagesZones;
    private Player p;
    
    public RepairScene(int w, int h, Game game, Player p){
        super(w, h, game);
        
        this.selectedItem = 0;
        this.timer = 0;
        this.backgroundPosY = -400;
        this.p = p;
        this.damagesZones = p.getDamedZones();
        this.displayError = this.out = false;
        
        this.foot1 = new BufferedImage[3];
        this.foot1[0] = this.gm.spritesheetsGUI.getSubimage(205, 160, 160, 145);
        this.foot1[1] = this.gm.spritesheetsGUI.getSubimage(586, 160, 160, 145);
        this.foot1[2] = this.gm.spritesheetsGUI.getSubimage(206, 450, 160, 145);
        
        this.foot2 = new BufferedImage[3];
        this.foot2[0] = this.gm.spritesheetsGUI.getSubimage(205, 305, 106, 130);
        this.foot2[1] = this.gm.spritesheetsGUI.getSubimage(586, 305, 106, 130);
        this.foot2[2] = this.gm.spritesheetsGUI.getSubimage(206, 595, 106, 130);
             
        this.body = new BufferedImage[3];
        this.body[0] = this.gm.spritesheetsGUI.getSubimage(0, 160, 189, 245);
        this.body[1] = this.gm.spritesheetsGUI.getSubimage(381, 160, 189, 245);
        this.body[2] = this.gm.spritesheetsGUI.getSubimage(1, 450, 189, 245);
    }

    @Override
    public void update(double dt) {
        this.timer += dt;
        if(this.timer < 55){
            if(!this.out)
                this.backgroundPosY = BezierCurve.easeOutBounce((int)this.timer, -400, 450, 50);
            else
                this.backgroundPosY = BezierCurve.easeInBounce((int)this.timer, 60, -400, 50);
        }
        
        if(this.inputsManager.escape.enabled && !this.out){
            this.timer = 0;
            this.out = true;
            new Thread(Sound.select::play).start();
        }
        
        if(this.timer > 55 && this.out){
            this.game.shiftScene();
        }
        
        if(this.displayError){
            this.timer2 += dt;
            if(this.timer2 > 80){
                this.displayError = false;
            }
        }
        
        this.processHover();
        this.processClick();
    }

    public void processHover(){
        int mouseX = this.inputsManager.getMouseX();
        int mouseY = this.inputsManager.getMouseY();
        if(mouseX >= AppDefines.SCREEN_WIDTH - 165 && mouseX <= AppDefines.SCREEN_WIDTH - 165 + 38 &&
                mouseY >= this.backgroundPosY + 25 && mouseY <= this.backgroundPosY + 25 + 38)
            this.selectedItem = 1;
        else if(mouseX >= 100 && mouseX <= 100 + 195 && 
                mouseY >= AppDefines.SCREEN_HEIGHT - 80 && mouseY <= AppDefines.SCREEN_HEIGHT - 80 + 40){
            this.selectedItem = 2;
        }
        else if(mouseX >= 302 && mouseX <= 302 + 195 && 
                mouseY >= AppDefines.SCREEN_HEIGHT - 80 && mouseY <= AppDefines.SCREEN_HEIGHT - 80 + 40){
            this.selectedItem = 3;
        }
        else if(mouseX >= 505 && mouseX <= 505 + 195 && 
                mouseY >= AppDefines.SCREEN_HEIGHT - 80 && mouseY <= AppDefines.SCREEN_HEIGHT - 80 + 40){
            this.selectedItem = 4;
        }
        else
            this.selectedItem = 0;
    }
    
    public void processClick(){
        if(this.inputsManager.mousePressed){
            switch(this.selectedItem){
                case 1:
                    if(!this.out){
                        new Thread(Sound.select::play).start();
                        this.timer = 0;
                        this.out = true;
                    }
                    break;
                case 2:
                    if(this.p.getScore() > this.damagesZones[0] * 50){
                        this.p.removeToScore(this.damagesZones[0] * 50);
                        this.p.addHealth(this.damagesZones[0] * 5);
                        this.p.damagesZones[0] = 0;
                    }
                    else{
                        this.displayError = true;
                        this.timer2 = 0;
                    }
                    break;
                case 3:
                    if(this.p.getScore() > this.damagesZones[1] * 20){
                        this.p.removeToScore(this.damagesZones[1] * 20);
                        this.p.addHealth(this.damagesZones[1] * 5);
                        this.p.damagesZones[1] = 0;
                    }
                    else{
                        this.displayError = true;
                        this.timer2 = 0;
                    }
                    break;
                case 4:
                    if(this.p.getScore() > this.damagesZones[2] * 20){
                        this.p.removeToScore(this.damagesZones[2] * 20);
                        this.p.addHealth(this.damagesZones[2] * 5);
                        this.p.damagesZones[2] = 0;
                    }
                    else{
                        this.displayError = true;
                        this.timer2 = 0;
                    }
                    break;
            }
            if(!this.displayError){
                if(!this.out){
                    new Thread(Sound.select::play).start();
                    this.timer = 0;
                    this.out = true;
                }
            }
        }
    }
    
    @Override
    public void render(Graphics g) {
        
        g.setColor(new Color(0,0,0,180));
        g.fillRect(0, 0, AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
        g.drawImage(this.gm.bpBackground, 100, this.backgroundPosY, null);
        if(this.selectedItem == 1){
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(38, 0, 38, 38), AppDefines.SCREEN_WIDTH - 165, this.backgroundPosY + 25, null);
        }
        else{
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(0, 0, 38, 38), AppDefines.SCREEN_WIDTH - 165, this.backgroundPosY + 25, null);
        }
        
        g.drawImage(this.body[((this.damagesZones[0]/2 > 2)?2:(int)this.damagesZones[0]/2)], AppDefines.SCREEN_WIDTH/2 - 94,  this.backgroundPosY + 50, null);
        g.drawImage(this.foot1[((this.damagesZones[1]/2 > 2)?2:(int)this.damagesZones[1]/2)], AppDefines.SCREEN_WIDTH/2 - 170, this.backgroundPosY + 205, null);
        g.drawImage(this.foot2[((this.damagesZones[2]/2 > 2)?2:(int)this.damagesZones[2]/2)], AppDefines.SCREEN_WIDTH/2 + 20, this.backgroundPosY + 215, null);
    
        if(this.selectedItem == 2){
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(410, 680, 195, 40), 100, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        else{
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(605, 680, 195, 40), 100, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        g.setFont(this.fm.font19);
        FontMetrics fontMetrics = g.getFontMetrics(this.fm.font19);
        g.setColor(new Color(17, 184, 255));
        String cost = "" + this.damagesZones[0] * 50;
        int costw = fontMetrics.stringWidth(cost);
        g.drawString(cost, 250 - costw, AppDefines.SCREEN_HEIGHT - 54);
        
        if(this.selectedItem == 3){
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(410, 720, 195, 40), 302, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        else{
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(605, 720, 195, 40), 302, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        cost = "" + this.damagesZones[1] * 20;
        costw = fontMetrics.stringWidth(cost);
        g.drawString(cost, 452 - costw, AppDefines.SCREEN_HEIGHT - 54);
        
        if(this.selectedItem == 4){
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(410, 760, 195, 40), 505, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        else{
            g.drawImage(this.gm.spritesheetsGUI.getSubimage(605, 760, 195, 40), 505, AppDefines.SCREEN_HEIGHT - 80, null);
        }
        cost = "" + this.damagesZones[2] * 20;
        costw = fontMetrics.stringWidth(cost);
        g.drawString(cost, 655 - costw, AppDefines.SCREEN_HEIGHT - 54);
        
        if(this.displayError){
            g.setColor(new Color(183, 15, 23));
            g.setFont(this.fm.font24);
            fontMetrics = g.getFontMetrics(this.fm.font24);
            String msg = "You have not enouth money.";
            int msgw = fontMetrics.stringWidth(msg);
            g.drawString(msg, AppDefines.SCREEN_WIDTH/2 - msgw/2, this.backgroundPosY + 30);
        }
    }
}
