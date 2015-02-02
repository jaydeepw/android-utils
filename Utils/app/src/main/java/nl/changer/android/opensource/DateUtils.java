
package nl.changer.android.opensource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.net.ParseException;
import android.util.Log;

/***
 * Provides helper methods for date utilities.
 ***/
public class DateUtils {

	private static final String TAG = DateUtils.class.getSimpleName();

	/***
	 * Converts ISO date string to UTC timezone equivalent.
	 * 
	 * @param dateAndTime
	 *            ISO formatted time string.
	 ****/
	public static String getUtcTime(String dateAndTime) {
		Date d = parseDate(dateAndTime);

		String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

		// Convert Local Time to UTC
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		return sdf.format(d);
	}

	/****
	 * Parses date string and return a {@link java.util.Date} object
	 * 
	 * @return The ISO formatted date object
	 *****/
	public static Date parseDate(String date) {

		if (date == null) {
			return null;
		}

		StringBuffer sbDate = new StringBuffer();
		sbDate.append(date);
		String newDate = null;
		Date dateDT = null;

		try {
			newDate = sbDate.substring(0, 19).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String rDate = newDate.replace("T", " ");
		String nDate = rDate.replaceAll("-", "/");

		try {
			dateDT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).parse(nDate);
			// Log.v( TAG, "#parseDate dateDT: " + dateDT );
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dateDT;
	}

	/***
	 * Converts UTC time formatted as ISO to device local time.
	 * 
	 * <br/>
	 * <br/>
	 * Sample usage
	 * 
	 * <pre>
	 * 
	 * {
	 * 	SimpleDateFormat sdf = new SimpleDateFormat(&quot;yyyy-MM-dd'T'HH:mm:ss.SSS'Z'&quot;);
	 * 	d = toLocalTime(&quot;2014-10-08T09:46:04.455Z&quot;, sdf);
	 * }
	 * </pre>
	 * 
	 * @param utcDate
	 * @param format
	 * @return Date
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	public static Date toLocalTime(String utcDate, SimpleDateFormat sdf) throws Exception {

		// create a new Date object using
		// the timezone of the specified city
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date localDate = sdf.parse(utcDate);

		sdf.setTimeZone(TimeZone.getDefault());
		String dateFormateInUTC = sdf.format(localDate);

		return sdf.parse(dateFormateInUTC);
	}

	/**
	 * Returns abbreviated (3 letters) day of the week.
	 * 
	 * @param date
	 *            ISO format date
	 * @return The name of the day of the week
	 */
	public static String getDayOfWeekAbbreviated(String date) {
		Date dateDT = parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.DAY_OF_WEEK);

		String dayStr = null;

		switch (day) {

		case Calendar.SUNDAY:
			dayStr = "Sun";
			break;

		case Calendar.MONDAY:
			dayStr = "Mon";
			break;

		case Calendar.TUESDAY:
			dayStr = "Tue";
			break;

		case Calendar.WEDNESDAY:
			dayStr = "Wed";
			break;

		case Calendar.THURSDAY:
			dayStr = "Thu";
			break;

		case Calendar.FRIDAY:
			dayStr = "Fri";
			break;

		case Calendar.SATURDAY:
			dayStr = "Sat";
			break;
		}

		return dayStr;
	}

	/***
	 * Gets the name of the month from the given date.
	 * 
	 * @param date
	 *            ISO format date
	 * @return Returns the name of the month
	 * ***/
	public static String getMonth(String date) {
		Date dateDT = parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.MONTH);

		String dayStr = null;

		switch (day) {

		case Calendar.JANUARY:
			dayStr = "January";
			break;

		case Calendar.FEBRUARY:
			dayStr = "February";
			break;

		case Calendar.MARCH:
			dayStr = "March";
			break;

		case Calendar.APRIL:
			dayStr = "April";
			break;

		case Calendar.MAY:
			dayStr = "May";
			break;

		case Calendar.JUNE:
			dayStr = "June";
			break;

		case Calendar.JULY:
			dayStr = "July";
			break;

		case Calendar.AUGUST:
			dayStr = "August";
			break;

		case Calendar.SEPTEMBER:
			dayStr = "September";
			break;

		case Calendar.OCTOBER:
			dayStr = "October";
			break;

		case Calendar.NOVEMBER:
			dayStr = "November";
			break;

		case Calendar.DECEMBER:
			dayStr = "December";
			break;
		}

		return dayStr;
	}

	/**
	 * Gets abbreviated name of the month from the given date.
	 * 
	 * @param date
	 *            ISO format date
	 * @return Returns the name of the month
	 */
	public static String getMonthAbbreviated(String date) {
		Date dateDT = parseDate(date);

		if (dateDT == null) {
			return null;
		}

		// Get current date
		Calendar c = Calendar.getInstance();
		// it is very important to
		// set the date of
		// the calendar.
		c.setTime(dateDT);
		int day = c.get(Calendar.MONTH);

		String dayStr = null;

		switch (day) {

		case Calendar.JANUARY:
			dayStr = "Jan";
			break;

		case Calendar.FEBRUARY:
			dayStr = "Feb";
			break;

		case Calendar.MARCH:
			dayStr = "Mar";
			break;

		case Calendar.APRIL:
			dayStr = "Apr";
			break;

		case Calendar.MAY:
			dayStr = "May";
			break;

		case Calendar.JUNE:
			dayStr = "Jun";
			break;

		case Calendar.JULY:
			dayStr = "Jul";
			break;

		case Calendar.AUGUST:
			dayStr = "Aug";
			break;

		case Calendar.SEPTEMBER:
			dayStr = "Sep";
			break;

		case Calendar.OCTOBER:
			dayStr = "Oct";
			break;

		case Calendar.NOVEMBER:
			dayStr = "Nov";
			break;

		case Calendar.DECEMBER:
			dayStr = "Dec";
			break;
		}

		return dayStr;
	}

}
