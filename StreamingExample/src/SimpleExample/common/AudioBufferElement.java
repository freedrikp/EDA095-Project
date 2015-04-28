package SimpleExample.common;


public class AudioBufferElement {
	private byte[] sample;
	private long timestamp;
	
	public AudioBufferElement(byte[] sample, long timestamp){
		this.sample = sample;
		this.timestamp = timestamp;
	}
	
	public byte[] getSample(){
		return sample;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
}
