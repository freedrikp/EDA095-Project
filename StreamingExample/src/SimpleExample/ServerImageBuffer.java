package SimpleExample;

import java.util.LinkedList;

public class ServerImageBuffer {
	private boolean closed;
	private LinkedList<ImageBufferElement> images;

	public ServerImageBuffer() {
		this.images = new LinkedList<ImageBufferElement>();
		this.closed = false;
	}

	public synchronized void addImage(ImageBufferElement image) {
		images.addLast(image);
		notifyAll();
	}

	public synchronized ImageBufferElement getNextImage() {
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

	public synchronized boolean hasMore() {
		if (images.size() > 0) {
			return true;
		}else{
			return false;
		}
	}
}
