import javax.swing.ImageIcon;

public class A_Shield extends Melee {

    public A_Shield(int x, int y, int width, int height, ImageIcon pic) {
        super(x, y, width, height, pic);
    }

    public A_Shield(int x, int y) {
        super(x, y, 132, 132, new ImageIcon("images\\ability\\shield.png"));
    }

}
