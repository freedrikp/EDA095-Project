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
		ServerSocket audioss;
		try {
			ss = new ServerSocket(Configuration.COM_PORT);
			audioss = new ServerSocket(Configuration.AUDIO_COM_PORT);
			while (true) {
				Socket socket = ss.accept();
				Socket audioSocket = audioss.accept();
				System.out.println(socket.isBound());
				es.submit(new Streamer(socket, audioSocket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
