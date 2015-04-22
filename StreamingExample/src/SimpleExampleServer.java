import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.video.ConverterFactory;

class ServerListener extends MediaListenerAdapter{
	private OutputStream out;
	public ServerListener(OutputStream out){
		super();
		this.out = out;
	}
	
	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {
		
	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		BufferedImage bi = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, event.getMediaData()).toImage(event.getMediaData());
		System.out.println(event.getMediaData());
		try {
			ImageIO.write(bi, "png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onReadPacket(IReadPacketEvent event) {
		
	}
	
	
	
}

public class SimpleExampleServer {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 IMediaReader reader = ToolFactory.makeReader("media/sw.mp4");
		 ServerSocket ss;
		try {
			ss = new ServerSocket(7373);
			 Socket socket = ss.accept();
			 reader.addListener(new ServerListener(socket.getOutputStream()));
			  while(reader.readPacket() == null)
			    Thread.sleep(1000);;
			  socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  
	}

}
