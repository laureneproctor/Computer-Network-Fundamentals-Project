package shared;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

// Class for the message
public class Message {
    // The length field of the message that is 4-bytes
    private int length;

    // 1-byte message type field
    private byte type;

    // Message payload that can be different sizes
    private byte[] payload;

    // Constructor used to define the variables type, payload, and length
    public Message(byte type, byte[] payload) {
        this.type = type;
        this.payload = payload;
        this.length = payload.length;
    }

    // Function that creates and returns a byte array which is the message
    public byte[] getBytes() {
        // Creates an object to write the bytes into
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Using try and catch to write bytes into the output stream and catch errors
        try {
            // Writing into the length
            byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(length).array());

            // Writing into the type
            byteArrayOutputStream.write(type);

            // Writing into the payload
            byteArrayOutputStream.write(payload);
        }

        // Catching errors and printing out the error
        catch (IOException e) {
            e.printStackTrace();
        }

        // Returns byte array created
        return byteArrayOutputStream.toByteArray();
    }
}
