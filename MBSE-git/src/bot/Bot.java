package bot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

import profiles.Command;
import profiles.CommandType;
import profiles.Option;
import profiles.PermLevel;
import profiles.Profile;
import profiles.ProfileManager;

public class Bot extends PircBot {

	private String channel;
	private Profile profile;
	private ArrayList<String[]> songrequests, betsmade;
	private boolean bets;

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
		bets = false;
		betsmade = new ArrayList<String[]>();
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

			switch (c.getPermlevel()) {
			case ALL:
				break;
			case BROADCASTER: {
				if (!sender.equals(channel.replace("#", ""))) {
					sendMessage(channel, "Sorry, " + sender
							+ ", but this command is only for the Broadcaster.");
					return;
				}
				break;
			}
			case MOD: {
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(channel, "Sorry, " + sender
							+ ", but this command is only for mods.");
					return;
				}
				break;
			}
			case REGULAR:
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(
							channel,
							"Sorry, "
									+ sender
									+ ", but this command is only for mods. (REGULAR support to be added SOON™");
					return;
				}
				break;
			case SUBSCRIBER:
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(
							channel,
							"Sorry, "
									+ sender
									+ ", but this command is only for mods. (SUBSCRIBER support to be added SOON™");
					return;
				}
				break;
			default:
				break;

			}
			Profile p = ProfileManager.getProfileByName(sender);
			Option o = p.getOption(channel.replace("#", ""));
			if (o.getValue().equals("INVALID")) {
				p.addOption(channel.replace("#", ""), "0");
				o = p.getOption(channel.replace("#", ""));
			}

			if (c.getCost() > Integer.parseInt(o.getValue())) {
				sendMessage(
						channel,
						"Sorry, "
								+ sender
								+ ", but it appears that you don't have enough currency in thic channel to execute this command.");
				return;
			}
			o.set((Integer.parseInt(o.getValue()) - c.getCost()) + "");

			switch (c.getType()) {
			case CALLMETHOD:
				java.lang.reflect.Method method;
				try {
					method = this.getClass().getMethod(c.getValue(),
							String.class, String.class);
					String args = message.replace(parts[0] + " ", "");
					method.invoke(this, sender, args);
				} catch (NoSuchMethodException | SecurityException
						| IllegalAccessException | IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (InvocationTargetException e) {
					System.err.println("INVOKED METHOD HAS CAUSED AN ERROR!");
					sendMessage(channel,
							"Uh oh, something went wrong. Please try again.");
					e.printStackTrace();
					e.getCause().printStackTrace();
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
		case "SAY": {
			type = CommandType.SAY;
			break;
		}

		case "CALLMETHOD": {
			type = CommandType.CALLMETHOD;
			break;
		}

		default: {
			return false;
		}
		}

		switch (perl) {
		case "ALL": {
			pl = PermLevel.ALL;
			break;
		}

		case "REGULAR": {
			pl = PermLevel.REGULAR;
			break;
		}

		case "MOD": {
			pl = PermLevel.MOD;
			break;
		}

		case "BROADCASTER": {
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
			if ((c = profile.getCommand(name)) != null) {
				return c;
			} else
				return null;
		} catch (NullPointerException e) {
			System.out.println("Couldn't get command. NULL POINTER");
			return null;
		}
	}

	protected void onUserMode(String targetNick, String sourceNick,
			String sourceLogin, String sourceHostname, String mode) {

		// mode includes: #marenthyu +o marenthyu
		// System.out.println("currChan: "+currChan);
		mode = mode.replace(channel + " ", "");
		// System.out.println("After mode.replace: "+mode);
		String[] modeparts = mode.split(" ");
		// System.out.println("Modeparts[0]: "+modeparts[0]+", Modeparts[1]: "+modeparts[1]);
		Profile p = ProfileManager.getProfileByName(modeparts[1]);
		switch (modeparts[0]) {

		case "+o": {
			System.out.println("Modding " + modeparts[1]);
			if (p.getOption(channel + "mod").getValue().equals("INVALID")) {
				p.addOption(channel + "mod", "true");
			}
			p.getOption(channel + "mod").set("true");

			break;
		}
		case "-o": {
			System.out.println("Unmodding " + modeparts[1]);
			if (p.getOption(channel + "mod").getValue().equals("INVALID")) {
				p.addOption(channel + "mod", "false");
			}
			p.getOption(channel + "mod").set("false");

			break;
		}
		default: {
			break;
		}

		}

	}

	private boolean isMod(String name) {
		if (ProfileManager.getProfileByName(name).getOption(channel + "mod")
				.getValue().equals("true")) {
			return true;
		} else
			return false;

	}

	public void myBucks(String sender, String otherargs) {
		sendMessage(
				channel,
				sender
						+ "'s TrampBucks: T"
						+ ((float) ((float) (ProfileManager
								.getProfileByName(sender).getFunds(channel
								.replace("#", "")))) / 100));
	}

	public void songRequest(String sender, String otherargs) {
		songrequests.add(new String[] { sender, otherargs });
		sendMessage(
				channel,
				"Thanks for the Songrequest, "
						+ sender
						+ "! Dan will play it soon™ and if he likes it, add it to the stream Playlist!");
	}

	public String[] getOldestSongrequest() {
		return songrequests.get(songrequests.size() - 1);
	}

	public void deleteOldestSongrequest() {
		try {
			songrequests.remove(songrequests.size() - 1);
		} catch (Exception e) {
			System.err.println("No more songrequests in instance " + channel
					+ ", but a delete was requested.");
		}
	}

	public void testYourLuck(String sender, String otherargs) {
		int win = (int) (Math.random() * 201);
		ProfileManager.getProfileByName(sender).addFunds(
				channel.replace("#", ""), win);
		if (win >= 100) {
			sendMessage(channel, "Gratulations, " + sender
					+ ", you have tested your luck and won T"
					+ ((float) ((float) (win - 100)) / 100));
			return;
		}
		sendMessage(channel, "Sorry, " + sender
				+ ", you have tested your luck and lost T"
				+ ((float) ((float) (win - 100)) / 100) * -1);
	}

	public void toggleBets(String sender, String otherargs) {
		bets = !bets;
		if (bets) {
			sendMessage(channel,
					"Bets are now open! Use !bet win OR !bet lose to bet how this game will go!");
		} else {
			sendMessage(channel, "Bets are now closed! Good Luck!");
		}
	}

	public void placeBet(String sender, String otherargs) {
		String[] s = new String[2];
		s[0] = sender;
		s[1] = "win";
		String[] s2 = new String[2];
		s2[0] = sender;
		s2[1] = "lose";

		String previousbet = "";
		int index = -1;
		for (String[] pb : betsmade) {
			index++;
			if (pb[0].equals(sender)) {
				switch (pb[1]) {
				case "win": {
					previousbet = "win";
					break;
				}
				case "lose": {
					previousbet = "lose";
					break;
				}
				}
				break;
			}
		}

		if (!bets) {
			switch (previousbet) {
			case "win": {
				sendMessage(
						channel,
						"Sorry, "
								+ sender
								+ ", but the bets are currently closed! Please wait for Dan to open them again! You bet for winning this time.");
				break;
			}

			case "lose": {
				sendMessage(
						channel,
						"Sorry, "
								+ sender
								+ ", but the bets are currently closed! Please wait for Dan to open them again! You bet for losing this time.");
				break;
			}
			default: {
				sendMessage(
						channel,
						"Sorry, "
								+ sender
								+ ", but the bets are currently closed! Please wait for Dan to open them again! You did not bet this time.");
				break;
			}
			}
			return;
		}

		switch (otherargs) {
		case "win": {
			switch (previousbet) {
			case "win": {
				sendMessage(channel, sender
						+ ", you already bet for winning! BloodTrail");
				break;
			}

			case "lose": {
				betsmade.remove(index);
				betsmade.add(s);
				sendMessage(
						channel,
						sender
								+ ", you successfully changed your mind from losing to winning! BloodTrail");
				break;
			}

			default: {
				betsmade.add(s);
				sendMessage(channel, sender
						+ ", you successfully bet for winning! BloodTrail");
				break;
			}
			}
			break;
		}
		case "lose": {
			switch (previousbet) {
			case "lose": {
				sendMessage(channel, sender
						+ ", you already bet for losing! BibleThump");
				break;
			}

			case "win": {
				betsmade.remove(index);
				betsmade.add(s2);
				sendMessage(
						channel,
						sender
								+ ", you successfully changed your mind from winning to losing! BibleThump");
				break;
			}

			default: {
				betsmade.add(s2);
				sendMessage(channel, sender
						+ ", you successfully bet for losing! BibleThump");
				break;
			}
			}
			break;
		}
		default: {
			switch (previousbet) {
			case "lose": {
				sendMessage(channel, sender
						+ ", you are currently betting for losing! BibleThump");
				break;
			}

			case "win": {
				sendMessage(channel, sender
						+ ", you are currently betting for winning! BloodTrail");
				break;
			}

			default: {
				sendMessage(channel, sender + ", usage: !bet win OR !bet lose");
				break;
			}
			}
			break;
		}
		}

	}

	public void resolveBets(boolean winlose) {
		int correct = 0;

		for (String[] e : betsmade) {
			switch (e[1]) {
			case "win": {
				if (winlose) {
					ProfileManager.getProfileByName(e[0]).addFunds(
							channel.replace("#", ""), 50);
					correct++;
				} else {
					ProfileManager.getProfileByName(e[0]).addFunds(
							channel.replace("#", ""), 15);
				}
				break;
			}
			case "lose": {
				if (!winlose) {
					ProfileManager.getProfileByName(e[0]).addFunds(
							channel.replace("#", ""), 50);
					correct++;
				} else {
					ProfileManager.getProfileByName(e[0]).addFunds(
							channel.replace("#", ""), 15);
				}
				break;
			}
			default: {
				System.err.println("ERROR while resolving bets in instance "
						+ channel + " - User " + e[0] + " apparantly bet "
						+ e[1] + "!");
			}
			}
		}
		if (winlose) {
			sendMessage(
					channel,
					"The game is over, it is gloriously won! "
							+ correct
							+ " People bet correctly and got T0.50, the rest that bet got T0.15!");
		} else {
			sendMessage(
					channel,
					"The game is over, it is lost with a big bunch of shame! "
							+ correct
							+ " People bet correctly and got T0.50, the rest that bet got T0.15!");
		}
		betsmade = new ArrayList<String[]>();
	}

	public void wonGame(String sender, String otherargs) {
		resolveBets(true);
	}

	public void lostGame(String sender, String otherargs) {
		resolveBets(false);
	}

	public void addBucks(String sender, String otherargs) {
		String[] parts = otherargs.split(" ");
		String destination = parts[0];
		int amount = Integer.parseInt(parts[1]);
		try {
			ProfileManager.getProfileByName(destination).addFunds(
					channel.replace("#", ""), amount);
			sendMessage(channel, "Successfully added " + amount + " to "
					+ destination + "'s account.");
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(channel, "Error while adding Bucks to " + destination
					+ " - please try again. USAGE: !COMMAND USER AMOUNT");
		}

	}
	
	public void shutdown(String sender, String otherargs) {
		terminate();
	}

}
