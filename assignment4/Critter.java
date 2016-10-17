/* CRITTERS <Critter.java>
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

import java.util.Iterator;
import java.util.List;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Critter[][] world = new Critter[Params.world_width][Params.world_height];
	private static java.util.HashMap<Critter, Boolean> hasMoved = new java.util.HashMap<Critter, Boolean>(); //keeps track of when critters moved
	private static boolean isFighting = false;
	private static int[][] worldEncounters = new int[Params.world_width][Params.world_height]; //used to tall where critters exist in the world during encounters
	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	//for walk and run prevent already-moved critters from moving
	protected final void walk(int direction) {
		energy -= Params.walk_energy_cost;
		Boolean check = hasMoved.get(this);
		if(((check != null) && check) || energy <= 0){
			return;
		}
		hasMoved.put(this, true);
		int x = x_coord;
		int y = y_coord;
		switch (direction){
		case 0: x++;
			break;
		case 1: x++; y--;
			break;
		case 2: y--;
			break;
		case 3: x--; y--;
			break;
		case 4: x--;
			break;
		case 5: x--; y++;
			break;
		case 6: y++;
			break;
		case 7: x++; y++;
			break;
		}
		//wrap around
		if(x < 0){
			x = Params.world_width + x;
		}
		if(y < 0){
			y = Params.world_height + y;
		}
		x %= Params.world_width;
		y %= Params.world_height;

		//prevent walking into another critter during fights
		if(isFighting && worldEncounters[x][y] != 0){
			return;
		}
		//update encounters grid
		worldEncounters[x_coord][y_coord] -= 1;
		x_coord = x;
		y_coord = y;
		worldEncounters[x_coord][y_coord] += 1;
		
	}
	
	protected final void run(int direction) {
		energy -= Params.run_energy_cost;
		//prevent multiple movements in a timestep
		Boolean check = hasMoved.get(this);
		if(((check != null) && check) || energy <= 0){
			return;
		}
		hasMoved.put(this, true);
		int x = x_coord;
		int y = y_coord;
		switch (direction){
		case 0: x+=2;
			break;
		case 1: x+=2; y-=2;
			break;
		case 2: y-=2;
			break;
		case 3: x-=2; y-=2;
			break;
		case 4: x-=2;
			break;
		case 5: x-=2; y+=2;
			break;
		case 6: y+=2;
			break;
		case 7: x+=2; y+=2;
			break;
		}
		//wrap around
		if(x < 0){
			x = Params.world_width + x;
		}
		if(y < 0){
			y = Params.world_height + y;
		}
		
		x %= Params.world_width;
		y %= Params.world_height;

		//prevent walking into another critter during fights
		if(isFighting && worldEncounters[x][y] != 0){
			return;
		}
		//update encounters grid
		worldEncounters[x_coord][y_coord] -= 1;
		x_coord = x;
		y_coord = y;
		worldEncounters[x_coord][y_coord] += 1;
		
	}
	
	protected final void reproduce(Critter offspring, int direction) {
		//Check if parents has minimum reproduce energy
		if (this.energy < Params.min_reproduce_energy) {
			return;
		}
		//Reassign parent and child energy
		offspring.energy = this.energy/2;
		this.energy = (this.energy+1)/2;
		//Set x and y coordinates and call walk
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		
		//enable walking into other critters
		boolean wasFighting = isFighting;
		isFighting = false;
		offspring.walk(direction);
		isFighting = wasFighting;
		//if in the fight, disable walking into other critters
		//PUT WALK ENERGY BACK!!
		offspring.energy += Params.walk_energy_cost;
		//Add offspring to babies
		babies.add(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		//create the critter, catch exceptions
		Critter newCritter;
		try {
			newCritter = (Critter) Class.forName(myPackage + "." + critter_class_name).newInstance();
		}
		catch (Throwable e) {
			throw new InvalidCritterException(critter_class_name);
		}
		if (!(newCritter instanceof Critter)) {
			throw new InvalidCritterException(critter_class_name);
		}
		//set coordinates
		newCritter.x_coord = getRandomInt(Params.world_width);
		newCritter.y_coord = getRandomInt(Params.world_height);
		//set energy
		newCritter.energy = Params.start_energy;
		//add to world
		population.add(newCritter);
		//world is updated each timestep
		//world[newCritter.x_coord][newCritter.y_coord] = newCritter;
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		for (int i = 0; i < population.size(); i++) {
			try {
				if (Class.forName(myPackage + "." + critter_class_name).isInstance(population.get(i)))
						result.add(population.get(i));
			}
			catch (Throwable e) {
				throw new InvalidCritterException(critter_class_name);
			}
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
			updateWorldGrid();
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
			updateWorldGrid();
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
			updateWorldGrid();
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/**
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 * @return list of critters
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 * @param none
	 * @return none
	 */
	public static void clearWorld() {
		babies.clear();
		population.clear();
		clearWorldGrid();
	}
	
	/**
	 * Clear the world grid of all critters, dead and alive
	 * Useful for repopulating the world grid
	 * @param none
	 * @return none
	 */
	private static void clearWorldGrid() {
		for(int x = 0; x < Params.world_width; ++x){
			for(int y = 0; y < Params.world_height; ++y){
				world[x][y] = null;
			}
		}
	}
	/**
	 * Set the world encounters grid (keeps track of how many critters are in each spot)
	 */
	private static void setWorldEncounters(){
		for(int x = 0; x < Params.world_width; ++x){
			for(int y = 0; y < Params.world_height; ++y){
				worldEncounters[x][y] = 0;
			}
		}
		for(int i = 0; i < population.size(); ++i){
			Critter temp = population.get(i);
			if(temp.energy > 0){
				worldEncounters[temp.x_coord][temp.y_coord] += 1;
			}
		}
	}
	/** 
	 * doEncounters
	 * using population, utilizes the world grid to resolve all encounters
	 * modifies the world grid in a way that does not break the rules
	 * @param none
	 * @return none
	 */
	private static void doEncounters(){
		isFighting = true;
		setWorldEncounters();
		clearWorldGrid(); //start fresh
		for(int i = 0; i < population.size(); ++i){
			Critter critter1 = population.get(i);
			if(critter1.energy <= 0){
				continue;
			}
			int x = critter1.x_coord;
			int y = critter1.y_coord;
			if(world[x][y] == null || world[x][y].energy <= 0){	//no encounter
				world[x][y] = critter1;
			}
			else{	//already occupied
				Critter critter2 = world[x][y];
				boolean is1Fight = critter1.fight(critter2.toString());
				boolean is2Fight = critter2.fight(critter1.toString());
				//check if they ran
				//correct for if they ran to the same position (obsolete, run and walk keep track of this, and the fight method prevent going into occupied territory)
//				if((critter2.x_coord != x || critter2.y_coord != y) && critter2.energy > 0){ //fixed bug caused by clearing grid
//				//critter2 ran, it gets the new location, critter1 cannot run there
//					if(critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord){
//						critter1.x_coord = x;
//						critter1.y_coord = y;
//					}
//				}
				//if they're still in the same position, and neither has died, see how they fight
				    if(critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord && critter1.energy > 0 && critter2.energy > 0){
					int diceRoll1;
					int diceRoll2;
					if(!is1Fight){
						diceRoll1 = 0;
					}
					else{
						diceRoll1 = getRandomInt(critter1.energy + 1);
					}
					if(!is2Fight){
						diceRoll2 = 0;
					}
					else{
						diceRoll2 = getRandomInt(critter2.energy + 1);
					}
					
					//FIGHT!
					if(diceRoll1 > diceRoll2){
						critter1.energy += critter2.energy / 2;
						critter2.energy = 0;
					}
					else{
						critter2.energy += critter1.energy / 2;
						critter1.energy = 0;
					}	
				}
				if(critter1.energy > 0){
					world[critter1.x_coord][critter1.y_coord] = critter1;
				}
				else{
					worldEncounters[critter1.x_coord][critter1.y_coord] -= 1;
				}
				if(critter2.energy > 0){
					world[critter2.x_coord][critter2.y_coord] = critter2;
				}
				else{
					worldEncounters[critter2.x_coord][critter2.y_coord] -= 1;
				}
			}
		}
		
		Iterator<Critter> it = population.iterator();
		//cull dead critters
		while(it.hasNext()){
			Critter crit = it.next();
			if(crit.energy <= 0){
				it.remove();
			}
		}
		isFighting = false;
	}
	
	
	public static void worldTimeStep() {
		updateWorldGrid(); //This is to keep track of where collisions in the world are
		hasMoved.clear();
		//all doTimeStep
		for(int i = 0; i < population.size(); ++i){
			hasMoved.put(population.get(i), false);
			population.get(i).doTimeStep();
		}
		//do encounters
		doEncounters();
		//update rest energy
		for(int i = 0; i < population.size(); ++i){
			population.get(i).energy -= Params.rest_energy_cost;
		}
		//Add algae
		addAlgae();
		//Add babies to population
		for (int i = 0; i < babies.size(); i++) {
			population.add(babies.get(i));
		}
		//Clear all babies
		babies.clear();
		//Update the world grid
		updateWorldGrid();
	}
	
	/** 
	 * displayWorld
	 * a rudimentary display of the critter world
	 * prints to stdout
	 * requires that world grid is updated
	 * @param none
	 * @return none
	 */
	public static void displayWorld() {
		updateWorldGrid();
		//top row
		System.out.print('+');
		for(int x = 0; x < Params.world_width; ++x){
			System.out.print('-');
		}
		System.out.println('+');
		
		//body
		for(int y = 0; y < Params.world_height; ++y){
			System.out.print('|');
			for(int x = 0; x < Params.world_width; ++x){
				if(world[x][y] == null){
					System.out.print(' ');
				}
				else{
					System.out.print(world[x][y].toString());
				}
			}
			System.out.println('|');
		}
		//bottom row
		System.out.print('+');
		for(int x = 0; x < Params.world_width; ++x){
			System.out.print('-');
		}
		System.out.println('+');
	}
	
	private static void addAlgae() {
		for (int i=0; i<Params.refresh_algae_count; i++) {
			try {
				makeCritter("Algae");
			}
			catch (InvalidCritterException e) {
			}
		}
	}
	
	private static void updateWorldGrid() {
		//Remove dead critters from population
		Iterator<Critter> it = population.iterator();
		
		//cull dead critters
		while(it.hasNext()){
			Critter crit = it.next();
			if(crit.energy <= 0){
				it.remove();
			}
		}
		//Update world array with new positions
		clearWorldGrid();
		for (int i = 0; i < population.size(); i++) {
			world[population.get(i).x_coord][population.get(i).y_coord] = population.get(i);
		}
		setWorldEncounters();
	}
}
