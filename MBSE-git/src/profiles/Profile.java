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
			options.add(new Option("srratings", "0", this));
			options.add(new Option("avgrating", "0", this));
		} else {
			try {
				List<String> lines = TXT.readFromFile(file.getAbsolutePath());
				for (String l : lines) {
					String[] parts = l.split("◘");
					options.add(new Option(parts[0], this));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file = new File(ProfileManager.getBaseFolder().getAbsolutePath()
				+ "/" + name);
		file = new File(file.getAbsolutePath() + "/commands.txt");

		if (!file.exists()) {

		} else {
			try {
				List<String> lines = TXT.readFromFile(file.getAbsolutePath());
				for (String l : lines) {
					String[] parts = l.split("◘");
					commands.add(new Command(parts[0], this));
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
		return new Option("INVALID", "INVALID", this);
	}

	public void addOption(String name, String value) {
		options.add(new Option(name, value, this));
	}

	public boolean addFunds(String channel, int amount) {
		Option o = getOption(channel);
		if (o.getValue().equals("INVALID")) {
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
		
		if (o != null) {
			if (o.getValue().equals("INVALID")) {
				addOption(channel, "0");
				o = getOption(channel);
			}
			return Integer.parseInt(o.getValue());
			}
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
	
	public void songRating(int rating) {
		Option o = getOption("srratings");
		o.set(o.getValue()+"#"+rating);
	}
	public double calcAvgRating() {
		String s = getOption("srratings").getValue();
		String[] parts = s.split("#");
		int sum = 0;
		for (String part:parts) {
			sum+=Integer.parseInt(part);
		}
		double avg = 0;
		if (parts.length>0) {
			avg = (double) sum/(double) parts.length;
		}
		getOption("avgrating").set(avg+"");
		return avg;
	}
}
