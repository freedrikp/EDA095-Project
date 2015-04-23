package SimpleExample;

import java.util.LinkedList;

public class ClientImageBuffer {
	private LinkedList<ImageBufferElement> buffer;

	public ClientImageBuffer() {
		this.buffer = new LinkedList<ImageBufferElement>();
	}
	
	public synchronized void addImage(ImageBufferElement image){
		buffer.add(image);
		notifyAll();
	}
	
	public synchronized ImageBufferElement getImage(){
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
