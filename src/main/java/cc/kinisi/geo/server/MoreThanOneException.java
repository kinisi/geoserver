package cc.kinisi.geo.server;

public class MoreThanOneException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MoreThanOneException() {
		super();
	}

	public MoreThanOneException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MoreThanOneException(String message, Throwable cause) {
		super(message, cause);
	}

	public MoreThanOneException(String message) {
		super(message);
	}

	public MoreThanOneException(Throwable cause) {
		super(cause);
	}

}
