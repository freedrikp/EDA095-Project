package SimpleExample.common;

import java.awt.image.BufferedImage;

public class ImageBufferElement {
	private BufferedImage image;
	private long timestamp;
	
	public ImageBufferElement(BufferedImage image, long timestamp){
		this.image = image;
		this.timestamp = timestamp;
	}
	
	public BufferedImage getImage(){
		return image;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
}
