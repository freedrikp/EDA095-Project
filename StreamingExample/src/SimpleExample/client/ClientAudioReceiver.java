package SimpleExample.client;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientAudioReceiver extends Thread {

	private ClientAudioBuffer cab;
	private Socket socket;
	private DataInputStream dis;

	public ClientAudioReceiver(ClientAudioBuffer cab, Socket socket) throws IOException {
		this.cab = cab;
		this.socket = socket;
		this.dis = new DataInputStream(socket.getInputStream());
	}

	public void run() {
		while (!socket.isClosed()) {
			receiveSample();
		}
		cab.setAllFramesSent(true);
	}



	private void receiveSample() {
		try {
			int length = dis.readInt();
			byte[] bytes = new byte[length];
			dis.readFully(bytes);
			cab.addSample(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
