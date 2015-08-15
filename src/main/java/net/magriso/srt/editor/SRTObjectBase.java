package net.magriso.srt.editor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import net.magriso.srt.editor.common.UIPropertiesUtil;
import net.magriso.srt.editor.error.MultiValidationException;
import net.magriso.srt.editor.error.ValidationException;

/**
 * The Class SRTObjectBase.
 *
 * @author Yakir
 */
public abstract class SRTObjectBase
{

	/** The dirty. */
	private boolean dirty;

	/** The properties. */
	protected Properties properties;

	/**
	 * Instantiates a new SRT object base.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public SRTObjectBase() throws FileNotFoundException, IOException
	{
		dirty = false;
		UIPropertiesUtil uiPropertiesUtil = new UIPropertiesUtil();
		properties = uiPropertiesUtil.readUiProperties(getClass(), Locale.getDefault());
	}

	/**
	 * Checks if is dirty.
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty()
	{
		return dirty;
	}

	/**
	 * Mark as dirty.
	 */
	public void markAsDirty()
	{
		dirty = true;
	}

	/**
	 * Mark as clean.
	 */
	public void markAsClean()
	{
		dirty = false;
	}

	/**
	 * Adds the milliseconds.
	 *
	 * @param ms
	 *            the ms
	 */
	public abstract void add(long ms);

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public abstract int hashCode();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public abstract boolean equals(Object obj);

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();

	/**
	 * Validate.
	 *
	 * @throws MultiValidationException
	 *             the multi validation exception
	 */
	public abstract void validate() throws MultiValidationException;

	/**
	 * Adds the error.
	 *
	 * @param mve
	 *            the exception
	 * @param message
	 *            the message
	 * @return the multi validation exception
	 */
	protected MultiValidationException addError(MultiValidationException mve, String message)
	{
		MultiValidationException multiValidationException = mve;
		if(multiValidationException == null)
		{
			multiValidationException = new MultiValidationException();
		}

		multiValidationException.add(new ValidationException(message));

		return multiValidationException;
	}

	/**
	 * Adds the error.
	 *
	 * @param mve
	 *            the mve
	 * @param ve
	 *            the ve
	 * @return the multi validation exception
	 */
	protected MultiValidationException addError(MultiValidationException mve, ValidationException ve)
	{
		MultiValidationException multiValidationException = mve;
		if(multiValidationException == null)
		{
			multiValidationException = new MultiValidationException();
		}

		multiValidationException.add(ve);

		return multiValidationException;
	}

	/**
	 * Gets the string.
	 *
	 * @param property
	 *            the property
	 * @return the string
	 */
	public String getString(String property)
	{
		return properties.getProperty(property);
	}
}
