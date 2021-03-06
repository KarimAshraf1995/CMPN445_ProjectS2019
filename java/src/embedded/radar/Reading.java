package embedded.radar;

/**
 * Class representing a radar mesurement
 * 
 * @author Karim Ashraf
 * 
 */

public class Reading {
    public int degree; // Angle in degrees
    public int value; // Mesurement value
    public boolean dir; // Direction of movement

    public Reading(int degree, int value, boolean dir) {
        this.degree = degree;
        this.value = value;
        this.dir = dir;
    }
}