package ca.owenpeterson.twittegorize.models;

import android.util.Log;

import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

/**
 * Created by Owen on 3/10/2015.
 *
 * Model class used to store basic information about a Tweet. Does not contain as much info as
 * the DetailTweet class.
 */
@Table(name = "Tweets")
public class Tweet extends BaseTweet {

//    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
//    private long tweetId;
//
//    @Column(name = "body")
//    private String body;
//
//    private boolean favorited;
//    private boolean retweeted;
//
//    @Column(name = "createdDate")
//    private DateTime createdDate;
//
//    @Column(name = "User", onUniqueConflict = Column.ConflictAction.REPLACE, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
//    private User user;

    public Tweet() {
        super();
    }

    public Tweet(long tweetId, String body, DateTime createdDate, User user ) {
        super(tweetId, body, createdDate, user);
        //super();
//        this.tweetId = tweetId;
//        this.body = body;
//        this.createdDate = createdDate;
//        this.user = user;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public long getTweetId() {
//        return tweetId;
//    }
//
//    public DateTime getCreatedDate() {
//        return createdDate;
//    }
//
//    public boolean isFavorited() {
//        return favorited;
//    }
//
//    public boolean isRetweeted() {
//        return retweeted;
//    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
//            tweet.body = jsonObject.getString("text");
//            tweet.tweetId = jsonObject.getLong("id");
//            tweet.favorited = jsonObject.getBoolean("favorited");
//            tweet.retweeted = jsonObject.getBoolean("retweeted");
//            tweet.createdDate = JodaDateUtils.parseDateTime(jsonObject.getString("created_at"));
//            tweet.user = User.queryOrCreateUser(jsonObject.getJSONObject("user"));

            tweet.setBody(jsonObject.getString("text"));
            tweet.setTweetId(jsonObject.getLong("id"));
            tweet.setFavorited(jsonObject.getBoolean("favorited"));
            tweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            tweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            tweet.setUser(User.queryOrCreateUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                Log.e("TWEET PARSE ERROR", e.getMessage());
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}
