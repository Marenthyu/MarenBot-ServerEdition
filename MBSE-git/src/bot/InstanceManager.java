package bot;

import java.util.ArrayList;

/**
 * Manager Class for all running instances of {@link Bot}s. <br>
 * Call {@link #initialize()} before using any other method.
 * 
 * @author Peter "Marenthyu" Fredebold
 * @see {@link #newInstance} {@link #initialize()}
 *
 */
public class InstanceManager {

	/**
	 * {@link ArrayList} of all running instances.
	 */
	private static ArrayList<Bot> instances;

	/**
	 * Initializes the InstanceManager by creating an empty {@link ArrayList}
	 * which will be filled with all running instances of {@link Bot}s.
	 */
	public static void initialize() {
		instances = new ArrayList<Bot>();
	}

	/**
	 * This Method returns an {@link ArrayList} of {@link Bot}s that are
	 * currently running.
	 * 
	 * @return an {@link ArrayList} of {@link Bot}s that are currently running.
	 */
	public static ArrayList<Bot> getAllInstances() {
		return instances;
	}

	/**
	 * Creates a new instance of a {@link Bot} and adds it to the list of
	 * running instances while returning if the creation was successful.
	 * 
	 * @param channel
	 *            - the channel to join
	 * @return <p>
	 *         <b>true</b> if Instance was successfully created in the specified
	 *         channel
	 *         </p>
	 *         <p>
	 *         <b>false</b> if another instance in that channel already exists
	 *         </p>
	 */
	public static boolean newInstance(String channel) {
		if (getInstanceByChannel(channel) == null)
			try {
				instances.add(new Bot("MarenBot", channel));
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {
			System.err
					.println("Instance already exists. Please terminate it before creating a new one!");

		}
		return false;
	}

	/**
	 * This Method terminates a running instance of a {@link Bot} in the
	 * specified channel and returns if the termination was successful.
	 * 
	 * @param channel
	 * @return <p>
	 *         <b>true</b> if Instance was successfully terminated from the
	 *         specified channel
	 *         </p>
	 *         <p>
	 *         <b>false</b> if no instance was in that channel
	 *         </p>
	 */
	public static boolean terminateInstanceByChannel(String channel) {

		for (Bot b : instances) {
			if (b.getChannel().equals(channel)) {
				b.terminate();
				instances.remove(b);
				System.out.println("Terminated instance \"" + channel + "\"");
				return true;
			}

		}
		return false;
	}

	/**
	 * This Method returns a {@link Bot} object that fits to the specified
	 * channel
	 * 
	 * @param channel
	 * @return <p>
	 *         a <b>{@link Bot}</b> object if there was a running instance in
	 *         the specified channel
	 *         </p>
	 *         <p>
	 *         <b>null</b> if no running instance was found for the specified
	 *         channel
	 *         </p>
	 */
	public static Bot getInstanceByChannel(String channel) {
		for (Bot b : instances) {
			if (b.getChannel().equals(channel)) {
				return b;
			}
		}
		return null;
	}

}
