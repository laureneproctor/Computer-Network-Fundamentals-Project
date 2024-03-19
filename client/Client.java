package client;

import java.net.*;
import java.io.*;
import shared.Handshake;
import shared.Message;

public class Client {
	private static final String SERVER_IP = "";
	private static final int DEFAULT_PORT = 8000;
	private static final int PEER_ID = 123; // temp

	public static void main(String[] args) {
		int SERVER_PORT = DEFAULT_PORT;
		if (args.length == 1) {
			SERVER_PORT = Integer.parseInt(args[0]);
		}
		try {
			// Establish connection with the server
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			System.out.println("Connected to server.");

			// Send handshake message to the server
			Handshake handshake = new Handshake(PEER_ID);
			byte[] handshakeBytes = handshake.getBytes();
			OutputStream outputStream = socket.getOutputStream();
			outputStream.write(handshakeBytes);
			System.out.println("Handshake sent to server.");

			// Receive handshake message from the server
			InputStream inputStream = socket.getInputStream();
			byte[] receivedHandshakeBytes = new byte[32];
			int bytesRead = inputStream.read(receivedHandshakeBytes);
			if (bytesRead == -1) {
				System.out.println("Failed to receive handshake message from server.");
				return;
			}

			System.out.println("Received handshake from server.");

			// Handle messages

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
