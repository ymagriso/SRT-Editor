package net.magriso.srt.editor.error;

/**
 * The Class ValidationException.
 *
 * @author Yakir
 */
public class ValidationException extends Exception
{

	/**
	 * Instantiates a new validation exception.
	 */
	public ValidationException()
	{
		super();
	}

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 * @param enableSuppression
	 *            the enable suppression
	 * @param writableStackTrace
	 *            the writable stack trace
	 */
	public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ValidationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param message
	 *            the message
	 */
	public ValidationException(String message)
	{
		super(message);
	}

	/**
	 * Instantiates a new validation exception.
	 *
	 * @param cause
	 *            the cause
	 */
	public ValidationException(Throwable cause)
	{
		super(cause);
	}
}
