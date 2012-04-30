package ar.com.restba.exception;

public class RestBANetworkException extends RestBAException {

	private static final long serialVersionUID = 3589595115893734452L;

	public RestBANetworkException(String message, Throwable t) {
		super(message, t);
	}

	public RestBANetworkException(String message) {
		super(message);
	}

}
