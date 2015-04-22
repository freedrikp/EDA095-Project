package SimpleExample;

import java.awt.image.BufferedImage;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.video.ConverterFactory;

public class ServerListener extends MediaListenerAdapter {
	private ImageBuffer monitor;

	public ServerListener(ImageBuffer monitor) {
		super();
		this.monitor = monitor;
	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {

	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		BufferedImage bi = ConverterFactory.createConverter(
				ConverterFactory.XUGGLER_BGR_24, event.getMediaData()).toImage(
				event.getMediaData());
		System.out.println(event.getMediaData());

		monitor.addImage(bi);
	}

	@Override
	public void onReadPacket(IReadPacketEvent event) {

	}

}
