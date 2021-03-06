package connection;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionManager {
	
	static ServerSocket serverSocket = null;
	public static boolean isHandling = false;

	public static void start(int port) throws IOException {
		
		try { //create Server
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Couldn't create Server on Port "+port);
			throw e;
		}
		startHandlingConnections();
		
	}
	
	private static void startHandlingConnections() {
		isHandling = true;
		Runnable r = new ConnectionHandler(serverSocket);
		new Thread(r).start();
	}
	
}
