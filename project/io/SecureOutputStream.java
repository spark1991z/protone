package project.io;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecureOutputStream extends OutputStream {
 
	private CipherOutputStream out;
	
	public SecureOutputStream(String name, String algorithm, OutputStream out) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(name.getBytes());
		byte[] key = md.digest();
		Cipher c = Cipher.getInstance(algorithm);
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, algorithm));
		this.out = new CipherOutputStream(out,c);
		this.out.write(key);
	}

	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
	
	@Override
	public void flush() throws IOException {
		super.flush();
		out.flush();
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		out.close();
	}
}