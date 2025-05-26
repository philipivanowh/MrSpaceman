package Game;

import Game.Constant.GAME_CONSTANT;
import Game.Constant.PHYSICS_CONSTANT;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

/*
 * Orbitor class represents the main game panel where the player can navigate through a solar system.
 * It handles the game loop, input, rendering, and updates for the player and solar systems.
 * It extends JPanel and implements Runnable to create a game thread.
 * The game starts with a player at the center of the screen and a camera that follows the player.
 * The solar system is loaded with a root celestrial body (the sun) and other celestrial bodies (planets).
 * The game updates the player position, camera position, and solar system every frame.
 * It also renders the player and solar system on the screen.
 * The game runs at a fixed frame rate defined by GAME_CONSTANT.FPS_SET.
 * The game panel is set to a preferred size and background color defined in GAME_CONSTANT.
 * The game can be extended to include more features such as collision detection, particle effects, and more complex solar systems.
 * The game uses a separate thread to run the game loop, allowing for smooth updates and rendering.
 */
public class Orbitor extends JPanel implements Runnable {
	final private Input input;
	final private Player player;
	final private Camera camera;

	Thread gameThread;
	ArrayList<SolarSystem> systems = new ArrayList<SolarSystem>();

	public static SolarSystem currentSolarSystem;

	/*
	 * Constructor for the Orbitor class.
	 * Initializes the player, camera, input handling, and starts the game.
	 * Preparing the game panel.
	 * Sets the preferred size and background color of the game panel.
	 * Adds key and mouse listeners for input handling.
	 */
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

	/*
	 * Generate a default solar system with a sun and planets.
	 * This method creates a solar system with a sun at the center and planets orbiting around it.
	 */
	private void loadSolarSystem(){
		SolarSystem solar = new SolarSystem(GAME_CONSTANT.GAME_WIDTH/2 ,GAME_CONSTANT.GAME_HEIGHT/2 );
		systems.add(solar);
		currentSolarSystem = solar;
	}

	/*
	 * This method is the main game loop that runs at a fixed frame rate.
	 * It calculates the time delta between frames and updates the game state accordingly.
	 * It also handles rendering the game components on the screen.
	 */
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

	/*
	 * This method updates the game state by following the player with the camera,
	 * updating the player's position, and updating all solar systems.
	 * It is called every frame in the game loop.
	 */
	public void update(double deltaSeconds) {
		camera.follow(player);
		player.update(deltaSeconds);

		for(SolarSystem system : systems){
			system.update();
		}
	}

	/*
	 * This method is called to paint the game components on the screen.
	 * It uses a Graphics2D object to render the player and solar system.
	 * It also draws a fixed HUD overlay with player information.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}

	/*
	 * This method renders the game components on the screen.
	 */
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

		String msg = String.format("x: %.0f   y: %.0f   θ: %.1f° vx: %.0f vy: %.0f",
				player.pos.x, player.pos.y, player.angle, player.vel.x, player.vel.y);
		hud.drawString(msg, 10, 20);

		g2.dispose();
	}

	// This method draws all solar systems in the game.
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