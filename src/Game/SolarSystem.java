package Game;

import Game.Constant.PHYSICS_CONSTANT;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class SolarSystem {

    private ArrayList<CelestrialBody> bodies = new ArrayList<>();

    private CelestrialBody root;

    private final int MAX_ORBIT = 4;
    private final int MIN_ORBIT = 4;


    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = 30;
        double sunMass = 1.98892e30;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE , sunRadius, sunMass, true, null,root);
        bodies.add(root);


        bodies.add(new CelestrialBody((-1 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 16, 5.9742e24, false, null,root));

        bodies.add(new CelestrialBody((-1.524  * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 12, 6.39e23, false, null,root));

        bodies.add(new CelestrialBody((0.387  * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 8, 3.30e23, false, null,root));
        
        bodies.add(new CelestrialBody((0.723   * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 14, 4.8685e24, false, null,root));

    }

    public void update() {

        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).update(bodies);
        }
    }

    private void generateSolarSystem(CelestrialBody parent, double distance) {

        Random rand = new Random();

        // The number of orbit can go from 1 to 4
        int numOrbit = rand.nextInt(4 - 1) + 1 + 1;
        numOrbit = 1;
        for (int i = 1; i <= numOrbit; i++) {

            double newAngle = rand.nextDouble(2 * Math.PI);
            newAngle = Math.PI / 2;
            int x = (int) ((Math.cos(newAngle) * distance * i) + parent.pos.x);
            int y = (int) ((Math.sin(newAngle) * distance * i) + parent.pos.y);

            int newRadius = rand.nextInt(2 * parent.radius / 3 - 10) + 1 + 10;
            // Making mass based on its volume
            double newMass =  (4 / 3 * Math.PI * newRadius * newRadius * newRadius);

            CelestrialBody newBody = new CelestrialBody(x, y, newRadius, newMass, false, null, root);
            bodies.add(newBody);

        }

    }

    public void render(Graphics2D g2) {

        for (CelestrialBody body : bodies) {
            body.render(g2);
        }

    }

}
