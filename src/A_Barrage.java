import javax.swing.ImageIcon;

public class A_Barrage extends W_Bomb {

    public A_Barrage(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic, dx, dy, range);
    }

    public A_Barrage(int x, int y, int dx, int dy) {
        super(x, y, 57, 57, new ImageIcon("images\\ability\\barrage.gif"), dx, dy, 400);
    }

}
