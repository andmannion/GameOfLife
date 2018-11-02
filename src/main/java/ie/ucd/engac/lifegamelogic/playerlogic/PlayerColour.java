package ie.ucd.engac.lifegamelogic.playerlogic;

public enum PlayerColour {
    pink(0),
    blue(1),
    green(2),
    yellow(3);

    private int value;

    PlayerColour(int Value) {
        this.value = Value;
    }
    private int getValue() {
        return value;
    }
    public static PlayerColour fromInt(int colourNumber) {
        for (PlayerColour pc : PlayerColour .values()) {
            if (pc.getValue() == colourNumber-1) { return pc; }
        }
        return null;
    }
}

