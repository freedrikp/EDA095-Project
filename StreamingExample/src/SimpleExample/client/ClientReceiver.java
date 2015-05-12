package SimpleExample.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.ImageBufferElement;
import SimpleExample.common.Protocol;

public class ClientReceiver extends Thread {

	private ClientBuffer cib;
	private Socket socket;
	private DataInputStream dis;

	public ClientReceiver(ClientBuffer cib, Socket socket) throws IOException {
		this.cib = cib;
		this.socket = socket;
		this.dis = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		try {
			byte command = 0;
			while (!socket.isClosed() && command != Protocol.STREAM_END) {
				command = dis.readByte();
				switch (command) {
				case Protocol.FRAME_BEGIN:
					receiveImage();
					break;
				case Protocol.STREAM_END:
					break;
				case Protocol.LIST_START:
					receiveMovieList();
					break;
				case Protocol.SAMPLE_BEGIN:
					receiveSample();
					break;
				default:
					System.out.println("Unknown command from server");
				}
			}
			cib.setAllFramesSent(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMovieList() throws IOException {
		int size = dis.readInt();
		String[] movieList = new String[size];
		for (int i = 0; i < size; i++) {
			movieList[i] = dis.readUTF();
		}
		cib.setMovieList(movieList);
	}

	private void receiveImage() {
		try {
			long timestamp = dis.readLong();
			int length = dis.readInt();
			byte[] bytes = new byte[length];
			dis.readFully(bytes);
			cib.addImage(new ImageBufferElement(createImageFromBytes(bytes),
					timestamp));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void receiveSample() {
		try {
			long timestamp = dis.readLong();
			float sampleRate = dis.readFloat();
			int sampleSize = dis.readInt();
			int channels = dis.readInt();
			int length = dis.readInt();
			byte[] bytes = new byte[length];
			dis.readFully(bytes);
			cib.addSample(new AudioBufferElement(bytes, timestamp, sampleRate,
					sampleSize, channels));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
