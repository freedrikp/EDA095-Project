package SimpleExample;

import java.awt.image.BufferedImage;
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
		while (true) {
			BufferedImage image = monitor.sendNextImage();

			try {
				ImageIO.write(image, "png", out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
