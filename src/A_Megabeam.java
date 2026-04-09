import javax.swing.ImageIcon;
import java.awt.Graphics;

public class A_Megabeam extends Melee {

    private ImageIcon charge, base, segment;

    public A_Megabeam(int x, int y) {
        super(x, y, 15, 15, null);
        charge = new ImageIcon("images\\ability\\megacharge.gif");
        base = new ImageIcon("images\\ability\\megabase.gif");
        segment = new ImageIcon("images\\ability\\megabeam.gif");
    }

    public A_Megabeam(int x, int y, int width, int height, ImageIcon pic) {
        super(x, y, width, height, pic);
    }

    public void drawCharge(Graphics g, int x, int y) {
        g.drawImage(charge.getImage(), x, y, 19, 19, null);
    }

    public void drawBeam(Graphics g, int x, int y) {
        g.drawImage(base.getImage(), x, y, 19, 19, null);

        for (int i = 0; i < 10; i++) {
            int segY = y - 96 - (i * 96);
            g.drawImage(segment.getImage(), x + 3, segY, 13, 96, null);
        }
    }
}
