package project.io.proto;

import project.Configurator;

public class Response {
	
	public final ResponseCode code;
	public final byte[] sessionId;
	protected Configurator config;
	
	public Response(ResponseCode code, byte[] sessionId){
		this.code = code;
		this.sessionId = sessionId;
		config = new Configurator();
	}
}
