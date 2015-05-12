package SimpleExample.server;

import java.awt.image.BufferedImage;

import SimpleExample.common.AudioBufferElement;
import SimpleExample.common.ImageBufferElement;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.video.ConverterFactory;

public class ServerListener extends MediaListenerAdapter {
	private ServerBuffer monitor;

	public ServerListener(ServerBuffer monitor) {
		super();
		this.monitor = monitor;
	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {
		IAudioSamples aSamples = event.getAudioSamples();
		if (aSamples.isComplete()) {
			byte[] rawBytes = aSamples.getData().getByteArray(0,
					aSamples.getSize());
			monitor.addSample(new AudioBufferElement(rawBytes, event
					.getTimeStamp() / 1000, (float) aSamples.getSampleRate(),
					(int) aSamples.getSampleBitDepth(), aSamples.getChannels()));
		}
		event.getMediaData().delete();
	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		if (event.getMediaData().isComplete()) {
			BufferedImage bi = ConverterFactory.createConverter(
					ConverterFactory.XUGGLER_BGR_24, event.getMediaData())
					.toImage(event.getMediaData());
			monitor.addImage(new ImageBufferElement(bi,
					event.getTimeStamp() / 1000));
		}
		event.getMediaData().delete();
	}

}
