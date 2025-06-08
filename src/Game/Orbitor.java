package Game;

import Game.Constant.GAME_CONSTANT;
import Game.GameState;
import Game.ui.GameMenu;
import Game.ui.InstructionMenu;
import Game.ui.SettingsMenu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Orbitor class represents the main game panel where the player can navigate
 * through a solar system.
 * It handles the game loop, input, rendering, and updates for the player and
 * solar systems.
 * It extends JPanel and implements Runnable to create a game thread.
 * The game starts with a player at the center of the screen and a camera that
 * follows the player.
 * The solar system is loaded with a root celestrial body (the sun) and other
 * celestrial bodies (planets).
 * The game updates the player position, camera position, and solar system every
 * frame.
 * It also renders the player and solar system on the screen.
 * The game runs at a fixed frame rate defined by GAME_CONSTANT.FPS_SET.
 * The game panel is set to a preferred size and background color defined in
 * GAME_CONSTANT.
 * The game can be extended to include more features such as collision
 * detection, particle effects, and more complex solar systems.
 * The game uses a separate thread to run the game loop, allowing for smooth
 * updates and rendering.
 */
public class Orbitor extends JPanel implements Runnable {
	final private Input input;
	final private Player player;
	final private Camera camera;

	private Thread gameThread;
	final private ArrayList<SolarSystem> solarSystems = new ArrayList<SolarSystem>();

	// Game menu
	final private GameMenu gameMenu;
	final private InstructionMenu instructionMenu;
	final private SettingsMenu settingsMenu;

	// Number of background stars
	private static final int STAR_COUNT = 200;

	// Star container
	private final ArrayList<Star> stars = new ArrayList<>();

	// Enemies container
	private final ArrayList<Enemy> enemies = new ArrayList<>();

	// Package delivery management system
	private final PackageManager packageManager = new PackageManager();

	/**
	 * Constructor for the Orbitor class.
	 * Initializes the player, camera, input handling, and starts the game.
	 * Preparing the game panel.
	 * Sets the preferred size and background color of the game panel.
	 * Adds key and mouse listeners for input handling.
	 */
	public Orbitor() {

		this.player = new Player(GAME_CONSTANT.GAME_WIDTH / 2.0 - 1000 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2.0,
				GAME_CONSTANT.GAME_HEIGHT / 2.0 - 1000 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2.0);
		this.camera = new Camera(GAME_CONSTANT.GAME_WIDTH / 2.0 - 1000 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2.0,
				GAME_CONSTANT.GAME_HEIGHT / 2.0 - 1000 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2.0);
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
		this.addMouseWheelListener(this.input);

		// input focus
		this.requestFocus();
		this.StartGame();
		this.setLayout(null);

		// Initialize menus
		gameMenu = new GameMenu();
		instructionMenu = new InstructionMenu();
		settingsMenu = new SettingsMenu();

		// Add slider and label
		this.add(settingsMenu.audioSlider);
		this.add(settingsMenu.volumeLabel);
		settingsMenu.audioSlider.setVisible(false);
		settingsMenu.volumeLabel.setVisible(false);

		// --- MUSIC SETUP ---
		MusicManager.init();

	}

	private void StartGame() {
		this.loadSolarSystem();
		this.generateStars();
		this.generateEnemies();
		this.gameThread = new Thread(this);
		this.gameThread.start();
		this.packageManager.generateNextPackage(solarSystems);
	}

	private void generateEnemies() {
		Random rand = new Random();
		for (int i = 0; i < 10; i++)
			this.enemies.add(new Enemy(this.player.pos.x + rand.nextInt(-1000, 1000),
					this.player.pos.y + rand.nextInt(-1000, 1000), (rand.nextDouble() + 1) * 2.5));
	}

	/**
	 * Generate a default solar system with a sun and planets.
	 * This method creates a solar system with a sun at the center and planets
	 * orbiting around it.
	 */
	private void loadSolarSystem() {

		// SolarSystem solar = new SolarSystem((int)(GAME_CONSTANT.GAME_WIDTH / 2.0 -
		// GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2.0),
		// (int)(GAME_CONSTANT.GAME_HEIGHT / 2.0 - GAME_CONSTANT.SOLAR_SYSTEM_SIZE /
		// 2.0));
		// this.solarSystems.add(solar);

		for (int i = 0; i < GAME_CONSTANT.GAME_HEIGHT_GRID; i++) {
			for (int j = 0; j < GAME_CONSTANT.GAME_WIDTH_GRID; j++) {
				SolarSystem solar = new SolarSystem(
						j * GAME_CONSTANT.SOLAR_SYSTEM_SIZE + GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2,
						i * GAME_CONSTANT.SOLAR_SYSTEM_SIZE + GAME_CONSTANT.SOLAR_SYSTEM_SIZE / 2);

				this.solarSystems.add(solar);
				System.out.println(solar.getRoot().getPos());
			}
		}
	}

