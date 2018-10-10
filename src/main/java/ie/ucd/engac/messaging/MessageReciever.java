package ie.ucd.engac.messaging;

public interface MessageReciever<T> {	
	// A receiver must return a message upon recieving one
	T receiveMessage(T receivedMessage);
}
