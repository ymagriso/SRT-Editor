package net.magriso.srt.editor.text;

import javax.swing.JTextField;

/**
 * The Class PaginationTextField.
 *
 * @author Yakir
 */
public class PaginationTextField extends JTextField
{

	/** The Constant DEFAULT_PAGE_SIZE. */
	public static final int DEFAULT_PAGE_SIZE = 200;

	/** The page size. */
	private int pageSize;

	/** The num of entries. */
	private int numOfEntries;

	/** The num of pages. */
	private int numOfPages;

	/** The current page num. */
	private int currentPageNum;

	/**
	 * Instantiates a new pagination text field.
	 */
	public PaginationTextField()
	{
		this(0);
	}

	/**
	 * Instantiates a new pagination text field.
	 *
	 * @param numOfEntries
	 *            the num of entries
	 */
	public PaginationTextField(int numOfEntries)
	{
		this(numOfEntries, DEFAULT_PAGE_SIZE);
	}

	/**
	 * Instantiates a new pagination text field.
	 *
	 * @param numOfEntries
	 *            the num of entries
	 * @param pageSize
	 *            the page size
	 */
	public PaginationTextField(int numOfEntries, int pageSize)
	{
		this.numOfEntries = numOfEntries;
		this.pageSize = pageSize;

		updateTextFieldContents(true);
		setEditable(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.text.JTextComponent#setEditable(boolean)
	 */
	@Override
	public void setEditable(boolean b)
	{
		// Ignore input to the method.
		// This one should not be editable
		super.setEditable(false);
	}

	/**
	 * Update text field contents.
	 *
	 * @param reset
	 *            the reset
	 */
	private void updateTextFieldContents(boolean reset)
	{
		if(reset)
		{
			if(numOfEntries == 0)
			{
				numOfPages = 0;
				currentPageNum = 0;
			}
			else
			{
				currentPageNum = 1;
				numOfPages = (int) Math.ceil(Double.valueOf(numOfEntries) / pageSize);
			}
		}

		setText(String.format("%d / %d", currentPageNum, numOfPages));
	}

	/**
	 * Go to next page.
	 */
	public void goToNextPage()
	{
		goToPage(currentPageNum + 1);
	}

	/**
	 * Go to previous page.
	 */
	public void goToPreviousPage()
	{
		goToPage(currentPageNum - 1);
	}

	/**
	 * Go to first page.
	 */
	public void goToFirstPage()
	{
		goToPage(1);
	}

	/**
	 * Go to last page.
	 */
	public void goToLastPage()
	{
		goToPage(numOfPages);
	}

	/**
	 * Go to page.
	 *
	 * @param newPageNum
	 *            the new page num
	 */
	public void goToPage(int newPageNum)
	{
		if(newPageNum < 1)
		{
			throw new IndexOutOfBoundsException("Requested page size is less than 1.");
		}

		if(newPageNum > numOfPages)
		{
			throw new IndexOutOfBoundsException("The requested page number (" + newPageNum
					+ ") exceeds the number of pages (" + numOfPages + ").");
		}

		currentPageNum = newPageNum;
		updateTextFieldContents(false);
	}

	/**
	 * Gets the num of entries.
	 *
	 * @return the num of entries
	 */
	public int getNumOfEntries()
	{
		return numOfEntries;
	}

	/**
	 * Sets the num of entries.
	 *
	 * @param numOfEntries
	 *            the new num of entries
	 */
	public void setNumOfEntries(int numOfEntries)
	{
		setNumOfEntries(numOfEntries, true);
	}

	/**
	 * Sets the num of entries.
	 *
	 * @param numOfEntries
	 *            the num of entries
	 * @param reset
	 *            the reset
	 */
	public void setNumOfEntries(int numOfEntries, boolean reset)
	{
		this.numOfEntries = numOfEntries;
		updateTextFieldContents(reset);
	}

	/**
	 * Gets the num of pages.
	 *
	 * @return the num of pages
	 */
	public int getNumOfPages()
	{
		return numOfPages;
	}

	/**
	 * Gets the current page num.
	 *
	 * @return the current page num
	 */
	public int getCurrentPageNum()
	{
		return currentPageNum;
	}
}