	/**
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

	/**
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

		GameState currGameState = GameState.MENU;

		// Game loop
		while (true) {

			long now = System.nanoTime();
			double delta = now - lastTime;
			lastTime = now;
			accumulator += delta;

			// Main Game
			// only update & repaint when enough time has passed
			while (accumulator >= nsPerFrame) {

				double seconds = nsPerFrame / nsPerSecond;
				this.update(seconds);
				this.repaint();

				accumulator -= nsPerFrame;

			}

		}
	}

	/**
	 * This method updates the game state by following the player with the camera,
	 * updating the player's position, and updating all solar systems.
	 * It is called every frame in the game loop.
	 */
	public void update(double dt) {

		// Show/hide slider based on state
        boolean show = (GameState.state == GameState.SETTINGS);
        settingsMenu.audioSlider.setVisible(show);
        settingsMenu.volumeLabel.setVisible(show);

		// Update in acoordance to the current game state
		if (GameState.state == GameState.PLAYING) {
			SolarSystem currentSolarSystem = getCurrentSolarSystem();

			this.camera.update(this.player, dt);
			this.player.update(enemies, currentSolarSystem, dt);

			for (SolarSystem system : this.solarSystems)
				system.update();

			for (Enemy enemy : this.enemies) {
				enemy.followAndAttack(player);
				enemy.update(dt);
			}

		}  else if (GameState.state == GameState.INSTRUCTION) {
            instructionMenu.update();
        } else if (GameState.state == GameState.MENU) {


        } else if (GameState.state == GameState.SETTINGS) {
            settingsMenu.update();
        } else if (GameState.state == GameState.QUIT) {
            System.exit(0);
        }

	}

	/**
	 * This method is called to paint the game components on the screen.
	 * It uses a Graphics2D object to render the player and solar system.
	 * It also draws a fixed HUD overlay with player information.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.render(g);
	}

	/**
	 * This method renders the game components on the screen.
	 */
	public void render(Graphics g) {
		if (GameState.state == GameState.PLAYING) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.scale(this.camera.getScale().x, this.camera.getScale().y);
			g2.translate(-this.camera.getX() + GAME_CONSTANT.WINDOW_WIDTH / camera.getScale().x / 2.0,
					-this.camera.getY() + GAME_CONSTANT.WINDOW_HEIGHT / camera.getScale().y / 2.0);

			// draw all orbits
			g2.setColor(new Color(255, 255, 255, 64)); // translucent white

			// draw stars
			this.drawBackgroundStars(g);

			// draw the fixed planet system
			this.drawSolarSystems(g2);

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

			this.player.renderHealthBar(hud);
			this.packageManager.renderPackageArrow(this.player, hud);
			// dispose of graphics
			g2.dispose();
		} else if (GameState.state == GameState.MENU) {
            gameMenu.render(g);
        } else if (GameState.state == GameState.INSTRUCTION) {
            // Instruction menu
            instructionMenu.render(g);
        } else if (GameState.state == GameState.SETTINGS) {
            settingsMenu.render(g);
        }


	}

	/**
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
	private void drawSolarSystems(Graphics2D g2d) {
		SolarSystem currSolarSystem = this.getCurrentSolarSystem();

		if (currSolarSystem != null)
			currSolarSystem.render(g2d);
	}

	// Getter method to retrieve the currentSolarSystem;
	private SolarSystem getCurrentSolarSystem() {
		int x = (int) player.pos.x / GAME_CONSTANT.SOLAR_SYSTEM_SIZE;
		int y = (int) player.pos.y / GAME_CONSTANT.SOLAR_SYSTEM_SIZE;

		if (x < 0 || x > 2 || y < 0 || y > 2)
			return null;

		int gridIndex = y * 3 + x;
		return this.solarSystems.get(gridIndex);
	}
}