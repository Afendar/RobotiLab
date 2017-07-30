package core;

public interface AppDefines {
    
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int TILE_SIZE = 64;
    public static final int GRAVITY = 1;
    public static final int SPEED = 2;
    public static final int MAX_VELOCITY = 6;
    public static final int MAX_GRAVITY = 10;
    public static final int NB_TILES_X = AppDefines.SCREEN_WIDTH / AppDefines.TILE_SIZE;
    public static final int NB_TILES_Y = AppDefines.SCREEN_HEIGHT / AppDefines.TILE_SIZE;
    public static final String SCREEN_TITLE = "LD39";
    public static final String VERSION = "0.1";
    
}
