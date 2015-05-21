package profiles;

import java.io.File;
import java.io.IOException;
import java.util.List;

import utils.TXT;

public class Command {

	String name;
	CommandType type;
	PermLevel permlevel;
	int cost;
	String value;
	Profile profile;
	File file;

	public Command(String n, CommandType t, PermLevel pl, int c, String v,
			Profile p) {
		this(n, p);
		value = v;
		type = t;
		permlevel = pl;
		cost = c;
		write();
	}

	public Command(String n, Profile p) {
		name = n;

		profile = p;
		file = new File(ProfileManager.getBaseFolder().getAbsolutePath() + "/"
				+ profile.getName() + "/commands.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		value = "";
		type = CommandType.SAY;
		permlevel = PermLevel.ALL;
		cost = 0;
		read();
		write();
	}

	public String getName() {
		return name;
	}

	public CommandType getType() {
		return type;
	}

	public PermLevel getPermlevel() {
		return permlevel;
	}

	public int getCost() {
		return cost;
	}

	public String getValue() {
		return value;
	}

	public File getFile() {
		return file;
	}

	public void set(String newvalue) {
		value = newvalue;
		write();
	}

	private void write() {
		boolean existed = false;
		String f = file.getAbsolutePath();
		try {
			List<String> lines = TXT.readFromFile(f);
			for (String l : lines) {

				String[] parts = l.split("◘");
				if (parts[0].equals(name)) {

					lines.remove(l);
					lines.add(parts[0] + "◘" + type.toString() + "◘"
							+ permlevel.toString() + "◘" + cost + "◘" + value);
					existed = true;
					break;
				}
			}
			if (existed)
				TXT.writeToFile(f, lines);
			else
			{
				lines.add(name + "◘" + type.toString() + "◘"
						+ permlevel.toString() + "◘" + cost + "◘" + value);
				TXT.writeToFile(f, lines);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] read() {
		String[] ret = new String[5];
		try {
			List<String> lines = TXT.readFromFile(file.getAbsolutePath());
			for (String l : lines) {
				String[] parts = l.split("◘");
				if (parts[0].equals(name)) {
					switch (parts[1]) {
					case "SAY": {
						type = CommandType.SAY;
						break;
					}
					case "CALLMETHOD": {
						type = CommandType.CALLMETHOD;
						break;
					}
					}
					switch (parts[2]) {
					case "ALL": {
						permlevel = PermLevel.ALL;
						break;
					}
					case "REGULAR": {
						permlevel = PermLevel.REGULAR;
						break;
					}
					case "MOD": {
						permlevel = PermLevel.MOD;
						break;
					}
					case "SUBSCRIBER": {
						permlevel = PermLevel.SUBSCRIBER;
						break;
					}
					case "BROADCASTER": {
						permlevel = PermLevel.BROADCASTER;
						break;
					}
					}
					cost = Integer.parseInt(parts[3]);
					value = parts[4];

					ret[0] = parts[0];
					ret[1] = parts[1];
					ret[2] = parts[2];
					ret[3] = parts[3];
					ret[4] = parts[4];
					return ret;

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		value = null;
		return null;
	}

}
