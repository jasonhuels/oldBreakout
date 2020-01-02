package breakout;

/**
 * Created by jason_000 on 5/26/2016.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class Board extends JPanel implements Settings {

    private Timer timer;
    private String message = "Game Over";
    private Ball ball;
    private Paddle paddle;
    private Brick bricks[];
    private boolean ingame = true;
    private int score = 0;
    private int mult = 1;
    private ArrayList<String> scores;


    public Board() {
        HighscoreDB hsdb = new HighscoreDB();
        scores = hsdb.readCustomers();
        message += scores;
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setFocusable(true);

        bricks = new Brick[N_OF_BRICKS];
        setDoubleBuffered(true);
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), DELAY, PERIOD);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        gameInit();
    }

    private void gameInit() {

        ball = new Ball();
        paddle = new Paddle();

        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                bricks[k] = new Brick(j * 60 + 45, i * 20 + 50);
                k++;
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (ingame) {
            Font font = new Font("Verdana", Font.BOLD, 28);
            FontMetrics metr = this.getFontMetrics(font);

            g2d.setColor(Color.BLACK);
            g2d.setFont(font);
            g2d.drawString("Score: " + score,10,25);
            drawObjects(g2d);
        } else {

            gameFinished(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics2D g2d) {

        g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                ball.getWidth(), ball.getHeight(), this);
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                paddle.getWidth(), paddle.getHeight(), this);

        for (int i = 0; i < N_OF_BRICKS; i++) {
            if (!bricks[i].isDestroyed()) {
                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getWidth(),
                        bricks[i].getHeight(), this);
            }
        }
    }

    private void gameFinished(Graphics2D g2d) {

        Font font = new Font("Verdana", Font.BOLD, 28);
        FontMetrics metr = this.getFontMetrics(font);

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        g2d.drawString(message,
                (Settings.WIDTH - metr.stringWidth(message)) / 2,
                Settings.WIDTH / 2);
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);
        }
    }

    private class ScheduleTask extends TimerTask {

        @Override
        public void run() {

            ball.move();
            paddle.move();
            checkCollision();
            repaint();
        }
    }

    private void stopGame() {

        ingame = false;
        timer.cancel();
    }

    private void checkCollision() {

        if (ball.getMask().getMaxY() > Settings.BOTTOM_EDGE) {
            stopGame();
        }

        for (int i = 0, j = 0; i < N_OF_BRICKS; i++) {

            if (bricks[i].isDestroyed()) {
                j++;
            }

            if (j == N_OF_BRICKS) {
                message = "Victory";
                stopGame();
            }
        }

        if ((ball.getMask()).intersects(paddle.getMask())) {
            mult = 1;
            int paddleLPos = (int) paddle.getMask().getMinX();
            int ballLPos = (int) ball.getMask().getMinX();

            int first = paddleLPos + 16;
            int second = paddleLPos + 32;
            int third = paddleLPos + 48;
            int fourth = paddleLPos + 64;

            if (ballLPos < first) {
                ball.setXDir(-2);
                ball.setYDir(-2);
            }

            if (ballLPos >= first && ballLPos < second) {
                ball.setXDir(-2);
                ball.setYDir(-2 * ball.getYDir());
            }

            if (ballLPos >= second && ballLPos < third) {
                ball.setXDir(0);
                ball.setYDir(-2);
            }

            if (ballLPos >= third && ballLPos < fourth) {
                ball.setXDir(2);
                ball.setYDir(-2 * ball.getYDir());
            }

            if (ballLPos > fourth) {
                ball.setXDir(2);
                ball.setYDir(-2);
            }
        }

        for (int i = 0; i < N_OF_BRICKS; i++) {

            if ((ball.getMask()).intersects(bricks[i].getMask())) {

                int ballLeft = (int) ball.getMask().getMinX();
                int ballHeight = (int) ball.getMask().getHeight();
                int ballWidth = (int) ball.getMask().getWidth();
                int ballTop = (int) ball.getMask().getMinY();

                Point pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                Point pointLeft = new Point(ballLeft - 1, ballTop);
                Point pointTop = new Point(ballLeft, ballTop - 1);
                Point pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].isDestroyed()) {
                    if (bricks[i].getMask().contains(pointRight)) {
                        ball.setXDir(-2);
                    } else if (bricks[i].getMask().contains(pointLeft)) {
                        ball.setXDir(2);
                    }

                    if (bricks[i].getMask().contains(pointTop)) {
                        ball.setYDir(2);
                    } else if (bricks[i].getMask().contains(pointBottom)) {
                        ball.setYDir(-2);
                    }

                    bricks[i].setDestroyed(true);
                    score += 5*mult;
                    mult++;
                }
            }
        }
    }
}

