package ca.owenpeterson.twittegorize.models;

import android.util.Log;

import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

/**
 * Created by owen on 7/26/15.
 */
@Table(name="Retweets")
public class Retweet extends BaseTweet {

    public Retweet() {
        super();
    }

    public Retweet(long tweetId, String body, DateTime createdDate, User user) {
        super(tweetId, body, createdDate, user);
    }

    public static Retweet fromJson(JSONObject jsonObject) {
        Retweet retweet = new Retweet();
        try {
            retweet.setBody(jsonObject.getString("text"));
            retweet.setTweetId(jsonObject.getLong("id"));
            retweet.setFavorited(jsonObject.getBoolean("favorited"));
            retweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            retweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            retweet.setUser(User.queryOrCreateUser(jsonObject.getJSONObject("user")));
            retweet.save();
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return retweet;
    }
}
