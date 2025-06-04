package Game;

import Game.Constant.GAME_CONSTANT;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

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

	// Number of background stars
	private static final int STAR_COUNT = 200;

	// Star container
	private final ArrayList<Star> stars = new ArrayList<>();

	// Enemies container
	private final ArrayList<Enemy> enemies = new ArrayList<>();

	private int[][] intGrid = {
			{ 0, 1, 2 },
			{ 3, 4, 5 },
			{ 6, 7, 8 }
	};

	/*
	 * Constructor for the Orbitor class.
	 * Initializes the player, camera, input handling, and starts the game.
	 * Preparing the game panel.
	 * Sets the preferred size and background color of the game panel.
	 * Adds key and mouse listeners for input handling.
	 */
	public Orbitor() {

		this.player = new Player(GAME_CONSTANT.GAME_WIDTH / 2.0 - 500 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2,
				GAME_CONSTANT.GAME_HEIGHT / 2.0 - 500 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2);
		this.camera = new Camera(GAME_CONSTANT.GAME_WIDTH / 2.0 - 500 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2,
				GAME_CONSTANT.GAME_HEIGHT / 2.0 - 500 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2);
		Input.setCamera(this.camera);

		this.setPreferredSize(new Dimension(GAME_CONSTANT.WINDOW_WIDTH, GAME_CONSTANT.WINDOW_HEIGHT));
		this.setBackground(GAME_CONSTANT.SPACE_COLOR);

		// make sure we can get key events
		this.setFocusable(true);
		this.requestFocusInWindow();

		this.input = new Input();
		this.addKeyListener(this.input);
		this.addMouseListener(this.input);
		this.addMouseMotionListener(this.input);

		// input focus
		this.requestFocus();
		this.StartGame();
		this.setLayout(null);

	}

	private void StartGame() {
		this.loadSolarSystem();
		this.generateStars();
		this.generateEnemies();
		this.gameThread = new Thread(this);
		this.gameThread.start();
	}

	private void generateEnemies() {
		Random rand = new Random();
		for (int i = 0; i < 10; i++)
			this.enemies.add(new Enemy(this.player.pos.x + rand.nextInt(-1000, 1000),
					this.player.pos.y + rand.nextInt(-1000, 1000), (rand.nextDouble() + 1) * 2.5));
	}

	/*
	 * Generate a default solar system with a sun and planets.
	 * This method creates a solar system with a sun at the center and planets
	 * orbiting around it.
	 */
	private void loadSolarSystem() {

		for (int i = 0; i < GAME_CONSTANT.GAME_HEIGHT_GRID; i++) {
			for (int j = 0; j < GAME_CONSTANT.GAME_WIDTH_GRID; j++) {
				SolarSystem solar = new SolarSystem(
						j * GAME_CONSTANT.SOLAR_SYSTEM_SIZE + GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2,
						i * GAME_CONSTANT.SOLAR_SYSTEM_SIZE + GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2);

				this.systems.add(solar);
				System.out.println(solar);

			}
		}

		currentSolarSystem = this.systems.get(4);

		// Create a solar system at the center of the game world
	}

	/*
	 * Populate background stars with random positions and depth.
	 */
	private void generateStars() {
		Random rnd = new Random();
		for (int i = 0; i < STAR_COUNT; i++) {
			float depth = 0.01f + rnd.nextFloat() * 0.1f; // between 0.01 and 0.1
			double x = rnd.nextDouble() * GAME_CONSTANT.WINDOW_WIDTH;
			double y = rnd.nextDouble() * GAME_CONSTANT.WINDOW_HEIGHT;
			this.stars.add(new Star(x, y, depth));
		}
	}

	/*
	 * This method is the main game loop that runs at a fixed frame rate.
	 * It calculates the time delta between frames and updates the game state
	 * accordingly.
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
				this.update(seconds);
				this.repaint();
				accumulator -= nsPerFrame;
			}
		}
	}

	/*
	 * This method updates the game state by following the player with the camera,
	 * updating the player's position, and updating all solar systems.
	 * It is called every frame in the game loop.
	 */
	public void update(double dt) {

		updateCurrentSolarSystem();

		this.camera.follow(this.player, dt);
		this.player.update(dt);

		for (SolarSystem system : this.systems)
			system.update();

		for (Enemy enemy : this.enemies) {
			enemy.follow(player.pos);
			enemy.update(dt);
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
		this.render(g);
	}

	/*
	 * This method renders the game components on the screen.
	 */
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.translate(-this.camera.getX(), -this.camera.getY());

		// FIRST draw all orbits
		g2.setColor(new Color(255, 255, 255, 64)); // translucent white

		// draw stars
		this.drawBackgroundStars(g);
		// draw the fixed planet system
		this.drawSolarSystem(g2);

		this.player.render(g2);

		for (Enemy enemy : this.enemies)
			enemy.render(g2);

		/* ---------- 2) fixed HUD overlay ---------- */
		Graphics2D hud = (Graphics2D) g; // uses panel coords (0,0 at top-left)
		hud.setColor(Color.WHITE);
		hud.setFont(new Font("Consolas", Font.PLAIN, 14));

		String msg = String.format("x: %.0f   y: %.0f   θ: %.0f° vx: %.0f vy: %.0f",
				this.player.pos.x, this.player.pos.y, this.player.angle, this.player.vel.x, this.player.vel.y);
		hud.drawString(msg, 10, 20);

		g2.dispose();
	}

	/*
	 * Draw parallax stars behind everything.
	 */
	private void drawBackgroundStars(Graphics g) {
		g.setColor(Color.WHITE);
		for (Star s : this.stars) {
			int sx = (int) ((s.pos.x - this.camera.getX() * s.depth) % GAME_CONSTANT.WINDOW_WIDTH);
			int sy = (int) ((s.pos.y - this.camera.getY() * s.depth) % GAME_CONSTANT.WINDOW_HEIGHT);
			if (sx < 0)
				sx += GAME_CONSTANT.WINDOW_WIDTH;
			if (sy < 0)
				sy += GAME_CONSTANT.WINDOW_HEIGHT;
			g.fillRect(sx, sy, 2, 2);
		}
	}

	// This method draws all solar systems in the game.
	private void drawSolarSystem(Graphics2D g2d) {
		for (SolarSystem system : this.systems)
			system.render(g2d);
	}

	// Getter method to retrieve the currentSolarSystem;
	private void updateCurrentSolarSystem() {

		int x = (int) player.pos.x / GAME_CONSTANT.SOLAR_SYSTEM_SIZE;
		int y = (int) player.pos.y / GAME_CONSTANT.SOLAR_SYSTEM_SIZE;

		int gridIndex = intGrid[y][x];

		currentSolarSystem = this.systems.get(gridIndex);
	}

}