package ie.ucd.engac.lifegamelogic;

import java.util.Random;

public class WheelSpin {
    private static final long randomSeed = 7777777777777777L;
    
    //rng for spinner
    private Random random;
    
    public WheelSpin(){
        random = new Random((randomSeed + System.nanoTime()));
    }
    
    public int spinTheWheel(){
        int temp = random.nextInt(9)+1;
        return temp;
    }

}
