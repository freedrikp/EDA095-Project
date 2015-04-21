package gammalt;
import java.io.FileInputStream;

//VideoStream


public class VideoStream {

    FileInputStream fis; //video file
    int frameNb; //current frame nb

    //-----------------------------------
    //constructor
    //-----------------------------------
    public VideoStream(String filename) throws Exception {

        //init variables
        fis = new FileInputStream(filename);
        frameNb = 0;
    }

    //-----------------------------------
    // getNextFrame
    // returns the next frame as an array of byte and the size of the frame
    //-----------------------------------
    public int getNextFrame(byte[] frame) throws Exception {
        int length = 0;
        String lengthString;
        byte[] frameLength = new byte[5];

        //read current frame length
        fis.read(frameLength, 0, 5);

        //transform frameLength to integer
        lengthString = new String(frameLength);
        length = Integer.parseInt(lengthString);
        return fis.read(frame, 0, length);
    }
}
