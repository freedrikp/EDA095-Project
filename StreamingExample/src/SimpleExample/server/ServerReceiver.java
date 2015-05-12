package SimpleExample.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.common.Protocol;

@SuppressWarnings("unused")
public class ServerReceiver extends Thread {
	private ServerBuffer sBuffer;
	private Socket socket;
	private DataInputStream in;
	private ServerSender sSender;

	public ServerReceiver(ServerBuffer sBuffer, Socket socket,
			ServerSender sSender) throws IOException {
		this.sBuffer = sBuffer;
		this.socket = socket;
		this.sSender = sSender;
		this.in = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		try {
			byte command = 0;
			while (sBuffer.isStreamOpen() && command != Protocol.CLOSE_STREAM) {
				command = in.readByte();
				switch (command) {
				case Protocol.PLAY_STREAM:
					sBuffer.setRunStream(true);
					break;
				case Protocol.PAUSE_STREAM:
					sBuffer.setRunStream(false);
					break;
				case Protocol.CLOSE_STREAM:
					sBuffer.setStreamOpen(false);
					sBuffer.setRunStream(true);
					break;
				case Protocol.GIVE_MOVIE_LIST:
					sSender.sendMovieList();
					break;
				case Protocol.CHOSEN_TITLE:
					String movieName = in.readUTF();
					sBuffer.setMovieName(movieName);
					break;
				default:
					System.out.println("Unknown command from client");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
