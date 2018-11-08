package ie.ucd.engac.lifegamelogic.playerlogic;

public enum MaritalStatus {
	Single(0),
	Married(1);
	private int value;

    MaritalStatus(int Value) {
		this.value = Value;
	}
	private int getValue() {
		return value;
	}
	public static MaritalStatus fromInt(int statusNumber) {
		for (MaritalStatus sn : MaritalStatus .values()) {
			if (sn.getValue() == statusNumber) { return sn; }
		}
		return null;
	}
	public int toInt(){
        return this.value;
    }
}
