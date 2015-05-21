package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ConnectionManager {

	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static int id = 0;

	public static void connect(String name) {
		try {
			socket = new Socket("marenthyu.de", 1515);
		} catch (IOException e) {
			System.err.println("Could not connect. Terminating.");
			JOptionPane.showMessageDialog(null,
					"FATAL: Could not connect to marenthyu.de",
					"Connection ERROR", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.err.println("Couldn't get output stream. Terminating.");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Couldn't get input stream. Terminating.");
			e.printStackTrace();
			System.exit(1);
		}

		System.out
				.println("Connection succesfully established! Identifying self as client...");
		out.println("client");
		String line = "";
		try {
			line = in.readLine();
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		id = Integer.parseInt(line.replace("client connection. id=", ""));
		try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.println(name);
		System.out.println("Successfully identified and registered.");

	}

	public static boolean status() {

		out.println("status");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while getting status. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "true": {
			return true;
		}

		case "false": {
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}

	}

	public static boolean join() {
		System.out.println("Joining....");
		out.println("join");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of joining. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "true": {
			System.out.println("Joined.");
			return true;
		}

		case "false": {
			System.out.println("couldnt join.");
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}

	}

	public static boolean leave() {
		out.println("leave");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of leaving. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "true": {
			return true;
		}

		case "false": {
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}
	}

	public static boolean betstatus() {
		out.println("betstatus");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of betstatus. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "true": {
			return true;
		}

		case "false": {
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}
	}

	public static boolean togglebets() {
		out.println("togglebets");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of togglebets. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "done": {
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}
	}

	public static String[] getOldestSongrequest() {
		out.println("getsongrequest");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of getsongrequest. Darnit.");
			e.printStackTrace();
			return null;
		}
		System.out.println("Line: " + line);
		String[] s = line.split("~#");
		for (int i = 0; i < s.length; i++) {
			System.out.println(i + ": " + s[i]);
		}
		return s;

	}

	public static boolean delOldestSongRequest() {
		out.println("delsongrequest");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of delsongrequest. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "done": {
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}
	}

	public static boolean wonGame() {
		out.println("wongame");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of wongame. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "done": {
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}

	}

	public static boolean lostGame() {
		out.println("lostgame");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of lostgame. Darnit.");
			e.printStackTrace();
			return false;
		}
		switch (line) {
		case "done": {
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			return false;
		}
		}

	}

	public static boolean addCommand(String name, String typ, String pl, String cost,
			String value) {
		out.println("addcomm");
		try {
			if (in.readLine().equals("ready")) {
				out.println(name + "," + typ + "," + pl + "," + cost);
				switch (in.readLine()) {
				case "existed": {
					return false;
				}
				case "value": {
					out.println(value);
					if (in.readLine().equals("done")) {
						return true;
					} else {
						System.err
								.println("Somethingweird happened while trying to add command.");
						return false;
					}
				}
				default: {
					System.err
							.println("Somethingweird happened while trying to add command.");
					return false;
				}
				}
			} else {
				System.err
						.println("Somethingweird happened while trying to add command.");
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Somethingweird happened while trying to add command.");
			return false;
		}

	}

	public static boolean delcomm(String name) {
		out.println("delcomm");
		try {
			if (in.readLine().equals("ready")) {
				out.println(name);
				switch (in.readLine()) {

				case "true": {
					return true;

				}
				case "false": {
					return false;
				}

				default: {
					System.err
							.println("Something weird happened while trying to remove command");
					return false;
				}
				}
			} else {
				System.err
						.println("Something weird happened while trying to remove command");
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to remove command");
			return false;
		}
	}

}
