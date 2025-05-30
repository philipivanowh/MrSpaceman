package Game;

import Game.Constant.CELESTRIAL_BODY_TYPE;
import Game.Constant.PHYSICS_CONSTANT;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/*
 * SolarSystem class represents a model of our solar system
 * It contains a root celestrial body (the sun) and other celestrial bodies (planets)
 * It can generate a default solar system or a random one
 * All the created celestrial bodies are added to the bodies list to be managed
 * It updates the positions of the celestrial bodies based on their gravitational interactions
 * It renders the celestrial bodies on the screen
 * It provides methods to get the celestrial bodies and the root celestrial body
 * It creates the mass of the celestrial bodies based on its volume and a constant factor
 */
public class SolarSystem {

    private ArrayList<CelestrialBody> bodies = new ArrayList<>();

    private CelestrialBody root;

    private CelestrialBody blackHole;

    public long delayTimeUntilTimeStepChange = 5000;    //5 second before its timestep will change its speed to default

    private final int EXP_SCALE = (int) (PHYSICS_CONSTANT.AU_TO_PIXELS_SCALE * PHYSICS_CONSTANT.AU / 100);

    private final int FIRST_ORBIT = 0;
    private final int SECOND_ORBIT = 1;
    private final int THIRD_ORBIT = 2;
    private final int FOURTH_ORBIT = 3;
    private final int FIFTH_ORBIT = 4;

    /**
     * Constructor for the SolarSystem class.
     * Initializes the solar system with a root celestrial body (the sun) at a given position.
     *
     * @param x The x-coordinate of the sun's position in pixels units.
     * @param y The y-coordinate of the sun's position in pixels units.
     */
    public SolarSystem(int x, int y) {

        // Creating the sun
        int sunRadius = PHYSICS_CONSTANT.SUN_RADIUS;
        double sunMass = PHYSICS_CONSTANT.SUN_MASS;
        root = new CelestrialBody(x * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE, y * PHYSICS_CONSTANT.PIXELS_TO_AU_SCALE,
                sunRadius * EXP_SCALE, sunMass, Constant.CELESTRIAL_BODY_TYPE.SUN, Color.ORANGE, root,this);
        bodies.add(root);

        generateSolarSystem(root);

        //Determining if creating a black hole in the solar system or not
        int chance = (int)generateRandomWithSteps(0, 2, 1);
        if(chance == 0){

            double distanceToSun = PHYSICS_CONSTANT.AU * generateRandomWithSteps(PHYSICS_CONSTANT.BLACK_DISTANCE_TO_SUN_AU.getMin(), PHYSICS_CONSTANT.BLACK_DISTANCE_TO_SUN_AU.getMax(), .1);
            double angle = generateRandomWithSteps(0, 2*Math.PI, .1);
            double blackHoleX = Math.cos(angle)* distanceToSun;
            double blackHoleY = Math.sin(angle) * distanceToSun; 

            blackHole = new CelestrialBody(blackHoleX+root.pos.x, blackHoleY+root.pos.y, PHYSICS_CONSTANT.BLACK_HOLE_RADIUS*EXP_SCALE, PHYSICS_CONSTANT.BLACK_HOLE_MASS, CELESTRIAL_BODY_TYPE.BLACK_HOLE, Color.black, blackHole, this);
            bodies.add(blackHole);
            System.out.println(blackHole.getPos());
        }
        
    }

    /*
     * Update the positions of all celestrial bodies in the solar system.
     */
    public void update() {

        for (int i = 1; i < bodies.size(); i++) {
            bodies.get(i).update(bodies);
        }
    }

