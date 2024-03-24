package server;

import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

import shared.Handshake;

public class Server extends Thread {

	int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		System.out.println("The server is running on port " + port);
		System.out.println();

		try {
			ServerSocket listener = new ServerSocket(port);
			int clientNum = 1;
			try {
				while (true) {
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

	private static class Handler extends Thread {
		String message; // Message received from the client
		String MESSAGE; // Uppercase message send to the client
		Socket connection; // Connection scoket
		ObjectInputStream in;
		ObjectOutputStream out;  
		int no; // The index number of the client

		boolean clientConnected;
		int connectedPeerID;

		public Handler(Socket connection, int no) {
			this.connection = connection;
			this.no = no;
		}

		public void run() {
			try {
				// Initialize Input and Output streams
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				in = new ObjectInputStream(connection.getInputStream());

				// Receive handshake message from the client
				byte[] receivedHandshakeBytes = new byte[32]; // Assuming handshake message size is 32 bytes
				int bytesRead = in.read(receivedHandshakeBytes);
				connectedPeerID = ByteBuffer.wrap(receivedHandshakeBytes, 28, 4).getInt();
				System.out.println("Peer " + connectedPeerID + " Connected");

				clientConnected = true;

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
