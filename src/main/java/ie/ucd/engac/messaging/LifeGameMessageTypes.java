package ie.ucd.engac.messaging;

public enum LifeGameMessageTypes {
	StartupMessage,
	SpinRequest,
	SpinResponse,
	LargeDecisionRequest,
    LargeDecisionResponse,
	OptionDecisionRequest,
	OptionDecisionResponse,
	AckRequest,
	AckResponse,
	EndGameMessage,
	UIConfigMessage,
	SpinResult
}
