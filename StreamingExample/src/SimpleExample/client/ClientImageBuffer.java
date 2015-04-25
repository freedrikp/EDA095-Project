package SimpleExample.client;

import java.util.LinkedList;

import SimpleExample.common.ImageBufferElement;

public class ClientImageBuffer {
	private LinkedList<ImageBufferElement> buffer;
	private ClientGui gui;
	private boolean firstImage = true;
	private boolean allFramesSent = false;
	private boolean playNotPause = false;

	public ClientImageBuffer(ClientGui gui) {
		this.buffer = new LinkedList<ImageBufferElement>();
		this.gui = gui;
	}

	public synchronized void addImage(ImageBufferElement image) {
		buffer.add(image);
		gui.updateProgressBar(buffer.size());
		notifyAll();
	}

	public synchronized ImageBufferElement getImage() {
		if (firstImage) {
			while (buffer.size() < 100) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			firstImage = false;
		}
		while (buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gui.updateProgressBar(buffer.size());
		return buffer.poll();
	}

	public synchronized int getBufferSize() {
		return buffer.size();
	}

	public synchronized boolean moreToShow() {
		while (!allFramesSent && buffer.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return !allFramesSent || !buffer.isEmpty();
	}

	public synchronized void setAllFramesSent(boolean allFramesSent) {
		this.allFramesSent = allFramesSent;
		notifyAll();
	}

	public synchronized void setPlayNotPause(boolean playNotPause) {
		this.playNotPause = playNotPause;
		notifyAll();
	}

	public synchronized void waitForPlay() {
		while (!playNotPause) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
