package embedded.radar.communication;

import embedded.radar.Reading;
import com.fazecast.jSerialComm.*;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class responsible for the serial communication with ardunio
 * 
 * @author Karim Ashraf
 * 
 */

public class Serial extends Communicator {
    private static Serial singleton = null;
    private SerialPort serial = null;
    private byte[] ReadBuffer;

    private String PortName;
    private int baudrate;

    /**
     * 
     * Returns Serial singleton instance. and sets its parameters
     * 
     * @param PortName Name of the port to connect from
     * @param baudrate Baudrate
     * 
     * @return Serial instance
     * 
     */
    public static Serial getInstance(String PortName, int baudrate) {
        if (singleton == null) {
            singleton = new Serial();
        }
        singleton.PortName = PortName;
        singleton.baudrate = baudrate;
        return singleton;
    }

    /**
     * 
     * Constructor
     * 
     */
    private Serial() {
        ReadBuffer = new byte[1024];
    }

    /**
     * 
     * Query for avaliable serial comm ports
     * 
     * @return List of serial comm ports
     * 
     */
    public static List<String> getCommPorts() {
        List<String> names = new LinkedList<String>();
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort s : ports) {
            names.add(s.getSystemPortName());
        }
        return names;
    }

    /**
     * 
     * Initialize connection with arduino
     * 
     * @return <code>true</code> if successfully connected to the ardunio,
     *         <code>false</code> if there was an error
     */
    @Override
    public boolean connect() {
        serial = SerialPort.getCommPort(PortName);

        if (serial == null) {
            return false;
        }

        if (!serial.openPort()) {
            return false;
        }
        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        serial.setBaudRate(baudrate);

        // Send start
        serial.writeBytes(startbytes, startbytes.length);

        // Wait for ack
        if (serial.readBytes(ReadBuffer, 4) <= 0) {
            serial.closePort();
            return false;
        }

        if (ReadBuffer[0] != ackbytes[0] || ReadBuffer[1] != ackbytes[1] || ReadBuffer[2] != ackbytes[2]
                || ReadBuffer[3] != ackbytes[3]) {
            serial.closePort();
            return false;
        }

        serial.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

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
        int len = serial.readBytes(ReadBuffer, 50);

        // degree,reading,dir
        String[] vals = new String(ReadBuffer, 0, len, StandardCharsets.US_ASCII).split(",");

        int degree = Integer.parseInt(vals[0]);
        int value = Integer.parseInt(vals[1]);
        boolean dir = (Integer.parseInt(vals[2]) == 1);

        return new Reading(degree, value, dir);
    }

    /**
     * 
     * Closes the open serial port
     * 
     * @return <code>true</code> if success, <code>false</code> if there was an
     *         error.
     */
    @Override
    public boolean disconnect() {
        return serial.closePort();
    }


    @Override
    public boolean isConnected()
    {
        return serial.isOpen();
    }
}