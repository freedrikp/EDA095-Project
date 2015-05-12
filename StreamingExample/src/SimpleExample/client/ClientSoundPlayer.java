package SimpleExample.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import SimpleExample.common.AudioBufferElement;

public class ClientSoundPlayer extends Thread {
	private ClientBuffer cBuffer;

	public ClientSoundPlayer(ClientBuffer cBuffer) {
		super();
		this.cBuffer = cBuffer;
	}

	public void run() {
		long pausTime = 0;
		SourceDataLine mLine = null;
		boolean firstTime = true;
		long savedStart = 0;
		while (cBuffer.moreToPlay()) {
			long tmp = System.currentTimeMillis();
			long movieStart = cBuffer.waitForPlay();
			if (savedStart == 0) {
				savedStart = movieStart;
			} else if (savedStart != movieStart) {
				break;
			}
			AudioBufferElement bufferElement = cBuffer.getSample();
			if (!firstTime) {
				pausTime += System.currentTimeMillis() - tmp;
			} else {
				firstTime = false;
			}
			if (mLine == null) {
				try {
					AudioFormat audioFormat = new AudioFormat(
							bufferElement.getSampleRate(),
							bufferElement.getSampleSize(),
							bufferElement.getChannels(), true, false);
					DataLine.Info info = new DataLine.Info(
							SourceDataLine.class, audioFormat);
					mLine = (SourceDataLine) AudioSystem.getLine(info);
					mLine.open(audioFormat);
					mLine.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			long timestamp = bufferElement.getTimestamp();
			try {
				long timeToSleep = timestamp + pausTime
						- System.currentTimeMillis() + movieStart;
				if (timeToSleep > 0) {
					Thread.sleep(timeToSleep);
				} else {
					continue;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (mLine != null) {
				mLine.write(bufferElement.getSample(), 0,
						bufferElement.getSample().length);
			}
		}

	}
}