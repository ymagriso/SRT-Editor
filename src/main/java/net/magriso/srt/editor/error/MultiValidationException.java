package net.magriso.srt.editor.error;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MultiValidationException.
 *
 * @author Yakir
 */
public class MultiValidationException extends ValidationException
{

	/** The exceptions. */
	private List<ValidationException> exceptions = new ArrayList<>();

	/**
	 * Adds the.
	 *
	 * @param exception
	 *            the exception
	 */
	public void add(ValidationException exception)
	{
		exceptions.add(exception);
	}

	/**
	 * Gets the exceptions.
	 *
	 * @return the exceptions
	 */
	public List<ValidationException> getExceptions()
	{
		return exceptions;
	}
}
