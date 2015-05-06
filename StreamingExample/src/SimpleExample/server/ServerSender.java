package SimpleExample.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import SimpleExample.common.Configuration;
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

	public synchronized void sendMovieList() throws IOException {
		dos.writeByte(Protocol.LIST_START);	
		File[] dir = new File(Configuration.SERVER_MEDIA_DIRECTORY).listFiles();
		ArrayList<String> toBeSent = new ArrayList<String>();
		for (File f : dir){
			if (f.getName().charAt(0) != '.'){
				toBeSent.add(f.getName());
			}
		}
		dos.writeInt(toBeSent.size());
		for(String s: toBeSent){
			dos.writeUTF(s);
		}
	}
	
	public synchronized void sendSample(byte[] sample, long timestamp,float sampleRate, int sampleSize, int channels) throws IOException{
		dos.writeByte(Protocol.SAMPLE_BEGIN);
		dos.writeLong(timestamp);
		dos.writeFloat(sampleRate);
		dos.writeInt(sampleSize);
		dos.writeInt(channels);
		dos.writeInt(sample.length);
		dos.write(sample);
	}
}
