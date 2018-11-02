package ie.ucd.engac.lifegamelogic;

public class TestSpinner implements Spinnable {
	private int numberToSpin;
	
	public TestSpinner(int numberToSpin) {
		this.numberToSpin = numberToSpin;
	}
	
	@Override	
	public int spinTheWheel() {		
		return numberToSpin;
	}
}
