import javax.swing.ImageIcon;

public class W_Blast extends Ranged {

    private int speed = 5;

    public W_Blast(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic, dx, dy, range);
    }

    public W_Blast(int x, int y, int dx, int dy) {
        super(x, y, 3, 24, new ImageIcon("images\\weapon\\blast.png"), dx, dy, 150);
    }

    public W_Blast(int x, int y, String direction) {
        super(x, y, 3, 24, new ImageIcon("images\\weapon\\blast.png"), 0, 0, 150);
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
