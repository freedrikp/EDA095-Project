package SimpleExample.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import SimpleExample.common.Protocol;

public class ServerSender {
	private Socket socket;
	private DataOutputStream dos;
	
	public ServerSender(Socket socket) throws IOException{
		this.socket = socket;
		dos = new DataOutputStream(socket.getOutputStream());
	}
	
	public synchronized void sendFrame(byte[] image, long timestamp) throws IOException{
		dos.writeByte(Protocol.FRAME_BEGIN);
		dos.writeLong(timestamp);
		dos.writeInt(image.length);
		dos.write(image);
	}
	
	public synchronized void sendEndOfStream() throws IOException{
		dos.writeByte(Protocol.STREAM_END);
		socket.close();
	}
}
