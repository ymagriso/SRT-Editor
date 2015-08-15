package net.magriso.srt.editor.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * The Class UIPropertiesUtil.
 *
 * @author Yakir
 */
public class UIPropertiesUtil
{

	/** The Constant DEFAULT_LOCALE. */
	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

	/**
	 * Read ui properties.
	 *
	 * @param cls
	 *            the cls
	 * @param locale
	 *            the locale
	 * @return the properties
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Properties readUiProperties(Class<?> cls, Locale locale) throws FileNotFoundException, IOException
	{
		Locale currentLocale = locale;
		if(!isLocaleSupported(currentLocale))
		{
			currentLocale = DEFAULT_LOCALE;
		}

		String filename = cls.getSimpleName() + "_" + currentLocale.getLanguage() + ".properties";
		Properties properties = new Properties();

		try(InputStream inStream = cls.getResourceAsStream(filename);)
		{
			properties.load(inStream);
		}

		return properties;
	}

	/**
	 * Checks if is locale supported.
	 *
	 * @param locale
	 *            the locale
	 * @return true, if is locale supported
	 */
	private boolean isLocaleSupported(Locale locale)
	{
		List<Locale> locales = new ArrayList<Locale>();
		locales.add(DEFAULT_LOCALE);

		return locales.contains(locale);
	}
}
