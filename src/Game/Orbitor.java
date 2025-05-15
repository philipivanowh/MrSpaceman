package Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Orbitor extends JPanel implements ActionListener, Runnable {
	private boolean runGame = true;
	private Input input;
	private Player player;
	private Camera camera;

	//JLabel coord = new JLabel("", 20);

	Thread gameThread;

	ArrayList<Planet> planets = new ArrayList<Planet>();
	private final int MAX_NUM_PLANET = 100;

	// Player ship

	Orbitor() {

		setPreferredSize(new Dimension(Constant.WINDOW_WIDTH, Constant.WINDOW_HEIGHT));
		this.setBackground(Constant.SPACE_COLOR);

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
		player = new Player(Constant.GAME_WIDTH/2, Constant.GAME_HEIGHT/2);
		camera = new Camera(0,0);

		planets.add(new Planet(1000, 1000, 100));
		
		planets.add(new Planet(6000, 200,  500));
		
		planets.add(new Planet(2000, 3000,  200));
		
		planets.add(new Planet(100, 500,  700));
		
		planets.add(new Planet(4000, 1000, 1000));
		
		planets.add(new Planet(1000, 7100, 600));
		
		planets.add(new Planet(3000, 2000, 100));
		
		planets.add(new Planet(5000, 2000, 1000));
		
		planets.add(new Planet(1000,1000,100));


		 Input.setCamera(camera);
	}

	private void StartGame() {
		//GeneratePlanetCoord();
		gameThread = new Thread(this);
		gameThread.start();

	}

	public void GeneratePlanetCoord() {
		for(int i = 0; i < MAX_NUM_PLANET; i++){
			Random planetRandX = new Random();
			Random planetRandY = new Random();
			Random planetRandsize = new Random();

			Planet newPlanet = new Planet(planetRandX.nextInt(Constant.GAME_WIDTH-100),planetRandY.nextInt(Constant.GAME_HEIGHT-100),planetRandsize.nextInt(1000-100 + 1) + 100);
			planets.add(newPlanet);

		}
	}



	@Override
	public void run() {

		final double nsPerSecond = 1_000_000_000.0;
		long lastTime = System.nanoTime();
		double accumulator = 0;
		double nsPerFrame = nsPerSecond / Constant.FPS_SET;

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

	//	coord.setText(player.pos.toString());

	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		 g2.translate(-camera.getX(), -camera.getY());
		 // draw the fixed planet system
        drawPlanet(g2);

		player.render(g2);

		  /* ---------- 2) fixed HUD overlay ---------- */
    Graphics2D hud = (Graphics2D) g;                   // uses panel coords (0,0 at top-left)
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
	private void drawPlanet(Graphics2D g2d){

		
		
		for(Planet planet : planets){
			planet.render(g2d);
		}
		


	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();

		
	}

	

}