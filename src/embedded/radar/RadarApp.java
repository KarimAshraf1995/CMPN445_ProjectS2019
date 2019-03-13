package embedded.radar;

import embedded.radar.gui.*;
import embedded.radar.communication.*;
import java.util.*;

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

        List<Reading> list = new LinkedList<Reading>();
        list.add(new Reading(92, 100, false));
        list.add(new Reading(91, 90, false));
        list.add(new Reading(90, 80, false));
        list.add(new Reading(89, 120, false));

        System.out.println(Serial.getCommPorts());

        while (true) {
            for (int d = 0; d < 180; d++) {
                gui.DrawScreen(d, false, list, "Connecting");
                Thread.sleep(10);
            }
            for (int d = 180; d >= 0; d--) {
                gui.DrawScreen(d, true, list, "Connecting");
                Thread.sleep(10);
            }
            sound.PlaySound();
        }

    }
}