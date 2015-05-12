package SimpleExample.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import SimpleExample.common.AudioBufferElement;

public class ClientSoundPlayer extends Thread {
	private ClientBuffer cab;

	public ClientSoundPlayer(ClientBuffer cab) {
		super();
		this.cab = cab;
	}

	public void run() {
		long pausTime = 0;
		SourceDataLine mLine = null;
		boolean firstTime = true;
		long savedStart = 0;
		while (cab.moreToPlay()) {
			long tmp = System.currentTimeMillis();
			long movieStart = cab.waitForPlay();
			if (savedStart == 0) {
				savedStart = movieStart;
			} else if (savedStart != movieStart) {
				break;
			}
			AudioBufferElement b = cab.getSample();
			if (!firstTime) {
				pausTime += System.currentTimeMillis() - tmp;
			} else {
				firstTime = false;
			}
			if (mLine == null) {
				try {
					AudioFormat audioFormat = new AudioFormat(
							b.getSampleRate(), b.getSampleSize(),
							b.getChannels(), true, false);
					DataLine.Info info = new DataLine.Info(
							SourceDataLine.class, audioFormat);
					mLine = (SourceDataLine) AudioSystem.getLine(info);
					mLine.open(audioFormat);
					mLine.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			long timestamp = b.getTimestamp();
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
				mLine.write(b.getSample(), 0, b.getSample().length);
			}
		}

	}
}