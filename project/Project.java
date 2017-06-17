package project;

/**
 * 
 * @author spark1991z
 * 
 */
public class Project {

	protected Configurator config;
	public final String name;
	public final int revision;
	public final Stage stage;

	public final double ver, build;

	protected Project(String name, double ver, int revision, Stage stage,
			double build) {
		this.name = name;
		this.ver = ver;
		this.build = build;
		this.revision = revision;
		this.stage = stage;

		config = new Configurator();
	}

	public Configurator config() {
		return config;
	};

	protected void start() {
	};

	protected void stop() {
	}

	@Override
	public String toString() {
		return String.format("%s version %s.%s%s (build %s)", name, ver,
				revision, stage.display, build);
	}

}
