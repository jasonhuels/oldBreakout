package breakout;

/**
 * Created by jason_000 on 5/26/2016.
 */
import javax.swing.ImageIcon;

public class Ball extends Sprite implements Settings {

    private int xdir;
    private int ydir;

    public Ball() {

        xdir = 0;
        ydir = 1;

        ImageIcon ii = new ImageIcon("ball.png");
        image = ii.getImage();

        i_width = image.getWidth(null);
        i_height = image.getHeight(null);

        resetState();
    }

    public void move() {

        x += xdir;
        y += ydir;

        if (x <= 0) {
            setXDir(2);
        }

        if (x >= WIDTH - i_width) {
            setXDir(-2);
        }

        if (y <= 0) {
            setYDir(2);
        }
    }

    private void resetState() {

        x = INIT_BALL_X;
        y = INIT_BALL_Y;
    }

    public void setXDir(int x) {
        xdir = x;
    }

    public void setYDir(int y) {
        ydir = y;
    }

    public int getYDir() {
        return ydir;
    }
}
