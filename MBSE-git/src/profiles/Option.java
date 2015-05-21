package profiles;

import java.io.File;
import java.io.IOException;
import java.util.List;

import utils.TXT;

public class Option {

	String name, value;
	Profile profile;
	File file;

	public Option(String n, String v, Profile p) {
		this(n, p);
		value = v;
		write();
	}

	public Option(String n, Profile p) {
		name = n;

		profile = p;
		file = new File(ProfileManager.getBaseFolder().getAbsolutePath() + "/"
				+ profile.getName() + "/options.txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		value = "";
		read();

		write();
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
					lines.add(parts[0] + "◘" + value);
					existed = true;
					break;
				}
			}
			if (existed)
				TXT.writeToFile(f, lines);
			else {
				lines.add(name + "◘" + value);
				TXT.writeToFile(f, lines);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String read() {
		try {
			List<String> lines = TXT.readFromFile(file.getAbsolutePath());
			for (String l : lines) {
				String[] parts = l.split("◘");
				if (parts[0].equals(name)) {
					value = parts[1];
					return value;

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		value = null;
		return null;
	}

	public String getValue() {
		return value;
	}

}
