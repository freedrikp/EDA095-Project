package SimpleExample;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ImageBuffer {
	private LinkedList<BufferedImage> images;

	public ImageBuffer() {
		this.images = new LinkedList<BufferedImage>();
	}

	public synchronized void addImage(BufferedImage image) {
		images.addLast(image);
		notifyAll();
	}

	public synchronized BufferedImage sendNextImage() {
		while (images.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return images.poll();
	}
}
