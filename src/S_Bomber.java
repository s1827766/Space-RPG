import javax.swing.ImageIcon;
import java.awt.Graphics;

public class S_Bomber extends Ship {

    public S_Bomber() {
        super(300, 100, 96, 132, new ImageIcon("images\\ship\\bomber.png"), "Bomber Ship", 25, 120, 3, 9, 28,
                new W_Bomb(300, 100, 0, 0), new A_Barrage(300, 100, 0, 0));
    }

    public S_Bomber(int xV, int yV) {
        super(xV, yV, 96, 132, new ImageIcon("images\\ship\\bomber.png"), "Bomber Ship", 25, 120, 3, 9, 28,
                new W_Bomb(xV, yV, 0, 0), new A_Barrage(xV, yV, 0, 0));
    }

    public String getAttack() {
        return "Plants bombs dealing area damage";
    }

    public String getSpecial() {
        return "Fires multiple bombs at once";
    }

    public void drawSelectShip(Graphics g) {
        g.drawImage(getPic().getImage(), getX(), getY(), getW(), getH(), null);
    }
}
