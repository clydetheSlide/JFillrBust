/*
 * Test the interaction of the AIPlayer in the FillRBustGame
 */
package fillrbust;

import java.util.*;

class testProbAI {

	static Random randy = new Random(new Date().getTime());

	public static void main(String[] args) {
		boolean debug = false;
		String[] list = {"aiPlayer1"};
		int max = 2000;
		if (args.length >0){
		    list=new String[]{args[0]};
		}
		FillRBustGame it = new FillRBustGame(list, max);
		Player pp = it.getPlayer();
		AIPlayer p = (AIPlayer) pp;
		it.setState(FillRBustGame.STATES.ROLLED,false,false,false,false,3);
		System.out.println("set game and State");

		// get samples, compute prob
		int nNo = 0;
		int nit;
		for(nit=0;nit<4000;nit++) {
			it.tSetND(nit%6);
			it.tSetTR(randy.nextInt(700),randy.nextInt(1400));
			p.tSetSc(randy.nextInt(max));
		    if (it.aiResponse(p,0).equals("b"))nNo++;
		}
		System.out.println(String.format("Chose B %d out of %d",nNo,nit));

		it.setState(FillRBustGame.STATES.FILLED,false,false,false,false);
		//it.debug = true;
		it.tSetTR(300,400);
		System.out.println("set game and State");

		// get samples, compute prob
		nNo = 0;
		for(nit=0;nit<4000;nit++) {
			p.tSetSc(randy.nextInt(max));
		    if (it.aiResponse(p,0).equals("b"))nNo++;
		}
		System.out.println(String.format("Chose B %d out of %d",nNo,nit));

		//it.debug = true;
		it.setState(FillRBustGame.STATES.DREWVENGEANCE,false,false,false,false);
		System.out.println("set game and State");

		// get samples, compute prob
		nNo = 0;
		for(nit=0;nit<4000;nit++) {
			p.tSetSc(randy.nextInt(max));
		    if (it.aiResponse(p,0).equals("b"))nNo++;
		}
		System.out.println(String.format("Chose B %d out of %d",nNo,nit));
	}
}
