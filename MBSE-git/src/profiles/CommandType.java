package profiles;

public enum CommandType {
	SAY("SAY"), CALLMETHOD("CALLMETHOD");
	
	private final String type;
	
	CommandType(String t) {
		type = t;
	}
	
	public String toString() {
		return type;
	}
	
}
