package SimpleExample.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

import SimpleExample.common.ImageBufferElement;
import SimpleExample.common.Protocol;

public class ImageSender extends Thread {
	private ServerImageBuffer monitor;
	private ServerSender ss;

	public ImageSender(ServerImageBuffer monitor, ServerSender ss) {
		super();
		this.monitor = monitor;
		this.ss = ss;
	}

	public void run() {
		try {
			while (!monitor.finished() || monitor.hasMore()) {
				ImageBufferElement image = monitor.getNextImage();
				byte[] bytes = createBytesFromImage(image.getImage());
				if (bytes != null){
						ss.sendFrame(bytes, image.getTimestamp());
				}
			}
			ss.sendEndOfStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	
	
	
	private static byte[] createBytesFromImage(BufferedImage image){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( image, "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
