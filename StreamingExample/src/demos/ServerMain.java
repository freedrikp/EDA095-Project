package demos;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.ToolFactory;


public class ServerMain {

	public static void main(String[] args) {
		System.out.println("Server start");
		
		IMediaReader reader = ToolFactory.makeReader("media/sw.mp4");
		  reader.addListener(ToolFactory.makeViewer());
		  while(reader.readPacket() == null);
	}

}
