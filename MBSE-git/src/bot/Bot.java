package bot;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import profiles.Command;
import profiles.CommandType;
import profiles.Option;
import profiles.PermLevel;
import profiles.Profile;
import profiles.ProfileManager;
import utils.MATH;

public class Bot extends PircBot {

	private String channel;
	private Profile profile;
	private ArrayList<String[]> songrequests = new ArrayList<String[]>(),
			betsmade;
	public boolean bets;
	private Timer timer = new Timer();
	private TimerTask passivefundgain, betsclosure;
	public boolean clientconnected = false;
	public ArrayList<Integer> ratings = new ArrayList<Integer>();
	private boolean userrequests = false, raffle = false;
	public String currUser = "NONE", currSong = "NONE";
	private ArrayList<String> rafflepot = new ArrayList<String>(), rated = new ArrayList<String>();

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
		sendMessage(channel,
				"MarenBot (ServerEdition) has joined this channel and is ready to rock!");
		bets = false;
		betsmade = new ArrayList<String[]>();

		clientconnected = false;

		passivefundgain = new TimerTask() {

			@Override
			public void run() {
				System.out.println("It's been a minute in " + channel
						+ ". Giving Bucks to all!");
				int multiplicator = 1;
				if (clientconnected) {
					multiplicator = 2;
				}
				addBucksAll("SERVER", 1 * multiplicator + "");
			}

		};

