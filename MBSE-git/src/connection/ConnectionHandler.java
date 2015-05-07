package connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
	ServerSocket socket = null;
	int id = 0;
	public ConnectionHandler(ServerSocket s) {
		socket = s;
	}
	
	@Override
	public void run() {
		while (ConnectionManager.isHandling) {
			System.out.println("Waiting for connection...");
			Socket s=null;
			try {
				s = socket.accept();
				id++;
				System.out.println(id+" - Incoming Connection. Waiting for identification...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				System.out.println("Sending identification request...");
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				out.println("Please identify.");
	
				System.out.println("sent. awaiting response...");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String line = "";
			try {
				line = new BufferedReader(
				        new InputStreamReader(s.getInputStream())).readLine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			switch (line) {
			
			case "person": {
				System.out.println(id+" Identificated as person. Starting handler.");
				Runnable r = new PersonConnection(s,id);
				new Thread(r).start();
				break;
			}
			case "client": {
				System.out.println(id+" Identificated as client. Starting handler.");
				Runnable r = new ClientConnection(s,id);
				new Thread(r).start();
				break;
			}
			default : {
				System.out.println(id+" couldn't be identified. Terminating.");
				try {
					new PrintWriter(s.getOutputStream()).println("Invalid identification.");
					s.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			}
		}

	}

}
