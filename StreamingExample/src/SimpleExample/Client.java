package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

public class Client {

	public static void main(String[] args) {

		ClientGui gui = new ClientGui();

		try {
			Socket socket = new Socket("localhost", 7373);
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			gui.setSocket(socket);
			while (!socket.isClosed()) {
				int length = dis.readInt();
				byte[] bytes = new byte[length];
				int bytesRead = 0;
				int read = 0;
				while ((read = dis.read(bytes, bytesRead, length - bytesRead)) > 0) {
					bytesRead += read;
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gui.setImage(createImageFromBytes(bytes));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bais);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
