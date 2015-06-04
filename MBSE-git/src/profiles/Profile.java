package profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utils.TXT;

public class Profile {

	ArrayList<Option> options = new ArrayList<Option>();
	ArrayList<Command> commands = new ArrayList<Command>();
	String name;

	public ArrayList<Option> getOptions() {
		return options;
	}

	public ArrayList<Command> getCommands() {
		return commands;
	}

	public Profile(String name) {
		File file = new File(ProfileManager.getBaseFolder().getAbsolutePath()
				+ "/" + name);
		System.out.println(file.getAbsolutePath());
		this.name = name;
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(file.getAbsolutePath() + "/options.txt");

		if (!file.exists()) {
			options.add(new Option("name", name, this));
			options.add(new Option("xp", "0", this));
			options.add(new Option("srratings", "0", this));
			options.add(new Option("avgrating", "0", this));
		} else {
			try {
				List<String> lines = TXT.readFromFile(file.getAbsolutePath());
				for (String l : lines) {
					String[] parts = l.split("◘");
					options.add(new Option(parts[0], this));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file = new File(ProfileManager.getBaseFolder().getAbsolutePath()
				+ "/" + name);
		file = new File(file.getAbsolutePath() + "/commands.txt");

		if (!file.exists()) {

		} else {
			try {
				List<String> lines = TXT.readFromFile(file.getAbsolutePath());
				for (String l : lines) {
					String[] parts = l.split("◘");
					commands.add(new Command(parts[0], this));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (getOption("joinmessage").getValue().equals("INVALID")) {
			
			System.out.println("joinmessage did not yet exist for "+name+". Creating messages...");
			
			addOption("joinmessage","MarenBot (ServerEdition) has joined this channel and is ready to rock!");
			addOption("leavemessage","Terminating connection to this channel");
			addOption("broadcasteronlymessage","Sorry, <sender>, but this command is only for the Broadcaster.");
			addOption("modsonlymessage","Sorry, <sender>, but this command is only for mods.");
			addOption("regularonlymessage","Sorry, <sender>, but this command is only for mods. (REGULAR Support to be added soon™)");
			addOption("subscriberonlymessage","Sorry, <sender>, but this command is only for mods. SUBSCRIBER Support to be added soon™)");
			addOption("insufficientfundsmessage","Sorry, <sender>, but it appears that you don't have enough currency to execute this command.");
			addOption("callmethoderrormessage","Uh oh, something went wrong. Please try again.");
			addOption("mybucksmessage","<sender>'s TrampBucks: T<amount2>");
			addOption("songrequestmessage","Thanks for the Songrequest, <sender>! Dan will play it soon™ and if he likes it, add it to the stream Playlist!");
			addOption("testyourluckwinmessage","Congratulations, <sender>, you have tested your luck and won T<amount2>");
			addOption("testyourlucklosemessage","Sorry, <sender>, you have tested your luck and lost T<amount2>");
			addOption("betsopenmessage","Bets are now open! Use !bet win OR !bet lose to bet how this game will go!");
			addOption("betsclosemessage","Bets are now closed! Good luck!");
			addOption("betsclosedwinmessage","Sorry, <sender>, but the bets are currently closed! Please wait for Dan to open them again! You bet for winning this time. BloodTrail");
			addOption("betsclosedlosemessage","Sorry, <sender>, but the bets are currently closed! Please wait for Dan to open them again! You bet for losing this time. BibleThump");
			addOption("betsclosednonemessage","Sorry, <sender>, but the bets are currently closed! Please wait for Dan to open them again! You did not bet this time.");
			addOption("betwinwinmessage","<sender>, you already bet for winning! BloodTrail");
			addOption("betlosewinmessage","<sender>, you successfully changed your mind from losing to winning! BloodTrail");
			addOption("betnonewinmessage","<sender>, you successfully bet for winning! BloodTrail");
			addOption("betloselosemessage","<sender>, you already bet for losing! BibleThump");
			addOption("betwinlosemessage","<sender>, you successfully changed your mind from winning to losing! BibleThump");
			addOption("betnonelosemessage","<sender>, you successfully bet for losing! BibleThump");
			addOption("betcurrlosemessage","<sender>, you are currently betting for losing! BibleThump");
			addOption("betcurrwinmessage","<sender>, you are currently betting for winning! BloodTrail");
			addOption("betcurrnonemessage","<sender>, usage: !bet win OR !bet lose");
			addOption("wongamemessage","The game is over, it is gloriously won! <correct> bet correctly and got T0.50, the rest that bet got T0.15!");
			addOption("lostgamemessage","The game is over, sadly it is lost! <correct> bet correctly and got T0.50, the rest that bet got T0.15!");
			addOption("bucksaddmessage","Successfully added <amount2> to <receiver>'s account.");
			addOption("bucksadderrormessage","Error while adding Bucks to <receiver> - please try again. USAGE: !COMMAND USER AMOUNT");
			addOption("addbucksallinvalidnumbermessage","Sorry, <sender>, but <input> is not a valid number. USAGE: !COMMAND AMOUNT");
			addOption("myratingmessage","<sender>'s average song request rating: <rating>");
			addOption("songlinkmessage","Currently Playing this request: <link>");
			addOption("songlinknotplayingmessage","Currently not playing user requests. Look in the top left corner for the currently playing song.");
			addOption("rafflestartmessage","A Raffle has been started! Type anything in chat and you will be egliable to win!");
			addOption("raffleendmessage","The raffle has ended and the lucky winner is <winner>! Congratulations!");
			
			
		}
		
	}

	public String getName() {
		return name;
	}

	public Option getOption(String name) {
		for (Option o : options) {
			if (name.equals(o.name)) {
				return o;
			}
		}
		return new Option("INVALID", "INVALID", this);
	}

	public void addOption(String name, String value) {
		options.add(new Option(name, value, this));
	}

	public boolean addFunds(String channel, int amount) {
		Option o = getOption(channel);
		if (o.getValue().equals("INVALID")) {
			o = new Option(channel, "0", this);
			options.add(o);

		}
		if (amount < 0 && Integer.parseInt(o.value) < (amount * -1))
			return false;
		getOption(channel).set((amount + Integer.parseInt(o.value)) + "");
		return true;

	}

	public int getFunds(String channel) {
		Option o = getOption(channel);
		
		if (o != null) {
			if (o.getValue().equals("INVALID")) {
				addOption(channel, "0");
				o = getOption(channel);
			}
			return Integer.parseInt(o.getValue());
			}
		return 0;
	}

	public Command getCommand(String name) {
		for (Command c : commands) {
			if (c.name.equals(name)) {
				return c;
			}
		}
		return null;
	}

	public boolean addCommand(String name, CommandType type, PermLevel pl,
			int c, String v) {
		System.out.println("PROFILE Adding command...");
		boolean exists = true;

		try {
			getCommand(name).getName();
		} catch (NullPointerException e) {
			System.out.println("NULL POINTER! NULL POINTER EVERYWHERE!");
			exists = false;
		}

		if (!exists) {
			commands.add(new Command(name, type, pl, c, v, this));
			return true;
		}
		return false;
	}

	public boolean delCommand(String name) {
		if (getCommand(name) != null) {
			for (Command c : commands) {
				if (c.name.equals(name)) {
					return commands.remove(c);
				}
			}
		}
		return false;
	}
	
	public void songRating(int rating) {
		Option o = getOption("srratings");
		o.set(o.getValue()+"#"+rating);
	}
	public double calcAvgRating() {
		String s = getOption("srratings").getValue();
		String[] parts = s.split("#");
		int sum = 0;
		for (String part:parts) {
			sum+=Integer.parseInt(part);
		}
		double avg = 0;
		if (parts.length>0) {
			avg = (double) sum/(double) parts.length;
		}
		getOption("avgrating").set(avg+"");
		return avg;
	}
}
