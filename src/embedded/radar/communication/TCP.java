package embedded.radar.communication;

import embedded.radar.Reading;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

/**
 * Class responsible for the TCP communication with ardunio
 * 
 * @author Karim Ashraf
 * 
 */
public class TCP extends Communicator {
    private static TCP singleton = null;
    private String host;
    private int port;
    private byte[] ReadBuffer;
    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;

    /**
     * 
     * Returns TCP singleton instance. and sets its parameters
     * 
     * @param host hostname or ip address of the server
     * @param port server tcp port
     * 
     * @return TCP instance
     * 
     */
    public static TCP getInstance(String host, int port) {
        if (singleton == null) {
            singleton = new TCP();
        }
        singleton.host = host;
        singleton.port = port;
        return singleton;
    }

    /**
     * 
     * Constructor
     * 
     */
    private TCP() {
        ReadBuffer = new byte[1024];
    }

    /**
     * 
     * Initialize connection with server
     * 
     * @return <code>true</code> if successfully connected to the ardunio,
     *         <code>false</code> if there was an error
     */
    @Override
    public boolean connect() {
        try {
            client = new Socket(host, port);
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
            out.write(startbytes);
            in.read(ReadBuffer);

            if (ReadBuffer[0] != ackbytes[0] || ReadBuffer[1] != ackbytes[1] || ReadBuffer[2] != ackbytes[2]
                    || ReadBuffer[3] != ackbytes[3]) {
                disconnect();
                return false;
            }
        } catch (UnknownHostException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 
     * Waits for measurement from the radar
     * 
     * @return Reading from the radar indicating value, angle at the reading, and
     *         direction
     * 
     */
    @Override
    public Reading read() {
        int len;
        try {
            len = in.read(ReadBuffer);
        } catch (IOException e) {
            return null;
        }

        Reading r = new Reading();
        // degree,reading,dir
        String[] vals = new String(ReadBuffer, 0, len, StandardCharsets.US_ASCII).split(",");

        r.degree = Integer.parseInt(vals[0]);
        r.value = Integer.parseInt(vals[1]);
        r.dir = (Integer.parseInt(vals[2]) == 1);

        return r;
    }

    /**
     * 
     * Closes the open socket
     * 
     * @return <code>true</code> if success, <code>false</code> if there was an
     *         error.
     */
    @Override
    public boolean disconnect() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}