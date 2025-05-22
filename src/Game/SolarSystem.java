package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Range;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SolarSystem {

    private ArrayList<CelestrialBody> bodies = new ArrayList<>();

    private CelestrialBody root;

    private final int EXP_SCALE = (int) (PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE * PHYSICS_CONSTANT.AU / 100);

    private final Range DISTANCE_TO_SUN_RANGE = new Range(0.48, 1.2);

    private final Range CELESTRIAL_BODY_RADIUS_RANGE = new Range(8, 17);

    private final Range AMOUNT_OF_ORBIT_RANGE = new Range(3, 5);

    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = 30;
        double sunMass = 1.98892e30;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE,
                sunRadius * EXP_SCALE, sunMass, true, null, root);
        bodies.add(root);


     //   generateDefaultSolarSystem(root);
        generateSolarSystem(root);
    }

    public void update() {

        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).update(bodies);
        }
    }

    // Generate the model of our solar system
    private void generateDefaultSolarSystem(CelestrialBody parent) {

        
        Random rand = new Random();
        // Will produce only bright / light colours:
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        Color newColor = new Color((float) r, (float) g, (float) b);

        System.out.println(EXP_SCALE);
        bodies.add(new CelestrialBody((-1.2 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 16 * EXP_SCALE, 5.9742e24,
                false, newColor, parent));
        
        System.out.println(bodies.get(1).pos.x*PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE + " " + bodies.get(0).pos.y*PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE);

        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float) r, (float) g, (float) b);

        bodies.add(new CelestrialBody((-1.724 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 12 * EXP_SCALE, 6.39e23,
                false, newColor, parent));

        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float) r, (float) g, (float) b);
        bodies.add(new CelestrialBody((-0.587 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 8 * EXP_SCALE, 3.30e23,
                false, newColor, parent));

        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float) r, (float) g, (float) b);
        bodies.add(new CelestrialBody((-0.823 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 14 * EXP_SCALE,
                4.8685e24, false, newColor, parent));

    }

    // Generate random solar system
    private void generateSolarSystem(CelestrialBody parent) {

        Random colorRand = new Random();

        int numberOfOrbit = (int) generateRandomWithSteps(
            AMOUNT_OF_ORBIT_RANGE.getMin(),
            AMOUNT_OF_ORBIT_RANGE.getMax(),
             1);

        System.out.println(numberOfOrbit);

        double previousBodyRadius = 0;

        for (int i = 1; i <= numberOfOrbit; i++) {

            double newRadius = generateRandomWithSteps(CELESTRIAL_BODY_RADIUS_RANGE.getMin(),
                    CELESTRIAL_BODY_RADIUS_RANGE.getMax(), 1);
            double newDistanceToSun = generateRandomWithSteps(DISTANCE_TO_SUN_RANGE.getMin(),
                    DISTANCE_TO_SUN_RANGE.getMax(), .1);

           

          //  double newMass = (4.0 / 3.0) * Math.PI * newRadius * newRadius * newRadius * EXP_SCALE;
            double newMass = 5.9742e24;
            // Will produce only bright / light colours:
            double r = colorRand.nextFloat() / 2f + 0.5;
            double g = colorRand.nextFloat() / 2f + 0.5;
            double b = colorRand.nextFloat() / 2f + 0.5;
            Color newColor = new Color((float) r, (float) g, (float) b);

             //This creates the spacing between the orbit require to compensate for the size of the planet
             //This offset is necessary to prevent two planets spawning close to one another
            double xPlanetOffset = 2*(newRadius+previousBodyRadius) * EXP_SCALE * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE;

            double xCord = ((PHYSICS_CONSTANT.AU * newDistanceToSun) + root.pos.x) +xPlanetOffset;

            bodies.add(new CelestrialBody(xCord, root.pos.y,
                    newRadius * EXP_SCALE, newMass, false, newColor, parent));
            
            sortBodiesInOrderByDistanceToSun(bodies);

            previousBodyRadius = bodies.get(i).radius/EXP_SCALE;



        }

         System.out.println(bodies);

    }

    //Peforms a Selection Sort based on its distance from the sun
    public static void sortBodiesInOrderByDistanceToSun(ArrayList<CelestrialBody> bodies){

     int n = bodies.size();
        for (int i = 0; i < n - 1; i++) {
            // Assume the i-th position has the smallest x
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (bodies.get(j).pos.x < bodies.get(minIndex).pos.x) {
                    minIndex = j;
                }
            }
            // Swap the found minimum into position i
            if (minIndex != i) {
                CelestrialBody tmp = bodies.get(i);
                bodies.set(i, bodies.get(minIndex));
                bodies.set(minIndex, tmp);
            }
        }

    }

    /** Returns a random number in [min, max] with the given step size. */
    public static double generateRandomWithSteps(double min,
            double max,
            double step) {
        if (!Double.isFinite(step) || step <= 0)
            throw new IllegalArgumentException("step must be finite and > 0");
        if (!Double.isFinite(min) || !Double.isFinite(max))
            throw new IllegalArgumentException("min/max must be finite");
        if (max < min) { // swap if caller mixed them up
            double tmp = max;
            max = min;
            min = tmp;
        }

        int steps = (int) Math.floor((max - min) / step + 1); // always ≥ 1 now
        int rnd = ThreadLocalRandom.current().nextInt(steps); // faster & thread-safe
        return min + rnd * step;
    }

    public void render(Graphics2D g2) {

        for (CelestrialBody body : bodies) {
            body.render(g2);
        }

    }

}
