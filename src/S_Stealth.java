import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class S_Stealth extends Ship {

    private boolean dashing = false, invisible = false;
    private int dashTimer = 0, invisTimer = 0;
    private ArrayList<Enemy> hitEnemies = new ArrayList<>();
    private Sound p = new Sound();

    public S_Stealth() {
        super(500, 100, 108, 96, new ImageIcon("images\\ship\\stealth.png"), "Stealth Ship", 20, 70, 6, 3, 25,
                new W_Dash(500, 100), new A_Invisibility(500, 100));
    }

    public S_Stealth(int xV, int yV) {
        super(xV, yV, 108, 96, new ImageIcon("images\\ship\\stealth.png"), "Stealth Ship", 20, 70, 6, 3, 25,
                new W_Dash(xV, yV), new A_Invisibility(xV, yV));
    }

    public String getAttack() {
        return "Dashes forward into enemies";
    }

    public String getSpecial() {
        return "Becomes temporarily invisible to enemies";
    }

    public boolean isDashing() {
        return dashing;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean rotate() {
        return !isDashing();
    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);
    }

    public void startDash() {
        if (!dashing && getAtkTimer() == 0) {
            dashing = true;
            dashTimer = 30;
            setPic(new ImageIcon("images\\weapon\\dash.png"));
        }
    }

    public void updateDash() {
        if (dashing) {
            dashTimer--;
            if (dashTimer <= 0) {
                dashing = false;
                if (invisible) {
                    setPic(new ImageIcon("images\\ability\\invis.png"));
                } else {
                    setPic(new ImageIcon("images\\ship\\stealth.png"));
                }
                hitEnemies.clear();
                setAtkTimer(getAtkSpd() * 30);
            }
        }
    }

    public void invis() {
        if (!invisible && getAblTimer() == 0) {
            invisible = true;
            invisTimer = 1000;
            setPic(new ImageIcon("images\\ability\\invis.png"));
        }
    }

    public void updateInvis() {
        if (invisible) {
            invisTimer--;
            if (invisTimer <= 0) {
                invisible = false;
                p.playmusic("sounds\\snd_ghostappear.wav");
                setPic(new ImageIcon("images\\ship\\stealth.png"));
                setAblTimer(getAblSpd() * 30);
            }
        }
    }

    public boolean checkCollision(Enemy enemy) {
        Rectangle weaponBounds = new Rectangle(getX(), getY(), getW(), getH());
        Rectangle enemyBounds = new Rectangle(enemy.getX(), enemy.getY(), enemy.getW(), enemy.getH());
        if (weaponBounds.intersects(enemyBounds) && !hitEnemies.contains(enemy) && dashing) {
            hitEnemies.add(enemy);
            return true;
        }
        return false;
    }

}
