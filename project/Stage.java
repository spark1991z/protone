package project;
/**
 *  
 * @author spark1991z
 *
 */
public enum Stage {
	INITIAL(0, "i"), ALPHA(1, "a"), BETA(2, "b"), RELEASE(3, "r"), FINAL(4,
			"f");

	public final int id;
	public final String display;

	private Stage(int id, String display) {
		this.id = id;
		this.display = display;
	}

	public static Stage valueOf(int id) {
		for (Stage s : values()) {
			if (s.id == id)
				return s;
		}
		return null;
	}
}