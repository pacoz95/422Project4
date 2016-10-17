/* CRITTERS Critter4.java
 * EE422C Project 4 submission by
 * Paul Cozzi
 * pac2472
 * 16450
 * Mina Sucur
 * ms64973
 * 16450
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;
/**
 * Critter4
 * A type of carnivorous critter: it only fights things that are not algae (but it will fight algae if it has already walked)
 * As such, it wants to be very good at fighting, so it doesn't reproduce often
 * When it does reproduce, it does so before a fight
 */
public class Critter4 extends Critter {
	private boolean walked = false; //keeps track of whether or not the critter walked in this time step
							//this allows it to smartly choose whether or not to walk in a fight
	private int walkThreshold = 50; //how likely the Critter is to walk
	/**
	 * critter only walks in the timeStep, and critter will walk randomly according to its genes
	 * @param none
	 * @return none
	 */
	@Override
	public void doTimeStep() {
		if(Critter.getRandomInt(100) < walkThreshold){
			walk(Critter.getRandomInt(8));
			walked = true;
		}
	}
	
	/**
	 * Critter will want to fight only things that are not algae
	 */
	@Override
	public boolean fight(String oponent) {
		Critter4 baby = new Critter4();
		baby.walkThreshold = walkThreshold + Critter.getRandomInt(11) - 5;
		if(this.getEnergy() >= 10*Params.min_reproduce_energy){
			this.reproduce(baby, getRandomInt(8));
		}
		if(oponent.equals("@") && !walked){
			walk(Critter.getRandomInt(8));
			walked = false;
			return false;
		}
		walked = false;
		return true;
	}
	
	/**
	 * returns "4"
	 * @param none
	 * @return 4 as a string
	 */
	@Override
	public String toString(){
		return "4";
	}

}
