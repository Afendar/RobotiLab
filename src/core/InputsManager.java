package core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.im.InputContext;
import java.util.ArrayList;
import java.util.Locale;

public class InputsManager implements KeyListener, MouseMotionListener, MouseListener {

    public class Action{
        public boolean enabled, typed;
        public int pressCpt, absorbCpt;
        
        public Action(){
            actions.add(this);
        }
        
        public void switched(boolean enabled){
            if(enabled != this.enabled)
                this.enabled = enabled;
            if(enabled)
                this.pressCpt++;
        }
        
        public void update(){
            if(this.absorbCpt < this.pressCpt){
                this.absorbCpt++;
                this.typed = true;
            }
            else{
                this.typed = false;
            }
        }
    }
    
    private static InputsManager _instance = null;
    private boolean[] keys;
    protected ArrayList<Action> actions;
    public Action jump, left, right, fire, up, down, open, escape;
    private int mouseX, mouseY;
    public int mouseClickCount;
    public boolean mouseExited, mousePressed;
    private KeyEvent e;
    
    public static InputsManager getInstance(){
        if(_instance == null)
            _instance = new InputsManager();
        return _instance;
    }
    
    private InputsManager(){
        this.actions = new ArrayList<>();
        
        this.jump = new Action();
        this.left = new Action();
        this.up = new Action();
        this.down = new Action();
        this.right = new Action();
        this.fire = new Action();
        this.open = new Action();
        this.escape = new Action();
        
        this.actions = new ArrayList<>();
        this.mouseX = this.mouseY = this.mouseClickCount = 0;
        this.mouseExited = false;
        this.mousePressed = false;
        this.keys = new boolean[KeyEvent.KEY_LAST];
    }
    
    public void setGame(Game game){
        game.addKeyListener(this);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
    }
    
    public void update(){
        this.mouseClickCount = 0;
        this.actions.stream().forEach((action) -> {
            action.update();
        });
    }
    
    public void processKey(KeyEvent e, boolean enabled){
        
        InputContext context = InputContext.getInstance();
        if(context.getLocale().equals(Locale.FRENCH) || context.getLocale().equals(Locale.FRANCE)){
            if(e.getKeyCode() == KeyEvent.VK_Q)
                this.left.switched(enabled);
            else if(e.getKeyCode() == KeyEvent.VK_D)
                this.right.switched(enabled);
            if(e.getKeyCode() == KeyEvent.VK_Z)
                this.up.switched(enabled);
            else if(e.getKeyCode() == KeyEvent.VK_S)
                this.down.switched(enabled);
        }
        else{
            if(e.getKeyCode() == KeyEvent.VK_A)
                this.left.switched(enabled);
            else if(e.getKeyCode() == KeyEvent.VK_D)
                this.right.switched(enabled);
            if(e.getKeyCode() == KeyEvent.VK_W)
                this.up.switched(enabled);
            else if(e.getKeyCode() == KeyEvent.VK_S)
                this.down.switched(enabled);
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
            this.jump.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_E)
            this.open.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            this.escape.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_F){
            this.fire.switched(enabled);
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.processKey(e, true);
        this.e = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.processKey(e, false);
        this.e = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mousePressed = true;
        this.mouseClickCount = e.getClickCount();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.mouseExited = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseExited = true;
    }
    
    public int getMouseX(){
        return this.mouseX;
    }
    
    public int getMouseY(){
        return this.mouseY;
    }
}
