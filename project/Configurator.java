package project;

import java.util.Hashtable;
import java.util.Set;

/**
 * 
 * @author spark1991z
 * 
 */
public class Configurator {

	public static final int OK = 0, UNKNOWN_ARGUMENT = 1, UNKNOWN_FLAG = 2,
			ALREADY_CHANGED = 3, SYNTAX_ERROR = 4, UNKNOWN_PARAMETER = 5;

	private Hashtable<Character, Flag> flags = new Hashtable<Character, Flag>();
	private Hashtable<String, Parameter<?>> params;

	public Configurator() {
		params = new Hashtable<String, Parameter<?>>();
		flags = new Hashtable<Character, Flag>();
	}

	public void add(char c, String display) {
		if (!flags.containsKey(c))
			flags.put(c, new Flag(display));
	}

	public void add(String key, Parameter<?> p) {
		if (key != null && p != null && !params.containsKey(key))
			params.put(key, p);
	}

	public Set<Character> flags() {
		return flags.keySet();
	}

	public Flag get(char c) {
		return flags.containsKey(c) ? flags.get(c) : null;
	}

	public Parameter<?> get(String key) {
		return key != null && params.containsKey(key) ? params.get(key) : null;
	}

	public int maxLabelLength() {
		int len = 0;
		for (String s : params()) {
			len = s.length() > len ? s.length() : len;
		}
		return 2 + len;
	}

	public Set<String> params() {
		return params.keySet();
	}

	public int pushArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			int index = args[i].lastIndexOf("-");
			switch (index) {
			case 0:
			case 1:
				String s = args[i].substring(index + 1);
				if (index == 0) {
					for (char c : s.toCharArray()) {
						if (!flags.containsKey(c))
							return UNKNOWN_FLAG;
						Flag f = flags.get(c);
						if (f.changed())
							return ALREADY_CHANGED;
						f.change();
						flags.put(c, f);
					}
					break;
				}
				if (args.length == i + 1)
					return SYNTAX_ERROR;
				if (!params.containsKey(s))
					return UNKNOWN_PARAMETER;
				Parameter<?> p = params.get(s);
				if (p.isSet())
					return ALREADY_CHANGED;
				if (!p.set(args[i + 1]))
					return SYNTAX_ERROR;
				params.put(s, p);
				i += 1;
				break;
			default:
				return UNKNOWN_ARGUMENT;
			}
		}
		return OK;
	}

}
