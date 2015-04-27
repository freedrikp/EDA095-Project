package SimpleExample.client;

public class ClientGuiUpdater extends Thread {

	private ClientGui gui;

	public ClientGuiUpdater(ClientGui gui) {
		this.gui = gui;
	}

	@Override
	public void run() {
		while (true) {
			gui.updateProgressBar();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
