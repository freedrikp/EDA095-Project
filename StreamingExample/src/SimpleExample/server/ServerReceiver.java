package SimpleExample.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.client.ClientImageBuffer;
import SimpleExample.common.Protocol;

public class ServerReceiver extends Thread {
	private ServerImageBuffer buffer;
	private Socket socket;
	private DataInputStream dis;

	public ServerReceiver(ServerImageBuffer buffer, Socket socket) throws IOException {
		this.buffer = buffer;
		this.socket = socket;
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
				default:
					System.out.println("Unknown command from client");
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	}

