package embedded.radar.gui;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiUnavailableException;

import static java.lang.Math.*;

import embedded.radar.Reading;

/**
 * Class responsible for drawing the GUI
 * 
 * @author Karim Ashraf
 */

public class UI {
    private JFrame window;
    private Image img;
    private Graphics2D g;
    private Graphics windowgfx;
    private int cw, ch;
    private int centerX, centerY, radius;

    private Audio sound;
    private static UI singleton = null;

    // private static Font font = new Font("Lucida Console", Font.PLAIN, 17);
    private static Font font = new Font("Courier", Font.PLAIN, 17);
    private static final float strokeWidth = 2;
    private static final float scanWidth = 7;
    private static final int bgColor = 32;
    private static final Color radarColor = new Color(98, 245, 31);
    private static final Color ObjectColor = new Color(142, 247, 94);
    private static final int divisons = 4;
    private static final int decayf = 8;
    
    

    /**
     * 
     * Returns GUI singleton instance.
     * 
     * @return UI instance
     * 
     */
    public static UI getInstance() {
        if (singleton == null) {
            singleton = new UI();
        }
        return singleton;
    }

    /**
     * 
     * Called by getInstance() when creating initial instance
     * 
     */
    private UI() {
        window = new JFrame();
        window.setSize(800, 450);
        window.setResizable(false);
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("RadarApp");
        windowgfx = window.getGraphics();
        try{
            sound = Audio.getInstance();
        } catch(MidiUnavailableException e){
            sound = null;
        }

        
    }

    /**
     * (Re)Draws radar background with no indication for current scan angle
     */
    public void DrawScreen() {
        DrawScreen(0, false, null);
    }

    /**
     * 
     * (Re)Draws radar background and draws scan line indicating current scan angle
     * and detect objects
     * 
     * @param scanDegree current scan angle in degrees
     * @param dir        true for clockwise direction, false for anticlockwise
     *                   direction
     * @param objects    Radar readings
     * 
     */
    public void DrawScreen(int scanDegree, boolean dir, java.util.List<Reading> objects) {
        cw = window.getWidth();
        ch = window.getHeight();
        centerX = cw / 2;
        centerY = ch;
        radius = cw / 2 - 50;

        img = window.createVolatileImage(cw, ch);
        g = (Graphics2D) img.getGraphics();
        g.setColor(new Color(bgColor, bgColor, bgColor));
        g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));

        /* Radar Arcs */
        g.setColor(radarColor);
        g.setStroke(new BasicStroke(strokeWidth));

        for (int i = 1; i <= divisons; i++) {
            int r = i * radius / divisons;
            g.drawArc(centerX - r, centerY - r, 2 * r, 2 * r, 0, 180);
        }

        /* Draw objects */
        g.setColor(ObjectColor);
        g.setStroke(new BasicStroke(scanWidth));
        int last_val = 0;
        int xx, yy;
        if (objects != null) {
            for (Reading r : objects) {

                float dist = radius * (1 - ((float) r.value-110) / 300.0f);

                
                if(r.value>130){// && abs(last_val-r.value)>20) {
                    xx = centerX - (int) round(dist * cos(r.degree * PI / 180));
                    yy = centerY - (int) round(dist * sin(r.degree * PI / 180));
                    g.drawRect(xx, yy, 1, 1);
                    
                    if(abs(r.degree-scanDegree)==2){
                        sound.PlaySound();
                    }
                }
                last_val = r.value;
            }
        }

        /* Trace drawing */
        g.setStroke(new BasicStroke(scanWidth));
        int dirf = dir ? 1 : -1;

        int R = radarColor.getRed();
        int G = radarColor.getGreen();
        int B = radarColor.getBlue();
        double a = 230f;

        for (int i = 0; i < 40; i++) {
            int d = scanDegree + i * dirf;

            if (d < 0 || d > 180)
                break;

            g.setColor(new Color(R, G, B, (int) a));
            g.drawLine(centerX, centerY, centerX - (int) round(radius * cos(d * PI / 180)),
                    centerY - (int) round(radius * sin(d * PI / 180)));

            a = a / 1.1f;
        }

        /* Print message(s) */
        g.setColor(radarColor);
        g.setFont(font);
        String message;
        int y = 50;
        while ((message = Logger.next()) != null) {
            g.drawString(message, 50, y);
            y = y + 20;
        }

        // Apply frame
        // windowgfx.setColor(Color.BLACK);
        windowgfx.drawImage(img, 0, 0, null);

    }

    public void DrawScreenElement() {

    }

}