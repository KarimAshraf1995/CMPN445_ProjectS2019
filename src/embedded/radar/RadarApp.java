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

        //TODO: Implement a method to get ip automatically
        Communicator comm = TCP.getInstance("192.168.1.6", 2019);

        Logger.log("Connecting..");
        gui.DrawScreen();

        comm.connect();

        if (!comm.isConnected()) {
            return;
        }
        while (true) {
            Reading last = comm.read();
            if (comm.isConnected() && last!=null) {
                Logger.log("Value of " + last.value + ", at angle " + last.degree + " detected!");
                if (last.degree == 180 || last.degree == 0) {
                    list.clear();
                }
                list.add(comm.read());
            }
            else{
                Logger.log("Connection lost!");
            }
            gui.DrawScreen(last.degree, last.dir, list);
        }

    }
}