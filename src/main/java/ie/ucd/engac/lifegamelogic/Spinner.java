package ie.ucd.engac.lifegamelogic;

import java.util.Random;

public final class Spinner implements Spinnable {	
	public static final int numPossibleValues = 10;
	
	public int spinTheWheel(){
    	long randomSeed = 7777777777777777L;
    	Random random = new Random((randomSeed + System.nanoTime()));
    	return random.nextInt(numPossibleValues - 1) + 1;
    }
	
	public static int spinWheel(){
    	long randomSeed = 7777777777777777L;
    	Random random = new Random((randomSeed + System.nanoTime()));
    	return random.nextInt(numPossibleValues - 1) + 1;
    }
}
