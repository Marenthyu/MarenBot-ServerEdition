package connection;

import java.net.Socket;

public class ClientConnection implements Runnable {

Socket socket = null;
int id;
	public ClientConnection(Socket s, int id) {
		socket = s;
		this.id=id;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	

}
