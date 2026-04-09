import javax.swing.ImageIcon;
import java.awt.Graphics;

public class S_Missile extends Ship {

    public S_Missile() {
        super(400, 100, 136, 120, new ImageIcon("images\\ship\\missile.png"), "Missile Ship", 20, 110, 3, 6, 30,
                new W_Missile(400, 100, 0, 0), new A_Nuke(400, 100, 0, 0));
    }

    public S_Missile(int xV, int yV) {
        super(xV, yV, 136, 120, new ImageIcon("images\\ship\\missile.png"), "Missile Ship", 20, 110, 3, 6, 30,
                new W_Missile(xV, yV, 0, 0), new A_Nuke(xV, yV, 0, 0));
    }

    public String getAttack() {
        return "Fires slow and powerful missiles";
    }

    public String getSpecial() {
        return "Fires a powerful homing missile";
    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);
    }
}
