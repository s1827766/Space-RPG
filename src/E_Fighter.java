import javax.swing.ImageIcon;
import java.awt.Graphics;

public class E_Fighter extends Enemy {

    public E_Fighter(int xV, int yV) {
        super(xV, yV, 68, 80, new ImageIcon("images\\ship\\enemyFighter.png"), 10, 100, 2,
                new W_Blast(xV, yV, 12, 60, new ImageIcon("images\\weapon\\enemyBlast.png"), 0, 0, 400));
    }

    public void drawEnemy(Graphics g) {
        super.drawEnemy(g);
    }

}
