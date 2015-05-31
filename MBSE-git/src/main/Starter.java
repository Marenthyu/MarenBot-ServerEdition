package main;

import java.io.IOException;

import profiles.ProfileManager;
import bot.InstanceManager;
import connection.ConnectionManager;

public class Starter {

	public static void main(String[] args) {
		System.out.println("MarenBot Server Edition v0.1");
		System.out.println("Starting connection Handler on Port 1515...");
		try {

			InstanceManager.initialize();
			ProfileManager.intialize();
			ConnectionManager.start(1515);
			System.out.println("Startup done!");
		} catch (IOException e) {
			System.err
					.println("Couldn't start ConnectionManager. Terminating.");
			System.exit(1);
		}

	}

}
