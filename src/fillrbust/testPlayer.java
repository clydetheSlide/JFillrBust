/*
test Player class:
make a couple players
update the score
compare the scores
*/
package fillrbust;

import java.util.HashMap;

class testPlayer {

    public static void main(String[] args) {
	HashMap<String, Player> players = new HashMap<String, Player>(4);
	players.put("Fred", new Player("Fred"));
	players.put("Clyde", new Player("Clyde", 100));
	players.put("aiAudrey", new AIPlayer("aiAudrey", 4));

	System.out.println("in the beginning:");
	for (String each : players.keySet()){
	    System.out.println(String.format("%s has %d points",
			players.get(each).getName(),
			players.get(each).getScore()));
	}

	players.get("Fred").update(25);
	System.out.println("after update:");
	for (String each : players.keySet()){
	    System.out.println(String.format("%s has %d points",
			players.get(each).getName(),
			players.get(each).getScore()));
	}
    }
}
