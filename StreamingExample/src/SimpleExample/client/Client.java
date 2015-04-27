package SimpleExample.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import SimpleExample.common.Configuration;
import SimpleExample.common.ImageBufferElement;

public class Client {
	
	private static void showMovie(ClientImageBuffer cib, ClientGui gui){
		long previousTimestamp = 0;
		long lastShown = 0;
		while (cib.moreToShow()) {
			cib.waitForPlay();
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
	}

	public static void main(String[] args) {

		try {
			Socket socket = new Socket(Configuration.CLIENT_HOST, Configuration.COM_PORT);
			ClientSender cs = new ClientSender(socket);
			ClientGui gui = new ClientGui(cs);
			gui.setSocket(socket);
			ClientImageBuffer cib = new ClientImageBuffer(gui);
			gui.setImageBuffer(cib);
			ClientReceiver ir = new ClientReceiver(cib,socket);
			ir.start();
			showMovie(cib,gui);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	

}
