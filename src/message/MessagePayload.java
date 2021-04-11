package message;

public class MessagePayload {
	/*
	 * choke, unchoke, interested, not_interested message has no payload
	 * have, request message has a payload of 4-byte piece index field
	 * bitfield message has a paylaod of a bit field
	 * piece message has a payload of 4-byte piece index field and the content of the piece
	 */
	public int piece_index;
	public byte[] content;
	public BitField bitField;
	
	public MessagePayload() {}
	
	public MessagePayload(int pieceIndex) {
		this.piece_index = pieceIndex;
	}

	public MessagePayload(int pieceIndex, byte[] content) {
		this.content = content;
		this.piece_index = pieceIndex;
	}

	public MessagePayload(BitField bitField) {
		this.bitField = bitField;
	}
	
	@Override
	public String toString() {
		return "piece index: " + Integer.toString(piece_index) + "\n"
				+ "bit field: " + bitField.toString() + "\n"
				+ "content: " + content.toString();
	}
	
}
