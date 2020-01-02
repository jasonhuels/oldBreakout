package breakout;

/**
 * Created by jason_000 on 5/26/2016.
 */
import javax.swing.ImageIcon;

public class Brick extends Sprite {

    private boolean destroyed;

    public Brick(int x, int y) {

        this.x = x;
        this.y = y;

        image = new ImageIcon("brick.png").getImage();

        i_width = image.getWidth(null);
        i_height = image.getHeight(null);

        destroyed = false;
    }

    public boolean isDestroyed() {

        return destroyed;
    }

    public void setDestroyed(boolean val) {

        destroyed = val;
    }
}

