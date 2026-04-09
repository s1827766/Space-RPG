import javax.swing.ImageIcon;

public class W_Missile extends Ranged {

    private int speed = 2;

    public W_Missile(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic, dx, dy, range);
    }

    public W_Missile(int x, int y, int dx, int dy) {
        super(x, y, 24, 60, new ImageIcon("images\\weapon\\missile.png"), dx, dy, 300);
    }

    public W_Missile(int x, int y, String direction) {
        super(x, y, 24, 60, new ImageIcon("images\\weapon\\missile.png"), 0, 0, 300);
        setSpeed(direction);
    }

    public void setSpeed(String direction) {
        if (direction == "left") {
            setDx(-speed);
            setDy(0);
        } else if (direction == "right") {
            setDx(speed);
            setDy(0);
        } else if (direction == "up") {
            setDx(0);
            setDy(-speed);
        } else if (direction == "down") {
            setDx(0);
            setDy(speed);
        }
    }
}