		timer.schedule(passivefundgain, 0, 60000);

	}

	public String getChannel() {
		return channel;
	}

	public ArrayList<Command> getCommands() {
		return profile.getCommands();
	}
	
	public void terminate() {
		// TODO Message doesn't get fired
		sendMessage(channel, "Terminating connection to this channel");
		while (this.getOutgoingQueueSize() > 0) {
			System.out
					.println("Termination of "
							+ channel
							+ " imminent, but Still some messages to be sent out. Running around in circles...");
		}
		passivefundgain.cancel();
		this.disconnect();
		this.dispose();

	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		System.out.println("Message in " + channel + " from " + sender + ": "
				+ message);
		
		if (raffle) {
			boolean a = true;
			for (String s:rafflepot) {
				if (s.equals(sender)) {
					a = false;
					break;
				}
			}
			if (a) {
				rafflepot.add(sender);
			}
		}

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
								+ ", but it appears that you don't have enough TrampBucks to execute this command.");
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

	public boolean removeCommand(String name) {
		return profile.delCommand(name);
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
						+ MATH.round(((float) ((float) (ProfileManager
								.getProfileByName(sender).getFunds(channel
								.replace("#", "")))) / 100), 2));
	}

	public void songRequest(String sender, String otherargs) {
		String[] s = new String[2];
		s[0] = sender;
		s[1] = otherargs;
		songrequests.add(s);
		sendMessage(
				channel,
				"Thanks for the Songrequest, "
						+ sender
						+ "! Dan will play it soon™ and if he likes it, add it to the stream Playlist!");
	}

	public String[] getOldestSongrequest() {
		if (songrequests.size() > 0)
			return songrequests.get(0);
		else {
			String[] s = new String[2];
			s[0] = "NO REQUESTS";
			s[1] = "NO REQUESTS";
			return s;
		}
	}

	public void deleteOldestSongrequest() {
		try {
			songrequests.remove(0);
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
			sendMessage(
					channel,
					"Congratulations, "
							+ sender
							+ ", you have tested your luck and won T"
							+ MATH.round(((float) ((float) (win - 100)) / 100),
									2));
			return;
		}
		sendMessage(
				channel,
				"Sorry, "
						+ sender
						+ ", you have tested your luck and lost T"
						+ MATH.round(
								(((float) ((float) (win - 100)) / 100) * -1), 2));
	}

	public void toggleBets(String sender, String otherargs) {
		switch (otherargs) {
		case "true": {
			bets = true;
			break;
		}
		case "false": {
			bets = false;
			break;
		}
		default: {
			bets = !bets;
		}
		}

		if (bets) {
			sendMessage(channel,
					"Bets are now open! Use !bet win OR !bet lose to bet how this game will go!");
			betsclosure = new TimerTask() {

				@Override
				public void run() {
					toggleBets("SERVER", "false");
					System.out.println("4 minutes elapsed, closed bets in "
							+ channel + " automatically!");
				}

			};
			timer.schedule(betsclosure, 240000);
		} else {
			sendMessage(channel, "Bets are now closed! Good luck!");
			betsclosure.cancel();
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
								+ ", but the bets are currently closed! Please wait for Dan to open them again! You bet for winning this time. BloodTrail");
				break;
			}

			case "lose": {
				sendMessage(
						channel,
						"Sorry, "
								+ sender
								+ ", but the bets are currently closed! Please wait for Dan to open them again! You bet for losing this time. BibleThump");
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
							+ " bet correctly and got T0.50, the rest that bet got T0.15!");
		} else {
			sendMessage(
					channel,
					"The game is over, sadly it is lost! "
							+ correct
							+ " bet correctly and got T0.50, the rest that bet got T0.15!");
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
			if (!sender.equals("SERVER"))
				sendMessage(
						channel,
						"Successfully added "
								+ MATH.round(
										((float) ((float) amount / (float) 100)),
										2) + " to " + destination
								+ "'s account.");
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(channel, "Error while adding Bucks to " + destination
					+ " - please try again. USAGE: !COMMAND USER AMOUNT");
		}

	}

	public void addBucksAll(String sender, String otherargs) {
		int amount;
		try {
			amount = Integer.parseInt(otherargs);
		} catch (Exception e) {
			sendMessage(channel, "Sorry, " + sender + ", but " + otherargs
					+ " is not a valid number. USAGE: !COMMAND AMOUNT");
			return;
		}

		User[] users = getUsers(channel);

		for (User u : users) {
			addBucks("SERVER", u.getNick().toLowerCase() + " " + amount);
		}

	}

	public void shutdown(String sender, String otherargs) {
		terminate();
	}

	public void rateSong(String sender, String otherargs) {
		int rating;

		try {
			rating = Integer.parseInt(otherargs);
		} catch (Exception e) {
			System.err.println("In " + channel + " someone rated " + otherargs
					+ "! How stupid!");
			return;
		}
		if ((rating < 1) || (rating > 10)) {
			System.err.println("DUH! We're on a scale of 1 to 10, " + sender
					+ " in " + channel + ". Don't try to break me.");
			return;
		}
		for (String s:rated) {
			if (s.equals(sender)) {
				return;
			}
		}
		if (userrequests) {
			ProfileManager.getProfileByName(currUser).songRating(rating);
			ratings.add(rating);
		} else

			ratings.add(rating);
		ProfileManager.getProfileByName(sender).addFunds(channel.replace("#", ""), 5);
		rated.add(sender);

	}

	public void resetRatings() {
		ratings.clear();
	}

	public void clientConnected() {
		clientconnected = true;
	}

	public void clientDisconnected() {
		clientconnected = false;
	}

	public void toggleSongMode(String sender, String otherargs) {
		switch (otherargs) {
		case "true": {
			userrequests = true;
			break;
		}
		case "false": {
			userrequests = false;
			break;
		}
		default: {
			userrequests = !userrequests;
			break;
		}
		}
	}

	public boolean songMode() {
		return userrequests;
	}

	public void myRating(String sender, String otherargs) {
		sendMessage(channel, sender + "'s average song request rating: "
				+ MATH.round((float) ProfileManager.getProfileByName(sender).calcAvgRating(), 2));
	}

	public void songLink(String sender, String otherargs) {
		if (userrequests) {
			sendMessage(channel, "Currently Playing this request: "
					+ getOldestSongrequest()[1]);
		} else {
			sendMessage(
					channel,
					"Currently not playing user requests. Look in the top left corner for the currently playing song.");
		}
	}
	
	public void startRaffle(String sender, String otherargs) {
		raffle = true;
		rafflepot = new ArrayList<String>();
		sendMessage(channel, "A Raffle has been started! Type anything in chat and you will be egliable to win!");
	}
	
	public void endRaffle(String sender, String otherargs) {
		raffle = false;
		int random = (int) (Math.random()*(rafflepot.size()));
		sendMessage(channel, "The raffle has ended and the lucky winner is "+rafflepot.get(random)+"! Congratulations!");
	}
	
	public void newSong(String song) {
		currSong = song;
		rated = new ArrayList<String>();
	}

}
