package ar.com.restba.exception;

public class RestBAException extends RuntimeException {

	private static final long serialVersionUID = 7979999937975490005L;

	public RestBAException(String message, Throwable t) {
		super(message, t);
	}

	public RestBAException(String message) {
		super(message);
	}

}
