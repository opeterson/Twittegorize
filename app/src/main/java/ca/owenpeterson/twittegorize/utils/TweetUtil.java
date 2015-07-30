package ca.owenpeterson.twittegorize.utils;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ca.owenpeterson.twittegorize.models.Retweet;
import ca.owenpeterson.twittegorize.models.Tweet;

/**
 * Created by owen on 7/29/15.
 */
public class TweetUtil {

    public TweetUtil() {
        //prevent instantiation
    }


    public static Tweet createTweetfromJson(JSONObject jsonObject, Tweet tweet) {
        try {
            tweet.setBody(jsonObject.getString("text"));
            tweet.setTweetId(jsonObject.getLong("id"));
            tweet.setFavorited(jsonObject.getBoolean("favorited"));
            tweet.setRetweeted(jsonObject.getBoolean("retweeted"));

            if (StringUtils.contains(tweet.getBody(), "RT @")) {
                Retweet retweet = createRetweetFromJson(jsonObject.getJSONObject("retweeted_status"));
                tweet.setRetweet(retweet);
            }
            tweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            tweet.setUser(UserUtil.queryOrCreateUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return tweet;
    }

    public static Retweet createRetweetFromJson(JSONObject jsonObject) {
        Retweet retweet = new Retweet();
        try {
            retweet.setBody(jsonObject.getString("text"));
            retweet.setTweetId(jsonObject.getLong("id"));
            retweet.setFavorited(jsonObject.getBoolean("favorited"));
            retweet.setRetweeted(jsonObject.getBoolean("retweeted"));
            retweet.setCreatedDate(JodaDateUtils.parseDateTime(jsonObject.getString("created_at")));
            retweet.setRetweetedUser(UserUtil.queryOrCreateRetweetedUser(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return retweet;
    }

}
