package net.magriso.srt.editor.listener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The listener interface for receiving singleSelectSelection events. The class
 * that is interested in processing a singleSelectSelection event implements
 * this interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addSingleSelectSelectionListener<code> method. When
 * the singleSelectSelection event occurs, that object's appropriate
 * method is invoked.
 *
 * @see SingleSelectSelectionEvent
 */
public class SingleSelectSelectionListener implements ListSelectionListener
{

	/** The txt selected. */
	private JTextField txtSelected;

	/** The srt object table. */
	private JTable srtObjectTable;

	/**
	 * Instantiates a new single select selection listener.
	 *
	 * @param txtSelected
	 *            the txt selected
	 * @param srtObjectTable
	 *            the srt object table
	 */
	public SingleSelectSelectionListener(JTextField txtSelected, JTable srtObjectTable)
	{
		this.txtSelected = txtSelected;
		this.srtObjectTable = srtObjectTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.
	 * event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		int leadSelectionIndex = ((DefaultListSelectionModel) e.getSource()).getLeadSelectionIndex();
		if(leadSelectionIndex != -1)
		{
			txtSelected.setText(srtObjectTable.getValueAt(leadSelectionIndex, 0).toString());
		}
	}
}