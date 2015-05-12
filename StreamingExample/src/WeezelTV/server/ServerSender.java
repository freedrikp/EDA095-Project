package WeezelTV.server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import WeezelTV.common.Configuration;
import WeezelTV.common.Protocol;

public class ServerSender {
	private Socket socket;
	private DataOutputStream out;

	public ServerSender(Socket socket) throws IOException {
		this.socket = socket;
		out = new DataOutputStream(socket.getOutputStream());
	}

	public synchronized void sendFrame(byte[] image, long timestamp)
			throws IOException {
		out.writeByte(Protocol.FRAME_BEGIN);
		out.writeLong(timestamp);
		out.writeInt(image.length);
		out.write(image);
	}

	public synchronized void sendEndOfStream() throws IOException {
		out.writeByte(Protocol.STREAM_END);
		socket.close();
	}

	public synchronized void sendMovieList() throws IOException {
		out.writeByte(Protocol.LIST_START);
		File[] dir = new File(Configuration.SERVER_MEDIA_DIRECTORY).listFiles();
		ArrayList<String> toBeSent = new ArrayList<String>();
		for (File f : dir) {
			if (f.getName().charAt(0) != '.') {
				toBeSent.add(f.getName());
			}
		}
		out.writeInt(toBeSent.size());
		for (String s : toBeSent) {
			out.writeUTF(s);
		}
	}

	public synchronized void sendSample(byte[] sample, long timestamp,
			float sampleRate, int sampleSize, int channels) throws IOException {
		out.writeByte(Protocol.SAMPLE_BEGIN);
		out.writeLong(timestamp);
		out.writeFloat(sampleRate);
		out.writeInt(sampleSize);
		out.writeInt(channels);
		out.writeInt(sample.length);
		out.write(sample);
	}
}
