package fillrbust;

import java.util.Random;
import java.util.Map;


/** Computer controlled player
   to make choices based of an assignment of risk
*/
class AIPlayer extends Player {
    private int risker = 5;
    private int score = 0;
    private boolean isai;
    private Random rg = new Random();

    public AIPlayer(String name, int risker) {
    	super(name, 0);
	this.risker=risker;
	this.isai=true;
    }

    public AIPlayer(String name, int risker, int score) {
    	super(name, score);
	this.risker=risker;
	this.isai=true;
    }

    public String listResp(String risky, String conservative) {
        return "";
    }

    /** decide whether to roll or not
    */
    public double rollRisk(int ndice){
	//Use probability of a favorable roll
	double p= 1.- Math.pow(.66667, ndice);
	return p;
    }

	public int getRisker() {
		return risker;
	}

	public void setRisk(int risker){
		this.risker = risker;
	}

	public String ynResp(String risky, String conservative, Map rtype) {
	return ynResp(risky, conservative, rtype, false);
    }
    public String ynResp(String risky,String conservative) {
	return ynResp(risky,conservative,null,false);
    }
    public String ynResp() {
	return ynResp("y", "n", null, false);
    }
    /** risky or conservative = 1 or 0
    */
    public String ynResp(String risky, String conservative, Map rtype, boolean debug) {
	//import math
	//if testing:
	//    prompt='  respond risky =',risky,' or conservative =',conservative
	//    return int(raw_input(prompt))
	//# base number = 10
	int prob=10;
	int risker=this.risker;
	int nrisk=1;
	String dprint="player risk="+this.risker;
	/*
	if (debug){
	    System.out.println( "rtype in ynResp: "+type(rtype)+" "+rtype);
	}
	if (type(rtype) is dict){
	    if (rtype['name'] == 'roll'){
	        //# factor in number of dice for roll probability
	        double rprob= self.rollRisk(rtype['ndice'])*10;
		risker+=rprob;
		nrisk+=1;
		dprint+=" roll risk is "+rprob;
	        if (debug){
	            System.out.println(String.format(" prob of scoring with %d dice is %f",rtype['ndice'],rprob));
		}
	    }
	    if (rtype['name'] is not 'vengeance'){
	        //# factor in the points at risk
	        //# linear such that r(0)=10, r(1000)=0
	        double pprob=max(0.,10.-rtype['score']*.01);
	        risker+=pprob;
	        nrisk+=1;
	        dprint+=" point risk is "+pprob;
	    }
	    //# relative to the leaders score
	    //# quadratic with log of ratio player:leader such that
	    //#r(.1)=10, r(10)=10, r(1)=1
	    app=rtype['app'];
	    if (app.maxScore > 0 and app.pui[app.player].player.score>0) {
	        double rtio=Math.log10(float(app.pui[app.player].player.score)/float(app.maxScore));
	        nprob=9*rtio*rtio +1;
	    }else{
		nprob=3;
	    }
	    risker+=nprob;
	    nrisk+=1;
	    dprint+=" nearness to leader risk is "+nprob;
	    //# and how close leader is to winning
	    //#linear with ratio of leader to goal score
	    double cprob=(double)(app.maxScore)/(double)(app.goal.cget('text'))*10;
	    if (app.player in app.leadingPlayers){
	        cprob=10-cprob;
	    }
	    risker+=cprob;
	    nrisk+=1;
	    dprint+=" nearness to goal risk is "+cprob;
	    if (debug){
	        System.out.println( dprint);
	    }
	}
	*/
	//if (random.triangular(0,10,risker/nrisk)>5)
	if (probTri(0,10,risker/nrisk)>5) {
	    if (debug) System.out.println( "Respyn says "+risky);
	    return risky;
	}else{
	    if (debug) System.out.println( "Respyn says "+conservative);
	    return conservative;
	}
    }

    /** triangular probabilistic distribution
     *
     */
    private double probTri(int min, int max, int mid) {
	double x = rg.nextDouble()*(max-min) +(double)min;
	if (x<min) return 0.;
	if (x<mid) return (x-min)*(x-min)/(mid-min) +min;
	if (x<max) return max - (max-x)*(max-x)/(max-mid);
	return (double)max;
    }

	public String toString() {
		return "AIPlayer "+this.getName()+" accepts risk level "+risker+" and has "+this.score+" points.";
	}
}
