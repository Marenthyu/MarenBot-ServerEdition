package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PersonConnection implements Runnable {

	Socket socket = null;
	int id;
	int mode = 0;
	PrintWriter out;
	BufferedReader in;
	boolean connected = true;
		public PersonConnection(Socket s, int id) {
			socket = s;
			this.id=id;
			try {
				out = new PrintWriter(s.getOutputStream(),true);
				in = new BufferedReader( new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		@Override
		public void run() {
			out.println("Successfully started Person Connection! Welcome! Your id is "+id);
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
					System.out.println(id+" Has disconnected.");
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
			switch (line)
			{
			case "login": { if (mode == 0) { out.println("You connected!"); mode = 1; break; } }
			}
		}
	

}
