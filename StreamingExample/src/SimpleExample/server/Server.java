package SimpleExample.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import SimpleExample.common.Configuration;

public class Server {
	public static void main(String[] args) {
		Configuration.loadConfiguration("config.txt");
		ExecutorService execService = Executors.newCachedThreadPool();
		ServerSocket sSocket;
		try {
			sSocket = new ServerSocket(Configuration.COM_PORT);
			while (true) {
				Socket socket = sSocket.accept();
				execService.submit(new Streamer(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
