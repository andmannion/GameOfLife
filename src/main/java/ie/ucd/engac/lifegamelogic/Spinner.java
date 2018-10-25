package ie.ucd.engac.lifegamelogic;

import java.util.Random;

public final class Spinner {	
	public static final int numPossibleValues = 10;
	
	public static int spinTheWheel(){
    	long randomSeed = 7777777777777777L;
    	Random random = new Random((randomSeed + System.nanoTime()));
    	return random.nextInt(numPossibleValues - 1)+1;
    }
}
