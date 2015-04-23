package SimpleExample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IMediaReader reader = ToolFactory.makeReader("media/sw.mp4");
		ServerSocket ss;
		try {
			ss = new ServerSocket(7373);
			Socket socket = ss.accept();
			ImageBuffer monitor = new ImageBuffer();
			ImageSender is = new ImageSender(monitor, socket);
			is.start();
			ServerListener sl = new ServerListener(monitor);
			reader.addListener(sl);
			while (reader.readPacket() == null)
				;
			monitor.closeIt();
			sl.printCounters();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
