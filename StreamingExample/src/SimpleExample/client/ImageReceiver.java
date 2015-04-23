package SimpleExample.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import SimpleExample.common.ImageBufferElement;

public class ImageReceiver extends Thread {

	private ClientImageBuffer buffer;
	private Socket socket;

	public ImageReceiver(ClientImageBuffer buffer, Socket socket) {
		this.buffer = buffer;
		this.socket = socket;
	}

	public void run() {
		try {
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while (!socket.isClosed()) {
				long timestamp = dis.readLong();
				int length = dis.readInt();
				byte[] bytes = new byte[length];
				int bytesRead = 0;
				int read = 0;
				while ((read = dis.read(bytes, bytesRead, length - bytesRead)) > 0) {
					bytesRead += read;
				}
				buffer.addImage(new ImageBufferElement(createImageFromBytes(bytes),timestamp));
				
			}
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
