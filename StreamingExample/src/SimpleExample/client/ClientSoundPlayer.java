package SimpleExample.client;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.ImageBufferElement;

public class ClientSoundPlayer extends Thread{
	private ClientBuffer cab;
	
	public ClientSoundPlayer(ClientBuffer cab){
		super();
		this.cab=cab;
	}

	
	public void run(){
		System.out.println("sound player Running");
		long previousTimestamp = 0;
		long lastPlayed = 0;
		while(cab.moreToPlay()){
			cab.waitForPlay();	
			AudioBufferElement b = cab.getSample();
			long timestamp = b.getTimestamp();
			try {
				long timeToSleep = timestamp-previousTimestamp-(System.currentTimeMillis()-lastPlayed);
				if (timeToSleep > 0){
					Thread.sleep(timeToSleep);				
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			previousTimestamp = timestamp;
			playJavaSound(b.getSample(), b.getSample().length);
			lastPlayed = System.currentTimeMillis();
		}
		
		
		
		
		
			
		
	}

	private void playJavaSound(byte[] sample, int size){
		//System.out.println("playing sample size: " + size);
	    Client.mLine.write(sample, 0, size);
	    //System.out.println("wrote to sound output. buffer size: ");
	  }

}