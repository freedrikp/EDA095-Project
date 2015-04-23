package SimpleExample.server;

import java.net.Socket;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Streamer implements Runnable {
	private Socket socket;
	private IMediaReader reader;
	
	public Streamer(Socket socket, String movie){
		this.socket = socket;
		reader = ToolFactory.makeReader(movie);
	}
	
	public void run(){
		ServerImageBuffer monitor = new ServerImageBuffer();
		ImageSender is = new ImageSender(monitor, socket);
		is.start();
		ServerListener sl = new ServerListener(monitor);
		reader.addListener(sl);
		while (reader.readPacket() == null)
			;
		monitor.closeIt();
	}
	
}
