package SimpleExample.client;

import java.util.LinkedList;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.Configuration;
import SimpleExample.common.ImageBufferElement;

public class ClientBuffer {
	private LinkedList<ImageBufferElement> images;
	private boolean firstImage = true;
	private boolean allFramesSent = false;
	private boolean playNotPause = false;
	private String[] movieList;
	private LinkedList<AudioBufferElement> samples;
	private boolean allSamplesSent = false;
	private long movieStart = 0;

	public ClientBuffer() {
		this.images = new LinkedList<ImageBufferElement>();
		samples = new LinkedList<AudioBufferElement>();
	}

	public synchronized void addImage(ImageBufferElement image) {
		images.add(image);
		notifyAll();
	}

	public synchronized ImageBufferElement getImage() {
		if (firstImage) {
			while (images.size() < Configuration.CLIENT_BUFFER_SIZE) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			firstImage = false;
		}
		while (images.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return images.poll();
	}

	public synchronized int getSize() {
		return images.size();
	}

	public synchronized boolean moreToShow() {
		while (!allFramesSent && images.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return !allFramesSent || !images.isEmpty();
	}

	public synchronized void setAllFramesSent(boolean allFramesSent) {
		this.allFramesSent = allFramesSent;
		notifyAll();
	}

	public synchronized void setPlayNotPause(boolean playNotPause) {
		if (movieStart == 0){
			movieStart = System.currentTimeMillis();
		}
		this.playNotPause = playNotPause;
		notifyAll();
	}

	public synchronized long waitForPlay() {
		while (!playNotPause) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return movieStart;
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
	
	public synchronized void addSample(AudioBufferElement sample) {
		samples.add(sample);
		notifyAll();
	}

	public synchronized AudioBufferElement getSample() {
		while (samples.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return samples.poll();
	}

	public synchronized int getSamplesSize() {
		return samples.size();
	}

	public synchronized boolean moreToPlay() {
		while (!allSamplesSent && samples.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return !allSamplesSent || !samples.isEmpty();
	}
}
