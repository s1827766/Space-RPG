import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Weapons {

    private int x, y, width, height;
    private ImageIcon pic;

    public Weapons() {
        x = 0;
        y = 0;
        width = 50;
        height = 50;
        pic = new ImageIcon("");
    }

    public Weapons(int x, int y, int width, int height, ImageIcon pic) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.pic = pic;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return width;
    }

    public int getH() {
        return height;
    }

    public ImageIcon getPic() {
        return pic;
    }

    public void setPic(ImageIcon pic) {
        this.pic = pic;
    }

    public void drawWeapon(Graphics g, int x, int y) {
        g.drawImage(pic.getImage(), x, y, width, height, null);
    }

    public boolean checkCollision(Ship ship) {
        Rectangle weaponBounds = new Rectangle(getX(), getY(), getW(), getH());
        Rectangle shipBounds = new Rectangle(ship.getX(), ship.getY(), ship.getW(), ship.getH());
        return weaponBounds.intersects(shipBounds);
    }
}
