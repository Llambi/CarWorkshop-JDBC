package uo.ri.persistence.exception;

public class PersistanceException extends Exception {
	private static final long serialVersionUID = -308694287126138961L;

	public PersistanceException() {
	}

	public PersistanceException(String message) {
		super(message);
	}

	public PersistanceException(Throwable cause) {
		super(cause);
	}

	public PersistanceException(String message, Throwable cause) {
		super(message, cause);
	}

}
