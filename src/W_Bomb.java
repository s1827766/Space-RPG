import javax.swing.ImageIcon;

public class W_Bomb extends Ranged {

    public W_Bomb(int x, int y, int width, int height, ImageIcon pic, int dx, int dy, int range) {
        super(x, y, width, height, pic, dx, dy, range);
    }

    public W_Bomb(int x, int y, int dx, int dy) {
        super(x, y, 57, 57, new ImageIcon("images\\weapon\\bomb.gif"), dx, dy, 400);
    }

}
