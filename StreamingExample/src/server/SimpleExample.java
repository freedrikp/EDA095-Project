package server;

import java.awt.image.BufferedImage;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.demos.VideoImage;
import com.xuggle.xuggler.video.ConverterFactory;

class Listener extends MediaListenerAdapter{
	private VideoImage vi;
	public Listener(VideoImage vi){
		super();
		this.vi = vi;
	}
	
	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {
		// TODO Auto-generated method stub
		super.onAudioSamples(event);
		
	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		BufferedImage bi = ConverterFactory.createConverter(ConverterFactory.XUGGLER_BGR_24, event.getMediaData()).toImage(event.getMediaData());
		System.out.println(event.getMediaData());
		vi.setImage(bi);
	}
	
}

public class SimpleExample {

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 IMediaReader reader = ToolFactory.makeReader("media/sw.mp4");
//		  reader.addListener(ToolFactory.makeViewer(true));
		 VideoImage vi = new VideoImage();
		 MediaToolAdapter mta = new MediaToolAdapter();
		 mta.addListener(new Listener(vi));
		 reader.addListener(mta);
		  while(reader.readPacket() == null)
		    ;
		  
	}

}
