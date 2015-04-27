package SimpleExample.server;

import java.awt.image.BufferedImage;

import SimpleExample.common.ImageBufferElement;

import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.video.ConverterFactory;

public class ServerListener extends MediaListenerAdapter {
	private ServerImageBuffer monitor;
	private ServerAudioBuffer audioMonitor;

	public ServerListener(ServerImageBuffer monitor, ServerAudioBuffer audioMonitor) {
		super();
		this.monitor = monitor;
		this.audioMonitor = audioMonitor;
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
		IAudioSamples aSamples = event.getAudioSamples();
		//System.out.println("samplerate: " + aSamples.getSampleRate());
		//System.out.println("sampleSize for format in bits: " + IAudioSamples.findSampleBitDepth(aSamples.getFormat()));
		//System.out.println("channels: " + aSamples.getChannels());
		if(aSamples.isComplete()){
			byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
			audioMonitor.addSample(rawBytes);
		}
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
