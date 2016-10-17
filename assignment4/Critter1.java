/* CRITTERS <Critter1.java>
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

//Critter1 always fights, but reproduces twice before it does
public class Critter1 extends Critter{
	
	private int dir;
	
	public Critter1() {
		dir = Critter.getRandomInt(8);
	}
	
	/*
	 * Sets the symbol of Critter1 on the board as "1"
	 */
	@Override
	public String toString() { return "1"; }
	
	/**
	 * Determines whether Critter1 will fight
	 * @param opponent is the symbol of the critter that Critter1 is being asked to fight
	 */
	@Override
	public boolean fight(String opponent) { 
		Critter1 baby1 = new Critter1();
		Critter1 baby2 = new Critter1();
		reproduce(baby1, baby1.dir);
		reproduce(baby2, baby2.dir);
		return true;
	}
	
	/*
	 * Makes Critter1 walk in a random direction for every time step
	 */
	@Override
	public void doTimeStep() {
		walk(dir);
	}
}
