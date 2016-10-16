/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

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
	
	protected final void walk(int direction) {
		switch (direction){
		case 0: x_coord++;
			break;
		case 1: x_coord++; y_coord--;
			break;
		case 2: y_coord--;
			break;
		case 3: x_coord--; y_coord--;
			break;
		case 4: x_coord--;
			break;
		case 5: x_coord--; y_coord++;
			break;
		case 6: y_coord++;
			break;
		case 7: x_coord++; y_coord++;
			break;
		}
		if(x_coord < 0){
			x_coord = Params.world_width - 1;
		}
		if(y_coord < 0){
			y_coord = Params.world_height - 1;
		}
		x_coord %= Params.world_width;
		y_coord %= Params.world_height;
		energy -= Params.walk_energy_cost;
		
	}
	
	protected final void run(int direction) {
		switch (direction){
		case 0: x_coord+=2;
			break;
		case 1: x_coord+=2; y_coord-=2;
			break;
		case 2: y_coord-=2;
			break;
		case 3: x_coord-=2; y_coord-=2;
			break;
		case 4: x_coord-=2;
			break;
		case 5: x_coord-=2; y_coord+=2;
			break;
		case 6: y_coord+=2;
			break;
		case 7: x_coord+=2; y_coord+=2;
			break;
		}
		if(x_coord < 0){
			x_coord = Params.world_width + x_coord;
		}
		if(y_coord < 0){
			y_coord = Params.world_height + y_coord;
		}
		
		x_coord %= Params.world_width;
		y_coord %= Params.world_height;
		
		energy -= Params.run_energy_cost;
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
		offspring.walk(direction);
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
			throw new InvalidCritterException(critter_class_name + " does not exist.");
		}
		if (!(newCritter instanceof Critter)) {
			throw new InvalidCritterException(critter_class_name + " is not a Critter.");
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
	//TODO implement this function
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
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
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
	 * doEncounters
	 * using population, utilizes the world grid to resolve all encounters
	 * modifies the world grid in a way that does not break the rules
	 * @param none
	 * @return none
	 */
	private static void doEncounters(){
		
		clearWorldGrid(); //start fresh
		for(int i = 0; i < population.size(); ++i){
			Critter critter1 = population.get(i);
			if(critter1.energy <= 0){
				population.remove(i);
				continue;
			}
			int x = critter1.x_coord;
			int y = critter1.y_coord;
			if(world[x][y] == null || world[x][y].energy == 0){	//no encounter
				world[x][y] = critter1;
			}
			else{	//already occupied
				Critter critter2 = world[x][y];
				boolean is1Fight = critter1.fight(critter2.toString());
				boolean is2Fight = critter2.fight(critter1.toString());
				//check if they ran
				
				//correct for if they ran to the same position
				if(critter2.x_coord != x || critter2.y_coord != y){ //TODO, fix bug caused by clearing grid
					//critter2 ran, it gets the new location, critter1 cannot run there
					if(critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord){
						critter1.x_coord = x;
						critter1.y_coord = y;
					}
				}
				//if they're still in the same position, see how they fight
				if(critter1.x_coord == critter2.x_coord && critter1.y_coord == critter2.y_coord){
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
				if(critter2.energy > 0){
					world[critter2.x_coord][critter2.y_coord] = critter2;
				}
			}
		}
	}
	
	
	public static void worldTimeStep() {
		//all doTimeStep
		for(int i = 0; i < population.size(); ++i){
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
				System.out.println("Cannot add Algae.");
			}
		}
	}
	
	private static void updateWorldGrid() {
		//Remove dead critters from population
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).energy <= 0)
				population.remove(i);
		}
		//Update world array with new positions
		clearWorldGrid();
		for (int i = 0; i < population.size(); i++) {
			world[population.get(i).x_coord][population.get(i).y_coord] = population.get(i);
		}
	}
}
