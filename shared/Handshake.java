package shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

// Class for the handshake
public class Handshake {

    // Handshake header that is an 18-byte string
    private final String header = "P2PFILESHARINGPROJ";

    // 10-byte zero bits
    private final byte[] zeros = new byte[10];

    // Integer of peer ID
    private int peerId;

    // Constructor that defines the peerID
    public Handshake(int peerId) {
        this.peerId = peerId;
    }

    // Function that creates and returns a byte array which is the handshake message
    public byte[] getBytes() {

        // Creating the ByteArrayOutputSteam to write bytes into
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Try and catch to write the bytes
        try
         {

            // Writing the header
            byteArrayOutputStream.write(header.getBytes());

            // Writing the zeros
            byteArrayOutputStream.write(zeros);

            // Writing the peerID
            byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(peerId).array());
        }

        // This is used to handle any errors
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        // Returns the output stream as a byte array
        return byteArrayOutputStream.toByteArray();
    }
}
