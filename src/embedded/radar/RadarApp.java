package embedded.radar;

import embedded.radar.gui.*;

/**
 * Main App class
 * 
 * @author Karim Ashraf
 * 
 */

public class RadarApp {
    public static void main(String[] args) throws Exception {
        UI gui = UI.getInstance();
        Audio sound = Audio.getInstance();

        while (true) {
            for (int d = 0; d < 180; d++) {
                gui.DrawScreen(d, false);
                Thread.sleep(10);
            }
            for (int d = 180; d >= 0; d--) {
                gui.DrawScreen(d, true);
                Thread.sleep(10);
            }
            sound.PlaySound();
        }

    }
}