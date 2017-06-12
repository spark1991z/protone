package project.io.proto;

import project.Configurator;

public class Request {
	
	public final RequestCode code;
	public final byte[] sessionId;
	protected Configurator config;
	
	public Request(RequestCode code, byte[] sessionId){
		this.code = code;
		this.sessionId = sessionId;
		config = new Configurator();
	}
}
