package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import bot.Bot;
import bot.InstanceManager;

public class PersonConnection implements Runnable {

	Socket socket = null;
	int id;
	int mode = 0;
	PrintWriter out;
	BufferedReader in;
	boolean connected = true;
	Bot selectedBot;
	String[] newCommand = new String[4];

	public PersonConnection(Socket s, int id) {
		socket = s;
		this.id = id;
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		out.println("Successfully started Person Connection! Welcome! Your id is "
				+ id);
		out.println("Awaiting input.");
		while (connected) {
			try {
				handleLine(in.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(id + " Has disconnected.");
				try {
					this.finalize();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
		}
	}

	private void handleLine(String line) throws IOException {
		if (line == null)
			throw new IOException();
		switch (line) {
		case "join": {
			if (mode == 0) {
				out.println("Enter the Channel to join!");
				mode = 1;
			}
			break;
		}

		case "leave": {
			if (mode == 0) {
				out.println("Enter the Channel to leave!");
				mode = 2;
			}
			break;
		}

		case "edit": {
			if (mode == 0) {
				out.println("Enter the instance to edit!");
				mode = 3;
			}
			break;
		}
		case "add": {
			if (mode == 4) {
				out.println("Enter the command to add!");
				mode = 5;
			}
			break;
		}
		case "remove": {
			if (mode == 4) {
				out.println("Enter the command to remove!");
				mode = 10;
			}
			break;
		}

		default: {
			switch (mode) {
			case 1: {
				if (InstanceManager.newInstance(line)) {
					out.println("Succesfully joined " + line);
					mode = 0;
					out.println("Awaiting Input...");

				} else {
					out.println("Couldn't join " + line);
					mode = 0;
					out.println("Awaiting Input...");
				}
				break;
			}
			case 2: {
				if (InstanceManager.terminateInstanceByChannel(line)) {
					out.println("Succesfully left " + line);
					mode = 0;
					out.println("Awaiting Input...");
				} else {
					out.println("Couldn't leave " + line);
					mode = 0;
					out.println("Awaiting Input...");
				}
				break;
			}

			case 3: {
				if ((selectedBot = InstanceManager.getInstanceByChannel(line)) != null) {
					out.println("add or remove command?");
					mode = 4;
				} else {
					out.println("No instance in Channel " + line
							+ ". Aborting.");
					mode = 0;
				}

				break;
			}

			case 5: {
				boolean exists = true;

				try {
					selectedBot.getCommand(line).getName();
				} catch (NullPointerException e) {
					exists = false;
				}

				if (!exists) {
					newCommand[0] = line;
					out.println("Success. CommandType? [SAY/CALLMETHOD]");
					mode = 6;
				} else {
					out.println("Command " + line
							+ " already exists. Aborting.");
					mode = 0;
				}
				break;
			}
			case 6: {
				switch (line) {
				case "SAY": {
					newCommand[1] = "SAY";
					out.println("Success. PermLevel? [ALL/REGULAR/SUBSCRIBER/MOD/BROADCASTER]");
					mode = 7;
					break;
				}
				case "CALLMETHOD": {
					newCommand[1] = "CALLMETHOD";
					out.println("Success. PermLevel? [ALL/REGULAR/SUBSCRIBER/MOD/BROADCASTER]");
					mode = 7;
					break;
				}
				default: {
					out.println("Invalid type. Aborting.");
					mode = 0;
				}
				}
				break;
			}
			case 7: {
				switch (line) {
				case "ALL": {
					newCommand[2] = "ALL";
					out.println("Success. Cost?");
					mode = 8;
					break;
				}
				case "REGULAR": {
					newCommand[2] = "REGULAR";
					out.println("Success. Cost?");
					mode = 8;
					break;
				}
				case "SUBSCRIBER": {
					newCommand[2] = "SUBSCRIBER";
					out.println("Success. Cost?");
					mode = 8;
					break;
				}
				case "MOD": {
					newCommand[2] = "MOD";
					out.println("Success. Cost?");
					mode = 8;
					break;
				}
				case "BROADCASTER": {
					newCommand[2] = "BROADCASTER";
					out.println("Success. Cost?");
					mode = 8;
					break;
				}
				default: {
					out.println("Invalid PermLevel. Aborting.");
					mode = 0;
				}

				}

				break;
			}
			case 8: {
				try {
					Integer.parseInt(line);
					newCommand[3] = line;
					out.println("Success. Last but not least: Value?");
					mode = 9;
				} catch (Exception e) {
					out.println("Not a number. Aborting.");
					mode = 0;
				}
				break;
			}

			case 9: {
				if (selectedBot.addCommand(newCommand[0], newCommand[1],
						newCommand[2], newCommand[3], line)) {
					out.println("Success! Command was successfully added to "
							+ newCommand[0] + "'s Commandlist.");
					mode = 0;
				} else {
					out.println("Could not Add command. Aborting.");
					mode = 0;
				}
				break;
			}
			case 10: {
				if (selectedBot.removeCommand(line)) {
					mode = 0;
					out.println("Command removed");
				} else {
					mode = 0;
					out.println("Could not remove command");
				}
			}
			}
		}
		}
	}

}
