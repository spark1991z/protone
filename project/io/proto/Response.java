package project.io.proto;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import project.io.SecureOutputStream;

/**
 * 
 * @author spark1991z
 * 
 */
public class Response extends OutputStream implements Closeable {

	private OutputStream out, sos;
	private ResponseCode rc;
	private final byte[] sessionId, secureKey, newSecureKey;
	private boolean closed;

	public Response(byte[] sessionId, byte[] secureKey, byte[] newSecretKey,
			OutputStream out) {
		this.sessionId = sessionId;
		this.secureKey = secureKey;
		this.newSecureKey = newSecretKey;
		this.out = out;
	}

	private void sendHeaders() throws IOException {
		if (!closed && sos == null) {
			out.write(rc != null ? rc.id : ResponseCode.OK.id);
			if (sessionId != null) {
				out.write(sessionId);
			}
			if (secureKey != null && newSecureKey != null) {
				try {
					sos = new SecureOutputStream(secureKey, "AES", out);
					sos.write(newSecureKey);
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean isClosed() {
		return closed;
	}

	public ResponseCode responseCode() {
		return rc;
	}

	public void responseCode(ResponseCode rc) {
		if (rc != null)
			this.rc = rc;
	}

	@Override
	public void close() throws IOException {
		if (!closed) {
			sendHeaders();
			sos.flush();
			sos.close();
			out.flush();
			out.close();
			closed = true;
		}
	}

	@Override
	public void write(int arg0) throws IOException {
		if (!closed) {
			sendHeaders();
			sos.write(arg0);
		}
	}

}
