import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class S_Sniper extends Ship {

    private boolean charging = false, firing = false, ability = false;
    private int chargeTimer = 0, beamTimer = 0, beamLength = 0;
    private ArrayList<Enemy> hitEnemies = new ArrayList<>();
    private Rectangle beamBounds, megaBounds, enemyBounds;

    public S_Sniper() {
        super(200, 100, 84, 124, new ImageIcon("images\\ship\\sniper.png"), "Sniper Ship", 40, 80, 3, 5, 26,
                new W_Beam(200, 100), new A_Megabeam(200, 100));
    }

    public S_Sniper(int xV, int yV) {
        super(xV, yV, 84, 124, new ImageIcon("images\\ship\\sniper.png"), "Sniper Ship", 40, 80, 3, 5, 26,
                new W_Beam(xV, yV), new A_Megabeam(xV, yV));
    }

    public String getAttack() {
        return "Charges and fires a high damage beam";
    }

    public String getSpecial() {
        return "Piercing beam to hit multiple enemies";
    }

    @Override
    public void drawGameShip(Graphics g, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        String direction = getDirection();

        int cx = getX() + w / 2;
        int cy = getY() + h / 2;

        if (direction == "right") {
            g2d.rotate(Math.PI / 2, cx, cy);
        } else if (direction == "down") {
            g2d.rotate(Math.PI, cx, cy);
        } else if (direction == "left") {
            g2d.rotate(-Math.PI / 2, cx, cy);
        }

        g2d.drawImage(getPic().getImage(), getX(), getY(), w, h, null);

        int x = getX() + w / 2 - 7;
        int y = getY();

        W_Beam beam = (W_Beam) getWeapon();
        A_Megabeam mega = (A_Megabeam) getAbility();

        if (charging) {
            beam.drawCharge(g2d, x, y);
        } else if (beamTimer > 0) {
            if (ability) {
                mega.drawBeam(g2d, x - 2, y);
            } else {
                beam.drawBeam(g2d, x, y, beamLength);
            }
        }

        g2d.setTransform(old);
    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);
    }

    public void charge() {
        if (!charging && !ability && beamTimer == 0 && getAtkTimer() == 0) {
            charging = true;
            chargeTimer = 0;
        }
    }

    public void updateCharge() {
        if (charging && !ability) {
            chargeTimer++;
        }
    }

    public void release() {
        if (charging && !ability) {
            charging = false;
            firing = true;
            beamLength = Math.max(1, chargeTimer / 30);
            beamTimer = 100;
            chargeTimer = 0;
        }
    }

    public void updateBeam() {
        if (!ability && beamTimer > 0) {
            beamTimer--;
            if (beamTimer == 0) {
                beamLength = 0;
                firing = false;
                hitEnemies.clear();
                setAtkTimer(getAtkSpd() * 30);
            }
        }
    }

    public void megaBeam() {
        if (!charging && getAblTimer() == 0 && beamTimer == 0) {
            ability = true;
            beamTimer = 150;
        }
    }

    public void updateMegabeam() {
        if (!charging && ability && beamTimer > 0) {
            beamTimer--;
            if (beamTimer == 0) {
                beamLength = 0;
                ability = false;
                hitEnemies.clear();
                setAblTimer(getAblSpd() * 30);
            }
        }
    }

    public boolean checkCollision(Enemy enemy) {
        int bx = getX() + getW() / 2 - 7;
        int by = getY();
        String direction = getDirection();

        if (direction.equals("down")) {
            by = getY() + getH() - 32;
        } else if (direction.equals("left")) {
            bx = getX() - 22;
            by = getY() + getH() / 2 - 16;
        } else if (direction.equals("right")) {
            bx = getX() + getW() - 14;
            by = getY() + getH() / 2 - 16;
        }

        if (direction == "up") {
            beamBounds = new Rectangle(bx, by - beamLength * 44, 12, beamLength * 44);
        } else if (direction == "down") {
            beamBounds = new Rectangle(bx, by, 12, beamLength * 44);
        } else if (direction == "left") {
            beamBounds = new Rectangle(bx - beamLength * 44, by, beamLength * 44, 12);
        } else {
            beamBounds = new Rectangle(bx, by, beamLength * 44, 12);
        }

        enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH());

        if (beamBounds.intersects(enemyBounds) && !hitEnemies.contains(enemy)) {
            hitEnemies.add(enemy);
            return true;
        }
        return false;
    }

    public boolean checkMegaCollision(Enemy enemy) {
        int bx = getX() + getW() / 2 - 7;
        int by = getY();
        String direction = getDirection();

        if (direction == "down") {
            by = getY() + getH() - 32;
        } else if (direction == "left") {
            bx = getX() - 22;
            by = getY() + getH() / 2 - 16;
        } else if (direction == "right") {
            bx = getX() + getW() - 14;
            by = getY() + getH() / 2 - 16;
        }

        if (direction == "up") {
            megaBounds = new Rectangle(bx, by - 10 * 96, 14, 10 * 96);
        } else if (direction == "down") {
            megaBounds = new Rectangle(bx, by, 14, 10 * 96);
        } else if (direction == "left") {
            megaBounds = new Rectangle(bx - 10 * 96, by, 10 * 96, 14);
        } else {
            megaBounds = new Rectangle(bx, by, 10 * 96, 14);
        }

        enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH());

        if (megaBounds.intersects(enemyBounds) && !hitEnemies.contains(enemy)) {
            hitEnemies.add(enemy);
            return true;
        }
        return false;
    }

    public int getChargeTimer() {
        return chargeTimer;
    }

    public int getBeamLength() {
        return beamLength;
    }

    public int getBeamTimer() {
        return beamTimer;
    }

    public boolean isCharging() {
        return charging;
    }

    public boolean isFiring() {
        return firing;
    }

    public boolean isAbility() {
        return ability;
    }

    public boolean rotate() {
        return !isFiring() && !isAbility();
    }

}
