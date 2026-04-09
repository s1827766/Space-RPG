import javax.swing.ImageIcon;

public class W_Dash extends Melee {

    public W_Dash(int x, int y, int width, int height, ImageIcon pic) {
        super(x, y, width, height, pic);
    }

    public W_Dash(int x, int y) {
        super(x, y, 108, 92, new ImageIcon("images\\weapon\\dash.png"));
    }

}
