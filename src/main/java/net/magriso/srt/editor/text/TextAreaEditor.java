package net.magriso.srt.editor.text;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The Class TextAreaEditor.
 *
 * @author Yakir
 */
public class TextAreaEditor extends DefaultCellEditor
{

	/**
	 * Instantiates a new text area editor.
	 */
	public TextAreaEditor()
	{
		super(new JTextField());

		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(null);
		editorComponent = scrollPane;

		delegate = new DefaultCellEditor.EditorDelegate()
		{

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * javax.swing.DefaultCellEditor.EditorDelegate#setValue(java.lang
			 * .Object)
			 */
			@Override
			public void setValue(Object value)
			{
				textArea.setText(value != null ? value.toString() : "");
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * javax.swing.DefaultCellEditor.EditorDelegate#getCellEditorValue()
			 */
			@Override
			public Object getCellEditorValue()
			{
				return textArea.getText();
			}
		};
	}
}
