package gammalt;
//class RTPPacket

public class RTPPacket {

    //size of the RTP header:
    static int HEADER_SIZE = 12;
    //Fields that compose the RTP header
    public int version;
    public int padding;
    public int extension;
    public int cc;
    public int marker;
    public int payloadType;
    public int sequenceNumber;
    public int timestamp;
    public int ssrc;
    //Bitstream of the RTP header
    public byte[] header;
    //size of the RTP payload
    public int payloadSize;
    //Bitstream of the RTP payload
    public byte[] payload;

    //--------------------------
    //Constructor of an RTPPacket object from header fields and payload bitstream
    //--------------------------
    public RTPPacket(int pType, int framenb, int time, byte[] data, int dataLength) {
        //fill by default header fields:
        version = 2;
        padding = 0;
        extension = 0;
        cc = 0;
        marker = 0;
        ssrc = 0;

        //fill changing header fields:
        sequenceNumber = framenb;
        timestamp = time;
        payloadType = pType;

        //build the header bistream:
        //--------------------------
        header = new byte[HEADER_SIZE];

        //fill the header array of byte with RTP header fields
        //pseudocode totally forgot the beginning of the header even though they declared version etc. up there...
        //should be 10000000 if I understand it correctly, so -128 because java uses signed bytes for whatever reason
        header[0] = -128;
        //payload type
        header[1] = 26;

        //sequence num
        header[2] = ((byte) (sequenceNumber / 256));
        header[3] = ((byte) (sequenceNumber % 256));

        //timestamp
        header[4] = ((byte) (timestamp / 16777216));
        header[5] = ((byte) (timestamp / 65536));
        header[6] = ((byte) (timestamp / 256));
        header[7] = ((byte) (timestamp % 256));

        //fill the payload bitstream:
        //--------------------------
        payloadSize = dataLength;
        payload = new byte[dataLength];

        //fill payload array of byte from data (given in parameter of the constructor)
        for (int i = 0; i < payload.length; i++) {
            payload[i] = data[i];
        }
        // ! Do not forget to uncomment method printHeader() below !
    }

    //--------------------------
    //Constructor of an RTPPacket object from the packet bistream
    //--------------------------
    public RTPPacket(byte[] packet, int packetSize) {
        //fill default fields:
        version = 2;
        padding = 0;
        extension = 0;
        cc = 0;
        marker = 0;
        ssrc = 0;

        //check if total packet size is lower than the header size
        if (packetSize >= HEADER_SIZE) {
            //get the header bitsream:
            header = new byte[HEADER_SIZE];
            for (int i = 0; i < HEADER_SIZE; i++) {
                header[i] = packet[i];
                //get the payload bitstream:
            }
            payloadSize = packetSize - HEADER_SIZE;
            payload = new byte[payloadSize];
            for (int i = HEADER_SIZE; i < packetSize; i++) {
                payload[i - HEADER_SIZE] = packet[i];
                //interpret the changing fields of the header:
            }
            payloadType = header[1] & 127;
            sequenceNumber = unsignedInt(header[3]) + 256 * unsignedInt(header[2]);
            timestamp = unsignedInt(header[7]) + 256 * unsignedInt(header[6]) + 65536 * unsignedInt(header[5]) + 16777216 * unsignedInt(header[4]);
        }
    }

    //--------------------------
    //getPayload: return the payload bistream of the RTPPacket and its size
    //--------------------------
    public int getPayload(byte[] data) {
        for (int i = 0; i < payloadSize; i++) {
            data[i] = payload[i];
        }
        return payloadSize;
    }

    //--------------------------
    //getPayloadLength: return the length of the payload
    //--------------------------
    public int getPayloadLength() {
        return payloadSize;
    }

    //--------------------------
    //getLength: return the total length of the RTP packet
    //--------------------------
    public int getLength() {
        return payloadSize + HEADER_SIZE;
    }

    //--------------------------
    //getPacket: returns the packet bitstream and its length
    //--------------------------
    public int getPacket(byte[] packet) {
        //construct the packet = header + payload
        for (int i = 0; i < HEADER_SIZE; i++) {
            packet[i] = header[i];
        }
        for (int i = 0; i < payloadSize; i++) {
            packet[i + HEADER_SIZE] = payload[i];
            //return total size of the packet
        }
        return payloadSize + HEADER_SIZE;
    }

    //--------------------------
    //getTimestamp
    //--------------------------
    public int getTimestamp() {
        return timestamp;
    }

    //--------------------------
    //getSequenceNumber
    //--------------------------
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    //--------------------------
    //getPayloadType
    //--------------------------
    public int getPayloadType() {
        return payloadType;
    }

    //--------------------------
    //print headers without the SSRC
    //--------------------------
    public void printHeader() {
        for (int i = 0; i < (HEADER_SIZE - 4); i++) {
            for (int j = 7; j >= 0; j--) {
                if (((1 << j) & header[i]) != 0) {
                    System.out.print("1");
                } else {
                    System.out.print("0");
                }
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    //return the unsigned value of 8-bit integer nb
    static int unsignedInt(int nb) {
        if (nb >= 0) {
            return nb;
        } else {
            return 256 + nb;
        }
    }
}
