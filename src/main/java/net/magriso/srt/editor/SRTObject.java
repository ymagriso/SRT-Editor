/**
 *
 */
package net.magriso.srt.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.magriso.srt.editor.error.MultiValidationException;
import net.magriso.srt.editor.error.ValidationException;

/**
 * The Class SRTFile.
 *
 * @author Yakir
 */
public class SRTObject extends SRTObjectBase
{

	/** The srt object entries. */
	private List<SRTObjectEntry> srtObjectEntries;

	/** The hashed object entries. */
	private Map<Integer, SRTObjectEntry> hashedObjectEntries;

	/**
	 * Instantiates a new SRT object.
	 *
	 * @param file
	 *            the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public SRTObject(File file) throws FileNotFoundException, IOException
	{
		super();
		try(InputStream in = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(in, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(reader);)
				{
			readObject(bufferedReader);
				}
	}

	/**
	 * Read object.
	 *
	 * @param in
	 *            the in
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readObject(BufferedReader in) throws IOException
	{
		srtObjectEntries = new ArrayList<>();
		hashedObjectEntries = new HashMap<>();

		// Skip first byte - irrelevant in Windows 10
		// in.skip(1);

		while(in.ready())
		{
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			do
			{
				line = in.readLine();
				if(line != null && !line.isEmpty())
				{
					stringBuilder.append(line).append(System.lineSeparator());
				}
			}
			while(line != null && !line.isEmpty());

			if(!stringBuilder.toString().isEmpty())
			{
				SRTObjectEntry srtObjectEntry = new SRTObjectEntry(stringBuilder.toString());
				srtObjectEntries.add(srtObjectEntry);
				hashedObjectEntries.put(srtObjectEntry.getIndex(), srtObjectEntry);
			}
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
		return srtObjectEntries.hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.magriso.srt.editor.SRTObjectBase#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof SRTObject)
		{
			if(((SRTObject) obj).srtObjectEntries.equals(this.srtObjectEntries))
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
		for(SRTObjectEntry fileEntry : srtObjectEntries)
		{
			stringBuilder.append(fileEntry.toString()).append(System.lineSeparator());
		}
		stringBuilder.append(System.lineSeparator());

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
		add(ms, 0);
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

		for(int indx = 0; indx < srtObjectEntries.size(); indx++)
		{
			try
			{
				srtObjectEntries.get(indx).validate();
				if(srtObjectEntries.get(indx).getIndex() != indx + 1)
				{
					throw new ValidationException(getString("invalidIndexValue"));
				}
			}
			catch(ValidationException ex)
			{
				exception = addError(exception, ex);
			}
		}

		if(exception != null)
		{
			throw exception;
		}
	}

	/**
	 * Adds the milliseconds.
	 *
	 * @param ms
	 *            the ms
	 * @param index
	 *            the index
	 */
	public void add(long ms, int index)
	{
		for(int indx = index; indx < srtObjectEntries.size(); indx++)
		{
			srtObjectEntries.get(indx).add(ms);
		}

		markAsDirty();
	}

	/**
	 * To2 dimensions array.
	 *
	 * @return the object[][]
	 */
	public Object[][] to2DimensionsArray()
	{
		return to2DimensionsArray(0);
	}

	/**
	 * To2 dimensions array.
	 *
	 * @param offset
	 *            the offset
	 * @return the object[][]
	 */
	public Object[][] to2DimensionsArray(int offset)
	{
		return to2DimensionsArray(offset, -1);
	}

	/**
	 * To2 dimensions array.
	 *
	 * @param offset
	 *            the offset
	 * @param size
	 *            the size
	 * @return the object[][]
	 */
	public Object[][] to2DimensionsArray(int offset, int size)
	{
		int actualSize = srtObjectEntries.size() > offset + size && size != -1 ? size : srtObjectEntries.size()
				- offset;
		Object[][] table = new Object[actualSize][5];
		for(int indx = offset; indx < offset + actualSize; indx++)
		{
			int column = 0;
			table[indx - offset][column++] = String.valueOf(srtObjectEntries.get(indx).getIndex());
			table[indx - offset][column++] = srtObjectEntries.get(indx).getStart().toString();
			table[indx - offset][column++] = srtObjectEntries.get(indx).getEnd().toString();

			StringBuilder stringBuilder = new StringBuilder();
			boolean isFirst = true;
			for(String subtitle : srtObjectEntries.get(indx).getSubtitles())
			{
				if(isFirst)
				{
					isFirst = false;
				}
				else
				{
					stringBuilder.append(System.lineSeparator());
				}

				stringBuilder.append(subtitle);
			}

			table[indx - offset][column++] = stringBuilder.toString();
		}

		return table;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize()
	{
		return srtObjectEntries.size();
	}

	/**
	 * Gets the object entry.
	 *
	 * @param index
	 *            the index
	 * @return the object entry
	 */
	public SRTObjectEntry getObjectEntry(int index)
	{
		return hashedObjectEntries.get(index);
	}

	/**
	 * Mark all clean.
	 */
	public void markAllClean()
	{
		for(SRTObjectEntry objectEntry : srtObjectEntries)
		{
			objectEntry.markAllClean();
		}

		markAsClean();
	}
}
