/* CRITTERS <Critter3.java>
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

/*Critter3 is a trapping critter
 * it does not move during timeSteps
 * it will always elect to fight, except when ecountered with a Critter3, when it will walk away
 * it will always reproduce in doTimeStep if it has enough energy (above its own genetic energy limit)
 */

public class Critter3 extends Critter {
	private int reproduceLimit = Params.min_reproduce_energy * 5;
	/**
	 * reproduces if over the energy limit for this specific critter
	 * @param none
	 * @return none
	 */
	@Override
	public void doTimeStep() {
		//want to be a good fighter, reproduce in timestep only if over the limit
		if(getEnergy() >= reproduceLimit){
			Critter3 baby = new Critter3();
			baby.reproduceLimit = reproduceLimit + getRandomInt(11) - 5; //mutate the reproduce limit
			this.reproduce(baby, Critter.getRandomInt(8));
		}
		
	}
	/**
	 * will fight anything but Critter3
	 * @param none
	 * @return true if critter3, false otherwise
	 */
	@Override
	public boolean fight(String oponent) {
		//Critter3 is a fighter
		if(oponent.equals("3")){
			walk(Critter.getRandomInt(8));
			return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		return "3";
	}

}
