package ca.owenpeterson.twittegorize.rest;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import ca.owenpeterson.twittegorize.utils.KeyManager;

/**
 * Created by Owen on 3/14/2015.
 *
 * Provides the interface between my application and Twitter.
 */
public class TwitterClient extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = KeyManager.CONSUMER_KEY;
    public static final String REST_CONSUMER_SECRET = KeyManager.CONSUMER_SECRET;
    public static final String REST_CALLBACK_URL = "oauth://ca.owenpeterson.twittergorize";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        String url = getApiUrl("statuses/home_timeline.json?count=200");
        client.get(url, null, handler);
    }

    public void  getNewTweets(long tweetId,AsyncHttpResponseHandler handler) {
        String id = String.valueOf(tweetId);
        String url = getApiUrl("statuses/home_timeline.json?count=200&since_id=" + id);
        client.get(url, null, handler);
    }

    public void getTweetById(long tweetId, AsyncHttpResponseHandler handler) {
        String id = String.valueOf(tweetId);
        String url = getApiUrl("/statuses/show.json?id=" + id);
        client.get(url, null, handler);
    }




}
