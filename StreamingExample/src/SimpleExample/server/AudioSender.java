package SimpleExample.server;

import java.io.IOException;

import SimpleExample.common.AudioBufferElement;

public class AudioSender extends Thread {
	private ServerBuffer monitor;
	private ServerSender ss;

	public AudioSender(ServerBuffer monitor, ServerSender ss) {
		super();
		this.monitor = monitor;
		this.ss = ss;
	}

	public void run() {
		try {
			while ((!monitor.finished() || monitor.hasMoreSamples()) && monitor.isStreamOpen() ) {
				AudioBufferElement abe = monitor.getNextSample();
				if (abe.getSample() != null){
						ss.sendSample(abe.getSample(),abe.getTimestamp(),abe.getSampleRate(),abe.getSampleSize(),abe.getChannels());
				}
			}
			System.out.println("Done sending audio");
			if (!monitor.hasMoreFrames()){
				ss.sendEndOfStream();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
}