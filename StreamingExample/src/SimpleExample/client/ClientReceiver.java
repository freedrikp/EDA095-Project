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

	private ClientBuffer cBuffer;
	private Socket socket;
	private DataInputStream in;

	public ClientReceiver(ClientBuffer cBuffer, Socket socket)
			throws IOException {
		this.cBuffer = cBuffer;
		this.socket = socket;
		this.in = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		try {
			byte command = 0;
			while (!socket.isClosed() && command != Protocol.STREAM_END) {
				command = in.readByte();
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
			cBuffer.setAllFramesSent(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMovieList() throws IOException {
		int size = in.readInt();
		String[] movieList = new String[size];
		for (int i = 0; i < size; i++) {
			movieList[i] = in.readUTF();
		}
		cBuffer.setMovieList(movieList);
	}

	private void receiveImage() {
		try {
			long timestamp = in.readLong();
			int length = in.readInt();
			byte[] bytes = new byte[length];
			in.readFully(bytes);
			cBuffer.addImage(new ImageBufferElement(
					createImageFromBytes(bytes), timestamp));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream byteIn = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(byteIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void receiveSample() {
		try {
			long timestamp = in.readLong();
			float sampleRate = in.readFloat();
			int sampleSize = in.readInt();
			int channels = in.readInt();
			int length = in.readInt();
			byte[] bytes = new byte[length];
			in.readFully(bytes);
			cBuffer.addSample(new AudioBufferElement(bytes, timestamp,
					sampleRate, sampleSize, channels));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
