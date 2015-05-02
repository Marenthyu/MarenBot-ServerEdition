package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TXT {

	public String readFromFile(String path) {
		
	}
	
	public void writeToFile(String path, String stuff) throws IOException {
		File file = new File(path);
		try {
			Files.write(file.toPath().toAbsolutePath(), stuff.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while writing File");
			throw e;
		}
	}
	
}
