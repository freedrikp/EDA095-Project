package SimpleExample.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import SimpleExample.common.ImageBufferElement;
import SimpleExample.common.Protocol;

public class ClientReceiver extends Thread {

	private ClientImageBuffer buffer;
	private Socket socket;
	private DataInputStream dis;

	public ClientReceiver(ClientImageBuffer buffer, Socket socket) throws IOException {
		this.buffer = buffer;
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
//					System.out.println("Frame received");
					receiveImage();
					break;
				case Protocol.STREAM_END:
//					System.out.println("End of stream");
					break;
				case Protocol.LIST_START:
					receiveMovieList();
					break;
				default:
					System.out.println("Unknown command from server");
				}
			}
			buffer.setAllFramesSent(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveMovieList() throws IOException {
		int size = dis.readInt();
		String [] movieList = new String[size];
		for(int i=0;i<size; i++){
			movieList[i] = dis.readUTF();
		}
		
	}

	private void receiveImage() {
		try {
			long timestamp = dis.readLong();
			int length = dis.readInt();
			byte[] bytes = new byte[length];
			dis.readFully(bytes);
			buffer.addImage(new ImageBufferElement(createImageFromBytes(bytes),
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

}
