package server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import shared.Handshake;
import shared.Message;

public class Server {
	private static final int DEFAULT_PORT = 8000;
	private static final int PEER_ID = 123;

	public static void main(String[] args) {
		try {
			int SERVER_PORT = DEFAULT_PORT;
			if (args.length == 1) {
				SERVER_PORT = Integer.parseInt(args[0]);
			}
			System.out.println("The server is running.");
			ServerSocket listener = new ServerSocket(SERVER_PORT);
			System.out.println("server.Server started. Waiting for client connection...");

			// Wait for a client to connect
			Socket connection = listener.accept();
			System.out.println("Got connection request from " + connection.getRemoteSocketAddress());

			// Send handshake message to the client
			Handshake handshake = new Handshake(PEER_ID);
			byte[] handshakeBytes = handshake.getBytes();
			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(handshakeBytes);
			System.out.println("Handshake sent to client.");

			// Receive handshake message from the client
			InputStream inputStream = connection.getInputStream();
			byte[] receivedHandshakeBytes = new byte[32]; // Assuming handshake message size is 32 bytes
			int bytesRead = inputStream.read(receivedHandshakeBytes);
			if (bytesRead == -1) {
				System.out.println("Failed to receive handshake message from client.");
				return;
			}

			// Close the connection
			connection.close();
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}