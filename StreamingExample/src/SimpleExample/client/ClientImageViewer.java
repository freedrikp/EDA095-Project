package SimpleExample.client;

import SimpleExample.common.ImageBufferElement;

public class ClientImageViewer extends Thread {
	private ClientBuffer cBuffer;
	private ClientGui gui;

	public ClientImageViewer(ClientBuffer cBuffer, ClientGui gui) {
		this.cBuffer = cBuffer;
		this.gui = gui;
	}

	public void run() {
		long pausTime = 0;
		boolean firstTime = true;
		long savedStart = 0;
		while (cBuffer.moreToShow()) {
			long tmp = System.currentTimeMillis();
			long movieStart = cBuffer.waitForPlay();
			if (savedStart == 0) {
				savedStart = movieStart;
			} else if (savedStart != movieStart) {
				break;
			}
			ImageBufferElement image = cBuffer.getImage();
			if (!firstTime) {
				pausTime += System.currentTimeMillis() - tmp;
			} else {
				firstTime = false;
			}
			long timestamp = image.getTimestamp();
			try {
				long timeToSleep = timestamp + pausTime
						- System.currentTimeMillis() + movieStart;
				if (timeToSleep > 0) {
					Thread.sleep(timeToSleep);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gui.setImage(image.getImage());
		}
	}
}
