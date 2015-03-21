package ca.owenpeterson.twittegorize.models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Owen on 3/16/2015.
 */
public class TweetComparator implements Comparator<Tweet> {

    private DateTimeParser[] parsers = {
            DateTimeFormat.forPattern("E MMM d HH:mm:ss Z yyyy").withLocale(Locale.CANADA).getParser()};
    private DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder().append(null, parsers).toFormatter();

    DateTime leftDate;
    DateTime rightDate;

    @Override
    public int compare(Tweet lhs, Tweet rhs) {

        leftDate = dateFormatter.parseDateTime(lhs.getCreatedDate());
        rightDate = dateFormatter.parseDateTime(rhs.getCreatedDate());

        return rightDate.compareTo(leftDate);
    }
}
