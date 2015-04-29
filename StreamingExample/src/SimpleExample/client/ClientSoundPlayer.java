package SimpleExample.client;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.ImageBufferElement;

public class ClientSoundPlayer extends Thread {
	private ClientBuffer cab;

	public ClientSoundPlayer(ClientBuffer cab) {
		super();
		this.cab = cab;
	}

	public void run() {
		

			System.out.println("sound player Running");
//			long previousTimestamp = 0;
//			long lastPlayed = 0;
			SourceDataLine mLine = null;
			while (cab.moreToPlay()) {
				long movieStart = cab.waitForPlay();
				AudioBufferElement b = cab.getSample();
				if (mLine == null){
					try {
						AudioFormat audioFormat = new AudioFormat(b.getSampleRate(), b.getSampleSize(), b.getChannels(), true,
								false);
						DataLine.Info info = new DataLine.Info(SourceDataLine.class,
								audioFormat);
						mLine = (SourceDataLine) AudioSystem
								.getLine(info);
						mLine.open(audioFormat);
						mLine.start();
					} catch (Exception e) {
//				throw new RuntimeException("could not open audio line");
						e.printStackTrace();
					}
				}

				long timestamp = b.getTimestamp();
				try {
//					long timeToSleep = timestamp - previousTimestamp
//							- (System.currentTimeMillis() - lastPlayed);
					long timeToSleep = timestamp - System.currentTimeMillis() + movieStart;
					if (timeToSleep > 0) {
						Thread.sleep(timeToSleep);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				previousTimestamp = timestamp;
				// playJavaSound(b.getSample(), b.getSample().length);
				if (mLine != null){
					mLine.write(b.getSample(), 0, b.getSample().length);					
				}
//				lastPlayed = System.currentTimeMillis();
			}
		
	}

	// private void playJavaSound(byte[] sample, int size){
	// //System.out.println("playing sample size: " + size);
	// Client.mLine.write(sample, 0, size);
	// //System.out.println("wrote to sound output. buffer size: ");
	// }

}