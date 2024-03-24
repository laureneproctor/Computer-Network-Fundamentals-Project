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

public class Server extends Thread {

	int port;

	// server constructor
	public Server(int port) {
		this.port = port;
	}

	// run() creates a new ServerSocket Object which listens to establish connection from client
	public void run() {
		System.out.println("The server is running on port " + port);
		System.out.println();

		// create a new Socket Server instance
		try {
			ServerSocket listener = new ServerSocket(port);
			int clientNum = 1;
			try {
				// while the connection is open
				while (true) {
					// while there is a viable connection to the current clientNum, create a new Handler object with 
					new Handler(listener.accept(), clientNum).start();
					clientNum++;
				}
			} 
			finally {
				listener.close();
			} 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }

	// Handler class handles the Socket connections between the server and a given client 
	private static class Handler extends Thread {
		String message; // Message received from the client
		String MESSAGE; // Uppercase message send to the client
		Socket connection; // Connection scoket
		ObjectInputStream in;
		ObjectOutputStream out;  
		int no; // The index number of the client

		// connection information: connection status and peerID
		boolean clientConnected;
		int connectedPeerID;

		// Handler constructor
		public Handler(Socket connection, int no) {
			this.connection = connection;
			this.no = no;
		}

		// the run function in the Handler class initilializes the recieving and sending processes of the Handshake
		public void run() {
			try {
				// Initialize Input and Output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());

				// Receive handshake message from the client
				byte[] receivedHandshakeBytes = new byte[32]; // Assuming handshake message size is 32 bytes
				int bytesRead = in.read(receivedHandshakeBytes);

				// connectedPeerID is the integer-conversion of the four bytes of receivedHandshakeBytes starting from index 28
				// AKA the last 4 bytes of receivedHandshakeBytes
				connectedPeerID = ByteBuffer.wrap(receivedHandshakeBytes, 28, 4).getInt();
				System.out.println("Peer " + connectedPeerID + " Connected");

				// set client Connection to true
				clientConnected = true;

				// case that connection failed or no bytes are read
				if (!Handshake.isValid(receivedHandshakeBytes) || bytesRead == -1) {
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
				while (clientConnected) {
					int incoming = in.read();

					// Client Disconnected
					if (incoming == -1) {
						clientConnected = false;
					}
				}
			
			}
			catch (IOException ioException){
				System.out.println("Socket Error");
			}
			finally{
				// Close connections 
				try {
					in.close();
					out.close();
					connection.close();
					System.out.println("Peer " + connectedPeerID + " Disconnected");
				}
				catch (IOException ioException){
					System.out.println("Disconnect with Client " + connectedPeerID);
				}
			}
		}

	// Send a message to the output stream
	public void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("Send message: " + msg + " to Client " + no);
		}
		catch (IOException ioException){
			ioException.printStackTrace();
		}
	}

    }

}
