package message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class HandshakeMessage {//implements Externalizable {
	public int peerID;
	public byte[] zeroBits;
	public String handshaker_header;
	
	public HandshakeMessage() {
		zeroBits = new byte[Constant.ZERO_BITS.length];
	}
	
	public HandshakeMessage(int peerID) {
		this.peerID = peerID;
		this.zeroBits = Constant.ZERO_BITS;
		this.handshaker_header = Constant.HANDSHAKER_HEADER;
	}
	
	public boolean checkReceivedHandshake(int serverID) {
		boolean result1 = this.handshaker_header == Constant.HANDSHAKER_HEADER;
		boolean result2 = this.peerID == serverID;
		return result1 && result2;
	}
	
	@Override
	public String toString() {
		String bitFieldString = new String();
		for(byte b : zeroBits)
			bitFieldString += Integer.toString(b & 0x00FF) + ',';
		bitFieldString = bitFieldString.substring(0, bitFieldString.length()-1);
		return handshaker_header + "\n"
				+ bitFieldString + "\n"
				+ Integer.toString(peerID);
	}

	private boolean getFromByteArray(byte[] byteArray) throws IOException {
		//check if the length of handshake message is correct
		if(byteArray.length != Constant.HANDSHAKER_LENGTH)
			return false;
		handshaker_header = new String(Arrays.copyOfRange(byteArray, 0, Constant.HANDSHAKER_HEADER_LENGTH));
		zeroBits = Arrays.copyOfRange(byteArray, Constant.HANDSHAKER_HEADER_LENGTH, Constant.HANDSHAKER_HEADER_LENGTH + Constant.ZERO_BITS.length);
		peerID = convertBytesToInteger(Arrays.copyOfRange(byteArray, Constant.HANDSHAKER_LENGTH - Integer.BYTES, Constant.HANDSHAKER_LENGTH));
		return true;
	}

	public void readHandshakeMessage(InputStream input) throws IOException, InterruptedException {
		byte[] byte_Array;
		byte_Array = new byte[Constant.HANDSHAKER_LENGTH];
		while (input.available() != Constant.HANDSHAKER_LENGTH) {
			Thread.sleep(Constant.SHORT_INTERVAL);
		}
		synchronized(input) {
			input.read(byte_Array, 0, Constant.HANDSHAKER_LENGTH);
		}
		getFromByteArray(byte_Array);
		return;
	}
	
	private int convertBytesToInteger(byte[] byteArray) {
		ByteBuffer wrapped = ByteBuffer.wrap(byteArray);
		return wrapped.getInt();
	}
	
	private byte[] convertToByteArray() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream (bos);
		
		dos.writeBytes(handshaker_header);
		dos.write(zeroBits);
		dos.writeInt(peerID);
		
		bos.flush();
		byte[] byteArray;
		byteArray = bos.toByteArray();
		bos.close();
		return byteArray;
	}

	public void writeHandshakeMessage(OutputStream output) throws IOException {
		byte[] byteArray = convertToByteArray();
		synchronized(output) {
			output.write(byteArray, 0, Constant.HANDSHAKER_LENGTH);
		}
		return;
	}

}
