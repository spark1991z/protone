package project.io;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecureInputStream extends InputStream {
	
 
	private CipherInputStream in;
	private int position;

	 public SecureInputStream(String name, String algorithm, InputStream in) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException{
		 MessageDigest md = MessageDigest.getInstance("MD5");
		 md.reset();
		 md.update(name.getBytes());
		 byte[] key=md.digest(),check=new byte[16];
		 Cipher c = Cipher.getInstance(algorithm);
		 c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, algorithm));
		 this.in = new CipherInputStream(in, c);
		 if(this.in.read(check)!=check.length || !Arrays.equals(key, check)){
			 throw new InvalidKeyException();
		 }
		 position = 0;
	 }

	 
	@Override
	public int read() throws IOException {
		int read = in.read();
		if(read>=0) position++;
		return read;
	}
	
	@Override
	public void reset() throws IOException {
		if(position>0){
			skip(-position);
			position = 0;
		}
	}
	
	public int position(){
		return position;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		in.close();
	}
}
