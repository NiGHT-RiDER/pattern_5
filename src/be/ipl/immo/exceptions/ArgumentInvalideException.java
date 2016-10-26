package be.ipl.immo.exceptions;

@SuppressWarnings("serial")
public class ArgumentInvalideException extends Exception {

	public ArgumentInvalideException() {
	}

	public ArgumentInvalideException(String message) {
		super(message);
	}

	public ArgumentInvalideException(Throwable cause) {
		super(cause);
	}

	public ArgumentInvalideException(String message, Throwable cause) {
		super(message, cause);
	}

}
