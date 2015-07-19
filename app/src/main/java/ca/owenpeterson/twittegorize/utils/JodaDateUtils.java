package ca.owenpeterson.twittegorize.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.util.Locale;

/**
 * Created by Owen on 3/26/2015.
 *
 * A centralized location for performing tasks on JodaTime DateTime objects.
 */
public class JodaDateUtils {
    //this is an array of DateTimeFormats that can be used to parse a Date.
    private static DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss zzz").withLocale(Locale.CANADA).getParser(),
            DateTimeFormat.forPattern("E, d MMM yyyy HH:mm:ss Z").withLocale(Locale.CANADA).getParser(),
            DateTimeFormat.forPattern("E MMM d HH:mm:ss Z yyyy").withLocale(Locale.CANADA).getParser(),
            DateTimeFormat.forPattern("E, d MMM yyyy HH:mm a").withLocale(Locale.CANADA).getParser()};
    private static DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    public static String formatDate(DateTime originalDate) {
        String formattedDate = originalDate.toString(AppConstants.Dates.DISPLAY_DATE_FORMAT, Locale.CANADA);
        return formattedDate;
    }

    public static DateTime parseDateTime(String originalDateString) {
        DateTime date = dateFormatter.parseDateTime(originalDateString);
        return date;
    }
}
