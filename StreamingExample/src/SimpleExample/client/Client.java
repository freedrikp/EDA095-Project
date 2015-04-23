package SimpleExample.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import SimpleExample.common.ImageBufferElement;

public class Client {

	public static void main(String[] args) {

		ClientGui gui = new ClientGui();
		try {
			Socket socket = new Socket("192.168.1.196", 7374);
			gui.setSocket(socket);
			ClientImageBuffer cib = new ClientImageBuffer(gui);
			ImageReceiver ir = new ImageReceiver(cib,socket);
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
