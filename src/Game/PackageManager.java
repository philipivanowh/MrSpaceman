package Game;

import Game.utils.FrameAnimation;
import Game.utils.Vector2D;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class PackageManager {
    private CelestialBody body;
    private final BufferedImage arrowImage;

    public PackageManager() {
        this.arrowImage = FrameAnimation.loadImage("Images/arrow.png", 0.2);
    }

    public void generateNextPackage(ArrayList<SolarSystem> solarSystems) {
        Random rand = new Random();

        // random solar system
        SolarSystem solarSystem = solarSystems.get(rand.nextInt(solarSystems.size()));

        // random body in that solar system
        ArrayList<CelestialBody> bodies = solarSystem.getCelestrialBodies();
        this.body = bodies.get(rand.nextInt(bodies.size()));
    }

    public Vector2D getPackagePos()
    {
        return this.body.getPos();
    }

    public void renderPackageArrow(Player player, Graphics2D hud) {
        Vector2D diff = Vector2D.subtract(this.body.getPos(), player.getPos());
        double dist = diff.length();

        diff.normalize();

        int minDim = Math.min(Constant.GAME_CONSTANT.WINDOW_WIDTH, Constant.GAME_CONSTANT.WINDOW_HEIGHT);
        diff.x *= minDim * 0.4;
        diff.y *= minDim * 0.4;

        AffineTransform old = hud.getTransform();
        hud.translate(diff.x + Constant.GAME_CONSTANT.WINDOW_WIDTH / 2, diff.y + Constant.GAME_CONSTANT.WINDOW_HEIGHT / 2);

        double scale = Math.min(Math.max(10000 / dist, 0.5), 2.5);
        hud.scale(scale, scale);

        hud.rotate(diff.getAngle());
        hud.drawImage(this.arrowImage, -this.arrowImage.getWidth() / 2, -this.arrowImage.getHeight() / 2, null);
        hud.setTransform(old);
    }
}
