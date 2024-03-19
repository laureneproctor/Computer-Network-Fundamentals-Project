// Packages needed
import java.io.*;
import java.nio.*;

// Class for the handshake
class Handshake 
{
	// Handshake header that is a 18-byte string
	private final String header = "P2PFILESHARINGPROJ";

	// 10-byte zero bits
	private final byte[] zeros = new byte[10];

	// Integer of peer ID
	private int peerId;

	// Constructor that defines the peerID
	public Handshake(int peerId) 
	{
		this.peerId = peerId;
	}

	// Function that creates and returns a byte array which is the handshake message
	public byte[] getBytes() 
	{
		// Creatingthe ByteArrayOutputSteam to write bytes into
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

// Class for the message
class Message
 {
	// The length field of the messafe that is 4-bytes
	private int length;

	// 1-byte message type field
	private byte type;
	
	// Message payload that can be different sizes
	private byte[] payload;

	// Constructor used to define the variables type, payload, and length
	public Message(byte type, byte[] payload) 
	{
		this.type = type;
		this.payload = payload;
		this.length = payload.length;
	}

	// Function that creates and returns a byte array which is the message
	public byte[] getBytes() 
	{
		// Creates an object to write the bytes into
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		// Using try and catch to write bytes into the output stream and catch errors
		try 
		{
			// Writing into the length
			byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(length).array());
			
			// Writing into the type
			byteArrayOutputStream.write(type);

			// Writing into the payload
			byteArrayOutputStream.write(payload);
		}

		// Catching errors and printing out the error
		 catch (IOException e) 
		 {
			e.printStackTrace();
		}

		// Returns byte array created
		return byteArrayOutputStream.toByteArray();
	}
}