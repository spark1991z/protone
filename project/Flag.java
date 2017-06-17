package project;

/**
 * 
 * @author spark1991z
 * 
 */
public class Flag {

	public final String display;
	private boolean changed;

	public Flag(String display) {
		this.display = display;
	}

	public void change() {
		if (!changed)
			changed = true;
	}

	public boolean changed() {
		return changed;
	}

}