    /**
     * Generates a default solar system with planets similar to our own.
     * This method creates the planets Mercury, Venus, Earth, and Mars with predefined properties.
     */
    private void generateDefaultSolarSystem() {

        Random rand = new Random();
        // Will produce only bright / light colours:
        double r = rand.nextFloat() / 2f + 0.5;
        double g = rand.nextFloat() / 2f + 0.5;
        double b = rand.nextFloat() / 2f + 0.5;
        Color newColor = new Color((float) r, (float) g, (float) b);

        // Generate Mercury celestrial body
        newColor = Color.DARK_GRAY;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.MERCURY_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                8 * EXP_SCALE, 3.30e23, CELESTRIAL_BODY_TYPE.PLANET, newColor, root,this));

        // Generate Venus celestrial body
        newColor = Color.WHITE;
        bodies.add(new CelestrialBody(-(PHYSICS_CONSTANT.VENUS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                14 * EXP_SCALE, 4.8685e24, CELESTRIAL_BODY_TYPE.PLANET, newColor, root,this));

        // Generate Earth celestrial body
        newColor = Color.BLUE;
        bodies.add(new CelestrialBody((PHYSICS_CONSTANT.EARTH_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                16 * EXP_SCALE, 5.9742e24, CELESTRIAL_BODY_TYPE.PLANET, newColor, root,this));

        // Generate Mars celestrial body
        newColor = Color.RED;
        bodies.add(new CelestrialBody(-(PHYSICS_CONSTANT.MARS_DISTANCE_TO_SUN_AU) + root.pos.x, root.pos.y,
                12 * EXP_SCALE, 6.39e23, CELESTRIAL_BODY_TYPE.PLANET, newColor, root,this));
    }

    /* * Generates a random solar system with planets.
     * It creates a number of planets based on a random count and assigns them random properties.
     * The planets are added to the solar system as celestrial bodies orbiting the sun.
     * * @param parent The parent celestrial body (the sun) around which the planets will orbit.
     */
    private void generateSolarSystem(CelestrialBody parent) {

        Random colorRand = new Random();

        // Determien how many orbits (planets) to generate
        int numberOfOrbit = (int) generateRandomWithSteps(
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMin(),
                PHYSICS_CONSTANT.AMOUNT_OF_ORBIT_RANGE.getMax(),
                1);

        for (int i = 0; i < numberOfOrbit; i++) {

            // Generate a random radius for the celestrial body
            double newRadius = generateRandomWithSteps(PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMin(),
                    PHYSICS_CONSTANT.CELESTRIAL_BODY_RADIUS_RANGE.getMax(), 1);
            double newDistanceToSun=0;

            // Determine the distance to the sun based on the orbit index
            // The distance to the sun is determined by the orbit index 
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
            // Calculate the mass of the celestrial body based on its volume and a constant factor
            double newMass = calculateSurfaceAreaOfPlanet(newRadius*EXP_SCALE)*1e22;
            // Will produce only bright / light colours:
            double r = colorRand.nextFloat() / 2f + 0.5;
            double g = colorRand.nextFloat() / 2f + 0.5;
            double b = colorRand.nextFloat() / 2f + 0.5;
            Color newColor = new Color((float) r, (float) g, (float) b);

            int dir = (int)generateRandomWithSteps(0, 1, 1);

            if(dir == 0)
                dir = -1;
            else
                dir = 1;

            // Create a new celestrial body (planet) and add it to the solar system
            bodies.add( new CelestrialBody( dir*(PHYSICS_CONSTANT.AU * newDistanceToSun) + root.pos.x, root.pos.y,
                    newRadius * EXP_SCALE, newMass, CELESTRIAL_BODY_TYPE.PLANET, newColor, parent,this));


            
            //Determine to create a moon or not
          /* 
            int chance =    (int)generateRandomWithSteps(0,4,1);
            // 1/4 chance of generating a moon
            if(chance == 5){
                System.out.println("Moon created");
                Color moonColor = new Color(246, 241, 213);
                int moonRadius = (int)generateRandomWithSteps(2,5,1);
                bodies.add(new CelestrialBody(dir*(PHYSICS_CONSTANT.AU * newDistanceToSun) + root.pos.x +0.1*PHYSICS_CONSTANT.AU, root.pos.y, moonRadius * EXP_SCALE, 7*1e20, CELESTRIAL_BODY_TYPE.MOON, moonColor, bodies.get(bodies.size()-1), this));
                
            }
                */


        }

    }

    public static void generateBlackHole(){

    }

     /*
     * Calculates the volume of a spherical body given its radius.
     * The formula used is V = 4/3 * π * r^3.
     *
     * @param radius The radius of the spherical body.
     * @return The volume of the body.
     */
    public static double calculateSurfaceAreaOfPlanet(double radius){
        return 4 * Math.PI * radius * radius;
    }

    /* Returns a random number in [min, max] with the given step size. */
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

    /*
     * Renders all celestrial bodies in the solar system.
     */
    public void render(Graphics2D g2) {

        for (CelestrialBody body : bodies) {
            body.render(g2);
        }

    }

    /**
     * Returns the list of all celestrial bodies in the solar system.
     *
     * @return An ArrayList containing all celestrial bodies.
     */
    public ArrayList<CelestrialBody> getCelestrialBodies() {
        return bodies;
    }
    /**
     * Returns the root celestrial body of the solar system (the sun).
     *
     * @return The root CelestrialBody representing the sun.
     */
    public CelestrialBody getRoot() {
        return root;
    }

}
