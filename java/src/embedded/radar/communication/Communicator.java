package embedded.radar.communication;

import embedded.radar.Reading;


/**
 * Abstract base class responsible for communication 
 * 
 * @author Karim Ashraf
 * 
 */

public abstract class Communicator {
    public abstract boolean connect();
    public abstract Reading read();
    public abstract boolean disconnect();
    public abstract boolean isConnected();
}