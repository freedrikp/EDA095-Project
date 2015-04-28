package SimpleExample.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.client.ClientIuffer;
import SimpleExample.common.Protocol;

public class ServerReceiver extends Thread {
	private ServerBuffer buffer;
	private Socket socket;
	private DataInputStream dis;
	private ServerSender ss;

	public ServerReceiver(ServerBuffer buffer, Socket socket, ServerSender ss) throws IOException {
		this.buffer = buffer;
		this.socket = socket;
		this.ss = ss;
		this.dis = new DataInputStream(socket.getInputStream());
	}
	
	public void run(){
		try{
			byte command = 0;
			while (buffer.isStreamOpen() && command != Protocol.STREAM_END) {
				command = dis.readByte();
				switch (command) {
				case Protocol.PLAY_STREAM:
					buffer.setRunStream(true);
					break;
				case Protocol.PAUSE_STREAM:
					buffer.setRunStream(false);
					break;
				case Protocol.CLOSE_STREAM:
					buffer.setStreamOpen(false);
					break;
				case Protocol.GIVE_MOVIE_LIST:
					ss.sendMovieList();
					break;
				case Protocol.CHOSEN_TITLE:
					String movieName = dis.readUTF();
					buffer.setMovieName(movieName);
					break;
				default:
					System.out.println("Unknown command from client");
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	}

