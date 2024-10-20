import java.awt.Image;

import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
        Image icon = new ImageIcon(App.class.getResource("./img/astronaut.png")).getImage();

        JFrame frame = new JFrame("Astro Escape Game");
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setIconImage(icon);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AstroEscape flappyBird = new AstroEscape();
        frame.add(flappyBird);
        frame.pack();
        frame.requestFocus();
        frame.setVisible(true);
    }
}
