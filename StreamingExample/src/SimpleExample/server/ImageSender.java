package SimpleExample.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import SimpleExample.common.Configuration;
import SimpleExample.common.ImageBufferElement;

public class ImageSender extends Thread {
	private ServerImageBuffer monitor;
	private ServerSender ss;

	public ImageSender(ServerImageBuffer monitor, ServerSender ss) {
		super();
		this.monitor = monitor;
		this.ss = ss;
	}

	public void run() {
		try {
			while ((!monitor.finished() || monitor.hasMore()) && monitor.isStreamOpen() ) {
				ImageBufferElement image = monitor.getNextImage();
				byte[] bytes = createBytesFromImage(image.getImage(),Configuration.SERVER_COMPRESSION_QAULITY);
				if (bytes != null) {
					ss.sendFrame(bytes, image.getTimestamp());
				}
			}
			ss.sendEndOfStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static byte[] createBytesFromImage(BufferedImage image) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] createBytesFromImage(
			BufferedImage image, float compression) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			Iterator<ImageWriter> writers = ImageIO
					.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(compression);
			writer.write(null, new IIOImage(image, null, null), param);

			baos.flush();
			byte[] imageInByte = baos.toByteArray();
			baos.close();
			
//			System.out.println(imageInByte.length);
			return imageInByte;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
