package project.io.proto;

/**
 * 
 * @author spark1991z
 * 
 */
public enum ResponseCode {
	OK(0x21), PRIVATE_KEY_ERROR(0x12), REQUEST_CODE_ERROR(0x10), SESSION_ID_CHANGED(
			0x20), SESSION_ID_ERROR(0x11);

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