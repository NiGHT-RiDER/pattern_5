package be.ipl.immo.exceptions;

@SuppressWarnings("serial")
public class BienExistantException extends Exception {

	public BienExistantException() {
	}

	public BienExistantException(String message) {
		super(message);
	}

	public BienExistantException(Throwable cause) {
		super(cause);
	}

	public BienExistantException(String message, Throwable cause) {
		super(message, cause);
	}

}
