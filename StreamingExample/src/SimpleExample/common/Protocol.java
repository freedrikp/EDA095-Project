package SimpleExample.common;

public class Protocol {
	
	// General

	// Server => Client
	public static final byte FRAME_BEGIN = 10;
	public static final byte STREAM_END = 11;
	
	// Client => Server
	public static final byte CLOSE_STREAM = 30;
	public static final byte PLAY_STREAM = 31;
	public static final byte PAUSE_STREAM = 32; 
	
	
}