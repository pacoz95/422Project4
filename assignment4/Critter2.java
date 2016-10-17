/* CRITTERS <Critter2.java>
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

public class Critter2 extends Critter{

	//Critter only fights algae, runs instead of fighting
	
	private int dir;
	
	public Critter2() {
		dir = Critter.getRandomInt(8);
	}
	
	/*
	 * Sets the symbol of Critter2 on the board as "2"
	 */
	@Override
	public String toString() { return "2"; }
	
	/*
	 * Determines whether Critter2 will fight
	 * @param opponent is the symbol of the critter that Critter2 is being asked to fight
	 */
	@Override
	public boolean fight(String opponent) { 
		if (opponent.equals("@")) {
			return true;
		}
		else {
			run(dir);
			return false;
		}
	}
	
	/*
	 * Makes Critter2 walk sometimes and reproduce at every time step
	 */
	@Override
	public void doTimeStep() {
		int x = Critter.getRandomInt(3);
		if (x == 1)
			walk(dir);
		Critter2 baby = new Critter2();
		reproduce(baby, baby.dir);
	}
}
