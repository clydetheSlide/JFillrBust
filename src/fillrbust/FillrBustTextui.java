package fillrbust;

/* fake a no-op class to test FillRBustGame */
class FillrBustTextui {

	public FillrBustTextui() {
	}

	public static void setOptionA(String dum) {
		System.out.println("optionA: "+dum);
	}
	public static void setOptionB(String dum) {
		System.out.println("optionB: "+dum);
	}
	public static void deRoll() {}

	/**
	 * display the card
	 * @param card
	 */
	public static void setCard(Cards.Name card) {System.out.println(card);}

	/**
	 * Instruct the user what just happened
	 * and what to do next.
	 * @param line
	 */
	public static void addDetails(String line) {System.out.print(line);}

	public static void declareWinner(String line) {
		System.out.println(String.format("  %s is the big winner !!!",line));
	}

	/**
	 * set display of selected dice score and running score
	 * @param run running score
	 * @param used selectid dice potential score
	 */
	public static void setRunning(int run, int used) {
		System.out.println(String.format(
				"Selected dice would add %d to the "+
				"running score of %d.",used, run ));
	}

	/**
	 * Report which dice are selected.
	 * @return String of 1s and zeros
	 */
	public static String getDiceSelection() {
		return "1111111";
	}

	/**
	 * switch which player is playing
	 * @param dum is the name of the current player
	 */
	public static void setCurrentPlayer(String dum) {System.out.println("player "+dum);}

	/**
	 * display the dice
	 * @param dum is the 7-character String of the dice values
	 *            ' ' indicates the fence
	 * @param dum2 is the 7-character String of which is selected;
	 *             '1' is yes
	 */
	public static void setDice(String dum, String dum2) {System.out.println("dice: "+dum+" "+dum2);}

	/**
	 * add a score to the currently playing player's scoresheet
	 * @param dum
	 */
	public static void addScore(int dum) {}

	/**
	 * set the total score for the currently playing player
	 * @param dum
	 */
	public static void updateScore(int dum) {
		System.out.println(String.format("    Your score is now %d.\n", dum));
	}

	/**
	 * set the total score for the specified player
	 * @param dummy is the name of the player
	 * @param dum is the player's new score
	 */
	public static void updateScore(String dummy,int dum) {}
}
