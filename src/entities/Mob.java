package entities;

import Particles.Bullet;
import Particles.TextParticle;
import audio.Sound;
import core.AppDefines;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import level.Level;

public abstract class Mob extends Entity {
    
    protected boolean moving;
    protected double timeMove, timer, timerAtk;
    protected ArrayList<Bullet> bullets;
    
    public Mob(Level level){
        this(0, 0, level);
    }
    
    public Mob(int posX, int posY, Level level){
        super(posX, posY, level);
        this.moving = false;
        this.timeMove = this.timer = this.timerAtk =0;
        this.bullets = new ArrayList<>();
        this.health = 100;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.posX, this.posY, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
    }

    @Override
    public void update(double dt) {
        
        this.timer += dt;
        
        Player p = this.level.getPlayer();
        int playerX = p.getPosX();
        int playerY = p.getPosY();
        if(playerX >= this.posX - (4*AppDefines.TILE_SIZE) && playerX <= this.posX - 3*AppDefines.TILE_SIZE && playerY >= this.posY - 40 && playerY <= this.posY + AppDefines.TILE_SIZE + 40){
            this.moving = true;
            this.timeMove = 0;
            this.velX -= AppDefines.GRAVITY;
            this.velX = Math.max(this.velX, -AppDefines.MAX_VELOCITY + 1);
            this.dir = LEFT;
        }
        else if(playerX <= this.posX + (4*AppDefines.TILE_SIZE) && playerX >= this.posX + 3*AppDefines.TILE_SIZE && playerY >= this.posY - 40 && playerY <= this.posY + AppDefines.TILE_SIZE + 40){
            this.moving = true;
            this.timeMove = 0;
            this.velX += AppDefines.GRAVITY;
            this.velX = Math.min(this.velX, AppDefines.MAX_VELOCITY - 1);
            this.dir = RIGHT;
        }
        else{
            this.moving = false;
            this.velX = 0;
            if(playerX >= this.posX - 3*AppDefines.TILE_SIZE && playerX <= this.posX + 3*AppDefines.TILE_SIZE &&
                    playerY >= this.posY - 40 && playerY <= this.posY + AppDefines.TILE_SIZE + 40){
                this.timerAtk += dt;
                if(this.timerAtk > 60){
                    this.timerAtk = 0;
                    new Thread(Sound.lazer[(int)Math.round(Math.random() * 1)]::play).start();
                    this.attack();
                }
            }
        }
        
        if(!this.moving && this.timeMove == 0){
            this.moving = rnd.nextBoolean();
            this.timeMove = rnd.nextInt(150 - 100) + 100;
        }
        
        if(this.moving && !this.dead){
            if(this.timer < this.timeMove){
                if(this.dir == LEFT){
                    this.velX -= AppDefines.GRAVITY;
                    this.velX = Math.max(this.velX, -AppDefines.MAX_VELOCITY);
                }
                else if(this.dir == RIGHT){
                    this.velX += AppDefines.GRAVITY;
                    this.velX = Math.min(this.velX, AppDefines.MAX_VELOCITY);
                }
                else{
                    this.velX = 0;
                }
            }
            this.move();
        }
        
        for(int i=0;i<this.bullets.size();i++){
            Bullet bullet = this.bullets.get(i);
            Rectangle playerHitbox = p.getBounds();
            if(bullet.x > playerHitbox.x && bullet.x < playerHitbox.x + playerHitbox.width &&
                bullet.y > playerHitbox.y && bullet.y < playerHitbox.y + playerHitbox.height){
                p.hit(5);
                this.bullets.remove(i);
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
    }

    @Override
    public void render(Graphics g) {
        
        this.textParticles.stream().forEach((tp) -> {
            tp.render(g);
        });
        
    }
    
    public void attack(){
        int vx = 0;
        int vy = 0;
        int playerX = this.level.getPlayer().getPosX();
        if(this.posX <= playerX){
            vx = AppDefines.MAX_VELOCITY + 2;
        }
        else if(this.posX > playerX){
            vx = -AppDefines.MAX_VELOCITY - 2;
        }
        
        Bullet b = new Bullet(
            (vx > 0)?this.posX + AppDefines.TILE_SIZE:this.posX,
            this.posY + 32,
            vx,
            vy,
            250
        );
        this.bullets.add(b);
    }
    
    public void move(){
        this.posX += this.velX;
        
        if(this.dir == RIGHT){
            int x = (int)(this.posX + 59) / AppDefines.TILE_SIZE;
            int y = (int)(this.posY + ((AppDefines.TILE_SIZE - 1) * 2)) / AppDefines.TILE_SIZE;
            if(this.level.getTile(x, y + 1).ID == 0){
                this.dir = LEFT;
            }
        }
        else if(this.dir == LEFT){
            int x = (int)(this.posX + 25) / AppDefines.TILE_SIZE;
            int y = (int)(this.posY + ((AppDefines.TILE_SIZE - 1) * 2)) / AppDefines.TILE_SIZE;
            if(this.level.getTile(x, y + 1).ID == 0){
                this.dir = RIGHT;
            }
        }
        
        if(this.posX < 0){
            this.posX = 0;
            this.dir = RIGHT;
        }
        else if(this.posX + 84 > this.level.getWidth()){
            this.posX = this.level.getWidth() - 84;
            this.dir = LEFT;
        }
    }
    
    public void die(){
        this.level.getPlayer().addToScore(50);
        this.dead = true;
    }
}
