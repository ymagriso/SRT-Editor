package net.magriso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.JFrame;

import net.magriso.srt.editor.common.UIPropertiesUtil;

/**
 * The Class JFrameExt.
 * 
 * @author Yakir
 */
public abstract class JFrameExt extends JFrame
{

	/** The properties. */
	private Properties properties;

	/**
	 * Instantiates a new j frame ext.
	 *
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public JFrameExt() throws FileNotFoundException, IOException
	{
		UIPropertiesUtil uiPropertiesUtil = new UIPropertiesUtil();
		properties = uiPropertiesUtil.readUiProperties(getClass(), Locale.getDefault());

		initComponents();
	}

	/**
	 * Inits the components.
	 */
	protected abstract void initComponents();

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
