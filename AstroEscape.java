import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class AstroEscape extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image BgImg;
    Image astronautImg;
    Image stoneImg;

    // Astronaut
    int astronautX = boardWidth / 8;
    int astronautY = boardHeight / 2;
    int astronautWidth = 56;
    int astronautHeight = 46;

    public class Astronaut {
        int x = astronautX;
        int y = astronautY;
        int width = astronautWidth;
        int height = astronautHeight;
        Image img;

        Astronaut(Image img) {
            this.img = img;
        }
    }

    // Stones
    int stoneWidth = 40;
    ArrayList<Stone> stones;

    public class Stone {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean passed = false;

        Stone(Image img, int width, int height) {
            this.img = img;
            this.width = width;
            this.height = height;
        }
    }

    // Logic
    Astronaut astronaut;
    int velocityX = -4; // Move stones to the left
    int velocityY = 0; // Move astronaut up and down speed
    int gravity = 1;

    Timer gameLoop;
    Timer placeStonesTimer;
    boolean gameOver = false;
    double score = 0;

    AstroEscape() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        BgImg = new ImageIcon(getClass().getResource("./img/FlappyAstronautBG.jpg")).getImage();
        astronautImg = new ImageIcon(getClass().getResource("./img/astronaut.png")).getImage();
        stoneImg = new ImageIcon(getClass().getResource("./img/stone.png")).getImage();

        astronaut = new Astronaut(astronautImg);
        stones = new ArrayList<>();

        placeStonesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeStones();
            }
        });
        placeStonesTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    public void placeStones() {
        int randomHeight = 20 + (int) (Math.random() * 100);
        Stone stone = new Stone(stoneImg, stoneWidth, randomHeight);
        stone.x = boardWidth;
        stone.y = (int) (Math.random() * (boardHeight - randomHeight));
        stones.add(stone);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Background
        g.drawImage(BgImg, 0, 0, boardWidth, boardHeight, null);

        // Astronaut
        g.drawImage(astronaut.img, astronaut.x, astronaut.y, astronaut.width, astronaut.height, null);

        // Stones
        for (Stone stone : stones) {
            g.drawImage(stone.img, stone.x, stone.y, stone.width, stone.height, null);
        }

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
            g.drawString("Press Space to Play Again!", 15, boardHeight / 2);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        // Astronaut
        velocityY += gravity;
        astronaut.y += velocityY;
        astronaut.y = Math.max(astronaut.y, 0);

        // Move stones
        for (int i = 0; i < stones.size(); i++) {
            Stone stone = stones.get(i);
            stone.x += velocityX;

            if (!stone.passed && astronaut.x > stone.x + stone.width) {
                stone.passed = true;
                score += 1;
            }

            if (colision(astronaut, stone)) {
                gameOver = true;
            }
        }

        stones.removeIf(stone -> stone.x + stone.width < 0);

        if (astronaut.y > boardHeight) {
            gameOver = true;
        }
    }

    public boolean colision(Astronaut a, Stone b) {
        return a.x + a.width > b.x &&
                a.x < b.x + b.width &&
                a.y + a.height > b.y &&
                a.y < b.y + b.height;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if (gameOver) {
            placeStonesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            velocityY = -9;
            if (gameOver) {
                astronaut.y = astronautY;
                velocityY = 0;
                stones.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placeStonesTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}
