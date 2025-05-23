package Game;
//It changes the game the Physics game Timestep speed after 5 seconds of the solarSystem being initialize;

public class TimeChangeThread implements Runnable{

    SolarSystem system;

    public TimeChangeThread(SolarSystem system)
    {
        this.system = system;
    }

    @Override 
    public void run(){
       
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
        system.TIMESTEP = Constant.PHYSICS_CONSTANT.TIMESTEP_DEFAULT;

    }



}
