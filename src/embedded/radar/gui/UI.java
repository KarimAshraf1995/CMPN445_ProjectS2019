package embedded.radar.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.sound.midi.Instrument;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import static java.lang.Math.*;

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
    private int centerX,centerY,radius;

    private static UI singleton = null;
    private final float strokeWidth = 2;
    private final int bgColor = 32;
    private final Color radarColor = new Color(98, 245, 31);
    private final int divisons = 4;
    private final int decayf = 8;

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
        window.setSize(800, 400);
        window.setResizable(false);
        window.setLocation(0, 0);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("RadarApp");
        windowgfx = window.getGraphics();
    }

    /**
     * (Re)Draws radar background with no indication for current scan angle
     */
    public void DrawScreen() {
        DrawScreen(0, false);
    }

    /**
     * 
     * (Re)Draws radar background and draws scan line indicating current scan angle
     * 
     * @param scanDegree current scan angle in degrees
     * @param dir        true for clockwise direction, false for anticlockwise
     *                   direction
     * 
     */
    public void DrawScreen(int scanDegree, boolean dir) {
        cw = window.getWidth();
        ch = window.getHeight();
        centerX = cw / 2;
        centerY = ch;
        radius = cw / 2 - 50;

        img = window.createVolatileImage(cw, ch);
        g = (Graphics2D) img.getGraphics();
        g.setColor(new Color(bgColor, bgColor, bgColor));
        g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));

        int dirf = dir ? 1 : -1;

        double R = radarColor.getRed();
        double G = radarColor.getGreen();
        double B = radarColor.getBlue();

        for (int i = 0; i < 40; i++) {
            int d = scanDegree + i * dirf;

            if (d < 0 || d > 180)
                break;

            g.setColor(new Color((int) R, (int) G, (int) B));
            g.drawLine(centerX, centerY, centerX - (int) round(radius * cos(d * PI / 180)),
                    centerY - (int) round(radius * sin(d * PI / 180)));

            R = R - decayf * R / (R + G + B);
            G = G - decayf * G / (R + G + B);
            B = B - decayf * B / (R + G + B);
        }

        g.setColor(radarColor);
        g.setStroke(new BasicStroke(strokeWidth));

        for (int r = radius / divisons; r < radius; r += radius / divisons) {
            g.drawArc(centerX - r, centerY - r, 2 * r, 2 * r, 0, 180);
        }

        windowgfx.setColor(Color.BLACK);
        windowgfx.drawImage(img, 0, 0, null);

    }

    public void DrawScreenElement() {

    }

}