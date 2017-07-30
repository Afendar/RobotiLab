package level.tiles;

import java.util.ArrayList;

public class TileAtlas {
    
    public static ArrayList<Tile> atlas = new ArrayList<>();
    
    public static Empty empty = new Empty(0, 0);
    public static Floor floor = new Floor(0, 0);
    public static PowerStation powerStation = new PowerStation(1, 0);
    public static Cristal cristal = new Cristal(0, 0);
}
