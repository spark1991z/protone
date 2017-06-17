package project;

/**
 * 
 * @author spark1991z
 * 
 * @param <T>
 */
public class Parameter<T> {

	public final T def;
	public final String display;
	private Object val;

	public Parameter(T def, String display) {
		this.def = def;
		this.display = display;
	}

	public T defaultValue() {
		return def;
	}

	public boolean isSet() {
		return val != null;
	}

	public boolean set(String s) {
		try {
			val = Integer.class.isInstance(def) ? Integer.valueOf(s).intValue()
					: Double.class.isInstance(def) ? Double.valueOf(s)
							.doubleValue()
							: Float.class.isInstance(def) ? Float.valueOf(s)
									.floatValue()
									: Long.class.isInstance(def) ? Long
											.valueOf(s).longValue() : s
											.startsWith("0x") ? Byte.decode(s)
											: s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val != null;
	}

	public Object value() {
		return val;
	}

}
