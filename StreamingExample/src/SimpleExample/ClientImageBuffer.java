package SimpleExample;

import java.util.LinkedList;

public class ClientImageBuffer {
	private LinkedList<ImageBufferElement> buffer;
	private ClientGui gui;
	private boolean firstImage = true;
	
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
		if(firstImage){
		while(buffer.size() < 100){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		firstImage = false;
		}
		while(buffer.isEmpty()){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gui.updateProgressBar(buffer.size());
		return buffer.poll();
	}
	
	public synchronized int getBufferSize(){
		return buffer.size();
	}
}
