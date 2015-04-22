package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.demos.VideoImage;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 7373);
			VideoImage vi = new VideoImage();
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			while (!socket.isClosed()) {
				int length = dis.readInt();
				byte[] bytes = new byte[length];
				int bytesRead = 0;
				int read = 0;
				while ((read = dis.read(bytes, bytesRead, length-bytesRead)) > 0){
					bytesRead += read;
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				vi.setImage(createImageFromBytes(bytes));
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private static BufferedImage createImageFromBytes(byte[] imageData) {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    try {
	        return ImageIO.read(bais);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}

}
