package profiles;

import java.util.ArrayList;

public class Profile {

	ArrayList<Option> options = new ArrayList<Option>();
	ArrayList<Command> commands = new ArrayList<Command>();
	String name;

	public ArrayList<Option> getOptions() {
		return options;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public Profile(String name) {
		this.name = name;
	}

}
