package project;

/**
 * 
 * @author spark1991z
 * 
 */
public enum Stage {
	ALPHA(1, "a"), BETA(2, "b"), FINAL(4, "f"), INITIAL(0, "i"), RELEASE(3, "r");

	public static Stage valueOf(int id) {
		for (Stage s : values()) {
			if (s.id == id)
				return s;
		}
		return null;
	}
	public final String display;

	public final int id;

	private Stage(int id, String display) {
		this.id = id;
		this.display = display;
	}

}