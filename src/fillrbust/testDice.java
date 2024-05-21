/*
test Dice class:
Roll all and display result and potential score
reserve a couple,
disallow reserving non-scorers
roll remainder, display result and potential score
*/
package fillrbust;

import java.util.*;

class testDice {

	public static void main(String[] args) {
		Dice dice;
		String rolled;
		int score, cum, dsc;
		String reserved;
		String masked;
		String used;
		String[] inputs = {"533555"
				          ,"654321"
						  ,"636456"
					      ,"323355"
					      ,"254252"
					      ,"324326"
					      ,"111111"
		                   };
		// start scripted tests
		System.out.println(" input  dice    score    mask   reserved");
		String outform1 = "%s  %s   % 5d    %s";
		for(String each: inputs) {
			dice = new Dice(each);
			rolled = dice.getRolled();
			score = dice.getScore();
			masked = dice.getMasked();
			System.out.println(String.format(outform1,
					each, rolled, score, masked));
			dice.selected("111000");
			score  = dice.getScore();
			used = dice.getUsed();
			masked = dice.getMasked();
			System.out.println(String.format(outform1,
					 rolled,used, score, masked));
		}

		System.out.println("\ntest multiple rolls");
		dice = new Dice("234353");
		rolled = dice.getRolled();  // 233345
		score = dice.getScore();    // 350
		masked = dice.getMasked();  // 011101
		System.out.println(String.format(outform1,
				"234353", rolled, score, masked));
		dice.selected("011100");
		score  = dice.getScore();   //  300
		used = dice.getUsed();      //  333
		masked = dice.getMasked();
		System.out.println(String.format(outform1,
				rolled,used, score, masked));

		System.out.println("\nRoll #2");         //   Roll 2
		cum=score;
		dice.rolagain("315");
		rolled =dice.getRolled() + " " +dice.getReserved() ;  // 135 333
		System.out.println(rolled);
		score = dice.getScore();    // 150
		used = dice.getUsed();      //  101
		masked = dice.getMasked();  // 101 111
		System.out.println(masked);
		System.out.println(String.format(outform1,
				rolled,used, score, masked));
		System.out.println("Select 100");
		dice.selected("100");
		score  = dice.getScore();   //  100
		used = dice.getUsed();      //  1
		masked = dice.getMasked();  //  100
		System.out.println(String.format(outform1,
				rolled,used, score, masked));
		System.out.println("Selection error 011");
		dice.selected("011");
		score  = dice.getScore();   //  50
		used = dice.getUsed();      //  5
		masked = dice.getMasked();  //  001
		System.out.println(String.format(outform1,
				rolled,used, score, masked));
		System.out.println("Selection error 000");
		dice.selected("000");
		score  = dice.getScore();   //  0
		used = dice.getUsed();      //  ' '
		masked = dice.getMasked();  //  000
		System.out.println(String.format(outform1,
				rolled,used, score, masked));
		System.out.println("Select 001");
		dice.selected("001");
		score  = dice.getScore();   //  50
		used = dice.getUsed();      //  5
		masked = dice.getMasked();  //  001
		System.out.println(String.format(outform1,
				rolled,used, score, masked));
		System.out.println("revert to default mask");
		dice.deselected();
		score  = dice.getScore();   //  150
		used = dice.getUsed();      //  15
		masked = dice.getMasked();  //  101
		System.out.println(String.format(outform1,
				rolled,used, score, masked));

		System.out.println("\nRoll #3");         //   Roll 3
		dice.rolagain("61");
		rolled =dice.getRolled() + " " +dice.getReserved() ;  // 16 3331
		System.out.println(rolled);
		score = dice.getScore();    // 100
		used = dice.getUsed();      //  10
		masked = dice.getMasked();  // 10 1111
		System.out.println(masked);
		System.out.println(String.format(outform1,
				rolled,used, score, masked));

		// let it play
		System.out.println("\nEnd of script. random now\n\n");
		if (args.length < 1) {
			dice = new Dice();
		} else {
			dice = new Dice(args[0]);
		}
	
		rolled = dice.getRolled();
		System.out.println(rolled+" "+dice.getReserved());

		score = dice.getScore();
		System.out.println(dice.getMasked());
		System.out.println("possible score: " + score);
		if (score == 0) {
			System.out.println("\n!!  BUSTED  !");
			System.exit(0);
		}
		if (args.length > 1) {
			dice.selected(args[1]);
			score = dice.getScore();
			System.out.println("chosen score: " + score);
		}
		String numUsed = dice.getUsed();
		if (numUsed.length() == rolled.length()) {
			System.out.println("You filled it!");
			System.out.println(dice.isFilled());
			System.exit(0);
		}
		System.out.println("Reserve " + numUsed);

		System.out.println("\nRoll #2");
		dice.rolagain();
		numUsed = dice.getUsed();
		System.out.println(dice.getRolled() + " " +dice.getReserved());
		System.out.println(dice.getMasked());
		dsc = dice.getScore();
		if (dsc == 0) {
			System.out.println("\n!!  BUSTED  !");
			System.exit(0);
		}
		System.out.println("possible additional score: " + dsc + " for total of " + (score + dsc));
		if (dice.isFilled()) {
			System.out.println("You filled it!");
			System.exit(0);
		}
		cum = dsc + score;

		System.out.println("\nRoll #3");
		dice.rolagain();
		numUsed = dice.getUsed();
		System.out.println(dice.getRolled() + " " +dice.getReserved());
		System.out.println(dice.getMasked());
		dsc = dice.getScore();
		if (dsc == 0) {
			System.out.println("\n!!  BUSTED  !");
			System.exit(0);
		}
		System.out.println("possible additional score: " + dsc + " for total of " + (cum + dsc));
		if (dice.isFilled()) {
			System.out.println("You filled it!");
		}
	}
}
