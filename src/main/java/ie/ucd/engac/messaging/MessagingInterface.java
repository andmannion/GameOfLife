package ie.ucd.engac.messaging;

public class MessagingInterface<T> {
	private MessageReceiverAndResponder<T> messageReceiverAndResponder;
	
	// At the moment only deal with one sender/acceptor and one receiver/responder
	public MessagingInterface(MessageReceiverAndResponder<T> messageRecieverAndResponder) {
		this.messageReceiverAndResponder = messageRecieverAndResponder;
	}
	
	public T sendMessageAcceptResponse(T messageToSend) {
		return messageReceiverAndResponder.receiveMessage(messageToSend);
	}
}
