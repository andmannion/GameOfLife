package ie.ucd.engac.lifegamelogic;

import java.util.Random;

public final class Spinner {	
	public static int spinTheWheel(){
    	long randomSeed = 7777777777777777L;
    	Random random = new Random((randomSeed + System.nanoTime()));
    	return random.nextInt(9)+1;
    }
}
