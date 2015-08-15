/**
 *
 */
package net.magriso.srt.editor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.magriso.srt.editor.error.MultiValidationException;

/**
 * The Class SRTObjectEntry.
 *
 * @author Yakir
 */
public class SRTObjectEntry extends SRTObjectBase
{

	/** The Constant TIMESTAMP_SEPERATOR. */
	private static final String TIMESTAMP_SEPERATOR = "-->";

	/** The index. */
	private int index;

	/** The start. */
	private SRTObjectTimestamp start;

	/** The end. */
	private SRTObjectTimestamp end;

	/** The subtitles. */
	private List<String> subtitles;

	/**
	 * Instantiates a new SRT object entry.
	 *
	 * @param entry
	 *            the entry
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public SRTObjectEntry(String entry) throws FileNotFoundException, IOException
	{
		super();
		readObject(entry);
	}

	/**
	 * Read object.
	 *
	 * @param entry
	 *            the entry
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readObject(String entry) throws FileNotFoundException, IOException
	{
		String[] lines = entry.split(System.lineSeparator());
		index = Integer.parseInt(lines[0]);
		String timesLine = lines[1];
		String[] times = timesLine.split(TIMESTAMP_SEPERATOR);

		start = new SRTObjectTimestamp(times[0].trim());
		end = new SRTObjectTimestamp(times[1].trim());

		subtitles = new ArrayList<>();
		for(int indx = 2; indx < lines.length; indx++)
		{
			subtitles.add(lines[indx]);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.magriso.srt.editor.SRTObjectBase#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.magriso.srt.editor.SRTObjectBase#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof SRTObjectEntry)
		{
			if(((SRTObjectEntry) obj).index == this.index && ((SRTObjectEntry) obj).start.equals(this.start)
					&& ((SRTObjectEntry) obj).end.equals(this.end)
					&& ((SRTObjectEntry) obj).subtitles.equals(this.subtitles))
			{
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.magriso.srt.editor.SRTObjectBase#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(index).append(System.lineSeparator());
		stringBuilder.append(start.toString()).append(" --> ");
		stringBuilder.append(end.toString()).append(System.lineSeparator());
		for(String subtitle : subtitles)
		{
			stringBuilder.append(subtitle).append(System.lineSeparator());
		}

		return stringBuilder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.magriso.srt.editor.SRTObjectBase#add(long)
	 */
	@Override
	public void add(long ms)
	{
		start.add(ms);
		end.add(ms);

		markAsDirty();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.magriso.srt.editor.SRTObjectBase#validate()
	 */
	@Override
	public void validate() throws MultiValidationException
	{
		MultiValidationException exception = null;
		try
		{
			start.validate();
		}
		catch(MultiValidationException ex)
		{
			exception = addError(exception, ex);
		}

		try
		{
			end.validate();

			// Validate that end timestamp is after start timestamp only if
			// start and end timestamps are valid
			if(exception == null && !end.after(start))
			{
				exception = addError(exception,
						String.format(getString("endTimeNotAfterStartTime"), start.toString(), end.toString()));
			}
		}
		catch(MultiValidationException ex)
		{
			exception = addError(exception, ex);
		}

		if(exception != null)
		{
			throw exception;
		}
	}

	/**
	 * Mark all clean.
	 */
	public void markAllClean()
	{
		start.markAsClean();
		end.markAsClean();

		markAsClean();
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public SRTObjectTimestamp getStart()
	{
		return start;
	}

	/**
	 * Gets the end.
	 *
	 * @return the end
	 */
	public SRTObjectTimestamp getEnd()
	{
		return end;
	}

	/**
	 * Gets the subtitles.
	 *
	 * @return the subtitles
	 */
	public List<String> getSubtitles()
	{
		return subtitles;
	}
}
