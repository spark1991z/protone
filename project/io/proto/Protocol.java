package project.io.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import project.io.SecureInputStream;
import project.io.SecureOutputStream;

public class Protocol {

	protected final String name;

	private InputStream in;
	private OutputStream out;

	public static Status open(Protocol p, Socket s) throws IOException {
		if (p == null)
			return Status.PROTOCOL_NOT_SET;
		if (s == null)
			return Status.SOCKET_NOT_SET;
		if (s.isClosed())
			return Status.SOCKET_CLOSED;
		Status ret = null;
		try {
			p.in = new SecureInputStream(p.name, "AES", s.getInputStream());
			p.out = new SecureOutputStream(p.name, "AES", s.getOutputStream());
			return Status.OK;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			ret = Status.SECRET_KEY_ERROR;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			ret = Status.ALGORITHM_ERROR;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			ret = Status.PADDING_ERROR;
		}
		p.in.close();
		return ret;
	}

	public static Request read(Protocol p) throws IOException {
		if (p == null)
			return new Request(RequestCode.PROTOCOL_ERROR, null);
		if (p.in == null || p.in.available() <= 0) {
			p.out.close();
			return new Request(RequestCode.CONNECTION_ERROR, null);
		}
		RequestCode rc = RequestCode.valueOf(p.in.read());
		if (rc == null) {
			p.out.close();
			return new Request(RequestCode.REQUEST_CODE_ERROR, null);
		}
		byte[] sid = new byte[16];
		if (p.in.read() == 1) {
			if (p.in.read(sid) < 16) {
				p.out.close();
				return new Request(RequestCode.SESSION_ERROR, null);
			}
		}
		return new Request(rc, sid);
	}

	public static ResponseCode write(Protocol p, Response r) throws IOException {
		if (p == null)
			return ResponseCode.PROTOCOL_ERROR;
		if (r == null)
			return ResponseCode.RESPONSE_ERROR;
		p.out.write(r.code.id);
		p.out.write(r.sessionId);
		
		return ResponseCode.OK;
	}
	
	public static InputStream input(byte[] privateKey, Protocol p){
		return null;
	}
	
	public static OutputStream output(byte[] privateKey, Protocol p){
		return null;
	}

	public Protocol(String name) {
		this.name = name;
	}
}
