package project.io.proto;

public enum RequestCode {
	
	STREAM_ERROR(0x10),
	REQUEST_CODE_ERROR(0x11),
	SESSION_ID_ERROR(0x12),
	PRIVATE_KEY_ERROR(0x13),
	GET_SESSION_ID(0x20),
	GET_POST_DATA(0x21);

	public static RequestCode valueOf(int id) {
		for (RequestCode rc : values()) {
			if (rc.id == id)
				return rc;
		}
		return null;
	}

	public final int id;

	private RequestCode(int id) {
		this.id = id;
	}

}
