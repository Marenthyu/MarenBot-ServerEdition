package main;

import java.io.IOException;

import bot.InstanceManager;
import connection.ConnectionManager;

public class Starter {

	public static void main(String[] args) {
		System.out.println("MarenBot Server Edition v0.1alpha");
		System.out.println("Starting connection Handler on Port 1515...");
		try {
			ConnectionManager.start(1515);
			InstanceManager.initialize();
		} catch (IOException e) {
			System.err
					.println("Couldn't start ConnectionManager. Terminating.");
			System.exit(1);
		}

	}

}
