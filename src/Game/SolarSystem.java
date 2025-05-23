package Game;

import Game.Constant.PHYSICS_CONSTANT;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class SolarSystem {

    private ArrayList<CelestrialBody> bodies = new ArrayList<>();

    public CelestrialBody root;

    public static double TIMESTEP = PHYSICS_CONSTANT.TIMESTEP_FAST;

    public long delayTimeUntilTimeStepChange = 5000;    //5 second before its timestep will change its speed to default

    private final int EXP_SCALE = (int) (PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE * PHYSICS_CONSTANT.AU / 100);
    private final int MASS_SCALE = (int) Math.pow(10, 15);

    private final int FIRST_PLANET = 0;
    private final int SECOND_PLANET = 1;
    private final int THIRD_PLANET = 2;
    private final int FOURTH_PLANET = 3;
    private final int FIFTH_PLANET = 4;

    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = 25;
        double sunMass = 1.98892e30;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE,
                sunRadius * EXP_SCALE, sunMass, true, null, root,this);
        bodies.add(root);

        generateSolarSystem(root);
   //     TimeChangeThread timeChangeThread = new TimeChangeThread(this);
     //   Thread myThread = new Thread(timeChangeThread);
       // myThread.start();
       TIMESTEP = PHYSICS_CONSTANT.TIMESTEP_DEFAULT;


    }

    public void update() {
        
        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).update(bodies);
        }
        
    }


    // Generate the model of our solar system
    private void generateDefaultSolarSystem(CelestrialBody parent) {

        Color newColor;

        // Generate Mercury celestrial body
        newColor = Color.DARK_GRAY;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.MERCURY_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                8 * EXP_SCALE, 3.30e23, false, newColor, parent,this));

        // Generate Venus celestrial body
        newColor = Color.WHITE;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.VENUS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                14 * EXP_SCALE, 4.8685e24, false, newColor, parent,this));

        // Generate Earth celestrial body
        newColor = Color.BLUE;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.EARTH_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                16 * EXP_SCALE, 5.9742e24, false, newColor, parent,this));

        // Generate Mars celestrial body
        newColor = Color.RED;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.MARS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                12 * EXP_SCALE, 6.39e23, false, newColor, parent,this));
    }

    // Generate random solar system
    private void generateSolarSystem(CelestrialBody parent) {

        Random colorRand = new Random();

        int numberOfOrbit = (int) generateRandomWithSteps(
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMin(),
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMax(), 1);

        for (int i = 0; i < numberOfOrbit; i++) {

            double newRadius = generateRandomWithSteps(
                    PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMin(),
                    PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMax(), 1);

            double newDistanceToSun = 0;
            // First planet's distance to sun
            if (i == FIRST_PLANET)
                newDistanceToSun = generateRandomWithSteps(
                        PHYSICS_CONSTANT.FIRST_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                        PHYSICS_CONSTANT.FIRST_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);
            // Second planet's distance to sun
            else if (i == SECOND_PLANET)
                newDistanceToSun = generateRandomWithSteps(
                        PHYSICS_CONSTANT.SECOND_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                        PHYSICS_CONSTANT.SECOND_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);

            // Third planet's distance to sun
            else if (i == THIRD_PLANET)
                newDistanceToSun = generateRandomWithSteps(
                        PHYSICS_CONSTANT.THIRD_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                        PHYSICS_CONSTANT.THIRD_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);

            else if (i == FOURTH_PLANET)
                newDistanceToSun = generateRandomWithSteps(
                        PHYSICS_CONSTANT.FOURTH_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                        PHYSICS_CONSTANT.FOURTH_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);

            else if (i == FIFTH_PLANET)
                newDistanceToSun = generateRandomWithSteps(
                        PHYSICS_CONSTANT.FITH_PLANET_DISTNACE_TO_SUN_RANGE.getMin(),
                        PHYSICS_CONSTANT.FITH_PLANET_DISTNACE_TO_SUN_RANGE.getMax(), .1);

            double newMass = calculateVolume(newRadius * EXP_SCALE) * MASS_SCALE;

            // Will produce only bright / light colours:
            double r = colorRand.nextFloat() / 2f + 0.5;
            double g = colorRand.nextFloat() / 2f + 0.5;
            double b = colorRand.nextFloat() / 2f + 0.5;
            Color newColor = new Color((float) r, (float) g, (float) b);

            bodies.add(new CelestrialBody((PHYSICS_CONSTANT.AU * newDistanceToSun) + root.pos.x, root.pos.y,
                    newRadius * EXP_SCALE, newMass, false, newColor, parent,this));

        }

    }

    // It returns the sphere volume based on its radius
    /*
     */
    public static double calculateVolume(double radius) {
        return 4 / 3 * Math.PI * Math.pow((radius), 3);
    }

    public static double generateRandomWithSteps(double min, double max, double step) {

        if(max < min){
            double temp = min;
            min = max;
            max = temp;
        }
        
        Random random = new Random();
        double range = (max - min) / step + 1;
        double randomIndex = random.nextDouble(range);

        return min + randomIndex * step;
    }

    public void render(Graphics2D g2) {

        for (CelestrialBody body : bodies) {
            body.render(g2);
        }

    }

}
