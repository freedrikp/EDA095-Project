package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.video.ConverterFactory;



public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IMediaReader reader = ToolFactory.makeReader("media/sw.mp4");
		ServerSocket ss;
		try {
			ss = new ServerSocket(7373);
			Socket socket = ss.accept();
			ImageBuffer monitor = new ImageBuffer();
			ImageSender is = new ImageSender(monitor,
					socket.getOutputStream());
			is.start();
			reader.addListener(new ServerListener(monitor));
			while (reader.readPacket() == null);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
