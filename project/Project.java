package project;
/**
 * 
 * @author spark1991z
 * 
 */
public class Project {

	public final String name;
	public final double ver, build;
	public final int revision;
	public final Stage stage;
	
	protected Configurator config;
	 
	protected Project(String name, double ver, 
			int revision,Stage stage,  double build) {
		this.name = name;
		this.ver = ver;
		this.build = build;
		this.revision = revision;
		this.stage = stage;
		
		config = new Configurator();
	}

	protected void start() {
	};

	protected void stop() {
	};
	
	public Configurator config(){
		return config;
	}

	@Override
	public String toString() {
		return String.format("%s v%s.%s%s (build %s)", name, ver, revision, stage.display,
				 build);
	}
}
