package SimpleExample.client;

import SimpleExample.common.ImageBufferElement;

public class ClientImageViewer extends Thread {
	private ClientBuffer cb;
	private ClientGui gui;

	public ClientImageViewer(ClientBuffer cb, ClientGui gui) {
		this.cb = cb;
		this.gui = gui;
	}

	public void run() {
		long previousTimestamp = 0;
		long lastShown = 0;
		while (cb.moreToShow()) {
			cb.waitForPlay();
			ImageBufferElement image = cb.getImage();
			long timestamp = image.getTimestamp();
			try {
				long timeToSleep = timestamp - previousTimestamp
						- (System.currentTimeMillis() - lastShown);
				if (timeToSleep > 0) {
					Thread.sleep(timeToSleep);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			previousTimestamp = timestamp;
			gui.setImage(image.getImage());
			lastShown = System.currentTimeMillis();
		}
	}
}
