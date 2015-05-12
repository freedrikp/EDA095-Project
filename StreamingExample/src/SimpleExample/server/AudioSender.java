package SimpleExample.server;

import java.io.IOException;

import SimpleExample.common.AudioBufferElement;

public class AudioSender extends Thread {
	private ServerBuffer sBuffer;
	private ServerSender sSender;

	public AudioSender(ServerBuffer sBuffer, ServerSender sSender) {
		super();
		this.sBuffer = sBuffer;
		this.sSender = sSender;
	}

	public void run() {
		try {
			while ((!sBuffer.finished() || sBuffer.hasMoreSamples())
					&& sBuffer.isStreamOpen()) {
				AudioBufferElement aBufferElement = sBuffer.getNextSample();
				if (aBufferElement.getSample() != null) {
					sSender.sendSample(aBufferElement.getSample(),
							aBufferElement.getTimestamp(),
							aBufferElement.getSampleRate(),
							aBufferElement.getSampleSize(),
							aBufferElement.getChannels());
				}
			}
			if (!sBuffer.hasMoreFrames()) {
				sSender.sendEndOfStream();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}
}