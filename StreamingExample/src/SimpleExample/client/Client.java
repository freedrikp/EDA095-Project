package SimpleExample.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import SimpleExample.common.Configuration;
import SimpleExample.common.ImageBufferElement;

public class Client {
public static SourceDataLine mLine;
	
private static void showMovie(ClientIuffer cib, ClientGui gui){
		long previousTimestamp = 0;
		long lastShown = 0;
		while (cib.moreToShow()) {
			cib.waitForPlay();
			ImageBufferElement image = cib.getImage();
			long timestamp = image.getTimestamp();
			try {
				long timeToSleep = timestamp-previousTimestamp-(System.currentTimeMillis()-lastShown);
				if (timeToSleep > 0){
					Thread.sleep(timeToSleep);				
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			previousTimestamp = timestamp;
			gui.setImage(image.getImage());
			lastShown = System.currentTimeMillis();
		}
	}
	
	
	private static boolean initAudio(){
		//This is taken straight from sw.mp4, should be sent through socket.
		AudioFormat audioFormat = new AudioFormat(44100, 16, 2, true, false);
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,
				audioFormat);
		try {
			mLine = (SourceDataLine) AudioSystem
					.getLine(info);
			mLine.open(audioFormat);
			mLine.start();
			return true;
			
		} catch (LineUnavailableException e) {
			throw new RuntimeException("could not open audio line");
			
		}
	}

	public static void main(String[] args) {

		try {
			Socket socket = new Socket(Configuration.CLIENT_HOST, Configuration.COM_PORT);
			ClientSender cs = new ClientSender(socket);
			ClientIuffer cib = new ClientIuffer();
			cs.sendGetMovieList();
			ClientReceiver ir = new ClientReceiver(cib,socket);
			ir.start();
			ClientGui gui = new ClientGui(cs,cib);
			ClientGuiUpdater ugui = new ClientGuiUpdater(gui);
			ugui.start();
			if(initAudio()){
				ClientSoundPlayer soundPlayer = new ClientSoundPlayer(cib);
				soundPlayer.start();
			}
			gui.setSocket(socket);
			showMovie(cib,gui);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
