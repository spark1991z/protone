package project;

/**
 * 
 * @author spark1991z
 * 
 */
public class Flag {

	private boolean changed;
	public final String display;

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
