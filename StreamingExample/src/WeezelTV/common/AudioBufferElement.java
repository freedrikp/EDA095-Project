package WeezelTV.common;


public class AudioBufferElement {
	private byte[] sample;
	private long timestamp;
	private float sampleRate;
	private int sampleSize;
	private int channels;
	
	public AudioBufferElement(byte[] sample, long timestamp, float sampleRate, int sampleSize, int channels){
		this.sample = sample;
		this.timestamp = timestamp;
		this.sampleRate = sampleRate;
		this.sampleSize = sampleSize;
		this.channels = channels;
	}
	
	public byte[] getSample(){
		return sample;
	}
	
	public long getTimestamp(){
		return timestamp;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	public int getSampleSize() {
		return sampleSize;
	}

	public int getChannels() {
		return channels;
	}
}
