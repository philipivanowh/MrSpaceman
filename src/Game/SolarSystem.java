package Game;

import Game.Constant.PHYSICS_CONSTANT;
import Game.utils.Range;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

public class SolarSystem {

    private ArrayList<CelestrialBody> bodies = new ArrayList<>();

    private CelestrialBody root;

    private final int EXP_SCALE = (int)(PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE * PHYSICS_CONSTANT.AU / 100);

    private final Range DISTANCE_TO_SUN_RANGE = new Range(0.58, 1.2);

    private final Range CELESTRIAL_BODY_RADIUS_RANGE = new Range(8, 20);

    private final Range AMOUNT_OF_ORBIT_RANGE = new Range(1,4);


    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = 30;
        double sunMass = 1.98892e30;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE , sunRadius*EXP_SCALE, sunMass, true, null,root);
        bodies.add(root);

        generateDefaultSolarSystem(root);
    }

    public void update() {

        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).update(bodies);
        }
    }

    //Generate the model of our solar system
    private void generateDefaultSolarSystem(CelestrialBody parent){

        Random rand = new Random();
        // Will produce only bright / light colours:
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        Color newColor = new Color((float)r,(float)g,(float)b);


        bodies.add(new CelestrialBody((-1.2 * PHYSICS_CONSTANT.AU) + root.pos.x, root.pos.y, 16*EXP_SCALE, 5.9742e24, false, newColor,parent));

        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float)r,(float)g,(float)b);

        bodies.add(new CelestrialBody((-1.724  * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 12*EXP_SCALE, 6.39e23, false, newColor,parent));

        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float)r,(float)g,(float)b);
        bodies.add(new CelestrialBody((-0.587  * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 8*EXP_SCALE, 3.30e23, false, newColor,parent));
        
        r = rand.nextFloat() / 2f + 0.5;
        g = rand.nextFloat() / 2f + 0.5;
        b = rand.nextFloat() / 2f + 0.5;
        newColor = new Color((float)r,(float)g,(float)b);
        bodies.add(new CelestrialBody((-0.823   * PHYSICS_CONSTANT.AU)  + root.pos.x, root.pos.y, 14*EXP_SCALE, 4.8685e24, false, newColor,parent));

    }

    //Generate random solar system
    private void generateSolarSystem(CelestrialBody parent) {

        Random colorRand = new Random();

        int numberOfOrbit = (int)generateRandomWithSteps(AMOUNT_OF_ORBIT_RANGE.getMin(),AMOUNT_OF_ORBIT_RANGE.getMin(),1);

        for(int i = 0; i < numberOfOrbit; i++){

        double newRadius = generateRandomWithSteps(CELESTRIAL_BODY_RADIUS_RANGE.getMin(), CELESTRIAL_BODY_RADIUS_RANGE.getMax(), 1);
        double newDistanceToSun = generateRandomWithSteps(DISTANCE_TO_SUN_RANGE.getMin(), DISTANCE_TO_SUN_RANGE.getMax(), .1);
        double newMass = 4/3 * Math.PI * newRadius*newRadius*newRadius*EXP_SCALE;

        // Will produce only bright / light colours:
        double r = colorRand.nextFloat() / 2f + 0.5;
        double g = colorRand.nextFloat() / 2f + 0.5;
        double b = colorRand.nextFloat() / 2f + 0.5;
        Color newColor = new Color((float)r,(float)g,(float)b);


        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.AU * newDistanceToSun)  + root.pos.x, root.pos.y, newRadius*EXP_SCALE, newMass, false, newColor,parent));

        
        }

    }

     public static double generateRandomWithSteps(double min, double max, double step) {

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
