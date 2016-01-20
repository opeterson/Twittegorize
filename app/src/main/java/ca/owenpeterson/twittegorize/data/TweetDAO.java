package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.TweetComparator;

/**
 * This class is used to persist and retrieve tweets from the local SQLite Database
 *
 * Created by owen on 7/14/15.
 */
public class TweetDAO {

    private TweetComparator comparator = new TweetComparator();

    public TweetDAO(){}

    public List<Tweet> getAllTweets() {
        List<Tweet> tweets = new Select().from(Tweet.class).execute();

        Collections.sort(tweets, comparator);
        return tweets;
    }

    public List<Tweet> getTweetsByCategoryId(long categoryId) {
        TwitterUserManager userManager = new TwitterUserManager();
        List<Long> userIdsForCategory = userManager.getUserIdsInCategory(categoryId);

        Object[] idsArray = userIdsForCategory.toArray();
        String userIds = Arrays.toString(idsArray);
        userIds = userIds.replace("[", "");
        userIds = userIds.replace("]", "");

        return new Select().from(Tweet.class).where("User IN (" + userIds + ")").orderBy("tweetId DESC").execute();
    }

    public Tweet getLatestTweet() {
        //TODO: There must be a better way to do this.
        return getAllTweets().get(0);
    }

    public int getTweetCount() {
        return new Select().from(Tweet.class).execute().size();
    }

    public List<Tweet> getTweetsOlderThanDate(DateTime olderThanDate) {
        Long dateInMillis = olderThanDate.getMillis();
        return new Select().from(Tweet.class).where("createdDate <= " + dateInMillis).execute();
    }

    public void deleteTweetList(List<Tweet> tweets) {
        ActiveAndroid.beginTransaction();
        try {
            for (Tweet tweet : tweets) {
                tweet.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    public boolean saveTweetList(List<Tweet> tweets) {
        boolean success = false;

        ActiveAndroid.beginTransaction();
        try {
            for (Tweet tweet : tweets) {
                tweet.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return success;
    }
}
