package SimpleExample;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class ImageBuffer {
	private boolean closed;
	private LinkedList<BufferedImage> images;

	public ImageBuffer() {
		this.images = new LinkedList<BufferedImage>();
		this.closed = false;
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

	public synchronized boolean finished() {
		return closed;
	}

	public synchronized void closeIt() {
		closed = true;
	}

	public boolean hasMore() {
		if (images.size() > 0) {
			return true;
		}else{
			return false;
		}
	}
}
