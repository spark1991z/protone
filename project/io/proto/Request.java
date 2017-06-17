package project.io.proto;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import project.io.SecureInputStream;

/**
 * 
 * @author spark1991z
 * 
 */
public class Request extends InputStream {

	private InputStream in;
	private Status status;
	private final RequestCode rc;

	public Request(RequestCode rc, byte[] secureKey, InputStream in) {
		this.rc = rc;
		try {
			this.in = new SecureInputStream(secureKey, "AES", in);
			status = Status.OK;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			status = Status.SECRET_KEY_ERROR;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			status = Status.ALGORITHM_ERROR;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			status = Status.PADDING_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			status = Status.IO_ERROR;
		}
	}

	public RequestCode rc() {
		return rc;
	}

	public Status status() {
		return status;
	}

	@Override
	public int read() throws IOException {
		if (in != null)
			return in.read();
		return -1;
	}

}
