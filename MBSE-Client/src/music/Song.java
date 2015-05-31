package music;

import java.util.ArrayList;
import utils.MATH;

public class Song {

	public String name;
	public float rating;
	private ArrayList<Integer> ratings = new ArrayList<Integer>();

	public Song(String string, ArrayList<Integer> ratings) {
		name = string;
		this.ratings = ratings;
		int sum = 0;
		for (int i : ratings) {
			sum += i;
		}
		if (ratings.size() != 0) {
			rating = ((float) ((float) sum) /( (float) ratings.size()));
		} else {
			rating = 0;
		}
	}

	public Song(String name) {
		this(name, new ArrayList<Integer>());
	}

	public void rate(int rating) {
		ratings.add(rating);
		recalculate();
	}
	
	public void recalculate() {
		int sum = 0;
		for (int i : ratings) {
			sum += i;
		}
		rating = (float) sum / ratings.size();
	}

	public String toString() {
		String ret = name;

		for (Integer r : ratings) {
			ret += ("#"+r);
		}
		
		return ret;
	}

	public String toFancyString() {
		return name + " [" + MATH.round(rating, 2) + "]";
	}

}
