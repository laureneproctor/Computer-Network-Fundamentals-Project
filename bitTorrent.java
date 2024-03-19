import java.io.*;
import java.nio.*;

class Handshake {
	private final String header = "P2PFILESHARINGPROJ";
	private final byte[] zeros = new byte[10];
	private int peerId;

	public Handshake(int peerId) {
		this.peerId = peerId;
	}

	public byte[] getBytes() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			byteArrayOutputStream.write(header.getBytes());
			byteArrayOutputStream.write(zeros);
			byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(peerId).array());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}
}

class Message {
	private int length;
	private byte type;
	private byte[] payload;

	public Message(byte type, byte[] payload) {
		this.type = type;
		this.payload = payload;
		this.length = payload.length;
	}

	public byte[] getBytes() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			byteArrayOutputStream.write(ByteBuffer.allocate(4).putInt(length).array());
			byteArrayOutputStream.write(type);
			byteArrayOutputStream.write(payload);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteArrayOutputStream.toByteArray();
	}
}