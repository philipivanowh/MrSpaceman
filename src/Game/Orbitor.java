package Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import Game.Constant.GAME_CONSTANT;

public class Orbitor extends JPanel implements Runnable {
	final private Input input;
	final private Player player;
	final private Camera camera;

	Thread gameThread;
	ArrayList<SolarSystem> celestrialBodies = new ArrayList<SolarSystem>();

	private SolarSystem currentSolarSystem;

	public Orbitor() {
		player = new Player(GAME_CONSTANT.GAME_WIDTH / 2, GAME_CONSTANT.GAME_HEIGHT / 2);
		camera = new Camera(0, 0);
		Input.setCamera(camera);

		setPreferredSize(new Dimension(GAME_CONSTANT.WINDOW_WIDTH, GAME_CONSTANT.WINDOW_HEIGHT));
		this.setBackground(GAME_CONSTANT.SPACE_COLOR);

		// make sure we can get key events
		setFocusable(true);
		requestFocusInWindow();

		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		// input focus
		requestFocus();
		StartGame();
		this.setLayout(null);
	}

	private void StartGame() {
		//GenerateCelestrialBody();
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		final double nsPerSecond = 1_000_000_000.0;
		long lastTime = System.nanoTime();
		double accumulator = 0;
		double nsPerFrame = nsPerSecond / GAME_CONSTANT.FPS_SET;

		while (true) {
			long now = System.nanoTime();
			double delta = now - lastTime;
			lastTime = now;
			accumulator += delta;

			// only update & repaint when enough time has passed
			while (accumulator >= nsPerFrame) {
				double seconds = nsPerFrame / nsPerSecond;
				update((double) seconds);
				repaint();
				accumulator -= nsPerFrame;
			}
		}
	}

	public void update(double deltaSeconds) {
		camera.follow(player);
		player.update(deltaSeconds);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.translate(-camera.getX(), -camera.getY());

		// FIRST draw all orbits
		g2.setColor(new Color(255, 255, 255, 64)); // translucent white

		// draw the fixed planet system
		drawPlanet(g2);

		player.render(g2);

		/* ---------- 2) fixed HUD overlay ---------- */
		Graphics2D hud = (Graphics2D) g; // uses panel coords (0,0 at top-left)
		hud.setColor(Color.WHITE);
		hud.setFont(new Font("Consolas", Font.PLAIN, 14));

		String msg = String.format("x: %.0f   y: %.0f   θ: %.1f°",
				player.pos.x, player.pos.y, player.angle);
		hud.drawString(msg, 10, 20);

		g2.dispose();
	}

	/**
	 * Recursively draws a planet (filled circle) at (x,y) with the given radius.
	 * Then spawns a few smaller child "planets" around it.
	 *
	 * @param g2d    the graphics context
	 * @param x      center X
	 * @param y      center Y
	 * @param radius radius of this planet
	 * @param depth  how many more levels to recurse
	 */
	private void drawPlanet(Graphics2D g2d) {
		for (SolarSystem body : celestrialBodies) {
			body.render(g2d);
		}
	}

}