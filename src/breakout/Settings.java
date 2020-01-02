package breakout;

/**
 * Created by jason_000 on 5/26/2016.
 */
public interface Settings {

    // board
    public static final int WIDTH = 450;
    public static final int HEIGTH = 600;
    public static final int BOTTOM_EDGE = HEIGTH + 90;
    public static final int N_OF_BRICKS = 30;

    // paddle
    public static final int INIT_PADDLE_X = 200;
    public static final int INIT_PADDLE_Y = HEIGTH - 50;

    // ball
    public static final int INIT_BALL_X = 230;
    public static final int INIT_BALL_Y = 355;

    // game
    public static final int DELAY = 1000;
    public static final int PERIOD = 10;
}

