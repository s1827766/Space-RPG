import javax.swing.ImageIcon;
import java.awt.Graphics;

public class E_Missile extends Enemy {

    public E_Missile(int xV, int yV) {
        super(xV, yV, 104, 80, new ImageIcon("images\\ship\\enemyMissile.png"), 10, 100, 1,
                new W_Missile(xV, yV, 40, 84, new ImageIcon("images\\weapon\\enemyMissile.png"), 0, 0, 300));
    }

    public void drawEnemy(Graphics g) {
        super.drawEnemy(g);
    }

}
