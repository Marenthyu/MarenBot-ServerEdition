package profiles;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

/**
 * Class used to manage all available {@link Profile}s which include their
 * {@link Option}s and {@link Command}s. Call {@link initialize()} before using
 * any other functions.
 * 
 * @author Peter "Marenthyu" Fredebold
 *
 */
public class ProfileManager {

	static File basefolder = new File("profiles");
	static ArrayList<Profile> profiles = new ArrayList<Profile>();

	/**
	 * Initializes Profile Manager for use.
	 */
	public static void intialize() {
		if (!basefolder.exists()) {
			basefolder.mkdir();
		}

		String[] directories = basefolder.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		for (String name : directories) {
			profiles.add(new Profile(name));
		}
	}

	public static File getBaseFolder() {
		return basefolder;
	}

	public static Profile getProfileByName(String name) {
		for (Profile p : profiles) {
			if (p.name.equals(name)) {
				return p;
			}
		}
		Profile p = new Profile(name);
		profiles.add(p);
		return p;
	}

}
