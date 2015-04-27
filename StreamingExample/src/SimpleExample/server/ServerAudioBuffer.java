package SimpleExample.server;

import java.util.LinkedList;

public class ServerAudioBuffer {
	private boolean closed;
	private LinkedList<byte[]> samples;
	private boolean runStream = false;
	private boolean streamOpen = true;

	public ServerAudioBuffer() {
		this.samples = new LinkedList<byte[]>();
		this.closed = false;
	}

	public synchronized void addSample(byte[] sample) {
		samples.addLast(sample);
		notifyAll();
	}

	public synchronized byte[] getNextSample() {
		while (samples.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return samples.poll();
	}

	public synchronized boolean finished() {
		return closed;
	}

	public synchronized void closeIt() {
		closed = true;
	}

	public synchronized boolean hasMore() {
		if (samples.size() > 0) {
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
}
