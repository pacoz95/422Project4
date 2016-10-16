/* CRITTERS Main.java
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
package assignment4; // cannot be in default package
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.lang.reflect.Method;
/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        while(true){
        //prompt user
        	System.out.print("critters> ");
        //get the next command
        	String arg = kb.nextLine();
        	String[] cmd = arg.split("\\s+");
        	int status = 0;		//will be 1 if a parse error occurred
        	if(cmd.length < 1){ //blank
        		System.out.println("invalid command: " + arg);
        	}
        	else if(cmd[0].equals("quit")){
        		status = handleQuit(cmd);
        	}
        	// show
        	else if(cmd[0].equals("show")){
        		status = handleShow(cmd);
        	}
        	// step (or step <count>)
        	else if(cmd[0].equals("step")){
        		status = handleStep(cmd);
        	}
        	// seed
        	else if(cmd[0].equals("seed")){
        		status = handleSeed(cmd);
        	}
        	//TODO make classname (or make classname <count>
        	else if(cmd[0].equals("make")){
        		status = handleMake(cmd);
        	}
        	//TODO stats classname
        	else if(cmd[0].equals("stats")){
        		status = handleStats(cmd);
        	}
        	//invalid command
        	else{
        		System.out.println("invalid command: " + arg);
        	}
        	if(status == 1){
        		System.out.println("error processing: " + arg);
        	}
        }
        
        /* Write your code above */
//       System.out.flush();

    }
    /**
     * handles the quit command, assumes the first argument is quit
     * @param cmd the string for the entire command that was given (including quit)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleQuit(String[] cmd){
    	if(cmd.length == 1){
    		System.exit(0);
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    /**
     * handles the show command,assumes the first argument is show
     * @param cmd the string for the entire command that was given (including show)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleShow(String[] cmd){
    	if(cmd.length == 1){
    		Critter.displayWorld();
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    /**
     * handles the step command,assumes the first argument is step
     * @param cmd the string for the entire command that was given (including step)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleStep(String[] cmd){
    
    	if(cmd.length == 1){
    		Critter.worldTimeStep();
    		return 0;
    	}
    	else if(cmd.length == 2){
    		try{
    			int numSteps = Integer.parseInt(cmd[1]);
    			if(numSteps < 0){
    				return 1;
    			}
    			for(int i = 0; i < numSteps; ++i){
    				Critter.worldTimeStep();
    			}
    		}
    		catch(Exception e){
    			return 1;
    		}
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    /**
     * handles the seed command,assumes the first argument is seed
     * @param cmd the string for the entire command that was given (including seed)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleSeed(String[] cmd){
    	if(cmd.length == 2){
    		try{
    			Critter.setSeed(Long.parseLong(cmd[1]));
    		}
    		catch(Exception e){
    			return 1;
    		}
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    /**
     * handles the make command,assumes the first argument is make
     * @param cmd the string for the entire command that was given (including make)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleMake(String[] cmd){
    	if(cmd.length == 2){
    		try{
    			Critter.makeCritter(cmd[1]);
    		}
    		catch(Exception e){
    			return 1;
    		}
    	}
    	else if (cmd.length == 3){
    		try{
    			int numTimes = Integer.parseInt(cmd[2]);
    			for(int i = 0; i < numTimes; ++i){
    				Critter.makeCritter(cmd[1]);
    			}
    		}
    		catch(Exception e){
    			return 1;
    		}
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    /**TODO
     * handles the stats command,assumes the first argument is stats
     * @param cmd the string for the entire command that was given (including stats)
     * @return 0 if parse worked, 1 if there was a problem
     */
    private static int handleStats(String[] cmd){
    	if(cmd.length == 2){
    		try{
    			Critter.getInstances(cmd[1]);
    			Method meth =  Class.forName(myPackage + "." + cmd[1]).getMethod("runStats", String.class);
    			meth.invoke(null, "DontCare");
    		}
    		catch(Exception e){
    			return 1;
    		}
    	}
    	else{
    		return 1;
    	}
    	return 0;
    }
    
}
