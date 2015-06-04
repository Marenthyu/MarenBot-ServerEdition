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
	private ArrayList<String> rafflepot = new ArrayList<String>(),
			rated = new ArrayList<String>();

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
		sendMessage(channel, profile.getOption("joinmessage").getValue());
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
		sendMessage(channel, profile.getOption("leavemessage").getValue());
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
			for (String s : rafflepot) {
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
					sendMessage(channel,
							profile.getOption("broadcasteronlymessage")
									.getValue().replace("<sender>", sender));
					return;
				}
				break;
			}
			case MOD: {
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(channel, profile.getOption("modsonlymessage")
							.getValue().replace("<sender>", sender));
					return;
				}
				break;
			}
			case REGULAR:
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(channel, profile
							.getOption("regularonlymessage").getValue()
							.replace("<sender>", sender));
					;
					return;
				}
				break;
			case SUBSCRIBER:
				if (!isMod(sender) && !sender.equals(channel.replace("#", ""))) {
					sendMessage(channel,
							profile.getOption("subscriberonlymessage")
									.getValue().replace("<sender>", sender));
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
				sendMessage(channel,
						profile.getOption("insufficientfundsmessage")
								.getValue().replace("<sender>", sender));
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
							profile.getOption("callmethoderrormessage")
									.getValue());
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

		Profile senderp = ProfileManager.getProfileByName(sender);
		int amount = senderp.getFunds(channel.replace("#", ""));

		sendMessage(
				channel,
				profile.getOption("mybucksmessage")
						.getValue()
						.replace("<sender>", sender)
						.replace("<amount0>", amount + "")
						.replace(
								"<amount1>",
								MATH.round(((float) ((float) (amount)) / 10), 1)
										.toString())
						.replace(
								"<amount2>",
								MATH.round(((float) ((float) (amount)) / 100),
										2).toString())
						.replace(
								"<amount3>",
								MATH.round(((float) ((float) (amount)) / 1000),
										3).toString()));
	}

	public void songRequest(String sender, String otherargs) {
		String[] s = new String[2];
		s[0] = sender;
		s[1] = otherargs;
		songrequests.add(s);
		sendMessage(channel, profile.getOption("songrequestmessage").getValue()
				.replace("<sender>", sender));
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
					profile.getOption("testyourluckwinmessage")
							.getValue()
							.replace("<sender>", sender)
							.replace("<amount0>", win + "")
							.replace(
									"<amount1>",
									MATH.round(((float) ((float) win) / 10), 1)
											.toString())
							.replace(
									"<amount2>",
									MATH.round(((float) ((float) win) / 100), 2)
											.toString())
							.replace(
									"<amount3>",
									MATH.round(((float) ((float) win) / 1000),
											3).toString()));
			return;
		}
		sendMessage(
				channel,
				profile.getOption("testyourluckwinmessage")
						.getValue()
						.replace("<sender>", sender)
						.replace("<amount0>", (win-100)*-1 + "")
						.replace(
								"<amount1>",
								MATH.round(
										((float) ((float) win - 100) / 10) * -1,
										1).toString())
						.replace(
								"<amount2>",
								MATH.round(
										((float) ((float) win - 100) / 100)
												* -1, 2).toString())
						.replace(
								"<amount3>",
								MATH.round(
										((float) ((float) win - 100) / 1000)
												* -1, 3).toString()));
		;
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
			sendMessage(channel, profile.getOption("betsopenmessage")
					.getValue());
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
			sendMessage(channel, profile.getOption("betsclosemessage")
					.getValue());
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
				sendMessage(channel, profile.getOption("betsclosedwinmessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			case "lose": {
				sendMessage(channel, profile.getOption("betsclosedlosemessage")
						.getValue().replace("<sender>", sender));
				break;
			}
			default: {
				sendMessage(channel, profile.getOption("betsclosednonemessage")
						.getValue().replace("<sender>", sender));
				break;
			}
			}
			return;
		}

		switch (otherargs) {
		case "win": {
			switch (previousbet) {
			case "win": {
				sendMessage(channel, profile.getOption("betwinwinmessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			case "lose": {
				betsmade.remove(index);
				betsmade.add(s);
				sendMessage(channel, profile.getOption("betlosewinmessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			default: {
				betsmade.add(s);
				sendMessage(channel, profile.getOption("betnonewinmessage")
						.getValue().replace("<sender>", sender));
				break;
			}
			}
			break;
		}
		case "lose": {
			switch (previousbet) {
			case "lose": {
				sendMessage(channel, profile.getOption("betloselosemessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			case "win": {
				betsmade.remove(index);
				betsmade.add(s2);
				sendMessage(channel, profile.getOption("betwinlosemessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			default: {
				betsmade.add(s2);
				sendMessage(channel, profile.getOption("betnonelosemessage")
						.getValue().replace("<sender>", sender));
				break;
			}
			}
			break;
		}
		default: {
			switch (previousbet) {
			case "lose": {
				sendMessage(channel, profile.getOption("betcurrlosemessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			case "win": {
				sendMessage(channel, profile.getOption("betcurrwinmessage")
						.getValue().replace("<sender>", sender));
				break;
			}

			default: {
				sendMessage(channel, profile.getOption("betcurrnonemessage")
						.getValue().replace("<sender>", sender));
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
			sendMessage(channel, profile.getOption("wongamemessage").getValue()
					.replace("<correct>", correct + ""));
		} else {
			sendMessage(channel, profile.getOption("lostgamemessage")
					.getValue().replace("<correct>", correct + ""));
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
						profile.getOption("bucksaddmessage")
								.getValue()
								.replace("<receiver>", destination)
								.replace("<amount0>", amount + "")
								.replace(
										"<amount1>",
										MATH.round(
												((float) ((float) (amount)) / 10),
												1).toString())
								.replace(
										"<amount2>",
										MATH.round(
												((float) ((float) (amount)) / 100),
												2).toString())
								.replace(
										"<amount3>",
										MATH.round(
												((float) ((float) (amount)) / 1000),
												3).toString()));

		} catch (Exception e) {
			e.printStackTrace();
			sendMessage(channel, profile.getOption("bucksadderrormessage")
					.getValue().replace("<receiver>", destination));
		}

	}

	public void addBucksAll(String sender, String otherargs) {
		int amount;
		try {
			amount = Integer.parseInt(otherargs);
		} catch (Exception e) {
			sendMessage(
					channel,
					profile.getOption("bucksadderrormessage").getValue()
							.replace("<sender>", sender)
							.replace("<input>", otherargs));
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
		for (String s : rated) {
			if (s.equals(sender)) {
				return;
			}
		}
		if (userrequests) {
			ProfileManager.getProfileByName(currUser).songRating(rating);
			ratings.add(rating);
		} else

			ratings.add(rating);
		ProfileManager.getProfileByName(sender).addFunds(
				channel.replace("#", ""), 5);
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
		String rating = MATH
				.round((float) ProfileManager.getProfileByName(sender)
						.calcAvgRating(), 2).toString();
		sendMessage(channel, profile.getOption("myratingmessage").getValue()
				.replace("<sender>", sender).replace("<rating>", rating));
	}

	public void songLink(String sender, String otherargs) {

		if (userrequests) {
			String link = getOldestSongrequest()[1];
			sendMessage(
					channel,
					profile.getOption("songlinkmessage").getValue()
							.replace("<sender>", sender)
							.replace("<link>", link));
		} else {
			sendMessage(channel, profile.getOption("songlinknotplayingmessage")
					.getValue().replace("<sender>", sender));
		}
	}

	public void startRaffle(String sender, String otherargs) {
		raffle = true;
		rafflepot = new ArrayList<String>();
		sendMessage(channel, profile.getOption("rafflestartmessage").getValue());
	}

	public void endRaffle(String sender, String otherargs) {
		raffle = false;
		int random = (int) (Math.random() * (rafflepot.size()));
		String winner = rafflepot.get(random);
		sendMessage(channel, profile.getOption("raffleendmessage").getValue()
				.replace("<winner>", winner));
	}

	public void newSong(String song) {
		currSong = song;
		rated = new ArrayList<String>();
	}

	public ArrayList<Option> getOptions() {
		// TODO Auto-generated method stub
		return profile.getOptions();
	}

}
