package embedded.radar.gui;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class reponsible for logging messages to be displayed at the UI
 * 
 * @author Karim Ashraf
 * 
 */

public final class Logger {
    private static Queue<String> messages = new ConcurrentLinkedQueue<String>();

    private Logger() {
    };

    public static void log(String message) {
        messages.add(message);
    }

    public static String next() {
        try {
            return messages.remove();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

}