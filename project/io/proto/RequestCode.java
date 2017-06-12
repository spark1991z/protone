package project.io.proto;

public enum RequestCode {
	PROTOCOL_ERROR(0x10),
	CONNECTION_ERROR(0x11),
	REQUEST_CODE_ERROR(0x14),
	SESSION_ERROR(0x15),
	
	
	GET_SESSION_ID(0x20),
	GET_DATA(0x21),
	POST_DATA(0x22);
	
	public static RequestCode valueOf(int id){
		for(RequestCode rc : values()){
			if(rc.id == id)
				return rc;
		}
		return null;
	}
	
	public final int id;
	
	private RequestCode(int id){
		this.id = id;
	}

}
