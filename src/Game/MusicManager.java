package Game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class MusicManager {
    private static final String MUSIC_FOLDER = "Sounds";
    private static final List<Clip>    playlist       = new ArrayList<>();
    private static final List<FloatControl> volumeControls = new ArrayList<>();
    private static int currentTrack = 0;

    /** 
     * Call this once at game startup.
     * It will load every .wav/.aiff in Sounds/ and fire off the first track. 
     */
    public static void init() {
        File dir = new File(MUSIC_FOLDER);
        File[] files = dir.listFiles((d, name) -> 
            name.toLowerCase().endsWith(".wav") || name.toLowerCase().endsWith(".aiff")
        );

        if (files == null || files.length == 0) {
            System.err.println("MusicManager: no audio files found in " + MUSIC_FOLDER);
            return;
        }

        try {
            for (File f : files) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                // grab the master gain control so we can adjust volume later
                FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControls.add(gain);

                // when this clip finishes, fire the next one
                clip.addLineListener(evt -> {
                    if (evt.getType() == LineEvent.Type.STOP) {
                        clip.stop();
                        clip.setFramePosition(0);
                        playNext();
                    }
                });

                playlist.add(clip);
            }

            // kick things off
            playlist.get(0).start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /** internal helper—advance the pointer and start the next clip */
    private static void playNext() {
        currentTrack = (currentTrack + 1) % playlist.size();
        playlist.get(currentTrack).start();
    }

    /**
     * level between 0.0 (mute) and 1.0 (full)
     * Will apply to the currently playing track.
     */
    public static void setVolume(float level) {
        if (playlist.isEmpty()) return;

        FloatControl ctrl = volumeControls.get(currentTrack);
        float min = ctrl.getMinimum();
        float max = ctrl.getMaximum();
        float dB  = level <= 0f 
                     ? min 
                     : Math.max(min, Math.min(20f * (float)Math.log10(level), max));
        ctrl.setValue(dB);
    }

    /** stops the current song immediately */
    public static void stop() {
        if (!playlist.isEmpty()) {
            playlist.get(currentTrack).stop();
        }
    }
}
