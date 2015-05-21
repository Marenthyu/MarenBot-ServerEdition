package profiles;

public enum PermLevel {

	ALL("ALL"), REGULAR("REGULAR"), SUBSCRIBER("SUBSCRIBER"), MOD("MOD"), BROADCASTER(
			"BROADCASTER");

	private final String type;

	PermLevel(String t) {
		type = t;
	}

	public String toString() {
		return type;
	}

}
