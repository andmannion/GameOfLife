package ie.ucd.engac.messaging;

public interface MessageReciever<T> {	
	T receiveMessage();
	
	void respondToMessage(T responseMessage);
}
