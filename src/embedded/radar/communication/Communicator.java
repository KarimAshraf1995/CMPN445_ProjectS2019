package embedded.radar.communication;

import embedded.radar.Reading;


/**
 * Abstract base class responsible for communication 
 * 
 * @author Karim Ashraf
 * 
 */

public abstract class Communicator {

    protected static final byte[] startbytes = { 0x1, 0x2, 0x3, 0x4 };
    protected static final byte[] ackbytes = { 0x1, 0x7, 0x1, 0x2 };

  
    public abstract boolean connect();
    public abstract Reading read();
    public abstract boolean disconnect();

}