package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.joda.time.DateTime;

import java.util.ArrayList;
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
        List<Tweet> tweets = new Select().from(Tweet.class).orderBy("tweetId").execute();

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
        return new Select().from(Tweet.class).orderBy("Id DESC").limit(1).executeSingle();
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

    public void saveTweetList(ArrayList<Tweet>tweets) {

        ActiveAndroid.beginTransaction();
        try {
            for (Tweet tweet : tweets) {
                tweet.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
}
