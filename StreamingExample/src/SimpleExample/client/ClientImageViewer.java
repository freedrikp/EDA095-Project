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
//		long previousTimestamp = 0;
//		long lastShown = 0;
		long pausTime = 0;
		boolean firstTime = true;
		while (cb.moreToShow()) {
			long tmp = System.currentTimeMillis();
			long movieStart = cb.waitForPlay();
//			long pausTime = cb.getPausTime();
			ImageBufferElement image = cb.getImage();
			if (!firstTime){
				pausTime += System.currentTimeMillis()-tmp;
			}else{
				firstTime = false;
			}
			long timestamp = image.getTimestamp();
			try {
//				long timeToSleep = timestamp - previousTimestamp
//						- (System.currentTimeMillis() - lastShown);
				long timeToSleep = timestamp + pausTime - System.currentTimeMillis() + movieStart;
				if (timeToSleep > 0) {
					Thread.sleep(timeToSleep);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			previousTimestamp = timestamp;
			gui.setImage(image.getImage());
//			lastShown = System.currentTimeMillis();
		}
	}
}
