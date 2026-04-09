import javax.swing.ImageIcon;
import java.awt.Graphics;

public class E_Bomber extends Enemy {

    public E_Bomber(int xV, int yV) {
        super(xV, yV, 88, 96, new ImageIcon("images\\ship\\enemyBomber.png"), 10, 100, 1,
                new W_Bomb(xV, yV, 92, 92, new ImageIcon("images\\weapon\\enemyBomb.png"), 0, 0, 500));
    }

    public void drawEnemy(Graphics g) {
        super.drawEnemy(g);
    }

}
