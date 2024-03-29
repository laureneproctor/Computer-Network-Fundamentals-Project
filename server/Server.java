/*
 * - The Server has clientNum and attempts make connection with clientNums starting at clientNum == 0
 * - This creates a new Handler object which handles the input and output streams between the client and server
 * - once a handshake is recieved and reciprocated, the messages are sent back and forth
 * 
 * TODO: (maybe) add message types that specify the values associated with each of the messages
 * [this is being implementing in Client.java]
 */
package server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import shared.Handshake;
import shared.MyLogger;
import peer.Peer;

// Server class
public class Server extends Thread
{

	Peer me;
	int port;

	// server constructor
	public Server(Peer me, int port)
	{	
		this.me = me;
		this.port = port;
	}

	// run() creates a new ServerSocket Object which listens to establish connection from client
	public void run() {

		// Printing that the server is running on the port
		System.out.println("The server is running on port " + port);
		System.out.println();

		// create a new Socket Server instance
		try 
		{

			ServerSocket listener = new ServerSocket(port);
			int clientNum = 1;
			try 
			{
				// while the connection is open
				while (true) 
				{
					// while there is a viable connection to the current clientNum, create a new Handler object with 
					new Handler(listener.accept(), me, clientNum).start();
					clientNum++;
				}
			} 
			finally
			 {
				listener.close();
			} 
		}

		// Catching errors
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }

	// Handler class handles the Socket connections between the server and a given client 
	private static class Handler extends Thread
	 {
		 // Message received from the client
		String message;
		// Uppercase message send to the client
		String MESSAGE; 
		// Connection scoket
		Socket connection; 
		ObjectInputStream in;
		ObjectOutputStream out;  
		// The index number of the client
		int no; 

		// connection information: connection status and peerID
		boolean clientConnected;
		int connectedPeerID;
		Peer me;

		// Handler constructor
		public Handler(Socket connection, Peer me, int no) 
		{
			this.connection = connection;
			this.me = me;
			this.no = no;
		}

		// the run function in the Handler class initilializes the recieving and sending processes of the Handshake
		public void run() 
		{
			try 
			{
				// Initialize Input and Output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());

				// Receive handshake message from the client
				// Assuming handshake message size is 32 bytes
				byte[] receivedHandshakeBytes = new byte[32]; 

				// Reading the bytes from the client
				int bytesRead = in.read(receivedHandshakeBytes);

				// connectedPeerID is the integer-conversion of the four bytes of receivedHandshakeBytes starting from index 28
				// AKA the last 4 bytes of receivedHandshakeBytes
				connectedPeerID = ByteBuffer.wrap(receivedHandshakeBytes, 28, 4).getInt();

				// Print out which peer is connected
				System.out.println("Peer " + connectedPeerID + " Connected");
				MyLogger.logTCPFrom(me.getId(), connectedPeerID);

				// set client Connection to true
				clientConnected = true;

				// case that connection failed or no bytes are read
				if (!Handshake.isValid(receivedHandshakeBytes) || bytesRead == -1) 
				{
					System.out.println("Failed to receive handshake message from client.");

					// Close connections
					connection.close();
					return;
				}

				// Send handshake message to the client
				Handshake handshake = new Handshake(connectedPeerID);
				byte[] handshakeBytes = handshake.getBytes();
				out.write(handshakeBytes);
				out.flush();
				
				// recieve message stream while connetion is viable
				while (clientConnected)
				 {
					int incoming = in.read();

					// Client Disconnected
					if (incoming == -1) 
					{
						clientConnected = false;
					}
				}
			
			}

			// Catching socket errors
			catch (IOException ioException)
			{
				System.out.println("Socket Error");
			}
			finally
			{
				// Close connections 
				try
				 {
					in.close();
					out.close();
					connection.close();

					// Printing out that the connections were closed
					System.out.println("Peer " + connectedPeerID + " Disconnected");
				}

				// Catching erros
				catch (IOException ioException)
				{
					System.out.println("Disconnect with Client " + connectedPeerID);
				}
			}
		}

	// Send a message to the output stream
	public void sendMessage(String msg) 
	{
		try
		 {
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}

		// Catching ioException error
		catch (IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

    }

}
