import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Ship {

    private int x, y, width, height;
    private ImageIcon pic;
    private String name, atk, abl;
    private int damage, health, maxHealth, speed;
    private int atkCooldown, ablCooldown;
    private int atkTimer, ablTimer;

    private Weapons weapon;
    private Weapons ability;
    private ArrayList<Ranged> projectiles;

    private String direction = "up";

    public Ship() {
        x = 0;
        y = 0;
        width = 50;
        height = 50;
        pic = new ImageIcon("");
        name = "";
        atk = "";
        abl = "";
        damage = 10;
        health = 100;
        maxHealth = health;
        speed = 3;
        atkCooldown = 5;
        ablCooldown = 10;
        atkTimer = 0;
        ablTimer = 0;
        projectiles = new ArrayList<>();
    }

    public Ship(int xV, int yV, int w, int h, ImageIcon p, String n, int dmg, int hp, int spd, int atkSpd, int ablSpd,
            Weapons wpn, Weapons abl) {

        x = xV;
        y = yV;
        width = w;
        height = h;
        pic = p;
        name = n;

        damage = dmg;
        health = hp;
        maxHealth = hp;
        speed = spd;
        atkCooldown = atkSpd;
        ablCooldown = ablSpd;

        atkTimer = 0;
        ablTimer = 0;

        weapon = wpn;
        ability = abl;
        projectiles = new ArrayList<>();
    }

    public Ship(int xV, int yV, int w, int h, ImageIcon p, int dmg, int hp, int spd, Weapons wpn) {

        x = xV;
        y = yV;
        width = w;
        height = h;
        pic = p;

        damage = dmg;
        health = hp;
        maxHealth = hp;
        speed = spd;

        weapon = wpn;

        projectiles = new ArrayList<>();
    }

    public void drawShip(Graphics g) {

    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(pic.getImage(), x, y, width, height, null);
    }

    public void drawGameShip(Graphics g, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;

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

        g2d.drawImage(pic.getImage(), getX(), getY(), w, h, null);
        g2d.setTransform(old);
    }

    public void drawEnemy(Graphics g) {
        drawGameShip(g, width, height);
    }

    public void moveShip(int dx, int dy) {
        if (this instanceof S_Stealth stealth && stealth.isDashing()) {
            if (direction == "up") {
                y += -14;
            } else if (direction.equals("right")) {
                x += 14;
            } else if (direction.equals("down")) {
                y += 14;
            } else if (direction.equals("left")) {
                x += -14;
            }
            return;
        }

        x += dx * speed;
        y += dy * speed;
    }

    public void updateDirection(int x, int y) {
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
    }

    public boolean checkShipCollision(Enemy enemy) {
        int x = getX();
        int y = getY();
        int w = getW();
        int h = getH();

        if (direction.equals("left") || direction.equals("right")) {
            int cx = getX() + getW() / 2;
            int cy = getY() + getH() / 2;
            int temp = w;
            x = cx - h / 2;
            y = cy - w / 2;
            w = h;
            h = temp;
        }

        int ex = enemy.getX();
        int ey = enemy.getY();
        int ew = enemy.getW();
        int eh = enemy.getH();

        if (enemy.getDirection().equals("left") || enemy.getDirection().equals("right")) {
            int ecx = enemy.getX() + enemy.getW() / 2;
            int ecy = enemy.getY() + enemy.getH() / 2;
            int etemp = ew;
            ex = ecx - eh / 2;
            ey = ecy - ew / 2;
            ew = eh;
            eh = etemp;
        }

        Rectangle shipBounds = new Rectangle(x, y, w, h);
        Rectangle enemyBounds = new Rectangle(ex, ey, ew, eh);
        return shipBounds.intersects(enemyBounds);
    }

    public void addHealth(int hp) {
        health += hp;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int xV) {
        x = xV;
    }

    public int getY() {
        return y;
    }

    public void setY(int yV) {
        y = yV;
    }

    public int getW() {
        return width;
    }

    public void setW(int w) {
        width = w;
    }

    public int getH() {
        return height;
    }

    public void setH(int h) {
        height = h;
    }

    public ImageIcon getPic() {
        return pic;
    }

    public void setPic(ImageIcon p) {
        pic = p;
    }

    public String getName() {
        return name;
    }

    public String getAttack() {
        return atk;
    }

    public String getSpecial() {
        return abl;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int dmg) {
        damage = dmg;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int hp) {
        health = hp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int mh) {
        maxHealth = mh;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int spd) {
        speed = spd;
    }

    public int getAtkSpd() {
        return atkCooldown;
    }

    public void setAtkSpd(int s) {
        atkCooldown = s;
    }

    public int getAblSpd() {
        return ablCooldown;
    }

    public void setAblSpd(int s) {
        ablCooldown = s;
    }

    public int getAtkTimer() {
        return atkTimer;
    }

    public void setAtkTimer(int t) {
        atkTimer = t;
    }

    public int getAblTimer() {
        return ablTimer;
    }

    public void setAblTimer(int t) {
        ablTimer = t;
    }

    public Weapons getWeapon() {
        return weapon;
    }

    public Weapons getAbility() {
        return ability;
    }

    public ArrayList<Ranged> getProjectiles() {
        return projectiles;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String d) {
        direction = d;
    }

    public boolean rotate() {
        return true;
    }

}
