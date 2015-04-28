package SimpleExample.server;

import java.util.LinkedList;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.ImageBufferElement;

public class ServerBuffer {
	private boolean closed;
	private LinkedList<ImageBufferElement> images;
	private boolean runStream = false;
	private boolean streamOpen = true;
	private String movieName;
	private LinkedList<AudioBufferElement> samples;

	public ServerBuffer() {
		this.images = new LinkedList<ImageBufferElement>();
		this.closed = false;
		samples = new LinkedList<AudioBufferElement>();
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

	public synchronized boolean hasMoreFrames() {
		if (images.size() > 0) {
			return true;
		}else{
			return false;
		}
	}
	
	public synchronized void setRunStream(boolean runStream){
		this.runStream = runStream;
		notifyAll();
	}
	
	public synchronized void waitForRunStream(){
		while (!runStream){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void setStreamOpen(boolean streamOpen){
		this.streamOpen = streamOpen;
		notifyAll();
	}
	
	public synchronized boolean isStreamOpen(){
		return streamOpen;
	}

	public synchronized String getMovieName() {
		while(movieName == null || movieName.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return movieName;
	}
	
	public synchronized void setMovieName(String movieName){
		this.movieName = movieName;
		notifyAll();
	}
	
	public synchronized void addSample(AudioBufferElement sample) {
		samples.addLast(sample);
		notifyAll();
	}

	public synchronized AudioBufferElement getNextSample() {
		while (samples.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return samples.poll();
	}
	
	public synchronized boolean hasMoreSamples() {
		if (samples.size() > 0) {
			return true;
		}else{
			return false;
		}
	}
}
