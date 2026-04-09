import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class S_Fighter extends Ship {

    private boolean shield = false, shieldBroken = false;
    private int shieldTimer = 0;

    public S_Fighter() {
        super(100, 100, 132, 128, new ImageIcon("images\\ship\\fighter.png"), "Fighter Ship", 10, 100, 4, 1, 24,
                new W_Blast(100, 100, 0, 0), new A_Shield(100, 100));
    }

    public S_Fighter(int xV, int yV) {
        super(xV, yV, 132, 128, new ImageIcon("images\\ship\\fighter.png"), "Fighter Ship", 10, 100, 4, 1, 24,
                new W_Blast(xV, yV, 0, 0), new A_Shield(xV, yV));
    }

    public String getAttack() {
        return "Fires rapid single blaster shots";
    }

    public String getSpecial() {
        return "Temporary shield to block damage";
    }

    @Override
    public void drawGameShip(Graphics g, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        String direction = getDirection();

        int cx = getX() + getW() / 2;
        int cy = getY() + getH() / 2;

        AffineTransform old = g2d.getTransform();

        if (direction.equals("right")) {
            g2d.rotate(Math.PI / 2, cx, cy);
        } else if (direction.equals("down")) {
            g2d.rotate(Math.PI, cx, cy);
        } else if (direction.equals("left")) {
            g2d.rotate(-Math.PI / 2, cx, cy);
        }

        g2d.drawImage(getPic().getImage(), getX(), getY(), w, h, null);

        if (shield && !shieldBroken) {
            g.drawImage(new ImageIcon("images\\ability\\shield.png").getImage(), getX() - 17, getY() - 18, 132, 132,
                    null);
        }

        g2d.setTransform(old);
    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);
    }

    public void activateShield() {
        if (!shield && getAblTimer() == 0) {
            shield = true;
            shieldBroken = false;
            shieldTimer = 1500;
        }
    }

    public void updateShield() {
        if (shield && shieldTimer > 0) {
            shieldTimer--;
            if (shieldTimer == 0) {
                shield = false;
                setAblTimer(getAblSpd() * 30);
            }
        }
    }

    public boolean isShieldActive() {
        return shield && !shieldBroken;
    }

    public void breakShield() {
        shieldBroken = true;
        shield = false;
        setAblTimer(getAblSpd() * 30);
    }

}
