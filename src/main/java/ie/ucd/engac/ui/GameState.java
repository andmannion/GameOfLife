package ie.ucd.engac.ui;

public enum GameState {
    WaitingForSpin,
    PostSpin,
    CardChoice, //3 variants
    Spin2WinPicking,
    Spin2WinRolling,
    BabyAcquisition,
    Wedding; //babies and wedding may need two states
}
