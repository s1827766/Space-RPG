import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Rectangle;
import java.util.ArrayList;

public class E_Sniper extends Enemy {

    private boolean charging = false, firing = false;
    private int chargeTimer = 0, beamTimer = 0, beamLength = 0;
    private int maxChargeTime = 0;
    private ArrayList<Ship> hitEnemies = new ArrayList<>();
    private Rectangle beamBounds;
    private Sound p = new Sound();

    public E_Sniper(int xV, int yV) {
        super(xV, yV, 72, 64, new ImageIcon("images\\ship\\enemySniper.png"), 10, 100, 2,
                new W_Beam(xV, yV, 16, 56, new ImageIcon("images\\weapon\\enemyBeam.gif")));
    }

    public void charge() {
        if (!charging && !firing && beamTimer == 0) {
            charging = true;
            chargeTimer = 0;
            maxChargeTime = 100 + (int) (Math.random() * 200);
            p.playmusic("sounds\\mus_sfx_spellcast.wav", 0.70f);
        }
    }

    public void updateCharge() {
        if (charging) {
            chargeTimer++;
            if (chargeTimer >= maxChargeTime) {
                firing = true;
                charging = false;
                beamLength = Math.max(1, chargeTimer / 30);
                beamTimer = 120;
                chargeTimer = 0;
                setFireTimer(300 + (int) (Math.random() * 120));
                p.playmusic("sounds\\mus_sfx_star.wav", 0.70f);
            }
        }
    }

    public void updateBeam() {
        if (beamTimer > 0) {
            beamTimer--;
            if (beamTimer == 0) {
                firing = false;
                beamLength = 0;
                hitEnemies.clear();
            }
        }
    }

    public boolean checkCollision(Ship ship) {
        int bx = getX() + getW() / 2 - 8;
        int by = getY();
        String direction = getDirection();

        if (direction.equals("down")) {
            by = getY() + getH();
        } else if (direction.equals("left")) {
            bx = getX() - 22;
            by = getY() + getH() / 2 - 8;
        } else if (direction.equals("right")) {
            bx = getX() + getW() + 6;
            by = getY() + getH() / 2 - 8;
        }

        if (direction.equals("up")) {
            beamBounds = new Rectangle(bx, by - beamLength * 56, 16, beamLength * 56);
        } else if (direction.equals("down")) {
            beamBounds = new Rectangle(bx, by, 16, beamLength * 56);
        } else if (direction.equals("left")) {
            beamBounds = new Rectangle(bx - beamLength * 56, by, beamLength * 56, 16);
        } else {
            beamBounds = new Rectangle(bx, by, beamLength * 56, 16);
        }

        Rectangle shipBounds = new Rectangle(ship.getX(), ship.getY(), ship.getW(), ship.getH());

        if (beamBounds.intersects(shipBounds) && !hitEnemies.contains(ship)) {
            hitEnemies.add(ship);
            return true;
        }
        return false;
    }

    public void drawEnemy(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform old = g2d.getTransform();
        String direction = getDirection();

        int cx = getX() + getW() / 2;
        int cy = getY() + getH() / 2;

        if (direction == "right") {
            g2d.rotate(Math.PI / 2, cx, cy);
        } else if (direction == "down") {
            g2d.rotate(Math.PI, cx, cy);
        } else if (direction == "left") {
            g2d.rotate(-Math.PI / 2, cx, cy);
        }

        g2d.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);

        int x = getX() + getW() / 2 - 7;
        int y = getY();

        if (charging) {
            drawEnemyCharge(g, x, y - 20);
        } else if (beamTimer > 0 && firing) {
            drawEnemyBeam(g, x - 1, y - 4, beamLength);
        }

        g2d.setTransform(old);
    }

    private void drawEnemyCharge(Graphics g, int x, int y) {
        g.drawImage(new ImageIcon("images\\weapon\\enemyCharge.gif").getImage(), x, y, 16, 16, null);
    }

    private void drawEnemyBeam(Graphics g, int x, int y, int segments) {
        for (int i = 0; i < segments; i++) {
            int segY = y - 56 - (i * 56);
            g.drawImage(new ImageIcon("images\\weapon\\enemyBeam.gif").getImage(), x, segY, 16, 56, null);
        }
    }

    @Override
    public void move(int x, int y) {
        if (getX() + getW() / 2 < x) {
            setX(getX() + getSpeed());
        } else if (getX() + getW() / 2 > x) {
            setX(getX() - getSpeed());
        }
        if (getY() + getH() / 2 < y) {
            setY(getY() + getSpeed());
        } else if (getY() + getH() / 2 > y) {
            setY(getY() - getSpeed());
        }

        if (!isFiring()) {
            updateDirection(x, y);
        }
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

}
