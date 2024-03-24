package client;

import java.net.*;

import peer.Peer;

import java.io.*;
import shared.Handshake;
import shared.Message;

public class Client extends Thread {
	String ip = "";
	Peer me;
	int theirPeerID;
	int port;

	Socket socket; // Socket connect to the server
	ObjectOutputStream out; // Stream write to the socket
 	ObjectInputStream in; // Stream read from the socket
	String message; // Message send to the server
	String MESSAGE; // Capitalized message read from the server


	public Client(Peer me, int thierPeerID, String ip, int port) {
		this.me = me;
		this.theirPeerID = thierPeerID;
		this.ip = ip;
		this.port = port;
	}
	
	public void run() {
		try {
			// Establish connection with the server
			socket = new Socket(ip, port);
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

			if (!Handshake.isValid(receivedHandshakeBytes) || bytesRead == -1) {
				System.out.println("Failed to receive handshake message from server.");

				// Close connections
				socket.close();
				return;
			}

			System.out.println("Handshake completed with Peer " + theirPeerID);

			while (true) {
			}

		}
		catch (ConnectException e) {
    		System.err.println("Connection refused. You need to initiate a server first.");
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally {
			try {
				// Close connections
				out.close();
				in.close();
				socket.close();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Send a message to the output stream
	void sendMessage(String msg)
	{
		try{
			// Stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

}
