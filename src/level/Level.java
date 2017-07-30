package level;

import core.AppDefines;
import core.GraphicsManager;
import entities.Mob;
import entities.MyjaBot;
import entities.Player;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import entities.RepairStation;
import java.awt.Rectangle;
import level.tiles.Tile;
import level.tiles.TileAtlas;

public class Level {
    
    private int width, height, numLevel, startPosY, nbTilesY, nbTilesX;
    private Player player;
    private int[][] map, data, bgMap;
    private ArrayList<RepairStation> repairStations;
    private ArrayList<Mob> enemies;
    private BufferedImage[] backgroundsSprites;
    
    public final int BLACK = 0;//RGB(0, 0, 0)
    public static final int GREEN = 2273612; //RGB(34, 177, 76)
    public static final int YELLOW = 16773632; //RGB(255, 242, 0)
    public static final int RED = 15539236;//RGB(237, 28, 36)
    public static final int WHITE = 16777215; //RGB(255, 255, 255)
    public static final int BLUE = 4147404;//RGB()
    
    public Level(int numLevel){
        this.numLevel = numLevel;
        this.startPosY = 10 * AppDefines.TILE_SIZE;
        
        this.repairStations = new ArrayList<>();
        this.enemies = new ArrayList<>();
        
        if(!this.loadLevel()){
            System.out.println("Unable to load level " + this.numLevel);
        }
        
        GraphicsManager gm = GraphicsManager.getInstance();
        
        this.backgroundsSprites = new BufferedImage[9];
        int index = 0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                this.backgroundsSprites[index] = gm.spritesheetsTerrain.getSubimage(i * AppDefines.TILE_SIZE, j * AppDefines.TILE_SIZE, AppDefines.TILE_SIZE, AppDefines.TILE_SIZE);
                index++;
            }
        }
        
        this.bgMap = new int[this.nbTilesX][this.nbTilesY];
        for (int[] bgMap1 : this.bgMap) {
            for (int j = 0; j < bgMap1.length; j++) {
                bgMap1[j] = (int)Math.round(Math.abs((Math.random() * 8) - 3));
            }
        }
    }
    
    public boolean loadLevel(){
        boolean result = false;
        result = this.loadFirstLayer();
        return result;
    }
    
    public boolean loadFirstLayer(){
        try{
            URL url = this.getClass().getResource("/levels/lvl" + this.numLevel + ".png");
            BufferedImage lvlImg = ImageIO.read(url);
            
            byte[] pixels = ((DataBufferByte) lvlImg.getRaster().getDataBuffer()).getData();
            int w = lvlImg.getWidth();
            int h = lvlImg.getHeight();
            this.width = w * AppDefines.TILE_SIZE;
            this.height = h * AppDefines.TILE_SIZE;
            this.nbTilesY = h;
            this.nbTilesX = w;
            
            this.map = new int[w][h];
            this.data = new int[w][h];
            
            for (int[] data1 : this.data) {
                for (int j = 0; j < data1.length; j++) {
                    data1[j] = 0;
                }
            }
            
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += 0; // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                
                //System.out.println(argb);
                
                switch(argb){
                    case WHITE:
                        map[col][row] = TileAtlas.empty.ID;
                        break;
                    case BLACK:
                        map[col][row] = TileAtlas.floor.ID;
                        break;
                    case GREEN:
                        this.repairStations.add(new RepairStation(col * AppDefines.TILE_SIZE, row * AppDefines.TILE_SIZE, this));
                        break;
                    case YELLOW:
                        map[col][row] = TileAtlas.powerStation.ID;
                        break;
                    case BLUE:
                        this.map[col][row] = TileAtlas.cristal.ID;
                        break;
                    case RED:
                        this.enemies.add(new MyjaBot(col * AppDefines.TILE_SIZE, row * AppDefines.TILE_SIZE, this));
                        break;
                }
                
                col++;
                if (col >= w) {
                   col = 0;
                   row++;
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        return true;
    }
    
    public void addPlayer(Player p){
        this.player = p;
        this.player.setPosY(this.startPosY);
    }
    
    public void update(double dt, int startX, int startY){
        
        for(int i=0;i<this.enemies.size();i++){
            Mob enemy = this.enemies.get(i);
            enemy.update(dt);
            if(enemy.isDead()){
                this.enemies.remove(i);
            }
        }
        
    }
    
    public void renderFirstLayer(Graphics g, int startX, int startY){
        int endX = (startX + AppDefines.NB_TILES_X + 2 <= this.nbTilesX) ? startX + AppDefines.NB_TILES_X + 2 : this.nbTilesX;
        int endY = (startY + AppDefines.NB_TILES_Y + 2 <= this.nbTilesY) ? startY + AppDefines.NB_TILES_Y + 2 : this.nbTilesY;
        
        for(int i=0;i<endX;i++){
            for(int j=0;j<endY;j++){
                
                g.drawImage(this.backgroundsSprites[this.bgMap[i][j]], i * AppDefines.TILE_SIZE, j * AppDefines.TILE_SIZE, null);
                
                switch(this.map[i][j]){
                    case 1:
                        boolean left = false;
                        boolean right = false;
                        boolean top = false;
                        boolean bottom = false;
                        if(i > 0)
                            if(this.map[i-1][j] == 1)
                                left = true;
                        if(i < this.nbTilesX - 1)
                            if(this.map[i + 1][j] == 1)
                                right = true;
                        if(j > 0)
                            if(this.map[i][j-1] == 1)
                                top = true;
                        if(j < this.nbTilesY - 1)
                            if(this.map[i][j + 1] == 1)
                                bottom = true;
                        TileAtlas.floor.render(g, i, j, left, right, top, bottom);
                        break;
                    case 2:
                        TileAtlas.powerStation.render(g, i, j);
                        break;
                    case 3:
                        TileAtlas.cristal.render(g, i, j);
                        break;
                }
            }
        }
        
        this.repairStations.stream().forEach((repairStation) -> {
            repairStation.render(g);
        });
        
        this.enemies.stream().forEach((enemy) -> {
            enemy.render(g);
        });
    }
    
    public int getWidth(){
        return this.width;
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public Tile getTile(int x, int y){
        x = Math.round(x / AppDefines.TILE_SIZE);
        y = Math.round(y / AppDefines.TILE_SIZE);
        
        if(x < 0 || x >= this.nbTilesX || y < 0 || y >= this.nbTilesY){
            return TileAtlas.floor;
        }
        
        return TileAtlas.atlas.get(this.map[x][y]);
    }
    
    public boolean getRepairStation(Rectangle r){
        return this.repairStations.stream().anyMatch((rs) -> (rs.getBounds().intersects(r)));
    }
    
    public Player getPlayer(){
        return this.player;
    }
    
    public ArrayList<Mob> getMobs(Rectangle r){
        ArrayList<Mob>result = new ArrayList<>();
        this.enemies.stream().forEach((m) -> {
            Rectangle r2 = m.getBounds();
            if (r.intersects(r2)) {
                result.add(m);
            }
        });
        return result;
    }
}
