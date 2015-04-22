package SimpleExample;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import com.xuggle.xuggler.demos.VideoImage;

public class Client {

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 7373);
			VideoImage vi = new VideoImage();
			while (!socket.isClosed()) {
				BufferedImage bi = ImageIO.read(socket.getInputStream());
				vi.setImage(bi);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
