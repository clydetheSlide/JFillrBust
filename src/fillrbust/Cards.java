package fillrbust;
import java.util.*;

/** A deck of 54 FillRBust cards shuffled randomly */
class Cards{
    private ArrayList <Integer> deck;
    /** make a deck of 54 FillRBust cards shuffled randomly */
    public Cards(){
            deck=shuffle();
    }
    public Cards(ArrayList<Integer> deck){
            this.deck=deck;
    }

    /** Card types */
    public enum Name {
	BONUS_300
	,BONUS_400
	,BONUS_500
	,MUST_BUST
	,FILL_1000
	,VENGEANCE
	,NO_DICE
	,DOUBLE_TROUBLE
    }

    /**build a random deck of 54 cards
    */
    public ArrayList<Integer> shuffle(){
	//deck= range(54);
	deck = new ArrayList <Integer>();
	for (int i=0; i<54;i++) deck.add(i);
	//random.shuffle(deck);
	Collections.shuffle(deck);
	return deck;
    }

    /** remove a card and return its type  */
    public Name draw(){
	return draw(false);
    }
    public Name draw(boolean debug){
	int id=deck.get(0);
	deck.remove(0);
        if (debug) System.out.println(String.format( "DEBUG: %d card left in deck after card %d (=%s) was drawn",deck.size(), id, ctype(id)));
	if (deck.size()<1){
	    deck=shuffle();
	}
	return ctype(id);
    }

	/** get the bonus points for the card */
    public static int getBonus(Name name){
	if (name==Name.BONUS_300) return 300;
	if (name==Name.BONUS_400) return 400;
	if (name==Name.BONUS_500) return 500;
	if (name==Name.FILL_1000) return 1000;
	return 0;
    }

    /*
    public static String ctype(int id){
	if (id<12) return "Bonus 300";
	if (id<22) return "Bonus 400";
	if (id<30) return "Bonus 500";
	if (id<34) return "Must Bust";
	if (id<40) return "Fill 1000";
	if (id<44) return "Vengeance";
	if (id<52) return "No Dice";
	return "Double Trouble";
    }
    */
    private static Name ctype(int id){
	if (id<12) return Name.BONUS_300;
	if (id<22) return Name.BONUS_400;
	if (id<30) return Name.BONUS_500;
	if (id<34) return Name.MUST_BUST;
	if (id<40) return Name.FILL_1000;
	if (id<44) return Name.VENGEANCE;
	if (id<52) return Name.NO_DICE;
	return Name.DOUBLE_TROUBLE;
    }
}
