package bot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

public class Bot extends PircBot {

	private String channel;

	public Bot(String Name, String Channel) throws Exception {
		this.setName(Name);
		this.setLogin(Name);
		channel = Channel;
		try {
			connect("irc.twitch.tv", 6667,
					"oauth:ulvex0dv2jk7nz38fd9l6bcqofw3ds");
			joinChannel(channel);
			System.out.println("Joined " + channel);
		} catch (IOException | IrcException e) {
			System.err.println("Failed to connect to channel " + channel);
			throw e;

		}
		sendMessage(
				channel,
				"MarenBot (ServerEdition) has joined this Channel but is not yet ready to rock.");
	}

	public String getChannel() {
		return channel;
	}

	public void terminate() {
		// TODO Message doesn't get fired
		sendMessage(channel, "Terminating Connection to this channel");
		while (this.getOutgoingQueueSize() > 0) {
			System.out
					.println("Termination of "
							+ channel
							+ " imminent, but Still some messages to be sent out. Waiting...");
		}
		this.disconnect();
		this.dispose();

	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		System.out.println("Message in " + channel + " from " + sender + ": "
				+ message);
	}

}
