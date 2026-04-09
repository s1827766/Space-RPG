import javax.swing.ImageIcon;

public class A_Nuke extends W_Missile {

    private int speed = 1;
    private Enemy target;

    public A_Nuke(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic, dx, dy, range);
    }

    public A_Nuke(int x, int y, int dx, int dy) {
        super(x, y, 45, 102, new ImageIcon("images\\ability\\nuke.png"), dx, dy, 500);
    }

    public A_Nuke(int x, int y, String direction) {
        super(x, y, 45, 102, new ImageIcon("images\\ability\\nuke.png"), 0, 0, 500);
        setSpeed(direction);
    }

    public void setTarget(Enemy enemy) {
        this.target = enemy;
    }

    @Override
    public void move() {
        if (target != null) {
            int tx = target.getX() + target.getW() / 2;
            int ty = target.getY() + target.getH() / 2;
            move(tx, ty, "");
            setRange(getRange() - 1);
        } else {
            super.move();
        }
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

    public void move(int x, int y, String direction) {
        if (getX() + getW() / 2 < x) {
            setX(getX() + speed);
        } else if (getX() + getW() / 2 > x)
            setX(getX() - speed);
        if (getY() + getH() / 2 < y)
            setY(getY() + speed);
        else if (getY() + getH() / 2 > y)
            setY(getY() - speed);
        updateDirection(x, y, direction);
    }

    public void updateDirection(int x, int y, String direction) {
        int dx = x - getX() - getW() / 2;
        int dy = y - getY() - getH() / 2;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                direction = "right";
            } else {
                direction = "left";
            }
        } else {
            if (dy > 0) {
                direction = "down";
            } else {
                direction = "up";
            }
        }
        setDirection(direction);
    }
}