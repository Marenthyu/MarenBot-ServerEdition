package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bot.InstanceManager;

public class PersonConnection implements Runnable {

	Socket socket = null;
	int id;
	int mode = 0;
	PrintWriter out;
	BufferedReader in;
	boolean connected = true;

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

	private void handleLine(String line) {
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

		default: {
			if (mode == 1) {
				if (InstanceManager.newInstance(line)) {
					out.println("Succesfully joined " + line);
					mode = 0;
					out.println("Awaiting Input...");
				} else {
					out.println("Couldn't join " + line);
					mode = 0;
					out.println("Awaiting Input...");
				}
			}
			if (mode == 2) {
				if (InstanceManager.terminateInstanceByChannel(line)) {
					out.println("Succesfully left " + line);
					mode = 0;
					out.println("Awaiting Input...");
				} else {
					out.println("Couldn't leave " + line);
					mode = 0;
					out.println("Awaiting Input...");
				}
			}
		}
		}
	}

}
