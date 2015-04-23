package SimpleExample.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		ServerSocket ss;
		try {
			ss = new ServerSocket(7373);
			while (true) {
				Socket socket = ss.accept();
				es.submit(new Streamer(socket, "media/sw.mp4"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
