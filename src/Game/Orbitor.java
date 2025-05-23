package Game;

import Game.Constant.GAME_CONSTANT;
import Game.Constant.PHYSICS_CONSTANT;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Orbitor extends JPanel implements Runnable {
	final private Input input;
	final private Player player;
	final private Camera camera;

	Thread gameThread;
	ArrayList<SolarSystem> systems = new ArrayList<SolarSystem>();

	public static SolarSystem currentSolarSystem;

	public Orbitor() {

		player = new Player((GAME_CONSTANT.GAME_WIDTH / 2)-1000, (GAME_CONSTANT.GAME_HEIGHT / 2) -1000);
		camera = new Camera(GAME_CONSTANT.GAME_WIDTH / 2, GAME_CONSTANT.GAME_HEIGHT / 2);
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
		loadSolarSystem();
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void loadSolarSystem(){
		SolarSystem solar = new SolarSystem(GAME_CONSTANT.GAME_WIDTH/2 ,GAME_CONSTANT.GAME_HEIGHT/2 );
		systems.add(solar);
		currentSolarSystem = solar;
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

		for(SolarSystem system : systems){
			system.update();
		}
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
		drawSolarSystem(g2);

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

	private void drawSolarSystem(Graphics2D g2d) {
		for (SolarSystem system : systems) {
			system.render(g2d);
		}
	}

	//Getter method to retrieve the currentSolarSystem;
	private SolarSystem getCurrentSolarSystem(){
		return currentSolarSystem;
	}

}