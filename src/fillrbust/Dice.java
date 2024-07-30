package fillrbust;

import java.util.*;

/** Set of six standard cubic dice with a number on each side*/
class Dice {
	static class Scoreable {
		String group;
		int score;

		public Scoreable(String s, int val) {
			group = s;
			score = val;
		}

		public boolean iskey(String s) {
			return s.equals(group);
		}

		public int getScore() {
			return score;
		}

		public String key() {
			return group;
		}
	}

	private Scoreable[] scores = {
			new Scoreable("111", 1000)
			, new Scoreable("123456", 1500)
			, new Scoreable("1", 100)
			, new Scoreable("222", 200)
			, new Scoreable("333", 300)
			, new Scoreable("444", 400)
			, new Scoreable("555", 500)
			, new Scoreable("5", 50)
			, new Scoreable("666", 600)
	};
	private int score;
	private String reserved;
	private String rollable;
	private String used;
	private String selectedD = "not";
	private char[] mask;
	private Random randy = new Random(new Date().getTime());

	/**
	 * Initialize a set of six dice.
	 * Define what is scoreable.
	 * Roll all six and reserve none.
	 */
	public Dice() {
		reserved = "";       //# list of values of reserved dice
		rollable = roll();    //# rollable is a list of the values of the dice not reserved
		score = scoreIt(rollable);
	}

	/* for testing */
	public Dice(String testroll) {
		reserved = "999999".substring(testroll.length());
		//System.out.println("initial reserved " + reserved);
		rollable = roll(testroll);
		score = scoreIt(rollable);
	}

	/**
	 * set which dice are selected to score
	 *
	 * @param mask is a String of 1s and zeros
	 *             where 1 indicates to use the corresponding die to score
	 */
	public void selected(String mask) {
		score = scoreIt(rollable, mask);
		selectedD = mask;
	}

	public void deselected() {
		score = scoreIt(rollable);
	}

	/**
	 * Pick a random number 1-6
	 * for each die that has not been reserved.
	 *
	 * @return the String of numbers
	 */
	private String roll() {
		int num = 6 - reserved.length();
		//dice=sorted([random.randint(1,6) for ii in range(num)])
		ArrayList<Integer> dice = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) dice.add(randy.nextInt(6));
		Collections.sort(dice);
		selectedD="not";
		return list2str(dice);
	}
	private String roll(String testRoll){
		int num = 6 - reserved.length();
		ArrayList<Integer> dice = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			int single = Integer.parseInt(testRoll.substring(i,i+1))-1;
			dice.add(single);
		}
		Collections.sort(dice);
		return list2str(dice);
	}

	/**
	 * convert list of integers to a String
	 */
	private String list2str(ArrayList<Integer> set) {
		char[] cars;
		cars = new char[set.size()];
		for (int i = 0; i < set.size(); i++) cars[i] = (char) (set.get(i) + 49);
		return new String(cars);
	}

	/**
	 * show the recently rolled dice
	 * @return the String of numbers
	 */
	public String getRolled() {
		return rollable;
	}

	private int scoreIt(String rolled, String mask) {
		String keepIt = "";
		for (int i = 0; i < rolled.length(); i++)
			if (i<mask.length() && mask.charAt(i) == '1') keepIt = keepIt + rolled.charAt(i);
		return scoreIt(keepIt, false);
	}

	private int scoreIt(String rolled) {
		return scoreIt(rolled, true);
	}

	private int scoreIt(String rolled, boolean makeMask) {
		// look for scoreable combinations in the set of dice
		int p = 0;
		int score = 0;
		int dscore = 0;
		used = "";
		if (makeMask){
			mask = "000000".substring(0, rolled.length()).toCharArray();
			selectedD="not";
		}
		//System.err.print(mask);
		//System.err.println(" A ");
		while (p < rolled.length()) {
			for (Scoreable each : scores) {
				//System.err.println("look for "+each.key()+" in "+rolled.substring(p));
				int ind = rolled.substring(p).indexOf(each.key());
				if (ind >= 0) {
					dscore += each.getScore();
					used += each.key();
					if (makeMask) for (int i = 0; i < each.key().length(); i++) mask[p + i + ind] = '1';
					//System.err.print(mask);
					//System.err.println(" B ");
					p += each.key().length() + ind;
					//System.err.println("found it at "+ind);
					//System.err.println("offset search to "+p);
					break;
				}
			}
			if (dscore == 0) {
				return score;
			} else {
				score += dscore;
				dscore = 0;
			}
		}
		return score;
	}

	/**
	 * roll all the dice that have not been reserved
	 */
	public void rolagain() {
		reserved += used;
		used = "";
		selectedD = "not";
		rollable = roll();
		score = scoreIt(rollable);
	}
	public void rolagain(String testRoll) {
		reserved += used;
		used = "";
		selectedD = "not";
		rollable = roll(testRoll);
		score = scoreIt(rollable);
	}

	/**
	 * report the score for the dice
	 */
	public int getScore() {
		return score;
	}

	/**
	 * return the set of dice that are used to score
	 * @return the String of numbers
	 */
	public String getUsed() {
		return used;
	}

	/**
	 * return the set of dice that have been reserved
	 * @return the String of numbers
	 */
	public String getReserved() {
		return reserved;
	}

	/**
	 * Show the selection mask of the recently
	 * rolled and the reserved
	 * @return a String of 1s and zeros
	 */
	public String getMasked() {
		if (selectedD.contains("not")) return getMaskedU();
		return selectedD + " " + "111111".substring(0, reserved.length());
	}

	/**
	 * Show the default selection mask of the recently
	 * rolled and the reserved
	 * @return a String of 1s and zeros
	 */
	public String getMaskedU() {
		String masked = new String(mask);
		return masked + " " + "111111".substring(0, reserved.length());
	}

	/**
	 * report whether all the dice are scoreable
	 */
	public boolean isFilled() {
		String masked = new String(mask);
		return !masked.contains("0");
	}
}
