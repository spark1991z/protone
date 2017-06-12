package project.io.proto;

public enum ResponseCode {
	
	PROTOCOL_ERROR(0x10),
	RESPONSE_ERROR(0x11),
	OK(0x20),
	SESSION_ERROR(0x21),
	SESSION_CHANGED(0x22);

	public static ResponseCode valueOf(int id) {
		for (ResponseCode rc : values()) {
			if (rc.id == id)
				return rc;
		}
		return null;
	}

	public final int id;

	private ResponseCode(int id) {
		this.id = id;
	}

}
