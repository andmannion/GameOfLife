package ie.ucd.engac.lifegamelogic.gameboardlogic;

public class BoardLocation {
	private String locationIdentifier;
	
	public String getLocation() {
		return locationIdentifier;
	}
	
	public BoardLocation(String locationIdentifier) {
		this.locationIdentifier = locationIdentifier;
	}
}
