package be.ipl.immo.exceptions;

@SuppressWarnings("serial")
public class BienInexistantException extends Exception {

	public BienInexistantException() {
	}

	public BienInexistantException(String message) {
		super(message);
	}

	public BienInexistantException(Throwable cause) {
		super(cause);
	}

	public BienInexistantException(String message, Throwable cause) {
		super(message, cause);
	}

}
