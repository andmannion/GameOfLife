package ie.ucd.engac.lifegamelogic;

import org.jetbrains.annotations.TestOnly;

public class TestSpinner implements Spinnable {
	private int numberToSpin;

	@TestOnly
	public TestSpinner(int numberToSpin) {
		this.numberToSpin = numberToSpin;
	}

	@TestOnly
	@Override	
	public int spinTheWheel() {		
		return numberToSpin;
	}
}
