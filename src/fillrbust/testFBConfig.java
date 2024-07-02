package fillrbust;

public class testFBConfig {

	public static void main(String[] args){
		FBConfig config = new FBConfig();

		System.out.println("The winning score is declared to be "+config.goal);
		System.out.println("There are "+config.players.length +" players:");
		for (String each: config.players) System.out.println("  "+each);
	}
}
