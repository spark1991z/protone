package project.main;

import static project.Configurator.OK;
import static project.Stage.ALPHA;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import project.Parameter;
import project.Project;
import project.Stage;
import project.io.FilteredPrintStream;
import project.io.proto.Protocol;
import project.io.proto.Status;
/**
 * 
 * @author spark1991z
 * 
 */
public class Main extends Project implements Runnable {

	private static Main project;

	public static final Project project() {
		return project;
	}

	public static void main(String[] args) {
		if (project != null)
			return;
		project = new Main("ProtONE", 0.5, 5, ALPHA, 143.2); // 13.06
		System.out.printf("%s%n-----------------------------%n", project);
		int push = project.config.pushArgs(args);
		if (push != OK || project.config.get('h').changed()) {
			usage();
			return;
		}
		if(!project.config.get('d').changed()){
			System.setOut(new PrintStream(new FilteredPrintStream(null)));
			System.setErr(new PrintStream(new FilteredPrintStream(null)));
		}
		project.start();
	}

	private static String genStr(int len, String append) {
		String s = "";
		for (int i = 0; i < len - append.length(); i++)
			s += " ";
		s += append;
		return s;

	}

	private static void usage() {
		StringBuffer out = new StringBuffer();
		int len = project.config.maxLabelLength();
		for (char c : project.config.flags()) {
			out.append(String.format("%s\t%s%n", genStr(len, "-" + c),
					project.config.get(c).display));
		}
		for (String k : project.config.params()) {
			out.append(String.format("%s\t%s%n", genStr(len, "--" + k),
					project.config.get(k).display));
		}
		System.out.println(out);
	}

	private Thread runnable;
	private ServerSocket server;

	
	private Main(String name, double ver, int revision, Stage stage,
			double build) {
		super(name, ver, revision, stage, build);
		config.add('d', "debug mode");
		config.add('h', "show this information");
		config.add("port", new Parameter<Integer>(9999, "connection port"));
	}

	@Override
	protected synchronized void start() {
		super.start();
		if (server == null) {
			int port = (Integer) (config.get("port").isSet() ? config.get(
					"port").value() : config.get("port").def);
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
	protected synchronized void stop() {
		super.stop();
		if (runnable != null) {
			System.out.printf("Stoping server on %s port...%n", server.getLocalPort());
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
							Protocol prot = new Protocol(name);
							Status status = Protocol.open(prot, s);
								System.out.printf("Connection status: %s%n",
										status);
							if (status != Status.OK) {
								return;
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
