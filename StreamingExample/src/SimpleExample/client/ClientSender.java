package SimpleExample.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.common.Protocol;

public class ClientSender {
	private Socket socket;
	private DataOutputStream dos;
	
	public ClientSender(Socket socket) throws IOException{
		this.socket = socket;
		dos = new DataOutputStream(socket.getOutputStream());
	}
	
	public synchronized void sendClose() throws IOException{
		dos.writeByte(Protocol.CLOSE_STREAM);
	}
	
	public synchronized void sendPlayStream() throws IOException{
		dos.writeByte(Protocol.PLAY_STREAM);
	}
	
	public synchronized void sendPauseStream() throws IOException{
		dos.writeByte(Protocol.PAUSE_STREAM);
	}
	
	public synchronized void sendGetMovieList() throws IOException{
		dos.writeByte(Protocol.GIVE_MOVIE_LIST);
	}
}
