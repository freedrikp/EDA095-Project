package SimpleExample.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import SimpleExample.common.Configuration;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		ServerSocket ss;
		try {
			ss = new ServerSocket(Configuration.COM_PORT);
			while (true) {
				Socket socket = ss.accept();
				es.submit(new Streamer(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
