package org.codait.tf;

public class TFException extends RuntimeException {

	private static final long serialVersionUID = -6854658830095644446L;

	public TFException() {
		super();
	}

	public TFException(String message, Throwable cause) {
		super(message, cause);
	}

	public TFException(String message) {
		super(message);
	}

	public TFException(Throwable cause) {
		super(cause);
	}

}