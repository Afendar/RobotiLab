package ld39;

import core.AppDefines;
import core.Game;
import core.TimerThread;
import java.awt.Dimension;
import javax.swing.JFrame;

public class LD39 {

    public static void main(String[] args) {
        TimerThread timer = new TimerThread();
        timer.start();
        Game g = Game.getInstance();
        g.setScreenSize(AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT);
        
        JFrame f = new JFrame(AppDefines.SCREEN_TITLE + " : v" + AppDefines.VERSION);
        f.add(g);
        f.getContentPane().setPreferredSize(new Dimension(AppDefines.SCREEN_WIDTH, AppDefines.SCREEN_HEIGHT));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLayout(null);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);

        g.start();
    }
}
