package SimpleExample;

import java.awt.image.BufferedImage;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IStream;
import com.xuggle.xuggler.IStreamCoder;
import com.xuggle.xuggler.video.ConverterFactory;

public class ServerListener extends MediaListenerAdapter {
	private ImageBuffer monitor;

	public ServerListener(ImageBuffer monitor) {
		super();
		this.monitor = monitor;
	}

	@Override
	public void onAddStream(IAddStreamEvent event) {
//		IStream stream = event.getSource().getContainer().getStream(event.getStreamIndex());
//		IStreamCoder coder = stream.getStreamCoder();
//
//		if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
//			videoStream = event.getStreamIndex();
//		}
	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {

	}

	@Override
	public void onVideoPicture(IVideoPictureEvent event) {
		BufferedImage bi = ConverterFactory.createConverter(
				ConverterFactory.XUGGLER_BGR_24, event.getMediaData()).toImage(
				event.getMediaData());
		monitor.addImage(new ImageBufferElement(bi,event.getTimeStamp()/1000));

	}

	@Override
	public void onReadPacket(IReadPacketEvent event) {
		
	}
}
