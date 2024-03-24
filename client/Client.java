// Packages and imports needed to run
package client;

import java.net.*;

import peer.Peer;

import java.io.*;
import shared.Handshake;
import shared.Message;

// Client class
public class Client extends Thread
 {
	String ip = "";
	Peer me;
	int theirPeerID;
	int port;

	// Socket connect to the server
	Socket socket; 

	// Stream write to the socket
	ObjectOutputStream out;
	
	 // Stream read from the socket
 	ObjectInputStream in;

	// Message send to the server
	String message; 

	 // Capitalized message read from the server
	String MESSAGE;
	
	// Constructor for Client class
	public Client(Peer me, int thierPeerID, String ip, int port) 
	{
		this.me = me;
		this.theirPeerID = thierPeerID;
		this.ip = ip;
		this.port = port;
	}
	
	public void run() {
		try
		 {
			// Establish connection with the server
			socket = new Socket(ip, port);

			// Print message stating the connection was made
			System.out.println("Connected to server with PeerID: " + theirPeerID + ", IP: " + ip + ", PORT: " + port);

			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();

			in = new ObjectInputStream(socket.getInputStream());

			// Send handshake message to the server
			Handshake handshake = new Handshake(me.getId());
			byte[] handshakeBytes = handshake.getBytes();
			out.write(handshakeBytes);
			out.flush();

			// Receive handshake message from the server
			byte[] receivedHandshakeBytes = new byte[32];
			int bytesRead = in.read(receivedHandshakeBytes);

			// If the handhshake is not valid then display the error message
			if (!Handshake.isValid(receivedHandshakeBytes) || bytesRead == -1) {

				System.out.println("Failed to receive handshake message from server.");

				// Close connections
				socket.close();

				return;
			}

			// Printing out that the handshake was complete
			System.out.println("Handshake completed with Peer " + theirPeerID);

			// Building a message by using Message class
			// receivedHandshakeBytes may not be the correct variable to pass in
			Message message = new Message((byte) 0, receivedHandshakeBytes);
			
			// Converting the message to bytes
			byte[] messageBytes = message.getBytes();

			// Sending message to the server
			out.write(messageBytes);
			out.flush();

			// Receiving message from the server
			byte[] receivedMessageBytes = new byte[32];
			bytesRead = in.read(receivedMessageBytes);

			while (true) {

			}

		}

		// Catching errors
		// If user does not initiate connection to server this error message is given
		catch (ConnectException e) 
		{
    		System.err.println("Connection refused. You need to initiate a server first.");
		}

		// If user is trying to connect to unkown host this will be the error message given
		catch(UnknownHostException unknownHost)
		{
			System.err.println("You are trying to connect to an unknown host!");
		}

		// ioException error
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}

		// Closing connections
		finally 
		{
			try
			 {
				out.close();
				in.close();
				socket.close();
			} 

			// Catches errors when closing connections
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	// Send a message to the output stream
	void sendMessage(String msg)
	{
		try
		{
			// Stream write the message
			out.writeObject(msg);
			out.flush();
		}

		// Catching errors when writing to output stream
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

}
