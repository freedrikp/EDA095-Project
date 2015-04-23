package SimpleExample;

import java.util.LinkedList;

public class ClientImageBuffer {
	private LinkedList<ImageBufferElement> buffer;
	private ClientGui gui;

	public ClientImageBuffer(ClientGui gui) {
		this.buffer = new LinkedList<ImageBufferElement>();
		this.gui = gui;
	}
	
	public synchronized void addImage(ImageBufferElement image){
		buffer.add(image);
		gui.updateProgressBar(buffer.size());
		notifyAll();
	}
	
	public synchronized ImageBufferElement getImage(){
		gui.updateProgressBar(buffer.size());
		while(buffer.size() < 100){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return buffer.poll();
	}
	
	public synchronized int getBufferSize(){
		return buffer.size();
	}
}
