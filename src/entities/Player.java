package entities;

import Particles.Bullet;
import Particles.TextParticle;
import audio.Sound;
import core.AppDefines;
import core.Camera;
import core.Game;
import core.InputsManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import level.Level;
import level.tiles.Tile;
import level.tiles.TileAtlas;
import scene.RepairScene;

public class Player extends Entity {
    
    private int battery, offset;
    private Camera cam;
    private InputsManager inputsManager;
    private Game game;
    private boolean jump, fall;
    public boolean hasCristal;
    private double timer, timerMove, timerLoading, timerAttack, timeAnim;
    protected ArrayList<Bullet> bullets;
    protected int score;
    public int damagesZones[];
    private BufferedImage sprite;
    public String deathMsg;

    public Player(Level level){
        this(0, 0, level);
    }
    
    public Player(int posX, int posY, Level level){
        super(posX, posY, level);
        
        this.battery = this.health = 100;
        this.jump = this.fall = false;
        this.timer = this.timerMove = this.timerLoading = this.timerAttack = 0;
        this.bullets = new ArrayList<>();
        this.dir = RIGHT;
        this.score = 0;
        this.timeAnim = 0;
        this.offset = 0;
        this.damagesZones = new int[3];
        this.sprite = this.gm.characterTileset.getSubimage(0, 0, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
    }
    
    public void addGame(Game game){
        this.game = game;
    }
    
    public void addInputsManager(InputsManager inputsManager){
        this.inputsManager = inputsManager;
    }
    
    public void setCamera(Camera cam){
        this.cam = cam;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.posX, this.posY, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
    }

    @Override
    public void update(double dt) {
        
        this.timer += dt;
        
        if(this.fall || this.jump){
            this.velY += AppDefines.GRAVITY;
            this.velY = Math.min(this.velY, AppDefines.MAX_GRAVITY);
        }
        
        if(this.inputsManager.jump.enabled && !this.jump && !this.fall){
            this.jump = true;
            new Thread(Sound.jump[(int)Math.round(Math.random() * 1)]::play).start();
            this.velY = -16;
        }
        
        if(this.inputsManager.open.enabled){
            if(this.level.getRepairStation(this.getBounds())){
                new Thread(Sound.select::play).start();
                RepairScene rs = new RepairScene(AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT, this.game, this);
                this.game.addScene(rs);
            }
        }
        
        this.timerAttack += dt;
        if(this.inputsManager.fire.enabled){
            if(this.timerAttack > 15){
                new Thread(Sound.lazer[(int)Math.round(Math.random() * 1)]::play).start();
                this.timerAttack = 0;
                this.attack();
            }
        }
        
        if(this.inputsManager.left.enabled){
            this.velX -= AppDefines.SPEED;
            this.velX = Math.max(this.velX, -AppDefines.MAX_VELOCITY);
            this.timerMove += dt;
            this.dir = LEFT;
        }
        else if(this.inputsManager.right.enabled){
            this.velX += AppDefines.SPEED;
            this.velX = Math.min(this.velX, AppDefines.MAX_VELOCITY);
            this.timerMove += dt;
            this.dir = RIGHT;
        }
        else{
            this.velX = 0;
        }
        
        //check for falling
        Tile tileDownX0 = this.level.getTile(this.posX, this.posY + this.getBounds().height + this.velY);
        Tile tileDownX1 = this.level.getTile(this.posX + this.getBounds().width, this.posY + this.getBounds().height + this.velY);
        if(tileDownX0.canPass(level, posX, posY) && tileDownX1.canPass(level, posX, posY) && !this.jump){
            this.fall = true;
        }
        
        //check for recharge power
        Tile tileUpX0 = this.level.getTile(this.posX, this.posY + AppDefines.TILE_SIZE/2);
        Tile tileUpX1 = this.level.getTile(this.posX + this.getBounds().width, this.posY + AppDefines.TILE_SIZE/2);
        if(tileUpX0.ID == TileAtlas.powerStation.ID || tileUpX1.ID == TileAtlas.powerStation.ID){
            this.timerLoading += dt;
            if(this.timerLoading > 70){
                this.timerLoading = 0;
                this.battery += 5;
                if(this.battery > 100){
                    this.battery = 100;
                    this.timerMove = 0;
                    this.timer = 0;
                }
            }
        }
        else{
            if(this.timerMove > 340){
                this.timerMove = 0;
                this.battery -= 10;
            }
            else if(this.timer > 540){
                this.timer = 0;
                this.battery -= 5;
            }
        }
        
        if(tileUpX0.ID == TileAtlas.cristal.ID || tileUpX1.ID == TileAtlas.cristal.ID){
            this.hasCristal = true;
        }
        
        if(this.battery <= 0){
            this.die();
            this.deathMsg = "You are run out of battery";
        }
        
        if(this.checkCollisionsX()){
            if(this.inputsManager.up.enabled){
                this.velY -= AppDefines.SPEED;
                this.velY = Math.max(this.velY, -AppDefines.MAX_VELOCITY);
                this.timerMove += dt;
            }
            else if(this.inputsManager.down.enabled){
                this.velY += AppDefines.SPEED;
                this.velY = Math.min(this.velY, AppDefines.MAX_VELOCITY);
                this.timerMove += dt;
            }
            else{
                this.velY = 0;
            }
        }
        for(int i=0;i<this.bullets.size();i++){
            Bullet bullet = this.bullets.get(i);
            ArrayList<Mob> mobs = this.level.getMobs(new Rectangle(this.cam.x, this.cam.y, this.cam.w, this.cam.h));
            if(mobs.size() > 0){
                for (Mob m : mobs) {
                    Rectangle r = m.getBounds();
                    if(bullet.x > r.x && bullet.x < r.x + r.width &&
                            bullet.y > r.y && bullet.y < r.y + r.height){
                        if(!m.dead)
                            m.hit(10);
                        this.bullets.remove(i);
                    }
                    else{
                        bullet.update(dt);
                        if(bullet.isDead())
                            this.bullets.remove(i);
                    }
                }
            }
            else{
                bullet.update(dt);
                if(bullet.isDead())
                    this.bullets.remove(i);
            }
        }
        
        for(int i=0;i<this.textParticles.size();i++){
            TextParticle tp = this.textParticles.get(i);
            tp.update(dt);
            if(tp.isDead())
                this.textParticles.remove(i);
        }
        
        if(this.velX != 0){
            this.timeAnim += dt;
            if(this.timeAnim >= 10){
                this.timeAnim = 0;
                this.offset++;
                if(this.offset > 3)
                    this.offset = 0;
                this.sprite = this.gm.characterTileset.getSubimage(this.offset * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
                if(this.dir == RIGHT){
                    AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                    tx.translate(-this.sprite.getWidth(null), 0);
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    this.sprite = op.filter(this.sprite, null);
                }
            }
        }
        
        this.move();
    }

    @Override
    public void render(Graphics g) {
        this.textParticles.stream().forEach((tp) -> {
            tp.render(g);
        });
        
        this.bullets.stream().forEach((bullet) -> {
            bullet.render(g);
        });
        
        g.drawImage(this.sprite, this.posX, this.posY, null);
    }
    
    public void move(){
        if(!this.checkCollisionsX()){
            this.posX += this.velX;
        }
        if(!this.checkCollisionsY()){
            this.posY += this.velY;
        }
        else{
            if(this.velY == 0){
                this.jump = false;
                this.fall = false;
            }
        }
    }
    
    public boolean checkCollisionsY(){
        //check up
        Tile tileUpX0 = this.level.getTile(this.posX + 1, this.posY + this.velY);
        Tile tileUpX1 = this.level.getTile(this.posX + this.getBounds().width - 1, this.posY + this.velY);
        if(!tileUpX0.canPass(this.level, this.posX, this.posY) || !tileUpX1.canPass(level, this.posX, this.posY)){
            //System.out.println("collision up");
            this.velY += 2;
            this.velY = Math.min(this.velY, 0);
            return true;
        }
        
        //check down
        Tile tileDownX0 = this.level.getTile(this.posX + 1, this.posY + this.getBounds().height + this.velY);
        Tile tileDownX1 = this.level.getTile(this.posX + this.getBounds().width - 1, this.posY + this.getBounds().height + this.velY);
        if(!tileDownX0.canPass(this.level, this.posX, this.posY + this.getBounds().height) || !tileDownX1.canPass(this.level, this.posX, this.posY + this.getBounds().height)){
            //System.out.println("collision down");
            this.velY -= 2;
            this.velY = Math.max(this.velY, 0);
            return true;
        }
        return false;
    }
    
    public boolean checkCollisionsX(){
        //check left
        Tile tileLeftY0 = this.level.getTile(this.posX + this.velX, this.posY + 1);
        Tile tileLeftY1 = this.level.getTile(this.posX + this.velX, this.posY + this.getBounds().height - 1);
        if(!tileLeftY0.canPass(this.level, this.posX, this.posY) || !tileLeftY1.canPass(level, this.posX, this.posY)){
            //System.out.println("collision left");
            this.velX += 1;
            this.velX = Math.min(this.velX, 0);
            return true;
        }
        
        //check right
        Tile tileRightY0 = this.level.getTile(this.posX + this.getBounds().width + this.velX, this.posY + 1);
        Tile tileRightY1 = this.level.getTile(this.posX + this.getBounds().width + this.velX, this.posY + this.getBounds().height - 1);
        if(!tileRightY0.canPass(this.level, this.posX  + this.getBounds().width, this.posY) || !tileRightY1.canPass(this.level, this.posX  + this.getBounds().width, this.posY)){
            //System.out.println("collision right");
            this.velX -= 1;
            this.velX = Math.max(this.velX, 0);
            return true;
        }
        return false;
    }
    
    public void attack(){
        int vx = 0;
        int vy = 0;
        if(this.dir == RIGHT){
            vx = AppDefines.MAX_VELOCITY + 2;
        }
        else if(this.dir == LEFT){
            vx = -AppDefines.MAX_VELOCITY - 2;
        }
        
        Bullet b = new Bullet(
            (vx > 0)?this.posX + AppDefines.TILE_SIZE:this.posX,
            this.posY + 32,
            vx,
            vy,
            250
        );
        b.setColor(new Color(17, 184, 255));
        this.bullets.add(b);
    }
    
    public int getBattery(){
        return this.battery;
    }
    
    public int getHealth(){
        return this.health;
    }
    
    public void removeToScore(int paid){
        this.score -= paid;
        this.score = Math.max(this.score, 0);
    }
    
    public void addToScore(int gain){
        this.score += gain;
    }
    
    public int[] getDamedZones(){
        return this.damagesZones;
    }
    
    public int getScore(){
        return this.score;
    }
    
    public void hit(int dammages){
        this.health -= dammages;
        
        this.damagesZones[(int)Math.round(Math.random() * 2)]++;
        
        this.textParticles.add(new TextParticle(""+dammages, this.posX + AppDefines.TILE_SIZE/2, this.posY + AppDefines.TILE_SIZE/2, new Color(17, 184, 255)));
        
        if(this.health <= 0){
            new Thread(Sound.explosion::play).start();
            this.deathMsg = "You are destroyed";
            this.die();
        }
        else{
            new Thread(Sound.hit[(int)Math.round(Math.random() * 1)]::play).start();
        }
    }
    
    public void addHealth(int hp){
        this.health += hp;
        this.health = Math.min(this.health, 100);
    }
}
