package net.magriso.srt.editor;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.magriso.srt.editor.error.MultiValidationException;

/**
 * The Class SRTObjectTimestamp.
 *
 * @author Yakir
 */
public class SRTObjectTimestamp extends SRTObjectBase
{

	/** The hours. */
	private int hours;

	/** The minutes. */
	private int minutes;

	/** The seconds. */
	private int seconds;

	/** The miliseconds. */
	private int milliseconds;

	/**
	 * Instantiates a new SRT object timestamp.
	 *
	 * @param timestamp
	 *            the timestamp
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public SRTObjectTimestamp(String timestamp) throws FileNotFoundException, IOException
	{
		super();
		readObject(timestamp);
	}

	/**
	 * Read object.
	 *
	 * @param timestamp
	 *            the timestamp
	 */
	private void readObject(String timestamp)
	{
		String[] components = timestamp.split(":");
		hours = Integer.parseInt(components[0]);
		minutes = Integer.parseInt(components[1]);

		String[] lastComponent = components[2].split(",");
		seconds = Integer.parseInt(lastComponent[0]);
		milliseconds = Integer.parseInt(lastComponent[1]);
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
		if(obj instanceof SRTObjectTimestamp)
		{
			if(((SRTObjectTimestamp) obj).hours == this.hours && ((SRTObjectTimestamp) obj).minutes == this.minutes
					&& ((SRTObjectTimestamp) obj).seconds == this.seconds
					&& ((SRTObjectTimestamp) obj).milliseconds == this.milliseconds)
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
		stringBuilder.append(String.format("%02d", hours)).append(":");
		stringBuilder.append(String.format("%02d", minutes)).append(":");
		stringBuilder.append(String.format("%02d", seconds)).append(",");
		stringBuilder.append(String.format("%03d", milliseconds));

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
		long sum = ms + milliseconds;
		if(sum < 0)
		{
			int secTaken = 0;
			int minTaken = 0;
			int hourTaken = 0;
			while(sum < 0)
			{
				sum += 1000;
				secTaken++;
				if(secTaken > seconds)
				{
					seconds = 59;
					secTaken = 0;
					minTaken++;
					if(minTaken > minutes)
					{
						minutes = 59;
						minTaken = 0;
						hourTaken++;
					}
				}
			}

			seconds -= secTaken;
			minutes -= minTaken;
			hours -= hourTaken;
		}

		hours += sum / 1000 / 60 / 60;

		minutes += sum / 1000 / 60;
		if(minutes >= 60)
		{
			hours += minutes / 60;
			minutes = minutes % 60;
		}

		seconds += sum / 1000;
		if(seconds >= 60)
		{
			minutes += seconds / 60;
			if(minutes >= 60)
			{
				hours += minutes / 60;
				minutes = minutes % 60;
			}
			seconds = seconds % 60;
		}

		milliseconds = (int) (sum % 1000);

		markAsDirty();
	}

	/**
	 * Before.
	 *
	 * @param objectTimestamp
	 *            the object timestamp
	 * @return true, if successful
	 */
	public boolean before(SRTObjectTimestamp objectTimestamp)
	{
		if(hours < objectTimestamp.hours)
		{
			return false;
		}

		if(minutes < objectTimestamp.minutes)
		{
			return false;
		}

		if(seconds < objectTimestamp.seconds)
		{
			return false;
		}

		if(milliseconds < objectTimestamp.milliseconds)
		{
			return false;
		}

		return true;
	}

	/**
	 * After.
	 *
	 * @param objectTimestamp
	 *            the object timestamp
	 * @return true, if successful
	 */
	public boolean after(SRTObjectTimestamp objectTimestamp)
	{
		if(hours > objectTimestamp.hours)
		{
			return false;
		}

		if(minutes > objectTimestamp.minutes)
		{
			return false;
		}

		if(seconds > objectTimestamp.seconds)
		{
			return false;
		}

		if(milliseconds > objectTimestamp.milliseconds)
		{
			return false;
		}

		return true;
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
		if(hours < 0)
		{
			exception = addError(exception, String.format(getString("hoursError"), hours));
		}

		if(minutes < 0 || minutes > 59)
		{
			exception = addError(exception, String.format(getString("minutesError"), minutes));
		}

		if(seconds < 0 || seconds > 59)
		{
			exception = addError(exception, String.format(getString("secondsError"), seconds));
		}

		if(milliseconds < 0 || milliseconds > 999)
		{
			exception = addError(exception, String.format(getString("millisecondsError"), milliseconds));
		}

		if(exception != null)
		{
			throw exception;
		}
	}
}
