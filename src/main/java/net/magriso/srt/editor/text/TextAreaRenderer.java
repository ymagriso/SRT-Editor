package net.magriso.srt.editor.text;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 * The Class TextAreaRenderer.
 *
 * @author Yakir
 */
public class TextAreaRenderer extends JTextArea implements TableCellRenderer
{

	/**
	 * An empty <code>Border</code>. This field might not be used. To change the
	 * <code>Border</code> used by this renderer override the
	 * <code>getTableCellRendererComponent</code> method and set the border of
	 * the returned component directly.
	 */
	private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	/** The Constant DEFAULT_NO_FOCUS_BORDER. */
	private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

	/** The no focus border. */
	protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

	// We need a place to store the color the JTextArea should be returned
	// to after its foreground and background colors have been set
	// to the selection background color.
	// These ivars will be made protected when their names are finalized.
	/** The unselected foreground. */
	private Color unselectedForeground;

	/** The unselected background. */
	private Color unselectedBackground;

	/**
	 * Instantiates a new text area renderer.
	 */
	public TextAreaRenderer()
	{
		super();
		setLineWrap(true);
		setWrapStyleWord(true);
		setOpaque(true);
		setBorder(getNoFocusBorder());
		setName("Table.cellRenderer");
	}

	/**
	 * Gets the no focus border.
	 *
	 * @return the no focus border
	 */
	private Border getNoFocusBorder()
	{
		Border border = UIManager.getBorder("Table.cellNoFocusBorder");
		if(System.getSecurityManager() != null)
		{
			if(border != null)
			{
				return border;
			}
			return SAFE_NO_FOCUS_BORDER;
		}
		else if(border != null)
		{
			if(noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)
			{
				return border;
			}
		}
		return noFocusBorder;
	}

	/**
	 * Overrides <code>JComponent.setForeground</code> to assign the
	 * unselected-foreground color to the specified color.
	 *
	 * @param c
	 *            set the foreground color to this value
	 */
	@Override
	public void setForeground(Color c)
	{
		super.setForeground(c);
		unselectedForeground = c;
	}

	/**
	 * Overrides <code>JComponent.setBackground</code> to assign the
	 * unselected-background color to the specified color.
	 *
	 * @param c
	 *            set the background color to this value
	 */
	@Override
	public void setBackground(Color c)
	{
		super.setBackground(c);
		unselectedBackground = c;
	}

	/**
	 * Notification from the <code>UIManager</code> that the look and feel
	 * [L&amp;F] has changed. Replaces the current UI object with the latest
	 * version from the <code>UIManager</code>.
	 *
	 * @see JComponent#updateUI
	 */
	@Override
	public void updateUI()
	{
		super.updateUI();
		setForeground(null);
		setBackground(null);
	}

	/**
	 *
	 * Returns the default table cell renderer.
	 * <p>
	 * During a printing operation, this method will be called with
	 * <code>isSelected</code> and <code>hasFocus</code> values of
	 * <code>false</code> to prevent selection and focus from appearing in the
	 * printed output. To do other customization based on whether or not the
	 * table is being printed, check the return value from
	 * {@link javax.swing.JComponent#isPaintingForPrint()}.
	 *
	 * @param table
	 *            the <code>JTable</code>
	 * @param value
	 *            the value to assign to the cell at <code>[row, column]</code>
	 * @param isSelected
	 *            true if cell is selected
	 * @param hasFocus
	 *            true if cell has focus
	 * @param row
	 *            the row of the cell to render
	 * @param column
	 *            the column of the cell to render
	 * @return the default table cell renderer
	 * @see javax.swing.JComponent#isPaintingForPrint()
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		boolean localIsSelected = isSelected;

		if(table == null)
		{
			return this;
		}

		Color fg = null;
		Color bg = null;

		JTable.DropLocation dropLocation = table.getDropLocation();
		if(dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn()
				&& dropLocation.getRow() == row && dropLocation.getColumn() == column)
		{
			fg = UIManager.getColor("Table.dropCellForeground");
			bg = UIManager.getColor("Table.dropCellBackground");

			localIsSelected = true;
		}

		if(localIsSelected)
		{
			super.setForeground(fg == null ? table.getSelectionForeground() : fg);
			super.setBackground(bg == null ? table.getSelectionBackground() : bg);
		}
		else
		{
			Color background = unselectedBackground != null ? unselectedBackground : table.getBackground();
			if(background == null || background instanceof javax.swing.plaf.UIResource)
			{
				Color alternateColor = UIManager.getColor("Table.alternateRowColor");
				if(alternateColor != null && row % 2 != 0)
				{
					background = alternateColor;
				}
			}
			super.setForeground(unselectedForeground != null ? unselectedForeground : table.getForeground());
			super.setBackground(background);
		}

		setFont(table.getFont());

		if(hasFocus)
		{
			Border border = null;
			if(localIsSelected)
			{
				border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
			}
			if(border == null)
			{
				border = UIManager.getBorder("Table.focusCellHighlightBorder");
			}
			setBorder(border);

			if(!localIsSelected && table.isCellEditable(row, column))
			{
				Color col;
				col = UIManager.getColor("Table.focusCellForeground");
				if(col != null)
				{
					super.setForeground(col);
				}
				col = UIManager.getColor("Table.focusCellBackground");
				if(col != null)
				{
					super.setBackground(col);
				}
			}
		}
		else
		{
			setBorder(getNoFocusBorder());
		}

		setValue(value);

		return this;
	}

	/*
	 * The following methods are overridden as a performance measure to to prune
	 * code-paths are often called in the case of renders but which we know are
	 * unnecessary. Great care should be taken when writing your own renderer to
	 * weigh the benefits and drawbacks of overriding methods like these.
	 */

	/**
	 * Overridden for performance reasons. See the <a
	 * href="#override">Implementation Note</a> for more information.
	 */
	@Override
	public boolean isOpaque()
	{
		Color back = getBackground();
		Component p = getParent();
		if(p != null)
		{
			p = p.getParent();
		}

		// p should now be the JTable.
		boolean colorMatch = back != null && p != null && back.equals(p.getBackground()) && p.isOpaque();
		return !colorMatch && super.isOpaque();
	}

	/**
	 * Sets the <code>String</code> object for the cell being rendered to
	 * <code>value</code>.
	 *
	 * @param value
	 *            the string value for this cell; if value is <code>null</code>
	 *            it sets the text value to an empty string
	 * @see JLabel#setText
	 *
	 */
	protected void setValue(Object value)
	{
		setText(value == null ? "" : value.toString());
	}
}
