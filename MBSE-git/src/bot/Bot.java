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
		sendMessage(channel, "Terminating Connection to this channel");
		this.disconnect();
		this.dispose();
	}

}
