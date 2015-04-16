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
 *
 * A compararator class used to sort tweets by date for the TwitterFeedFragment list view.
 * In the future I think I will have this functionality incorporated into the database query for
 * tweets instead, which may improve performance.
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
