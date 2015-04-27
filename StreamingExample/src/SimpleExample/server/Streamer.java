package SimpleExample.server;

import java.io.IOException;
import java.net.Socket;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Streamer implements Runnable {
	private Socket socket;
	private IMediaReader reader;

	public Streamer(Socket socket, String movie) {
		this.socket = socket;
		reader = ToolFactory.makeReader(movie);
	}

	public void run() {
		try {
			ServerImageBuffer monitor = new ServerImageBuffer();
			ServerSender ss = new ServerSender(socket);
			ImageSender is = new ImageSender(monitor, ss);
			is.start();
			new ServerReceiver(monitor,socket).start();
			ServerListener sl = new ServerListener(monitor);
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
