package ca.owenpeterson.twittegorize.models;

import org.joda.time.DateTime;

import java.util.Comparator;

import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

/**
 * Created by Owen on 3/16/2015.
 *
 * A compararator class used to sort tweets by date for the TwitterFeedFragment list view.
 * In the future I think I will have this functionality incorporated into the database query for
 * tweets instead, which may improve performance.
 */
public class TweetComparator implements Comparator<Tweet> {
    DateTime leftDate;
    DateTime rightDate;

    @Override
    public int compare(Tweet lhs, Tweet rhs) {

        leftDate = JodaDateUtils.parseDateTime(lhs.getCreatedDate());
        rightDate = JodaDateUtils.parseDateTime(rhs.getCreatedDate());

        return rightDate.compareTo(leftDate);
    }
}
