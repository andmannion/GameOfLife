package ie.ucd.engac.ui;

public enum UIState {
    Init,
    WaitingForSpin,
    PostSpin,
    CardChoice, //3 variants
    LargeChoice,
    Spin2WinPicking,
    Spin2WinRolling,
    BabyAcquisition,
    Wedding //babies and wedding may need two states
}
