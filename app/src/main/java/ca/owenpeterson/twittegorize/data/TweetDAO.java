package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.TweetComparator;

/**
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

        List<Tweet> tweets = new Select().from(Tweet.class).where("User IN (" + userIds + ")").orderBy("tweetId DESC").execute();

        return tweets;

    }

    public Tweet getLatestTweet() {
        Tweet latestTweet = new Select().from(Tweet.class).orderBy("Id DESC").limit(1).executeSingle();
        return latestTweet;
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
