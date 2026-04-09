import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Ranged extends Weapons {

    private int dx, dy, range, explosionTimer = 0;
    public int ex, ey, radius = 80;
    public boolean exploding = false;
    private String direction = "up";
    private ImageIcon explosion = new ImageIcon("images\\weapon\\explosion.gif");
    public ArrayList<Ship> hitEnemies = new ArrayList<>();

    public Ranged(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic);
        this.dx = dx;
        this.dy = dy;
        this.range = range;
    }

    public Ranged() {
        super();
    }

    public void move() {
        setX(getX() + getDx());
        setY(getY() + getDy());
        range--;
    }

    public void drawWeapon(Graphics g, int x, int y) {
        if (exploding) {
            dx = 25;
            dy = 34;
            int w = 75;
            int h = 69;
            if (this instanceof A_Nuke) {
                dx = 60;
                dy = 69;
                w = 150;
                h = 138;
            }
            g.drawImage(explosion.getImage(), x - dx, y - dy, w, h, null);
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();

        int cx = x + getW() / 2;
        int cy = y + getH() / 2;

        if (direction.equals("right")) {
            g2d.rotate(Math.PI / 2, cx, cy);
        } else if (direction.equals("down")) {
            g2d.rotate(Math.PI, cx, cy);
        } else if (direction.equals("left")) {
            g2d.rotate(-Math.PI / 2, cx, cy);
        }

        g2d.drawImage(getPic().getImage(), x, y, getW(), getH(), null);
        g2d.setTransform(old);
    }

    public void explode() {
        exploding = true;
        explosionTimer = 100;
        explosion.getImage().flush();
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDirection(String d) {
        direction = d;
    }

    public String getDirection() {
        return direction;
    }

    public int getExplosionTimer() {
        return explosionTimer;
    }

    public void setExplosionTimer(int t) {
        this.explosionTimer = t;
    }
}
