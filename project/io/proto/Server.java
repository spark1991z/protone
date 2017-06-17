package project.io.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Random;

import javax.crypto.NoSuchPaddingException;
import javax.net.ServerSocketFactory;

import project.Project;
import project.Stage;
import project.io.SecureInputStream;
import project.io.SecureOutputStream;

/**
 * 
 * @author spark1991z
 * 
 */
public class Server extends Project implements Runnable {

	private static String normalizeHash(byte[] token) {
		String md5s = "";
		for (byte b : token)
			md5s += Integer.toHexString(b & 0xff);
		return md5s;
	}
	
	private ConnectionListener listener;
	private final int port;
	private Thread runnable;
	private ServerSocket server;
	private Hashtable<byte[], byte[]> sessionPrivateKeys;

	public Server(int port, ConnectionListener listener) {
		super("ProServer", 0.1, 1, Stage.ALPHA, 1.3); // 17.06
		this.port = port;
		this.listener = listener;
		sessionPrivateKeys = new Hashtable<byte[], byte[]>();
	}
	
	private byte[] genKey() throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update((name + "_" + Long.toString(new Random().nextLong()))
				.getBytes());
		return md.digest();
	}

	public boolean isWork() {
		return runnable != null && runnable.isInterrupted();
	}

	@Override
	public synchronized void run() {
		while (runnable != null) {
			try {
				final Socket s = server.accept();

				new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						System.out.printf("Connected '%s'%n", s);
						try {
							InputStream sis = new SecureInputStream(name
									.getBytes(), "AES", s.getInputStream());
							OutputStream sos = new SecureOutputStream(name
									.getBytes(), "AES", s.getOutputStream());
							System.out
									.println("Security channel validation was successful");

							RequestCode rc = RequestCode.valueOf(sis.read());
							System.out.printf("RequestCode: %s%n", rc);
							Request req = null;
							Response res = null;
							if (rc != null) {

								byte[] sid = null, nsk = null;
								switch (rc) {
								case GET_SESSION_ID:
									sid = genKey();
									nsk = genKey();
									res = new Response(sid, sid, nsk, sos);
									res.responseCode(ResponseCode.SESSION_ID_CHANGED);
									res.close();
									sessionPrivateKeys.put(sid, nsk);
									System.out
											.printf("SessionID & SecureKEY for '%s' changed: %s | %s%n",
													s, normalizeHash(sid),
													normalizeHash(nsk));
									break;
								case GET_POST_DATA:
									sid = new byte[16];
									if (sis.read(sid) != 16
											|| !sessionPrivateKeys
													.containsKey(sid)) {
										res = new Response(null, null, null,
												sos);
										res.responseCode(ResponseCode.SESSION_ID_ERROR);
										res.close();
										break;
									}
									System.out
											.println("SessionID validation was successful");
									byte[] osk = sessionPrivateKeys.get(sid);
									nsk = genKey();
									req = new Request(rc, osk, sis);
									if (req.status() != Status.OK) {
										res = new Response(null, null, null,
												sos);
										res.responseCode(ResponseCode.PRIVATE_KEY_ERROR);
										res.close();
										break;
									}
									System.out
											.printf("Private security channel validation was successful and NEW secureKEY changed: %s%n",
													normalizeHash(nsk));
									res = new Response(null, osk, nsk, sos);
									if (listener != null) {
										System.out
												.printf("Connection control was forwarded into ConnectionListener: %s%n",
														listener.getClass()
																.getName());
									}
									break;
								default:
									break;
								}
							} else {
								res = new Response(null, null, null, sos);
								res.responseCode(ResponseCode.REQUEST_CODE_ERROR);
								res.close();
							}
							sis.close();
							sos.flush();
							sos.close();
							s.close();
							System.out
									.printf("Connection for '%s' closed%n", s);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InvalidKeyException e) {
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						} catch (NoSuchPaddingException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	@Override
	public void start() {
		super.start();
		if (server == null) {
			System.out.printf("Starting server on %s port...%n", port);
			try {
				server = ServerSocketFactory.getDefault().createServerSocket(
						port);

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		if (runnable == null) {
			System.out.println("Server was successfully started");
			runnable = new Thread(this);
			runnable.start();
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (runnable != null) {
			System.out.printf("Stoping server on %s port...%n",
					server.getLocalPort());
			try {
				Socket s = new Socket("localhost", server.getLocalPort());
				s.close();
				runnable = null;
				System.out.println("Server was stoped");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
