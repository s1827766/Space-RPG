import javax.swing.ImageIcon;
import java.awt.Graphics;

public class W_Beam extends Melee {

    private ImageIcon charge, base, segment;

    public W_Beam(int x, int y) {
        super(x, y, 15, 15, null);
        charge = new ImageIcon("images\\weapon\\charge.gif");
        base = new ImageIcon("images\\weapon\\base.gif");
        segment = new ImageIcon("images\\weapon\\beam.gif");
    }

    public W_Beam(int x, int y, int width, int height, ImageIcon pic) {
        super(x, y, width, height, pic);
    }

    public void drawCharge(Graphics g, int x, int y) {
        g.drawImage(charge.getImage(), x, y, 15, 15, null);
    }

    public void drawBeam(Graphics g, int x, int y, int segments) {
        g.drawImage(base.getImage(), x, y, 15, 15, null);

        for (int i = 0; i < segments; i++) {
            int segY = y - 44 - (i * 44);
            g.drawImage(segment.getImage(), x + 2, segY, 11, 44, null);
        }
    }
}
