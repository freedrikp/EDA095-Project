package WeezelTV.server;

import java.io.IOException;
import java.net.Socket;

import WeezelTV.common.Configuration;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Streamer implements Runnable {
	private Socket socket;

	public Streamer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ServerBuffer sBuffer = new ServerBuffer();
			ServerSender sSender = new ServerSender(socket);
			AudioSender aSender = new AudioSender(sBuffer, sSender);
			ImageSender iSender = new ImageSender(sBuffer, sSender);
			iSender.start();
			aSender.start();
			new ServerReceiver(sBuffer, socket, sSender).start();
			ServerListener sListener = new ServerListener(sBuffer);
			String movieName = sBuffer.getMovieName();
			IMediaReader reader = ToolFactory
					.makeReader(Configuration.SERVER_MEDIA_DIRECTORY + "/"
							+ movieName);
			reader.addListener(sListener);
			int counter = 0;
			do {
				if (counter == Configuration.SERVER_BLOCK_SIZE) {
					try {
						Thread.sleep(Configuration.SERVER_WAIT_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					counter = 0;
				}
				counter++;
				sBuffer.waitForRunStream();
			} while (reader.readPacket() == null && sBuffer.isStreamOpen());
			sBuffer.closeIt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
