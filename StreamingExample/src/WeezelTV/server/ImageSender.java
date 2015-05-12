package WeezelTV.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import WeezelTV.common.Configuration;
import WeezelTV.common.ImageBufferElement;

public class ImageSender extends Thread {
	private ServerBuffer sBuffer;
	private ServerSender sSender;

	public ImageSender(ServerBuffer sBuffer, ServerSender sSender) {
		super();
		this.sBuffer = sBuffer;
		this.sSender = sSender;
	}

	public void run() {
		try {
			while ((!sBuffer.finished() || sBuffer.hasMoreFrames())
					&& sBuffer.isStreamOpen()) {
				ImageBufferElement image = sBuffer.getNextImage();
				byte[] bytes = createBytesFromImage(image.getImage(),
						Configuration.SERVER_COMPRESSION_QUALITY);
				if (bytes != null) {
					sSender.sendFrame(bytes, image.getTimestamp());
				}
			}
			if (!sBuffer.hasMoreSamples()) {
				sSender.sendEndOfStream();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private static byte[] createBytesFromImage(BufferedImage image) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteOut);
			byteOut.flush();
			byte[] imageInByte = byteOut.toByteArray();
			byteOut.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] createBytesFromImage(BufferedImage image,
			float compression) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

			Iterator<ImageWriter> writers = ImageIO
					.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream imageOut = ImageIO
					.createImageOutputStream(byteOut);
			writer.setOutput(imageOut);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compression);
			writer.write(null, new IIOImage(image, null, null), param);

			byteOut.flush();
			byte[] imageInByte = byteOut.toByteArray();
			byteOut.close();

			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
