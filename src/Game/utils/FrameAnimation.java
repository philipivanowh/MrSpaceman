package Game.utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class FrameAnimation {
    private int index = 0;
    private double timer = 0;

    private ArrayList<BufferedImage> frames;
    final private double secondsPerFrame;
    final private boolean singleShot;

    public FrameAnimation(double secondsPerFrame, boolean singleShot) {
        this.secondsPerFrame = secondsPerFrame;
        this.singleShot = singleShot;
    }

    public BufferedImage getFrame() {
        return frames.get(index);
    }

    public void tick(double dt) {
        if(singleShot && isFinished())
            return;

        timer += dt;
        if(timer >= secondsPerFrame / 1000) {
            timer = 0;
            index = (index + 1) % frames.size();
        }
    }

    public boolean isFinished() {
        return index == frames.size() - 1;
    }

    public static BufferedImage scaleImage(BufferedImage image, double factor) {
        Vector2D newSize = new Vector2D(image.getWidth() * factor, image.getHeight() * factor);
        BufferedImage scaled = new BufferedImage((int)newSize.x, (int)newSize.y, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = scaled.createGraphics();
        g2.drawImage(image, 0, 0, (int)newSize.x, (int)newSize.y, null);
        g2.dispose();

        return scaled;
    }

    public void loadFrames(String[] filePaths) {
        try {
            for (String filePath : filePaths) {
                File imageFile = new File(filePath);
                frames.add(ImageIO.read(imageFile));
            } 
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void loadFramesFromPath(String path, double scaleFactor) {
        File dir = new File(path);

        try {
            for (File file : dir.listFiles()) {
                File imageFile = new File(file.getPath());
                frames.add(FrameAnimation.scaleImage(ImageIO.read(imageFile), scaleFactor));
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}