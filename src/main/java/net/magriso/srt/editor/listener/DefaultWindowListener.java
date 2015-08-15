package net.magriso.srt.editor.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import net.magriso.srt.editor.common.Closable;

/**
 * The listener interface for receiving defaultWindow events. The class that is
 * interested in processing a defaultWindow event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addDefaultWindowListener<code> method. When
 * the defaultWindow event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DefaultWindowEvent
 */
public class DefaultWindowListener implements WindowListener
{

	/** The closeable. */
	private Closable closeable;

	/**
	 * Instantiates a new default window listener.
	 *
	 * @param closeable
	 *            the closeable
	 */
	public DefaultWindowListener(Closable closeable)
	{
		this.closeable = closeable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent )
	 */
	@Override
	public void windowOpened(WindowEvent event)
	{
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.
	 * WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent event)
	{
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event
	 * .WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent event)
	{
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event
	 * .WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent event)
	{
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.
	 * WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent event)
	{
		closeable.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent )
	 */
	@Override
	public void windowClosed(WindowEvent event)
	{
		// Do nothing...
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.
	 * WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent event)
	{
		// Do nothing...
	}
}