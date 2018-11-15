package ie.ucd.engac.lifegamelogic.playerlogic;

public enum PlayerColour {
    pink(1),
    blue(2),
    green(3),
    yellow(4);

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

