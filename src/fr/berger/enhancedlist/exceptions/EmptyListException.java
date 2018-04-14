package fr.berger.enhancedlist.exceptions;

public class EmptyListException extends RuntimeException {
	
	public EmptyListException(String message, Exception innerException) {
		super(message, innerException);
	}
	public EmptyListException(String message) {
		super(message);
	}
	public EmptyListException() {
		super("The list is empty");
	}
}
