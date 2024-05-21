/*
test Cards class:
make a deck
pick the next card
cycle through a total of 54 cards
count how many are "no dice"
*/
package fillrbust;
//import fillrbust.Cards.getBonus;

class testCards {

    public static void main(String[] args) {
	Cards deck = new Cards();
	int countb3 = 0;
	int countb4 = 0;
	int countb5 = 0;
	int countmb = 0;
	int countf1 = 0;
	int countv = 0;
	int countnd = 0;
	int countdt = 0;
	Cards.Name type;

	for (int i=0;i<54;i++){
	    type = deck.draw();
	    System.out.println( type);
	    if (Cards.getBonus(type) >0) System.out.println(String.format(
			"    it gets a bonus of %d", Cards.getBonus(type)));
	    if (type==Cards.Name.BONUS_300) countb3++;
	    if (type==Cards.Name.BONUS_400) countb4++;
	    if (type==Cards.Name.BONUS_500) countb5++;
	    if (type==Cards.Name.MUST_BUST) countmb++;
	    if (type==Cards.Name.FILL_1000) countf1++;
	    if (type==Cards.Name.VENGEANCE) countv++;
	    if (type==Cards.Name.NO_DICE) countnd++;
	    if (type==Cards.Name.DOUBLE_TROUBLE) countdt++;
	}
	System.out.println(String.format("There are %d BONUS_300 cards",countb3));
	System.out.println(String.format("There are %d BONUS_400 cards",countb4));
	System.out.println(String.format("There are %d BONUS_500 cards",countb5));
	System.out.println(String.format("There are %d MUST_BUST cards",countmb));
	System.out.println(String.format("There are %d FILL_1000 cards",countf1));
	System.out.println(String.format("There are %d VENGEANCE cards",countv));
	System.out.println(String.format("There are %d No Dice cards",countnd));
	System.out.println(String.format("There are %d DOUBLE_TROUBLE cards",countdt));
	    type = deck.draw();
	    System.out.println( type);
    }
}
