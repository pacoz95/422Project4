package assignment4;

public class Critter2 extends Critter{

	//Critter only fights algae, runs instead of fighting
	
	private int dir;
	
	public Critter2() {
		dir = Critter.getRandomInt(8);
	}
	
	@Override
	public String toString() { return "2"; }
	
	public boolean fight(String opponent) { 
		if (opponent.equals("@")) {
			return true;
		}
		else {
			run(dir);
			return false;
		}
	}
	
	public void doTimeStep() {
		int x = Critter.getRandomInt(3);
		if (x == 1)
			walk(dir);
		Critter2 baby = new Critter2();
		reproduce(baby, baby.dir);
	}
}
