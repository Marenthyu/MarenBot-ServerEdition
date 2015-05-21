package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
					} else {
						out.println("false");
					}
					break;
				}

				case "leave": {
					if (InstanceManager.terminateInstanceByChannel(name)) {
						out.println("true");
					} else {
						out.println("false");
					}
					break;
				}

				case "betstatus": {
					if (InstanceManager.getInstanceByChannel(name).bets) {
						out.println("true");
					} else {
						out.println("false");
					}
					break;
				}
				case "togglebets": {
					InstanceManager.getInstanceByChannel(name).toggleBets(
							"client", "");
					out.println("done");
					break;
				}
				case "getsongrequest": {
					String[] request = InstanceManager.getInstanceByChannel(
							name).getOldestSongrequest();
					out.println(request[0] + "~#" + request[1]);
					break;
				}
				case "delsongrequest": {
					InstanceManager.getInstanceByChannel(name)
							.deleteOldestSongrequest();
					out.println("done");
					break;
				}
				case "wongame": {
					InstanceManager.getInstanceByChannel(name)
							.resolveBets(true);
					out.println("done");
					break;
				}
				case "lostgame": {
					InstanceManager.getInstanceByChannel(name).resolveBets(
							false);
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
				}
				break;
			}
			case 1: {
				String[] parts = line.split(",");
				try {
					InstanceManager.getInstanceByChannel(name)
							.getCommand(parts[0]).getCost();
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
				InstanceManager.getInstanceByChannel(name).addCommand(
						newcommname, newcommtype, newcommpl, newcommcost,
						newcommv);
				out.println("done");
				mode = 0;
				break;
			}
			case 3: {
				if (InstanceManager.getInstanceByChannel(name).removeCommand(
						line)) {
					out.println("true");
					mode = 0;
				} else {
					out.println("false");
					mode = 0;
				}
			}
			}
		} catch (Exception e) {
			throw e;
		}
	}
}