package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ImageSender extends Thread {
	private ServerImageBuffer monitor;
	private Socket socket;

	public ImageSender(ServerImageBuffer monitor, Socket socket) {
		super();
		this.monitor = monitor;
		this.socket = socket;
	}

	public void run() {
		DataOutputStream dos;
		try {
			dos = new DataOutputStream(socket.getOutputStream());

			while (!monitor.finished() || monitor.hasMore()) {
				ImageBufferElement image = monitor.getNextImage();
				byte[] bytes = createBytesFromImage(image.getImage());
				if (bytes != null){
						dos.writeLong(image.getTimestamp());
						dos.writeInt(bytes.length);
						dos.write(bytes);
				}
				
			}
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	
	
	
	private byte[] createBytesFromImage(BufferedImage image){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( image, "jpg", baos );
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
