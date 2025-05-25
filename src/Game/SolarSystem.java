package Game;

import Game.Constant.CELESTRIAL_BODY_TYPE;
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

    public long delayTimeUntilTimeStepChange = 5000;    //5 second before its timestep will change its speed to default

    private final int EXP_SCALE = (int) (PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE * PHYSICS_CONSTANT.AU / 100);

    private final int FIRST_ORBIT = 0;
    private final int SECOND_ORBIT = 1;
    private final int THIRD_ORBIT = 2;
    private final int FOURTH_ORBIT = 3;
    private final int FIFTH_ORBIT = 4;

    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = 30;
        double sunMass = 1.98892e30;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE,
                sunRadius * EXP_SCALE, sunMass, Constant.CELESTRIAL_BODY_TYPE.SUN, Color.ORANGE, root,this);
        bodies.add(root);

        // generateDefaultSolarSystem(root);
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

        // Generate Mercury celestrial body
        newColor = Color.DARK_GRAY;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.MERCURY_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                8 * EXP_SCALE, 3.30e23, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));

        // Generate Venus celestrial body
        newColor = Color.WHITE;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.VENUS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                14 * EXP_SCALE, 4.8685e24, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));

        // Generate Earth celestrial body
        newColor = Color.BLUE;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.EARTH_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                16 * EXP_SCALE, 5.9742e24, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));

        // Generate Mars celestrial body
        newColor = Color.RED;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.MARS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                12 * EXP_SCALE, 6.39e23, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));
    }

    // Generate random solar system
    private void generateSolarSystem(CelestrialBody parent) {

        Random colorRand = new Random();

        int numberOfOrbit = (int) generateRandomWithSteps(
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMin(),
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMax(),
                1);

        for (int i = 0; i < numberOfOrbit; i++) {

            double newRadius = generateRandomWithSteps(PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMin(),
                    PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMax(), 1);
            double newDistanceToSun=0;

            if(i == FIRST_ORBIT){
             newDistanceToSun = generateRandomWithSteps(PHYSICS_CONSTANT.FIRST_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                    PHYSICS_CONSTANT.FIRST_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            }else if(i == SECOND_ORBIT){
                 newDistanceToSun = generateRandomWithSteps(PHYSICS_CONSTANT.SECOND_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                    PHYSICS_CONSTANT.SECOND_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            }else if(i == THIRD_ORBIT){
                 newDistanceToSun = generateRandomWithSteps(PHYSICS_CONSTANT.THIRD_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                    PHYSICS_CONSTANT.THIRD_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            }else if(i == FOURTH_ORBIT){
                 newDistanceToSun = generateRandomWithSteps(PHYSICS_CONSTANT.FOURTH_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                    PHYSICS_CONSTANT.FOURTH_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            }else if(i == FIFTH_ORBIT){
                 newDistanceToSun = generateRandomWithSteps(PHYSICS_CONSTANT.FIFTH_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                    PHYSICS_CONSTANT.FIFTH_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            }

            // double newMass = (4.0 / 3.0) * Math.PI * newRadius * newRadius * newRadius *
            // EXP_SCALE;
            double newMass = calculateSurfaceAreaOfBody(newRadius*EXP_SCALE)*1e15;
            // Will produce only bright / light colours:
            double r = colorRand.nextFloat() / 2f + 0.5;
            double g = colorRand.nextFloat() / 2f + 0.5;
            double b = colorRand.nextFloat() / 2f + 0.5;
            Color newColor = new Color((float) r, (float) g, (float) b);

            bodies.add(new CelestrialBody((PHYSICS_CONSTANT.AU * newDistanceToSun) + root.pos.x, root.pos.y,
                    newRadius * EXP_SCALE, newMass, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));


        }

    }

    public static double calculateSurfaceAreaOfBody(double radius){
        return 4/3 * Math.PI * radius * radius * radius;
    }

    /** Returns a random number in [min, max] with the given step size. */
    public static double generateRandomWithSteps(double min,
            double max,
            double step) {
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

    public ArrayList<CelestrialBody> getCelestrialBodies() {
        return bodies;
    }
    public CelestrialBody getRoot() {
        return root;
    }

}
