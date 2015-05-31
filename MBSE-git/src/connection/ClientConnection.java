package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;

import profiles.Profile;
import profiles.ProfileManager;
import bot.Bot;
import bot.InstanceManager;

public class ClientConnection implements Runnable {

	Socket socket = null;
	int id;
	int mode = 0;
	PrintWriter out;
	BufferedReader in;
	boolean connected = true;
	String name = "UNKNOWN";
	String newcommname = "", newcommpl = "", newcommcost = "", newcommv = "",
			newcommtype = "";
	Bot bot;
	Profile profile;

	public ClientConnection(Socket s, int id) {
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
		out.println("client connection. id=" + id);
		out.println("name");
		try {
			name = in.readLine();
			bot = InstanceManager.getInstanceByChannel(name);
			profile = ProfileManager.getProfileByName(name);
			try {
				bot.clientconnected = true;
			} catch (Exception e) {

			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			System.out.println(id + " Has disconnected.");
			try {
				this.finalize();
			} catch (Throwable e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		while (connected) {
			try {
				handleLine(in.readLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					socket.close();
					bot.clientDisconnected();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println(id + "," + name + " has disconnected.");
				
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

	private void handleLine(String line) {
		try {
			switch (mode) {
			case 0: {

				switch (line) {
				case "status": {
					if (InstanceManager.status(name)) {
						out.println("true");

					} else {
						out.println("false");
					}
					break;
				}

				case "join": {
					if (InstanceManager.newInstance(name)) {
						out.println("true");
						bot = InstanceManager.getInstanceByChannel(name);

						bot.clientConnected();

					} else {
						out.println("false");
						bot = InstanceManager.getInstanceByChannel(name);
						bot.clientDisconnected();
					}
					break;
				}

				case "leave": {
					if (InstanceManager.terminateInstanceByChannel(name)) {
						out.println("true");
						bot = null;
					} else {
						out.println("false");
						bot = null;
					}
					break;
				}

				case "betstatus": {
					if (bot.bets) {
						out.println("true");
					} else {
						out.println("false");
					}
					break;
				}
				case "togglebets": {
					bot.toggleBets("client", "");
					out.println("done");
					break;
				}
				case "getsongrequest": {
					String[] request = bot.getOldestSongrequest();
					out.println(request[0] + "~#" + request[1]);
					break;
				}
				case "delsongrequest": {
					bot.deleteOldestSongrequest();
					out.println("done");
					break;
				}
				case "wongame": {
					bot.resolveBets(true);
					out.println("done");
					break;
				}
				case "lostgame": {
					bot.resolveBets(false);
					out.println("done");
					break;
				}
				case "addcomm": {
					mode = 1;
					out.println("ready");
					break;
				}
				case "delcomm": {
					mode = 3;
					out.println("ready");
					break;
				}
				case "import": {
					mode = 4;
					out.println("ready");
				}
				case "ratings": {
					try {
						for (int i : bot.ratings) {
							out.println(i);
						}
						out.println("end");
						bot.ratings = new ArrayList<Integer>();
					} catch (Exception e) {
						out.println("end");
					}
					break;
				}
				case "newsong": {
					mode = 5;
					out.println("ready");
					break;
				}
				case "toggleusersongs" : {
					try {
						bot.toggleSongMode("CLIENT", "");
						if (bot.songMode()) {
							out.println("true");
							break;
						}
					} catch (Exception e) {
						
					}
					out.println("false");
					break;
				}
				case "usersongs" : {
					try {
						if (bot.songMode()) {
							out.println("true");
							break;
						}
					} catch (Exception e) {
						
					}
					out.println("false");
					break;
				}
				case "setuser" : {
					out.println("ready");
					mode = 6;
					break;
				}
				case "getrating" : {
					try {
						out.println(ProfileManager.getProfileByName(bot.currUser).calcAvgRating()+"");
					} catch (Exception e) {
						out.println("0");
					}
					break;
				}
				case "methods" : {
					Method[] methods = Bot.class.getDeclaredMethods();
					for (Method m:methods) {
						Class<?>[] parameters = m.getParameterTypes();
						try{
						if (parameters[0].equals(String.class)&&parameters[1].equals(String.class)) {
							if (m.getName().equals("onMessage")||m.getName().equals("onUserMode")||m.getName().equals("addCommand")) {
								continue;
							}
							out.println(m.getName());
						}} catch (Exception e) {
							continue;
						}
					}
					out.println("end");
					break;
				}
				}
				break;
			}
			case 1: {
				String[] parts = line.split(",");
				try {
					bot.getCommand(parts[0]).getCost();
					out.println("existed");
					mode = 0;
				} catch (Exception e) {
					newcommname = parts[0];
					newcommtype = parts[1];
					newcommpl = parts[2];
					newcommcost = parts[3];
					mode = 2;
					out.println("value");
				}

				break;

			}
			case 2: {
				newcommv = line;
				bot.addCommand(newcommname, newcommtype, newcommpl,
						newcommcost, newcommv);
				out.println("done");
				mode = 0;
				break;
			}
			case 3: {
				if (bot.removeCommand(line)) {
					out.println("true");
					mode = 0;
				} else {
					out.println("false");
					mode = 0;
				}
				break;
			}
			case 4: {
				switch (line) {
				case "end": {
					out.println("done");
					mode = 0;
					break;
				}

				default: {
					String[] s = line.split(",");
					try {
						ProfileManager.getProfileByName(s[0]).addFunds(
								"trampolinetales", Integer.parseInt(s[1]));
						out.println("OK");
					} catch (Exception e) {
						out.println("ERR");
						mode = 0;
					}
				}
				}
				break;
			}
			case 5: {
				try {
					bot.sendMessage(bot.getChannel(), "New song playing now: "
							+ line);
				} catch (Exception e) {
					System.out.println("Bot was not yet in " + name
							+ ", but a new song was received. How lovely.");
				}
				mode = 0;
				out.println("done");
				break;
			}
			case 6 : {
				try {
					bot.currUser = line;
					
				} catch (Exception e){
					
				}
				mode = 0;
				out.println("done");
				break;
			}
			
			}
		} catch (Exception e) {
			throw e;
		}
	}
}