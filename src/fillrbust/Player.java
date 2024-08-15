package fillrbust;

/** Player defined by age, score,
  and 'artificial-ness'
*/
class Player{
    private String name;
    private int score;
    private boolean isai;

    public Player(String name, int score){
	this.name=name;
	this.score=score;
	this.isai=false;
    }
    public Player(String name){
	this(name, 0);
    }

    /** update the player's score by the specified amount */
    public void update(int score){
	this.score+=score;
	if (this.score < 0) {
	    this.score=0;
	}
    }

    /** get the player's current score */
    public int getScore(){
	return score;
    }

    public boolean getAI() {
	return isai;
    }

    /** get the player's name */
    public String getName(){
	return name;
    }

    public void changeName(String name){
	this.name = name;
    }

	/** copy one player to another
	 *
	 */
	public void copy(Player from){
		this.score = from.score;
		this.isai = from.isai;
	}

	/** for testing purposes, set players total score
	 *
	 * @param sc
	 */
	public void tSetSc(int sc){
		score = sc;
	}
}
