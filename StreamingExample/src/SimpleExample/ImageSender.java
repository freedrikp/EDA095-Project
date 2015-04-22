package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImageSender extends Thread {
	private ImageBuffer monitor;
	private OutputStream out;

	public ImageSender(ImageBuffer monitor, OutputStream out) {
		super();
		this.monitor = monitor;
		this.out = out;
	}

	public void run() {
		DataOutputStream dos = new DataOutputStream(out);
		while (true) {
			BufferedImage image = monitor.sendNextImage();
			byte[] bytes = createBytesFromImage(image);
			if (bytes != null){
				try {
					dos.writeInt(bytes.length);
					dos.write(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
	}

	
	
	
	private byte[] createBytesFromImage(BufferedImage image){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( image, "png", baos );
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
