package Game;
import Game.Entity;
import Game.utils.FrameAnimation;
import Game.utils.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {
    private final FrameAnimation currAnimation;
    private final FrameAnimation idleAnimation;
    private final FrameAnimation deathAnimation;
    private final double size;

    public Enemy(double x, double y, double size) {
        super(x, y, 1e10);
        this.size = size;
        this.idleAnimation = new FrameAnimation(0.8, false);
        this.idleAnimation.loadFramesFromPath("Images/Enemy Idle", 1);

        this.deathAnimation = new FrameAnimation(0.5, false);
        this.deathAnimation.loadFramesFromPath("Images/Enemy Death", 1);
        this.currAnimation = this.idleAnimation;

    }
    @Override
    public void render(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        g2.translate(this.pos.x, this.pos.y);
        g2.rotate(this.angle + Math.PI / 2);
        g2.scale(this.size, this.size);
        BufferedImage img = this.currAnimation.getFrame();
        g2.drawImage(img, -img.getWidth() / 2, -img.getHeight() / 2, null);
        g2.setTransform(old);
    }

    public void followAndAttack(Player player) {
        Vector2D diff = Vector2D.subtract(player.pos, this.pos);

        // if the enemy is close to the target
        if(diff.length() < this.size * 20) {
//            player.health -= 1;
            return;
        }

        diff.normalize();
        diff.multiply(60 / (this.size * 0.5));

        this.vel = diff;
        this.angle = diff.getAngle();
    }

    public void update(double dt) {
        this.currAnimation.tick(dt);

        // decay acceleration
        this.acc.multiply(0.9);

        // apply acceleration and velocity
        this.vel.add(Vector2D.multiply(this.acc, dt));
        this.pos.add(Vector2D.multiply(this.vel, dt));
    }

    @Override
    public Vector2D getPos() {
        return null;
    }

    @Override
    public Vector2D getVel() {
        return null;
    }

    @Override
    public Vector2D getAcc() {
        return null;
    }

    @Override
    public Vector2D getForce() {
        return null;
    }
}
