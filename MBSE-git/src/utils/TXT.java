package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class TXT {

	static Charset charset = StandardCharsets.UTF_8;

	public static List<String> readFromFile(String path) throws IOException {
		// System.out.println("Reading from file "+path);
		File file = new File(path);
		List<String> stuff = Files.readAllLines(file.toPath().toAbsolutePath(),
				charset);
		// System.out.println("Done reading. read: "+stuff);
		return stuff;
	}

	public static void writeToFile(String path, String stuff)
			throws IOException {
		// System.out.println("Writing "+stuff+" to file "+path);
		File file = new File(path);
		try {
			Files.write(file.toPath().toAbsolutePath(), stuff.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while writing File");
			throw e;
		}
		// System.out.println("Done writing");
	}

	public static void writeToFile(String path, List<String> stuff)
			throws IOException {

		// System.out.println("Writing to file "+path);

		String out = "";

		for (String s : stuff) {
			out += s + System.lineSeparator();
		}

		File file = new File(path);

		try {
			Files.write(file.toPath().toAbsolutePath(), out.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while writing File");
			throw e;
		}
		// System.out.println("Done writing.");
	}

}
