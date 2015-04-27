package SimpleExample.client;

public class ClientSoundPlayer extends Thread{
	private ClientAudioBuffer cab;
	
	public ClientSoundPlayer(ClientAudioBuffer cab){
		super();
		this.cab=cab;
	}

	
	public void run(){
		System.out.println("sound player Running");
		while(cab.moreToPlay()){
			cab.waitForPlay();	
			byte[] b = cab.getSample();
			playJavaSound(b, b.length);
		}
	}

	private void playJavaSound(byte[] sample, int size){
		//System.out.println("playing sample size: " + size);
	    Client.mLine.write(sample, 0, size);
	    //System.out.println("wrote to sound output. buffer size: ");
	  }

}