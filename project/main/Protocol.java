package project.main;

import static project.main.Status.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import project.io.SecureInputStream;

public class Protocol {

	protected final String name;

	private InputStream in;
	
	public static Status open(Protocol p, InputStream in) throws IOException {
		if (p == null)
			return PROTOCOL_NOT_SET;
		if (in == null)
			return STREAM_NOT_SET;
		if (in.available() < 0)
			return STREAM_CLOSED;
		Status ret = null;
		try {
			p.in = new SecureInputStream(p.name, "AES", in);
			return OK;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			ret = SECRET_KEY_ERROR;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			ret = ALGORITHM_ERROR;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			ret = PADDING_ERROR;
		}
		p.in.close();
		return ret;
	}

	protected Protocol(String name) {
		this.name = name;
	}
}
