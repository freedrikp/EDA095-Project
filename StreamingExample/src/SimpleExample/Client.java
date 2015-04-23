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
			Socket socket = new Socket("localhost", 7374);
			gui.setSocket(socket);
			ClientImageBuffer cib = new ClientImageBuffer();
			ImageReceiver ir = new ImageReceiver(cib,socket,gui);
			ir.start();
			long previousTimestamp = 0;
			long lastShown = 0;
			while (!socket.isClosed()) {
				ImageBufferElement image = cib.getImage();
				long timestamp = image.getTimestamp();
				try {
					long timeToSleep = timestamp-previousTimestamp-(System.currentTimeMillis()-lastShown);
					if (timeToSleep > 0){
						Thread.sleep(timeToSleep);				
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				previousTimestamp = timestamp;
				gui.setImage(image.getImage());
				lastShown = System.currentTimeMillis();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	

}
