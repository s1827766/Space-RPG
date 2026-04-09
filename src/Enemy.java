import javax.swing.ImageIcon;

public class Enemy extends Ship {

    private int fireTimer = 0;

    public Enemy() {
        super();
        fireTimer = (int) (Math.random() * 120);
    }

    public Enemy(int xV, int yV, int w, int h, ImageIcon p, int dmg, int hp, int spd, Weapons wpn) {
        super(xV, yV, w, h, p, dmg, hp, spd, wpn);
        fireTimer = (int) (Math.random() * 120);
    }

    public void move(int x, int y) {
        if (getX() + getW() / 2 < x) {
            setX(getX() + getSpeed());
        } else if (getX() + getW() / 2 > x)
            setX(getX() - getSpeed());
        if (getY() + getH() / 2 < y)
            setY(getY() + getSpeed());
        else if (getY() + getH() / 2 > y)
            setY(getY() - getSpeed());
        updateDirection(x, y);
    }

    public void updateFireTimer() {
        if (fireTimer > 0) {
            fireTimer--;
        } else {
            fireTimer = 150 + (int) (Math.random() * 120);
        }
    }

    public boolean canFire() {
        return fireTimer == 0;
    }

    public int getFireTimer() {
        return fireTimer;
    }

    public void setFireTimer(int timer) {
        fireTimer = timer;
    }
}
