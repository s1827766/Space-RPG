import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {

	private BufferedImage back;
	private int key = -1, i = 0;
	private int mouseX = 0, mouseY = 0, camX = 0, camY = 0;
	private int currentWave = 0;
	private int hitCooldown = 0;
	private int loadTimer = 0;
	private Sound p;
	private boolean up, down, left, right;
	private String level;
	private ArrayList<Ship> ships;
	private ArrayList<Enemy> enemies;
	private Ship player;
	private File saveFile;
	private int gameSeed;

	private ImageIcon background = new ImageIcon("images\\background\\space.png");
	private ImageIcon redPlanet = new ImageIcon("images\\background\\redPlanet.png");
	private ImageIcon bluePlanet = new ImageIcon("images\\background\\bluePlanet.png");
	private ImageIcon purplePlanet = new ImageIcon("images\\background\\purplePlanet.png");
	private ImageIcon ringPlanet = new ImageIcon("images\\background\\ringPlanet.png");
	private ImageIcon blackHole = new ImageIcon("images\\background\\blackHole.png");
	private ImageIcon asteroid1 = new ImageIcon("images\\background\\asteroid1.png");
	private ImageIcon asteroid2 = new ImageIcon("images\\background\\asteroid2.png");
	private ImageIcon asteroid3 = new ImageIcon("images\\background\\asteroid3.png");

	private ImageIcon border = new ImageIcon("images\\ui\\border.png");
	private ImageIcon attack = new ImageIcon("images\\ui\\attackButton.png");
	private ImageIcon ability = new ImageIcon("images\\ui\\abilityButton.png");
	private ImageIcon wasd = new ImageIcon("images\\ui\\wasdButton.png");
	private ImageIcon mouse = new ImageIcon("images\\ui\\mouse.png");
	private ImageIcon leftClick = new ImageIcon("images\\ui\\leftClick.png");
	private ImageIcon rightClick = new ImageIcon("images\\ui\\rightClick.png");
	private ImageIcon arrow = new ImageIcon("images\\ui\\arrow.png");

	Color white = new Color(248, 245, 252);
	Color ship = new Color(217, 206, 236);

	public Game() {
		new Thread(this).start();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		p = new Sound();
		level = "start";
		ships = setShips();
		player = ships.get(i);
		enemies = new ArrayList<>();
		saveFile = new File("savefile.txt");
	}

	@SuppressWarnings("static-access")
	public void run() {
		try {
			while (true) {
				Thread.currentThread().sleep(5);
				repaint();
			}
		} catch (Exception e) {
		}
	}

	public void drawLevel(Graphics g2d) {
		switch (level) {
			case "start":
				drawStart(g2d);
				break;

			case "select":
				ships = setShips();
				player = ships.get(i);
				drawSelect(g2d);
				break;

			case "instructions":
				drawInstructions(g2d, player.getName());
				break;

			case "game":
				drawGame(g2d);
				move();
				moveProjectiles(g2d);
				checkCollisions();
				checkLose();

				if (player instanceof S_Stealth && ((S_Stealth) player).isInvisible()) {
				} else {
					moveEnemies();
				}

				if (player instanceof S_Sniper sniper) {
					sniper.updateCharge();
					sniper.updateBeam();
					sniper.updateMegabeam();
				}

				if (player instanceof S_Stealth stealth) {
					stealth.updateDash();
					stealth.updateInvis();
				}

				if (player instanceof S_Fighter fighter) {
					fighter.updateShield();
				}

				if (player.getAtkTimer() > 0) {
					player.setAtkTimer(player.getAtkTimer() - 1);
				}
				if (player.getAblTimer() > 0) {
					player.setAblTimer(player.getAblTimer() - 1);
				}
				if (player.rotate()) {
					player.updateDirection(mouseX, mouseY);
				}
				if (hitCooldown > 0) {
					hitCooldown--;
				}

				break;

			case "lose":
				drawLose(g2d);
				break;
		}

	}

	public void createFile() {
		try {
			if (saveFile.createNewFile()) {
				System.out.println("File created: " + saveFile.getName());
			} else {
				System.out.println("Save file loaded.");
			}
		} catch (Exception e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void writeToFile() {
		if (!level.equals("game")) {
			saveFile.delete();
			return;
		}

		try (FileWriter w = new FileWriter(saveFile)) {

			w.write("LEVEL game\n");

			w.write("PLAYER " + player.getClass().getSimpleName() + " "
					+ player.getX() + " "
					+ player.getY() + " "
					+ player.getHealth() + " "
					+ player.getMaxHealth() + " "
					+ player.getDirection() + "\n");

			w.write("CAMERA " + camX + " " + camY + "\n");

			w.write("WAVE " + currentWave + "\n");

			w.write("GAMESEED " + gameSeed + "\n");

			for (Enemy e : enemies) {
				w.write("ENEMY " + e.getClass().getSimpleName() + " "
						+ e.getX() + " "
						+ e.getY() + " "
						+ e.getHealth() + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readFile() {
		try (Scanner sc = new Scanner(saveFile)) {

			enemies.clear();
			boolean loadedPlayer = false;

			while (sc.hasNext()) {
				String type = sc.next();

				switch (type) {

					case "LEVEL":
						sc.next();
						break;

					case "PLAYER":
						String shipType = sc.next();
						int px = sc.nextInt();
						int py = sc.nextInt();
						int hp = sc.nextInt();
						int maxHp = sc.nextInt();
						String dir = sc.next();

						player = createShip(shipType, px, py);
						player.setHealth(hp);
						player.setMaxHealth(maxHp);
						player.setDirection(dir);
						player.setW((player.getW() * 3) / 4);
						player.setH((player.getH() * 3) / 4);

						loadedPlayer = true;
						break;

					case "CAMERA":
						camX = sc.nextInt();
						camY = sc.nextInt();
						break;

					case "WAVE":
						currentWave = sc.nextInt();
						break;

					case "GAMESEED":
						gameSeed = sc.nextInt();
						break;

					case "ENEMY":
						String eType = sc.next();
						int ex = sc.nextInt();
						int ey = sc.nextInt();
						int ehp = sc.nextInt();

						Enemy e = createEnemy(eType, ex, ey);
						e.setHealth(ehp);
						enemies.add(e);
						break;
				}
			}

			if (loadedPlayer) {
				level = "game";
				loadTimer = 200;
			} else {
				level = "start";
			}

		} catch (Exception e) {
			e.printStackTrace();
			level = "start";
		}
	}

	public Ship createShip(String name, int x, int y) {
		return switch (name) {
			case "S_Fighter" -> new S_Fighter(x, y);
			case "S_Sniper" -> new S_Sniper(x, y);
			case "S_Bomber" -> new S_Bomber(x, y);
			case "S_Stealth" -> new S_Stealth(x, y);
			case "S_Missile" -> new S_Missile(x, y);
			default -> null;
		};
	}

	public Enemy createEnemy(String name, int x, int y) {
		return switch (name) {
			case "E_Fighter" -> new E_Fighter(x, y);
			case "E_Bomber" -> new E_Bomber(x, y);
			case "E_Sniper" -> new E_Sniper(x, y);
			case "E_Missile" -> new E_Missile(x, y);
			default -> null;
		};
	}

	public void paint(Graphics g) {
		Graphics2D twoDgraph = (Graphics2D) g;

		if (back == null) {
			back = (BufferedImage) ((createImage(getWidth(), getHeight())));
		}

		Graphics g2d = back.createGraphics();

		g2d.clearRect(0, 0, getSize().width, getSize().height);

		drawLevel(g2d);
		if (!level.equals("game")) {
			g2d.drawImage(border.getImage(), 0, 0, getWidth(), getHeight(), null);
		}
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 30));

		twoDgraph.drawImage(back, null, 0, 0);

	}

	public void drawStart(Graphics g2d) {
		g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(new Color(100, 225, 247));
		g2d.setFont(new Font("Courier New", Font.BOLD, 200));
		g2d.drawString("Galaxy", (getWidth() - g2d.getFontMetrics().stringWidth("Galaxy")) / 2, 350);
		g2d.drawString("Guardians", (getWidth() - g2d.getFontMetrics().stringWidth("Guardians")) / 2, 500);
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 60));
		g2d.drawString("Press Space to start",
				(getWidth() - g2d.getFontMetrics().stringWidth("Press Space to start")) / 2, 700);
	}

	public void drawSelect(Graphics g2d) {
		g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(ship);
		g2d.setFont(new Font("Courier New", Font.BOLD, 90));
		g2d.drawString("Select a Ship", (getWidth() - g2d.getFontMetrics().stringWidth("Select a Ship")) / 2, 120);
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 40));
		g2d.drawString("Use W and A to select",
				(getWidth() - g2d.getFontMetrics().stringWidth("Use W and A to select")) / 2, 170);
		g2d.drawString("Press Space to confirm",
				(getWidth() - g2d.getFontMetrics().stringWidth("Press Space to confirm")) / 2, 210);

		int x = player.getX();
		int y = player.getY();
		int w = player.getW();
		int h = player.getH();

		drawShips(g2d);
		player.setW(player.getW());
		player.setH(player.getH());
		g2d.drawRect(x, y, w, h);
		g2d.setColor(ship);
		g2d.setFont(new Font("Courier New", Font.BOLD, 60));
		g2d.drawString(player.getName(), x + (w / 2) - (g2d.getFontMetrics().stringWidth(player.getName()) / 2),
				y + h + 120);
		drawStats(g2d, x + (w / 2) - (g2d.getFontMetrics().stringWidth(player.getName()) / 2), y + h + 160);
	}

	public void drawStats(Graphics g2d, int x, int y) {
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.drawString("Attack: " + player.getAttack(), x, y);
		g2d.drawString("Ability: " + player.getSpecial(), x, y + 20);

		g2d.setFont(new Font("Courier New", Font.BOLD, 30));
		g2d.drawString("Health: " + player.getHealth(), x, y + 90);
		g2d.drawString("Damage: " + player.getDamage(), x, y + 120);
		g2d.drawString("Speed: " + player.getSpeed(), x, y + 150);
		g2d.drawString("Attack Cooldown: " + player.getAtkSpd(), x, y + 180);
		g2d.drawString("Ability Cooldown: " + player.getAblSpd(), x, y + 210);
	}

	public void drawLose(Graphics g2d) {
		g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(new Color(233, 11, 0));
		g2d.setFont(new Font("Courier New", Font.BOLD, 200));
		g2d.drawString("Game Over!", (getWidth() - g2d.getFontMetrics().stringWidth("Game Over!")) / 2, 400);
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 60));
		g2d.drawString("You made it to Wave " + currentWave + "!",
				(getWidth() - g2d.getFontMetrics().stringWidth("You made it to Wave " + currentWave + "!")) / 2, 500);
		g2d.setFont(new Font("Courier New", Font.BOLD, 40));
		g2d.drawString("Press Space to return to menu",
				(getWidth() - g2d.getFontMetrics().stringWidth("Press Space to return to menu")) / 2, 700);
	}

	public void drawInstructions(Graphics g2d, String shipName) {
		g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
		g2d.setColor(new Color(128, 128, 128));
		g2d.setFont(new Font("Courier New", Font.BOLD, 90));
		g2d.drawString("Instructions", (getWidth() - g2d.getFontMetrics().stringWidth("Instructions")) / 2, 120);
		g2d.setColor(white);
		g2d.setFont(new Font("Courier New", Font.BOLD, 55));
		String message1 = "Use      to move and dodge the enemies' attacks!";
		g2d.drawString(message1, (getWidth() - g2d.getFontMetrics().stringWidth(message1)) / 2, 300);
		g2d.drawImage(wasd.getImage(), (getWidth() - g2d.getFontMetrics().stringWidth(message1)) / 2 + 130, 250, 140,
				84, null);
		String message2 = "Use the mouse   to aim at enemies!";
		g2d.drawString(message2, (getWidth() - g2d.getFontMetrics().stringWidth(message2)) / 2, 400);
		g2d.drawImage(mouse.getImage(), (getWidth() - g2d.getFontMetrics().stringWidth(message2)) / 2 + 445, 355, 45,
				65, null);

		String message3 = player.getName() + "    Controls";
		int y = 540, x = (getWidth() - g2d.getFontMetrics().stringWidth(message3)) / 2 + 390;
		g2d.setColor(ship);
		g2d.setFont(new Font("Courier New", Font.BOLD, 65));
		g2d.drawString(message3, (getWidth() - g2d.getFontMetrics().stringWidth(message3)) / 2, 600);
		
		String message4 = "", message5 = "";
		switch (shipName) {
			case "Fighter Ship" -> {
				message4 = "Left Click   to fire quick blasts!";
				message5 = "Right Click   to activate a temporary shield!";
				x += 30;
			}
			case "Sniper Ship" -> {
				message4 = "Hold Left Click   to charge a beam, release to fire!";
				message5 = "Right Click   to fire a huge megabeam!";
			}
			case "Bomber Ship" -> {
				message4 = "Left Click   to drop bombs!";
				message5 = "Right Click   to drop three bombs at once!";
			}
			case "Stealth Ship" -> {
				message4 = "Left Click   to dash into enemies!";
				message5 = "Right Click   to turn invisible and hide from enemies!";
				x += 30;
				y += 15;
			}
			case "Missile Ship" -> {
				message4 = "Left Click   to launch missiles!";
				message5 = "Right Click   to launch a large homing missile!";
				x += 25;
			}
		}
		
		g2d.drawImage(player.getPic().getImage(), x, y, player.getW()*3/4, player.getH()*3/4, null);
		g2d.setFont(new Font("Courier New", Font.BOLD, 45));
		g2d.setColor(white);
		g2d.drawString(message4, (getWidth() - g2d.getFontMetrics().stringWidth(message4)) / 2, 700);
		g2d.drawString(message5, (getWidth() - g2d.getFontMetrics().stringWidth(message5)) / 2, 770);
		if (shipName.equals("Sniper Ship")) {
			g2d.drawImage(leftClick.getImage(), (getWidth() - g2d.getFontMetrics().stringWidth(message4)) / 2 + 415,
					650, 45,
					65, null);
		} else {
			g2d.drawImage(leftClick.getImage(), (getWidth() - g2d.getFontMetrics().stringWidth(message4)) / 2 + 285,
					650, 45,
					65, null);
		}
		g2d.drawImage(rightClick.getImage(), (getWidth() - g2d.getFontMetrics().stringWidth(message5)) / 2 + 310, 720,
				45,
				65, null);
	}

	public ArrayList<Ship> setShips() {
		ArrayList<Ship> temp = new ArrayList<Ship>();
		int x = getWidth() / 6;
		int y = 400;

		temp.add(new S_Fighter(x - 132 / 2, y - 128 / 2));
		temp.add(new S_Sniper(x * 2 - 84 / 2, y - 124 / 2));
		temp.add(new S_Bomber(x * 3 - 96 / 2, y - 132 / 2));
		temp.add(new S_Stealth(x * 4 - 108 / 2, y - 96 / 2));
		temp.add(new S_Missile(x * 5 - 136 / 2, y - 120 / 2));
		return temp;
	}

	public void drawShips(Graphics g2d) {
		for (Ship s : ships) {
			s.drawSelectShip(g2d);
		}
	}

	public void drawGame(Graphics g2d) {
		drawBackground(g2d);

		Graphics2D g = (Graphics2D) g2d;
		AffineTransform oldTransform = g.getTransform();
		g.translate(-camX, -camY);

		if (!(hitCooldown > 0 && (hitCooldown / 7) % 2 == 0)) {
			player.drawGameShip(g2d, player.getW(), (player.getH()));
		}
		drawHealthBar(g2d, player.getX(), player.getY() + player.getH() + 6, player.getW(), 5, player.getHealth(),
				player.getMaxHealth());

		for (Ranged r : player.getProjectiles()) {
			if (r.exploding) {
				g2d.setColor(new Color(255, 0, 0, 75));
				g2d.fillOval(r.ex - r.radius + 12, r.ey - r.radius, r.radius * 2, r.radius * 2);
				r.drawWeapon(g2d, r.ex, r.ey);
				continue;
			}
			r.drawWeapon(g2d, r.getX(), r.getY());
		}
		for (Enemy e : enemies) {
			for (Ranged r : e.getProjectiles()) {
				if (r.exploding) {
					g2d.setColor(new Color(255, 0, 0, 75));
					g2d.fillOval(r.ex - r.radius, r.ey - r.radius, r.radius * 2, r.radius * 2);
					r.drawWeapon(g2d, r.ex - 12, r.ey);
					continue;
				}
				r.drawWeapon(g2d, r.getX(), r.getY());
			}

		}

		if (!enemies.isEmpty()) {
			for (Enemy e : enemies) {
				if (e instanceof E_Sniper sniper) {
					sniper.updateCharge();
					sniper.updateBeam();
				}
				e.drawEnemy(g2d);
				drawHealthBar(g2d, e.getX(), e.getY() - 10, e.getW(), 5, e.getHealth(), e.getMaxHealth());
				e.updateFireTimer();
				if (e.canFire() && !(player instanceof S_Stealth stealth && stealth.isInvisible())) {
					fireEnemyWeapon(e);
				}
			}
		}

		g.setTransform(oldTransform);

		for (Enemy e : enemies) {
			int screenX = e.getX() - camX + e.getW() / 2;
			int screenY = e.getY() - camY + e.getH() / 2;
			if (screenX < -50 || screenX > getWidth() + 50 || screenY < -50 || screenY > getHeight() + 50) {
				int indX, indY;
				String direction = "up";
				
				if (screenY < 0) { // off top
					indX = Math.max(20, Math.min(getWidth() - 20, screenX));
					indY = 20;
					direction = "up";
				} else if (screenY > getHeight()) {
					indX = Math.max(20, Math.min(getWidth() - 20, screenX));
					indY = getHeight() - 20;
					direction = "down";
				} else if (screenX < 0) {
					indX = 20;
					indY = Math.max(20, Math.min(getHeight() - 20, screenY));
					direction = "left";
				} else {
					indX = getWidth() - 20;
					indY = Math.max(20, Math.min(getHeight() - 20, screenY));
					direction = "right";
				}
				
				Graphics2D g2 = (Graphics2D) g2d;
				int cx = indX;
				int cy = indY;
				AffineTransform old = g2.getTransform();
				if (direction.equals("right")) {
					g2.rotate(Math.PI / 2, cx, cy);
				} else if (direction.equals("down")) {
					g2.rotate(Math.PI, cx, cy);
				} else if (direction.equals("left")) {
					g2.rotate(-Math.PI / 2, cx, cy);
				}
				g2d.drawImage(arrow.getImage(), indX - 10, indY - 10, 52, 52, null);
				g2.setTransform(old);
			}
		}

		g2d.drawImage(border.getImage(), 0, 0, getWidth(), getHeight(), null);
		drawCooldowns(g2d);
		g2d.setFont(new Font("Courier New", Font.BOLD, 40));
		g2d.setColor(white);
		g2d.drawString("Wave: " + currentWave, 20, getHeight() - 125);

		if (loadTimer > 0) {
			g2d.drawString("Save file loaded", 20, 50);
			loadTimer--;
		}
	}

	public void drawBackground(Graphics g2d) {
		int bgW = background.getIconWidth();
		int bgH = background.getIconHeight();

		int xOffset = -camX % bgW;
		int yOffset = -camY % bgH;

		for (int x = xOffset - bgW; x < getWidth(); x += bgW) {
			for (int y = yOffset - bgH; y < getHeight(); y += bgH) {
				g2d.drawImage(background.getImage(), x, y, null);

				int tileX = (x + camX) / bgW;
				int tileY = (y + camY) / bgH;
				drawPlanets(g2d, x, y, bgW, bgH, tileX, tileY);
			}
		}

	}

	public void drawPlanets(Graphics g2d, int screenX, int screenY, int bgW, int bgH, int tileX, int tileY) {
		Random rand = new Random(gameSeed ^ tileX ^ tileY);
		
		int numPlanets = 1 + rand.nextInt(2);
		ImageIcon[] planets = {bluePlanet, purplePlanet, redPlanet, ringPlanet, blackHole, asteroid1, asteroid2, asteroid3};
		ArrayList<Rectangle> p = new ArrayList<>();
		
		for (int i = 0; i < numPlanets; i++) {
			int size = 50 + rand.nextInt(101);
			int alpha = 80 + rand.nextInt(81);
			
			ImageIcon planet = planets[rand.nextInt(planets.length)];
			
			if (planet == blackHole || planet == ringPlanet) {
				size *= 2;
			}
			if (planet == asteroid1 || planet == asteroid2 || planet == asteroid3) {
				size = (size * 1) / 2;
			}
			
			int planetW = planet.getIconWidth();
			int planetH = planet.getIconHeight();
			int maxDim = Math.max(planetW, planetH);
			int scaledW = (planetW * size) / maxDim;
			int scaledH = (planetH * size) / maxDim;
			
			int planetX = screenX + rand.nextInt(Math.max(1, bgW - scaledW));
			int planetY = screenY + rand.nextInt(Math.max(1, bgH - scaledH));
			
			Rectangle rect = new Rectangle(planetX, planetY, scaledW, scaledH);
			boolean overlaps = false;
			for (Rectangle existing : p) {
				if (rect.intersects(existing)) {
					overlaps = true;
					break;
				}
			}
			
			if (overlaps) {
				continue;
			}
			
			p.add(rect);
			
			Graphics2D g2 = (Graphics2D) g2d;
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f);
			AlphaComposite oldComposite = (AlphaComposite) g2.getComposite();
			
			g2.setComposite(composite);
			g2d.drawImage(planet.getImage(), planetX, planetY, scaledW, scaledH, null);
			g2.setComposite(oldComposite);
		}
	}

	public void spawnWave(int wave) {
		enemies.clear();
		int minEnemies, maxEnemies;
		if (wave <= 5) {
			minEnemies = 1;
			maxEnemies = 2;
		} else if (wave <= 10) {
			minEnemies = 2;
			maxEnemies = 3;
		} else {
			minEnemies = 3;
			maxEnemies = 6;
		}
		int numEnemies = minEnemies + (int) (Math.random() * (maxEnemies - minEnemies + 1));
		for (int i = 0; i < numEnemies; i++) {
			int x = player.getX() + (int) (Math.random() * 2000) - 1000;
			int y = player.getY() + (int) (Math.random() * 2000) - 1000;
			Enemy enemy = createRandomEnemy(x, y);
			enemies.add(enemy);
		}
		p.playmusic("sounds\\snd_mercyadd.wav");
	}
	

	public Enemy createRandomEnemy(int x, int y) {
		int type = (int) (Math.random() * 4);
		Enemy enemy;
		switch (type) {
			case 0:
				enemy = new E_Fighter(x, y);
				break;
			case 1:
				enemy = new E_Bomber(x, y);
				break;
			case 2:
				enemy = new E_Sniper(x, y);
				break;
			case 3:
				enemy = new E_Missile(x, y);
				break;
			default:
				enemy = new E_Fighter(x, y);
				break;
		}
		enemy.setSpeed(1 + (int) (Math.random() * 2));
		return enemy;
	}

	public void moveEnemies() {
		for (Enemy e : enemies) {
			e.move(player.getX() + player.getW() / 2, player.getY() + player.getH() / 2);
		}
	}

	public void fireEnemyWeapon(Enemy e) {
		String direction = e.getDirection();

		if (e.getWeapon() instanceof W_Blast) {
			int x = e.getX() + e.getW() / 2 - 6;
			int y = e.getY() - e.getH() / 2 - 20;

			if (direction.equals("down")) {
				y = e.getY() + e.getH();
			} else if (direction.equals("left")) {
				x = e.getX() - 42;
				y = e.getY() + e.getH() / 2 - 30;
			} else if (direction.equals("right")) {
				x = e.getX() + e.getW() + 30;
				y = e.getY() + e.getH() / 2 - 30;
			}

			W_Blast b = (W_Blast) e.getWeapon();
			W_Blast blast = new W_Blast(x, y, b.getW(), b.getH(), b.getPic(), 0, 0, b.getRange());
			blast.setDirection(direction);
			blast.setSpeed(direction);
			e.getProjectiles().add(blast);
			p.playmusic("sounds\\snd_crow.wav");
		} else if (e.getWeapon() instanceof W_Beam) {
			if (e instanceof E_Sniper sniper) {
				sniper.charge();
			}
		} else if (e.getWeapon() instanceof W_Bomb) {
			int x = e.getX() + e.getW() / 2 - 92 / 2;
			int y = e.getY() + e.getH() / 2 - 92 / 2;
			W_Bomb b = (W_Bomb) e.getWeapon();
			W_Bomb bomb = new W_Bomb(x, y, b.getW(), b.getH(), b.getPic(), 0, 0, b.getRange());
			e.getProjectiles().add(bomb);
			p.playmusic("sounds\\snd_bombfall1.wav", 0.50f);
		} else if (e.getWeapon() instanceof W_Missile) {
			int x = e.getX() + e.getW() / 2 - 20;
			int y = e.getY() - e.getH() / 2 - 50;
			if (direction.equals("down")) {
				y = e.getY() + e.getH() + 6;
			} else if (direction.equals("left")) {
				x = e.getX() - 56;
				y = e.getY() + e.getH() / 2 - 42;
			} else if (direction.equals("right")) {
				x = e.getX() + e.getW() + 16;
				y = e.getY() + e.getH() / 2 - 42;
			}
			W_Missile m = (W_Missile) e.getWeapon();
			W_Missile missile = new W_Missile(x, y, m.getW(), m.getH(), m.getPic(), 0, 0, m.getRange());
			missile.setDirection(direction);
			missile.setSpeed(direction);
			e.getProjectiles().add(missile);
			p.playmusic("sounds\\snd_fireball.wav");
		}
	}

	public void move() {
		int dx = 0;
		int dy = 0;

		if (left)
			dx -= 1;
		if (right)
			dx += 1;
		if (up)
			dy -= 1;
		if (down)
			dy += 1;
		player.moveShip(dx, dy);

		camX = player.getX() - getWidth() / 2 + player.getW() / 2;
		camY = player.getY() - getHeight() / 2 + player.getH() / 2;
	}

	public void moveProjectiles(Graphics g2d) {
		ArrayList<Ranged> removeList = new ArrayList<>();

		for (Ranged r : player.getProjectiles()) {
			if (r.exploding) {
				r.setExplosionTimer(r.getExplosionTimer() - 1);
				if (r.getExplosionTimer() <= 0)
					removeList.add(r);
				continue;
			}

			r.move();

			if (r.getRange() <= 0) {
				if (r instanceof W_Bomb || r instanceof A_Barrage || r instanceof W_Missile || r instanceof A_Nuke) {
					explode(r);
					continue;
				}
				removeList.add(r);
			}
		}
		player.getProjectiles().removeAll(removeList);

		for (Enemy e : enemies) {
			ArrayList<Ranged> enemyRemoveList = new ArrayList<>();
			for (Ranged r : e.getProjectiles()) {
				if (r.exploding) {
					r.setExplosionTimer(r.getExplosionTimer() - 1);
					if (r.getExplosionTimer() <= 0)
						enemyRemoveList.add(r);
					continue;
				}

				r.move();

				if (r.getRange() <= 0) {
					if (r instanceof W_Bomb || r instanceof W_Missile) {
						explodeEnemy(r);
						continue;
					}
					enemyRemoveList.add(r);
				}
			}
			e.getProjectiles().removeAll(enemyRemoveList);
		}
	}

	public void checkCollisions() {
		ArrayList<Enemy> deadEnemies = new ArrayList<>();
		for (Enemy e : enemies) {
			for (Ranged r : player.getProjectiles()) {
				if (r.checkCollision(e)) {
					if (r instanceof W_Bomb || r instanceof A_Barrage || r instanceof W_Missile
							|| r instanceof A_Nuke) {
						r.setRange(0);
					} else {
						e.setHealth(e.getHealth() - player.getDamage());
						p.playmusic("sounds\\snd_damage.wav", 0.50f);
						r.setRange(0);
					}
				}
			}
			for (Ranged r : player.getProjectiles()) {
				if (r.exploding) {
					Ellipse2D explosionCircle = new Ellipse2D.Double(r.ex - r.radius + 12, r.ey - r.radius,
							r.radius * 2, r.radius * 2);
					Rectangle enemyBounds = new Rectangle(e.getX(), e.getY(), e.getW(), e.getH());
					if (explosionCircle.intersects(enemyBounds) && !r.hitEnemies.contains(e)) {
						if (r instanceof A_Nuke) {
							e.setHealth(e.getHealth() - player.getDamage() * 2);
						} else {
							e.setHealth(e.getHealth() - player.getDamage());
						}
						p.playmusic("sounds\\snd_damage.wav", 0.50f);
						r.hitEnemies.add(e);
					}
				}
			}
			if (player instanceof S_Sniper sniper) {
				if (sniper.isFiring() && sniper.checkCollision(e)) {
					e.setHealth(e.getHealth() - player.getDamage());
					p.playmusic("sounds\\snd_damage.wav", 0.50f);
				}
				if (sniper.isAbility() && sniper.checkMegaCollision(e)) {
					e.setHealth(e.getHealth() - player.getDamage() * 2);
					p.playmusic("sounds\\snd_damage.wav", 0.50f);
				}
			}
			if (player instanceof S_Stealth stealth) {
				if (stealth.isDashing() && stealth.checkCollision(e)) {
					e.setHealth(e.getHealth() - player.getDamage());
					p.playmusic("sounds\\snd_damage.wav", 0.50f);
				}
			}
			for (Ranged r : e.getProjectiles()) {
				if (r.checkCollision(player) && hitCooldown == 0) {
					if ((r instanceof W_Bomb || r instanceof W_Missile)
							&& !(player instanceof S_Stealth stealth && stealth.isDashing())) {
						r.setRange(0);
					} else {
						if (player instanceof S_Fighter fighter && fighter.isShieldActive()) {
							r.setRange(0);
							fighter.breakShield();
							p.playmusic("sounds\\snd_bell.wav");
							hitCooldown = 120;
						} else if (player instanceof S_Stealth stealth && stealth.isDashing()) {
						} else {
							r.setRange(0);
							player.setHealth(player.getHealth() - e.getDamage());
							p.playmusic("sounds\\snd_hurt1.wav");
							hitCooldown = 60;
						}
					}
				}
			}
			for (Ranged r : e.getProjectiles()) {
				if (r.exploding) {
					Ellipse2D explosionCircle = new Ellipse2D.Double(r.ex - r.radius, r.ey - r.radius, r.radius * 2,
							r.radius * 2);
					Rectangle playerBounds = new Rectangle(player.getX(), player.getY(), player.getW(), player.getH());
					if (explosionCircle.intersects(playerBounds) && !r.hitEnemies.contains(player)
							&& hitCooldown == 0) {
						if (player instanceof S_Fighter fighter && fighter.isShieldActive()) {
							fighter.breakShield();
							p.playmusic("sounds\\snd_bell.wav");
							hitCooldown = 120;
						} else if (player instanceof S_Stealth stealth && stealth.isDashing()) {
						} else {
							player.setHealth(player.getHealth() - e.getDamage());
							p.playmusic("sounds\\snd_hurt1.wav");
							hitCooldown = 60;
						}
						r.hitEnemies.add(player);
					}
				}
			}
			if (e instanceof E_Sniper enemySniper) {
				if (enemySniper.isFiring() && enemySniper.checkCollision(player)) {
					if (player instanceof S_Fighter fighter && fighter.isShieldActive()) {
						fighter.breakShield();
						p.playmusic("sounds\\snd_bell.wav");
						hitCooldown = 120;
					} else if (player instanceof S_Stealth stealth && stealth.isDashing()) {
					} else {
						player.setHealth(player.getHealth() - e.getDamage());
						p.playmusic("sounds\\snd_hurt1.wav");
						hitCooldown = 60;
					}
				}
			}
			if (player.checkShipCollision(e) && hitCooldown == 0) {
				if (player instanceof S_Fighter fighter && fighter.isShieldActive()) {
					fighter.breakShield();
					p.playmusic("sounds\\snd_bell.wav");
					hitCooldown = 120;
				} else if (player instanceof S_Stealth stealth && stealth.isDashing()) {
				} else {
					player.setHealth(player.getHealth() - e.getDamage());
					p.playmusic("sounds\\snd_hurt1.wav");
					hitCooldown = 60;
				}
			}
			if (e.getHealth() <= 0) {
				deadEnemies.add(e);
			}
		}
		for (Enemy e : deadEnemies) {
			enemies.remove(e);
			p.playmusic("sounds\\snd_deathnoise.wav");
		}
		if (enemies.isEmpty()) {
			currentWave++;
			player.addHealth(player.getMaxHealth() / 10);
			spawnWave(currentWave);
		}
	}

	public void checkLose() {
		if (player.getHealth() <= 0) {
			level = "lose";
			left = false;
			right = false;
			up = false;
			down = false;
			p.loopmusic("stop");
		}
	}

	public void drawHealthBar(Graphics g2d, int x, int y, int width, int height, int health, int maxHealth) {
		int filled = 0;
		if (maxHealth > 0) {
			if (health <= 0) {
				filled = 0;
			} else if (health >= maxHealth) {
				filled = width;
			} else {
				filled = (width * health) / maxHealth;
			}
		} else {
			filled = 0;
		}
		g2d.setColor(Color.red);
		g2d.fillRect(x, y, width, height);
		g2d.setColor(Color.green);
		g2d.fillRect(x, y, filled, height);
	}

	public void drawCooldowns(Graphics g2d) {
		g2d.drawImage(attack.getImage(), 20, getHeight() - 110, 90, 90, this);
		g2d.drawImage(ability.getImage(), 130, getHeight() - 110, 90, 90, this);

		int atkMax = player.getAtkSpd() * 30;
		int ablMax = player.getAblSpd() * 30;
		if (atkMax > 0 && player.getAtkTimer() > 0) {
			int fill = (90 * player.getAtkTimer()) / atkMax;
			int step = 6;
			fill = (fill + step - 1) / step * step;
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(20, getHeight() - 110, 90, fill);
		}
		if (ablMax > 0 && player.getAblTimer() > 0) {
			int fill = (90 * player.getAblTimer()) / ablMax;
			int step = 6;
			fill = (fill + step - 1) / step * step;
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(130, getHeight() - 110, 90, fill);
		}
		if ((player instanceof S_Stealth && ((S_Stealth) player).isInvisible())
				|| (player instanceof S_Fighter && ((S_Fighter) player).isShieldActive())) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(130, getHeight() - 110, 90, 90);
		}

	}

	public void explode(Ranged r) {
		int bx = r.getX();
		int by = r.getY();

		if (r instanceof W_Bomb) {
			r.ex = bx + 12;
			r.ey = by + 34;
			if (Math.random() < 0.05) {
				p.playmusic("sounds\\snd_badexplosion.wav", 0.60f);
			} else {
				p.playmusic("sounds\\snd_bombsplosion.wav", 0.60f);
			}
		}

		if (r instanceof W_Missile) {
			r.ex = bx;
			r.ey = by;
			if (r.getDirection().equals("left")) {
				r.ex = bx - 20;
				r.ey = by + 30;
			} else if (r.getDirection().equals("right")) {
				r.ex = bx + 20;
				r.ey = by + 30;
			} else if (r.getDirection().equals("down")) {
				r.ey = by + 40;
			}
			p.playmusic("stop");
			if (Math.random() < 0.05) {
				p.playmusic("sounds\\snd_badexplosion.wav", 0.60f);
			} else {
				p.playmusic("sounds\\snd_bombsplosion.wav", 0.60f);
			}
		}

		if (r instanceof A_Nuke) {
			r.ex = bx + 10;
			r.ey = by;
			r.radius = 160;
			if (r.getDirection().equals("left")) {
				r.ex = bx - 40;
				r.ey = by + 50;
			} else if (r.getDirection().equals("right")) {
				r.ex = bx + 45;
				r.ey = by + 50;
			} else if (r.getDirection().equals("down")) {
				r.ey = by + 80;
			}
			p.playmusic("stop");
			if (Math.random() < 0.05) {
				p.playmusic("sounds\\snd_badexplosion.wav", 0.60f);
			} else {
				p.playmusic("sounds\\snd_explosion.wav", 0.60f);
			}
		}

		r.explode();
	}

	public void explodeEnemy(Ranged r) {
		int bx = r.getX() + r.getW() / 2;
		int by = r.getY() + r.getH() / 2;

		if (r instanceof W_Bomb) {
			r.ex = bx;
			r.ey = by;
			r.radius = 100;
		}

		if (r instanceof W_Missile) {
			r.ex = bx;
			r.ey = by - 25;
			r.radius = 120;
			if (r.getDirection().equals("left")) {
				r.ex = bx - 25;
				r.ey = by;
			} else if (r.getDirection().equals("right")) {
				r.ex = bx + 25;
				r.ey = by;
			} else if (r.getDirection().equals("down")) {
				r.ey = by + 25;
			}
		}

		p.playmusic("sounds\\snd_bomb.wav");
		r.explode();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		key = e.getKeyCode();
		// System.out.println(key);

		if (level == "start") {
			if (key == 32) {
				key = -1;
				level = "select";
				player = ships.get(i);
				p.playmusic("sounds\\snd_select.wav");
			}
		}

		if (level == "select") {
			if (key == 68 && i <= 3) {
				i++;
				player = ships.get(i);
				p.playmusic("sounds\\snd_menumove.wav");
			} else if (key == 65 && i >= 1) {
				i--;
				player = ships.get(i);
				p.playmusic("sounds\\snd_menumove.wav");
			}
			if (key == 32) {
				key = -1;
				level = "instructions";
				p.playmusic("sounds\\snd_select.wav");
			}
		}

		if (level == "instructions") {
			if (key == 32) {
				level = "game";
				p.playmusic("sounds\\snd_select.wav");
				player.setX(getWidth() / 2 - player.getW() / 2);
				player.setY(getHeight() / 2 - player.getH() / 2);
				player.setW(((player.getW() * 3) / 4));
				player.setH(((player.getH() * 3) / 4));
				currentWave = 1;
				hitCooldown = 0;
				spawnWave(currentWave);
				gameSeed = new Random().nextInt();
			}
		}

		if (level == "game") {

			if (key == 65) {
				left = true;
			} else if (key == 68) {
				right = true;
			} else if (key == 87) {
				up = true;
			} else if (key == 83) {
				down = true;
			}

		}

		if (key == 27) {
			level = "select";
			left = false;
			right = false;
			up = false;
			down = false;
		}

		if (level == "lose") {
			if (key == 32) {
				level = "start";
				p.playmusic("sounds\\snd_select.wav");
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (level == "game") {
			if (key == 65) {
				left = false;
			} else if (key == 68) {
				right = false;
			} else if (key == 87) {
				up = false;
			} else if (key == 83) {
				down = false;
			}
		}

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!(player instanceof S_Sniper sniper && (sniper.isFiring() || sniper.isAbility()))) {
			mouseX = e.getX() + camX;
			mouseY = e.getY() + camY;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!(player instanceof S_Sniper sniper && (sniper.isFiring() || sniper.isAbility()))) {
			mouseX = e.getX() + camX;
			mouseY = e.getY() + camY;
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		if (level == "game") {
			String direction = player.getDirection();
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				if (player.getAtkTimer() == 0) {
					if (player.getWeapon() instanceof W_Blast) {
						int x = player.getX() + player.getW() / 2 - 1;
						int y = player.getY() - 30;

						if (direction.equals("down")) {
							y = player.getY() + player.getH() + 6;
						} else if (direction.equals("left")) {
							x = player.getX() - 18;
							y = player.getY() + player.getH() / 2 - 12;
						} else if (direction.equals("right")) {
							x = player.getX() + player.getW() + 15;
							y = player.getY() + player.getH() / 2 - 12;
						}

						W_Blast blast = new W_Blast(x, y, direction);
						blast.setDirection(direction);
						player.getProjectiles().add(blast);
						player.setAtkTimer(player.getAtkSpd() * 30);
						p.playmusic("sounds\\snd_heartshot.wav", 0.30f);
					} else if (player.getWeapon() instanceof W_Beam) {
						((S_Sniper) player).charge();
						if (player instanceof S_Sniper sniper && sniper.isCharging()) {
							p.loopmusic("sounds\\snd_chargeshot_charge.wav", 0.40f);
						}
					} else if (player.getWeapon() instanceof W_Bomb) {
						player.getProjectiles()
								.add(new W_Bomb((player.getX() + player.getW() / 2 - 24), (player.getY() + 15), 0, 0));
						player.setAtkTimer(player.getAtkSpd() * 30);
						p.playmusic("sounds\\snd_bombfall.wav");
					} else if (player.getWeapon() instanceof W_Dash
							&& !(player instanceof S_Stealth stealth && stealth.isDashing())) {
						((S_Stealth) player).startDash();
						p.playmusic("sounds\\snd_dash.wav");
					} else if (player.getWeapon() instanceof W_Missile) {
						int x = player.getX() + player.getW() / 2 - 12;
						int y = player.getY() - 66;

						if (direction.equals("down")) {
							y = player.getY() + player.getH() + 6;
						} else if (direction.equals("left")) {
							x = player.getX() - 42;
							y = player.getY() + 15;
						} else if (direction.equals("right")) {
							x = player.getX() + player.getW() + 18;
							y = player.getY() + 15;
						}

						W_Missile missile = new W_Missile(x, y, direction);
						missile.setDirection(direction);
						player.getProjectiles().add(missile);
						player.setAtkTimer(player.getAtkSpd() * 30);
						p.playmusic("sounds\\snd_rocket.wav");
					}
				}

			} else if (arg0.getButton() == MouseEvent.BUTTON3) {
				if (player.getAblTimer() == 0) {
					if (player.getAbility() instanceof A_Shield
							&& !(player instanceof S_Fighter fighter && fighter.isShieldActive())) {
						((S_Fighter) player).activateShield();
						p.playmusic("sounds\\snd_spellcast.wav");
					} else if (player.getAbility() instanceof A_Megabeam
							&& !(player instanceof S_Sniper sniper
									&& (sniper.isFiring() || sniper.isCharging() || sniper.isAbility()))) {
						((S_Sniper) player).megaBeam();
						p.playmusic("sounds\\mus_sfx_rainbowbeam_1.wav");
					} else if (player.getAbility() instanceof A_Barrage) {
						player.getProjectiles().add(
								new A_Barrage((player.getX() + player.getW() / 2 - 114), (player.getY() + 80), 0, 0));
						player.getProjectiles().add(
								new A_Barrage((player.getX() + player.getW() / 2 - 24), (player.getY() - 80), 0, 0));
						player.getProjectiles().add(
								new A_Barrage((player.getX() + player.getW() / 2 + 67), (player.getY() + 80), 0, 0));
						player.setAblTimer(player.getAblSpd() * 30);
						p.playmusic("sounds\\snd_bombsfall.wav");
					} else if (player.getAbility() instanceof A_Invisibility
							&& !(player instanceof S_Stealth stealth && stealth.isInvisible())) {
						((S_Stealth) player).invis();
						p.playmusic("sounds\\snd_magicsprinkle.wav");
					} else if (player.getAbility() instanceof A_Nuke) {
						int x = player.getX() + 29;
						int y = player.getY() - 108;

						if (direction.equals("down")) {
							y = player.getY() + player.getH() + 6;
						} else if (direction.equals("left")) {
							x = player.getX() - 73;
							y = player.getY() - 6;
						} else if (direction.equals("right")) {
							x = player.getX() + player.getW() + 29;
							y = player.getY() - 6;
						}

						A_Nuke nuke = new A_Nuke(x, y, direction);
						if (!enemies.isEmpty()) {
							nuke.setTarget(enemies.get(0));
						}
						player.getProjectiles().add(nuke);
						player.setAblTimer(player.getAblSpd() * 30);
						p.playmusic("sounds\\snd_rocket_long.wav");
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		if (level == "game") {
			if (player.getWeapon() instanceof W_Beam && (player instanceof S_Sniper sniper && (sniper.isCharging()))) {
				p.loopmusic("stop");
				((S_Sniper) player).release();
				p.playmusic("sounds\\snd_chargeshot_fire.wav", 0.70f);
			}
		}

	}

}
