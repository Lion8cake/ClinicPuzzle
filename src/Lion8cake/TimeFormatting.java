package Lion8cake;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatting {

	/*
	 * The custom formatting of the date for stuff such as files
	 */
	public static String TimeFormat()
	{
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH) + 1;
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	
    	int hour = cal.get(Calendar.HOUR_OF_DAY);
    	int min = cal.get(Calendar.MINUTE);
    	int second = cal.get(Calendar.SECOND);
    	
    	String seperator = "_";
    	
		return (String)(year + seperator + month + seperator + day + seperator + hour + min + second);
	}
	
	/*
	 * The custom formatting of the date for stuff such for logging
	 */
	public static String TimeLogFormat()
	{
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	
    	int year = cal.get(Calendar.YEAR);
    	int month = cal.get(Calendar.MONTH) + 1;
    	int day = cal.get(Calendar.DAY_OF_MONTH);
    	
    	int hour = cal.get(Calendar.HOUR_OF_DAY);
    	int min = cal.get(Calendar.MINUTE);
    	int second = cal.get(Calendar.SECOND);
    	
    	String seperator = ".";
    	String seperator2 = ":";

		return (String)("[" + year + seperator + month + seperator + day + "][" + hour + seperator2 + min + seperator2 + second + "]");
	}
}
