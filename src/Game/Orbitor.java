package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import Game.utils.*;
import Game.Constant.GAME_CONSTANT;

public class Orbitor extends JPanel implements ActionListener, Runnable {
	private boolean runGame = true;
	private Input input;
	private Player player;
	private Camera camera;

	// JLabel coord = new JLabel("", 20);

	Thread gameThread;

	ArrayList<CelestrialBody> celestrialBodies = new ArrayList<CelestrialBody>();
	CelestrialBody rootCelestrialBody;
	private final int MAX_NUM_PLANET = 100;

	private final ArrayList<float[]> orbitCoords = new ArrayList<>();

	Orbitor() {

		setPreferredSize(new Dimension(GAME_CONSTANT.WINDOW_WIDTH, GAME_CONSTANT.WINDOW_HEIGHT));
		this.setBackground(GAME_CONSTANT.SPACE_COLOR);

		// make sure we can get key events
		setFocusable(true);
		requestFocusInWindow();

		input = new Input();
		addKeyListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		// Input focus
		requestFocus();

		initClasses();

		StartGame();
		this.setLayout(null);

	}

	private void initClasses() {
		player = new Player(GAME_CONSTANT.GAME_WIDTH / 2, GAME_CONSTANT.GAME_HEIGHT / 2);
		camera = new Camera(0, 0);

		Input.setCamera(camera);
	}

	private void StartGame() {
		GenerateCelestrialBody();
		gameThread = new Thread(this);
		gameThread.start();

	}

	public void GenerateCelestrialBody() {

		GenerateCelestrialBody(GAME_CONSTANT.GAME_WIDTH / 2, GAME_CONSTANT.GAME_HEIGHT / 2, 4000, 10000, true, true,
				30000, 6);

		rootCelestrialBody = celestrialBodies.get(0);
	}

	public void GenerateCelestrialBody(int x, int y,
			int radius, int mass,
			boolean isSun, boolean isRoot,
			float distance, int level) {
		if (level == 0)
			return;

		Random rand = new Random();

		// decide how many rings (1–3 on 20% chance)
		int rings = 2;
		if (!isRoot && rand.nextFloat() < 0.20f) {
			rings = rand.nextInt(2) + 1;
		}
		
		// add the “body” itself (same as before)…
		CelestrialBody parent = new CelestrialBody(x, y, radius, mass, isSun, isRoot,new Color((int)(Math.random() * 0x1000000)));
		celestrialBodies.add(parent);
		if (isRoot)
			rootCelestrialBody = parent;

		// now for each ring…
		for (int ring = 1; ring <= rings; ring++) {
			float ringDist = distance / (ring*1.5f);
			int sats = rand.nextInt(2) + 1; // 1–3 satellites

			for (int i = 0; i < sats; i++) {
				double angle = rand.nextDouble() * Math.PI * 2;
				int childX = x + (int) (Math.cos(angle) * ringDist);
				int childY = y + (int) (Math.sin(angle) * ringDist);
				int childR = radius / 2;
				int childM = mass / 2;

				// RECORD this orbit’s center + radius
				
				if(level >= 5)
				orbitCoords.add(new float[] { x, y, ringDist - distance / 50 });

				orbitCoords.add(new float[] { x, y, ringDist });
				
					if(level >= 5)
				orbitCoords.add(new float[] { x, y, ringDist + distance / 50 });

				// create the satellite
				CelestrialBody sat = new CelestrialBody(
						childX, childY,
						childR, childM,
						false, false,
						new Color((int)(Math.random() * 0x1000000)));
				celestrialBodies.add(sat);



				// recurse
				GenerateCelestrialBody(
						childX, childY,
						childR, childM,
						false, false,
						ringDist / 1.5f,
						level - 1);
			}
		}
	}

	@Override
	public void run() {

		final double nsPerSecond = 1_000_000_000.0;
		long lastTime = System.nanoTime();
		double accumulator = 0;
		double nsPerFrame = nsPerSecond / GAME_CONSTANT.FPS_SET;

		while (runGame) {
			long now = System.nanoTime();
			double delta = now - lastTime;
			lastTime = now;
			accumulator += delta;

			// only update & repaint when enough time has passed
			while (accumulator >= nsPerFrame) {
				double seconds = nsPerFrame / nsPerSecond;
				update((float) seconds);
				repaint();
				accumulator -= nsPerFrame;
			}
		}
	}

	public void update(float deltaSeconds) {

		camera.tick(player);

		player.update(deltaSeconds);

		// coord.setText(player.pos.toString());

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
		for (float[] oc : orbitCoords) {
			float cx = oc[0], cy = oc[1], r = oc[2];
			g2.drawOval((int)(cx - r), (int)(cy - r), (int)(r * 2), (int)(r * 2));
		}

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

		for (CelestrialBody body : celestrialBodies) {
			body.render(g2d);

		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

	}

}