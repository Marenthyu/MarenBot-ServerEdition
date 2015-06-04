package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ConnectionManager {

	static Socket socket = null;
	static PrintWriter out = null;
	static BufferedReader in = null;
	static int id = 0;
	static boolean busy = false;

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
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("status");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while getting status. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "true": {
			busy = false;
			return true;
		}

		case "false": {
			busy = false;
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}

	}

	public static boolean join() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		System.out.println("Joining....");
		out.println("join");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of joining. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "true": {
			System.out.println("Joined.");
			busy = false;
			return true;
		}

		case "false": {
			System.out.println("couldnt join.");
			busy = false;
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}

	}

	public static boolean leave() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("leave");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of leaving. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "true": {
			busy = false;
			return true;
		}

		case "false": {
			busy = false;
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}
	}

	public static boolean betstatus() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("betstatus");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of betstatus. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "true": {
			busy = false;
			return true;
		}

		case "false": {
			busy = false;
			return false;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}
	}

	public static boolean togglebets() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("togglebets");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of togglebets. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "done": {
			busy = false;
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}
	}

	public static String[] getOldestSongrequest() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("getsongrequest");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of getsongrequest. Darnit.");
			e.printStackTrace();
			busy = false;
			return null;
		}
		System.out.println("Line: " + line);
		String[] s = line.split("~#");
		for (int i = 0; i < s.length; i++) {
			System.out.println(i + ": " + s[i]);
		}
		busy = false;
		return s;

	}

	public static boolean delOldestSongRequest() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("delsongrequest");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of delsongrequest. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "done": {
			busy = false;
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}
	}

	public static boolean wonGame() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("wongame");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of wongame. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "done": {
			busy = false;
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}

	}

	public static boolean lostGame() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("lostgame");
		String line = "";
		try {
			line = in.readLine();
		} catch (IOException e) {
			System.err
					.println("Some kind of error while waiting for response of lostgame. Darnit.");
			e.printStackTrace();
			busy = false;
			return false;
		}
		switch (line) {
		case "done": {
			busy = false;
			return true;
		}
		default: {
			System.err.println("Impossible answer from server: " + line);
			busy = false;
			return false;
		}
		}

	}

	public static boolean addCommand(String name, String typ, String pl,
			String cost, String value) {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("addcomm");
		try {
			if (in.readLine().equals("ready")) {
				out.println(name + "," + typ + "," + pl + "," + cost);
				switch (in.readLine()) {
				case "existed": {
					busy = false;
					return false;
				}
				case "value": {
					out.println(value);
					if (in.readLine().equals("done")) {
						busy = false;
						return true;
					} else {
						System.err
								.println("Somethingweird happened while trying to add command.");
						busy = false;
						return false;
					}
				}
				default: {
					System.err
							.println("Somethingweird happened while trying to add command.");
					busy = false;
					return false;
				}
				}
			} else {
				System.err
						.println("Somethingweird happened while trying to add command.");
				busy = false;
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Somethingweird happened while trying to add command.");
			busy = false;
			return false;
		}

	}

	public static boolean delcomm(String name) {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("delcomm");
		try {
			if (in.readLine().equals("ready")) {
				out.println(name);
				switch (in.readLine()) {

				case "true": {
					busy = false;
					return true;

				}
				case "false": {
					busy = false;
					return false;
				}

				default: {
					System.err
							.println("Something weird happened while trying to remove command");
					busy = false;
					return false;
				}
				}
			} else {
				System.err
						.println("Something weird happened while trying to remove command");
				busy = false;
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to remove command");
			busy = false;
			return false;
		}
	}

	public static ArrayList<Integer> getRatings() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		ArrayList<Integer> ret = new ArrayList<Integer>();
		out.println("ratings");
		try {
			String s;
			while (!(s = in.readLine()).equals("end")) {
				ret.add(Integer.parseInt(s));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to get ratings");
			busy = false;
			return null;
		}
		busy = false;
		return ret;
	}

	public static boolean newSong(String songname) {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("newsong");

		try {
			if (in.readLine().equals("ready")) {
				out.println(songname);
				if (in.readLine().equals("done")) {
					busy = false;
					return true;
				} else {
					busy = false;
					return false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			busy = false;
			return false;
		}
		busy = false;
		return false;

	}

	public static boolean songMode() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("usersongs");

		try {
			switch (in.readLine()) {

			case "true": {
				busy = false;
				return true;

			}
			case "false": {
				busy = false;
				return false;
			}

			default: {
				System.err
						.println("Something weird happened while trying to get songMode");
				busy = false;
				return false;
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to get songMode");
			busy = false;
			return false;
		}

	}

	public static boolean toggleSongMode() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("toggleusersongs");
		try {
			switch (in.readLine()) {

			case "true": {
				busy = false;
				return true;

			}
			case "false": {
				busy = false;
				return false;
			}

			default: {
				System.err
						.println("Something weird happened while trying to get songMode");
				busy = false;
				return false;
			}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to get songMode");
			busy = false;
			return false;
		}

	}

	public static void setUser(String user) {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("setuser");

		try {
			if (in.readLine().equals("ready")) {
				out.println(user);
				if (in.readLine().equals("done")) {
					busy = false;
					return;
				} else {
					System.err
							.println("Something weird happened while trying to set user");
					busy = false;
					return;
				}
			} else {
				System.err
						.println("Something weird happened while trying to set user");
				busy = false;
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err
					.println("Something weird happened while trying to set user");
			busy = false;
			return;
		}
	}

	public static int getRating() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		out.println("getrating");
		try {
			int i = Integer.parseInt(in.readLine());
			busy = false;
			return i;
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			busy = false;
			return 0;
		}
	}
	
	public static ArrayList<String> methods() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		out.println("methods");
		String line;
		try {
			while (!(line = in.readLine()).equals("end")) {
				ret.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while grabbing methods from Server. odd.");
			JOptionPane.showMessageDialog(null, "ERROR while grabbing Methods from Server. Please try to update your client. If you are on the newest version, please contact Marenthyu.");
			System.exit(1);
		}
		
		busy = false;
		return ret;
	}
	
	public static ArrayList<String> commands() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		out.println("commands");
		String line;
		try {
			while (!(line = in.readLine()).equals("end")) {
				ret.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while grabbing commands from Server. odd.");
			JOptionPane.showMessageDialog(null, "ERROR while grabbing Commands from Server. Please try to update your client. If you are on the newest version, please contact Marenthyu.");
			System.exit(1);
		}
		
		busy = false;
		return ret;
		
		
	}
	public static ArrayList<String> messages() {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		out.println("messages");
		String line;
		try {
			while (!(line = in.readLine()).equals("end")) {
				ret.add(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Error while grabbing messages from Server. odd.");
			JOptionPane.showMessageDialog(null, "ERROR while grabbing messages from Server. Please try to update your client. If you are on the newest version, please contact Marenthyu.");
			System.exit(1);
		}
		
		busy = false;
		return ret;
		
		
	}
	
	public static boolean editOption(String name, String value) {
		while (busy) {
			System.out.println("[Connection Manager] HEY! I'm busy!");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		busy = true;
		boolean ret = false;
		
		
		out.println("editoption");
		try {
			if ((in.readLine()).equals("ready")) {
				out.println(name+"~#"+value);
				switch (in.readLine()) {
				case "good" : {
					ret = true;
					break;
				}
				
				case "bad" : {
					ret = false;
					break;
				}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ret = false;
		}
		
		
		
		busy = false;
		return ret;
	}
	
}
