package SimpleExample.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.common.Protocol;

@SuppressWarnings("unused")
public class ClientSender {
	private Socket socket;
	private DataOutputStream out;

	public ClientSender(Socket socket) throws IOException {
		this.socket = socket;
		out = new DataOutputStream(socket.getOutputStream());
	}

	public synchronized void sendClose() throws IOException {
		out.writeByte(Protocol.CLOSE_STREAM);
	}

	public synchronized void sendPlayStream() throws IOException {
		out.writeByte(Protocol.PLAY_STREAM);
	}

	public synchronized void sendPauseStream() throws IOException {
		out.writeByte(Protocol.PAUSE_STREAM);
	}

	public synchronized void sendGetMovieList() throws IOException {
		out.writeByte(Protocol.GIVE_MOVIE_LIST);
	}

	public synchronized void sendTitle(String title) throws IOException {
		out.writeByte(Protocol.CHOSEN_TITLE);
		out.writeUTF(title);
	}

	public void setSocket(Socket socket) throws IOException {
		this.socket = socket;
		out = new DataOutputStream(socket.getOutputStream());
	}
}
