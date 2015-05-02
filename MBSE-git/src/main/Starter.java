package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import connection.ConnectionManager;

public class Starter {

	public static void main(String[] args) {
		System.out.println("MarenBot Server Edition v0.1alpha");
		System.out.println("Starting connection Handler on Port 1515...");
		try {
			ConnectionManager.start(1515);
		} catch (IOException e) {
			System.err.println("Couldn't start ConnectionManager. Terminating.");
			System.exit(1);
		}
		

	}

}
