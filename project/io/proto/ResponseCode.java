package project.io.proto;

public enum ResponseCode{
	PROTOCOL_ERROR(0x10),
	RESPONSE_ERROR(0x11),
	
	OK(0x20),
	SESSION_ERROR(0x21),
	SESSION_CHANGED(0x22);
	
	
	public final int id;
	
	private ResponseCode(int id){
		this.id = id;
	}

}
