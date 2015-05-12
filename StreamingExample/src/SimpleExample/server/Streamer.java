package SimpleExample.server;

import java.io.IOException;
import java.net.Socket;

import SimpleExample.common.Configuration;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Streamer implements Runnable {
	private Socket socket;

	public Streamer(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			ServerBuffer monitor = new ServerBuffer();
			ServerSender ss = new ServerSender(socket);
			AudioSender as = new AudioSender(monitor, ss);
			ImageSender is = new ImageSender(monitor, ss);
			is.start();
			as.start();
			new ServerReceiver(monitor, socket, ss).start();
			ServerListener sl = new ServerListener(monitor);
			String movie = monitor.getMovieName();
			IMediaReader reader = ToolFactory
					.makeReader(Configuration.SERVER_MEDIA_DIRECTORY + "/"
							+ movie);
			reader.addListener(sl);
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
				monitor.waitForRunStream();
			} while (reader.readPacket() == null && monitor.isStreamOpen());
			monitor.closeIt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
