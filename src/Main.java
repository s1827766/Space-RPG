import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("unused")
public class Main extends JFrame {
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 985;

	public Main() {
		super("Space RPG");
		setSize(WIDTH, HEIGHT);
		// setSize(Toolkit.getDefaultToolkit().getScreenSize().width,
		// Toolkit.getDefaultToolkit().getScreenSize().height);
		Game play = new Game();
		((Component) play).setFocusable(true);

		Color bg = new Color(17, 2, 25);

		setBackground(bg);
		getContentPane().add(play);

		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				play.createFile();
				play.readFile();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				play.writeToFile();
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});
	}

	public static void main(String[] args) {
		Main run = new Main();

	}

}
