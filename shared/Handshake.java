package shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
/*
 *  - The handshake class defines the basic handshake transaction between the client and server
 *  - This would include how the header is passed through the stream
 * - This also checks whether the header is equal to whats expected, returning a boolean accordingly
 */

// Class for the handshake
public class Handshake 
{
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
    public byte[] getBytes() 
    {

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

    // Validate handshake
    public static boolean isValid(byte[] handshakeBytes) 
    {
        // Check length
        if (handshakeBytes.length != 32) 
        {
            return false;
        }

        // String portion
        String header = new String(handshakeBytes, 0, 18, StandardCharsets.UTF_8);

        // header is not correct, not validated
        if (!header.equals("P2PFILESHARINGPROJ")) 
        {
            return false;
        }

        // Ensure next 10 bits are zero
        for (int i = 18; i < 28; i++)
         {
            if (handshakeBytes[i] != 0)
             {
                return false;
            }
        }
        
        return true;
    }
}
