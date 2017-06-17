package project.main;

import java.io.PrintStream;

import project.Configurator;
import project.Parameter;
import project.Project;
import project.Stage;
import project.io.FilteredPrintStream;
import project.io.proto.ConnectionListener;
import project.io.proto.Request;
import project.io.proto.Response;
import project.io.proto.Server;

/**
 * 
 * @author spark1991z
 * 
 */
public class Main extends Project implements ConnectionListener {

	private static Main project;

	private static String genStr(int len, String append) {
		String s = "";
		for (int i = 0; i < len - append.length(); i++)
			s += " ";
		s += append;
		return s;

	}

	public static void main(String[] args) {
		if (project != null)
			return;
		project = new Main();
		System.out.printf("%s%n%s%n-----------------------------%n", project,
				project.server);
		int push = project.config.pushArgs(args);
		if (push != Configurator.OK || project.config.get('h').changed()) {
			usage();
			return;
		}
		if (!project.config.get('d').changed()) {
			System.setOut(new PrintStream(new FilteredPrintStream(null)));
			System.setErr(new PrintStream(new FilteredPrintStream(null)));
		}
		project.start();
	}

	public static final Project project() {
		return project;
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

	private Server server;

	private Main() {
		super("ProtONE", 0.6, 1, Stage.BETA, 147.3); // 17.06
		config.add('d', "debug mode");
		config.add('h', "show this information");
		config.add("port", new Parameter<Integer>(9999, "connection port"));
		server = new Server((Integer) (config.get("port").isSet() ? config.get(
				"port").value() : config.get("port").def), this);
	}

	@Override
	public void onGetOrPost(Request req, Response res) {

	}

	@Override
	protected synchronized void start() {
		super.start();
		if (!server.isWork())
			server.start();
	}

	@Override
	protected synchronized void stop() {
		super.stop();
		if (server.isWork())
			server.stop();
	}

}
