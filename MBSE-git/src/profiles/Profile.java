package profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.TXT;

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
		File file = new File(ProfileManager.getBaseFolder().getAbsolutePath()
				+ "/" + name);
		System.out.println(file.getAbsolutePath());
		this.name = name;
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file.getAbsolutePath() + "/options.txt");
		
		if (!file.exists()) {
			options.add(new Option("name", name, this));
			options.add(new Option("xp", "0", this));
		} else {
			try {
				List<String> lines = TXT.readFromFile(file.getAbsolutePath());
				for (String l : lines) {
					String[] parts = l.split("=");
					options.add(new Option(parts[0], this));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public String getName() {
		return name;
	}

	public Option getOption(String name) {
		for (Option o : options) {
			if (name.equals(o.name)) {
				return o;
			}
		}
		return null;
	}

	public boolean addFunds(String channel, int amount) {
		Option o = getOption(channel);
		if (o == null) {
			o = new Option(channel, "0", this);
			options.add(o);

		}
		if (amount < 0 && Integer.parseInt(o.value) < (amount * -1))
			return false;
		getOption(channel).set((amount + Integer.parseInt(o.value)) + "");
		return true;

	}

	public int getFunds(String channel) {
		Option o = getOption(channel);
		if (o != null)
			Integer.parseInt(o.value);
		return 0;
	}

	public Command getCommand(String name) {
		for (Command c : commands) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}

	public boolean addCommand(String name, CommandType type, PermLevel pl,
			int c, String v) {
		System.out.println("PROFILE Adding command...");
		boolean exists = true;

		try {
			getCommand(name).getName();
		} catch (NullPointerException e) {
			System.out.println("NULL POINTER! NULL POINTER EVERYWHERE!");
			exists = false;
		}

		if (!exists) {
			commands.add(new Command(name, type, pl, c, v, this));
			return true;
		}
		return false;
	}

	public boolean delCommand(String name) {
		if (getCommand(name) != null) {
			for (Command c : commands) {
				if (c.name.equals(name)) {
					return commands.remove(c);
				}
			}
		}
		return false;
	}
}
