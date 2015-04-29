package SimpleExample.client;

import java.util.LinkedList;

import SimpleExample.common.Configuration;
import SimpleExample.common.ImageBufferElement;

public class ClientImageBuffer {
	private LinkedList<ImageBufferElement> buffer;
	private boolean firstImage = true;
	private boolean allFramesSent = false;
	private boolean playNotPause = false;
	private String[] movieList;

	public ClientImageBuffer() {
		this.buffer = new LinkedList<ImageBufferElement>();
	}

	public synchronized void addImage(ImageBufferElement image) {
		buffer.add(image);
		notifyAll();
	}

	public synchronized ImageBufferElement getImage() {
		if (firstImage) {
			while (buffer.size() < Configuration.CLIENT_BUFFER_SIZE) {
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
		return buffer.poll();
	}

	public synchronized int getSize() {
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

	public synchronized boolean isPlaying() {
		return playNotPause;
	}

	public synchronized void setMovieList(String[] movieList) {
		this.movieList = movieList;
		notifyAll();
	}

	public synchronized String[] getMovieList() {
		while (movieList == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return movieList;
	}
}