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
			ServerImageBuffer monitor = new ServerImageBuffer();
			ServerSender ss = new ServerSender(socket);
			ImageSender is = new ImageSender(monitor, ss);
			is.start();
			new ServerReceiver(monitor,socket,ss).start();
			ServerListener sl = new ServerListener(monitor);
			String movie = monitor.getMovieName();
			IMediaReader reader = ToolFactory.makeReader(Configuration.MEDIA_DIRECTORY+"/"+movie);
			reader.addListener(sl);
			do{
				monitor.waitForRunStream();
			}while (reader.readPacket() == null && monitor.isStreamOpen());
			monitor.closeIt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
