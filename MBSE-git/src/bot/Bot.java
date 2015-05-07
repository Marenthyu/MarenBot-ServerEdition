package bot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import profiles.Command;
import profiles.CommandType;
import profiles.PermLevel;
import profiles.Profile;
import profiles.ProfileManager;

public class Bot extends PircBot {

	private String channel;
	private Profile profile;

	public Bot(String Name, String Channel) throws Exception {
		profile = ProfileManager.getProfileByName(Channel.replace("#", ""));
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
							+ " imminent, but Still some messages to be sent out. Running around in circles...");
		}
		this.disconnect();
		this.dispose();

	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		System.out.println("Message in " + channel + " from " + sender + ": "
				+ message);

		String[] parts = message.split(" ");
		Command c;
		if ((c = getCommand(parts[0])) != null) {
			switch (c.getType()) {
			case CALLMETHOD:
				java.lang.reflect.Method method;
				try {
					method = this.getClass().getMethod(c.getValue(),
							String.class, String.class);
					String args = message.replace(parts[0] + " ", "");
					method.invoke(this, sender, args);
				} catch (NoSuchMethodException | SecurityException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case SAY:
				sendMessage(channel, c.getValue().replace("<sender>", sender));
				break;
			default:
				break;

			}
		}
	}

	public boolean addCommand(String name, String typ, String perl,
			String cost, String v) {
		CommandType type;
		PermLevel pl;
		int c;
		switch (typ) {
		case "SAY" : {
			type = CommandType.SAY;
			break;
		}
		
		case "CALLMETHOD" : {
			type = CommandType.CALLMETHOD;
			break;
		}
		
		default: {
			return false;
		}
		}
		
		switch (perl) {
		case "ALL" : {
			pl = PermLevel.ALL;
			break;
		}
		
		case "REGULAR" : {
			pl = PermLevel.REGULAR;
			break;
		}
		
		case "MOD" : {
			pl = PermLevel.MOD;
			break;
		}
		
		case "BROADCASTER" : {
			pl = PermLevel.BROADCASTER;
			break;
		}
		
		default: {
			return false;
		}
		}
		c = Integer.parseInt(cost);
		System.out.println("Adding command...");
		boolean ret = profile.addCommand(name, type, pl, c, v);
		return ret;

	}

	public Command getCommand(String name) {
		
		Command c;
		try {
		if ((c= profile.getCommand(name))!=null) {
			return c;
		} else return null; } catch (NullPointerException e) {
			System.out.println("Couldn't get command. NULL POINTER");
			return null;
		}
	}

}
