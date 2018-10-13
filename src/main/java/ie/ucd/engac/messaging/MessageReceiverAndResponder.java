package ie.ucd.engac.messaging;

public interface MessageReceiverAndResponder<T> {	
	// The return value T is the response.
	T receiveMessage(T messageBeingSent);
}
