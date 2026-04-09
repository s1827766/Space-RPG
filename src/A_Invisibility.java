import javax.swing.ImageIcon;

public class A_Invisibility extends Weapons {

    public A_Invisibility(int x, int y, int width, int height, ImageIcon pic) {
        super(x, y, width, height, pic);
    }

    public A_Invisibility(int x, int y) {
        super(x, y, 108, 96, new ImageIcon("images\\ability\\invis.png"));
    }

}
