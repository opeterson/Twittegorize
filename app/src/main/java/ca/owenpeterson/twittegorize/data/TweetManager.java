package ca.owenpeterson.twittegorize.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.twittegorize.listeners.OnFeedLoaded;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.TweetComparator;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.rest.TwitterClient;

/**
 * Created by Owen on 3/15/2015.
 *
 * This class is used both for getting new tweets form twitter, and getting existing tweets from
 * the database.
 * TODO: Separate the different functionality into two classes - One for getting responses from
 * TODO: twitter and another for getting them from the database
 */
public class TweetManager {
    private Context context;
    private ArrayList<Tweet> tweets;
    private TweetComparator comparator = new TweetComparator();
    private ProgressDialog dialog;
    private TweetDAO tweetDAO;
    private TwitterClient twitterClient;

    public TweetManager(Context context) {
        this.context = context;
        tweetDAO = new TweetDAO();
        twitterClient = TwitterApplication.getRestClient();
    }

    public void putTweetsToDatabase(OnFeedLoaded listener) {
        //TODO: Remove this dialog box and create an Async task that calls this method instead.
        //Put the dialog box in the async task.
        dialog = new ProgressDialog(context);
        dialog.setMessage("Contacting Twitter");
        dialog.show();
        TweetResponseHandler responseHandler = new TweetResponseHandler();
        responseHandler.setOnFeedLoadedListener(listener);
        twitterClient.getHomeTimeline(responseHandler);
    }

    public void putNewTweetsToDatabase(long tweetId, OnFeedLoaded listener) {
        //TODO: Remove this dialog box and create an Async task that calls this method instead.
        //Put the dialog box in the async task.
        dialog = new ProgressDialog(context);
        dialog.setMessage("Contacting Twitter");
        dialog.show();
        TweetResponseHandler responseHandler = new TweetResponseHandler();
        responseHandler.setOnFeedLoadedListener(listener);
        twitterClient.getNewTweets(tweetId, responseHandler);
    }

    public List<Tweet> getAllTweets() {
        return tweetDAO.getAllTweets();
    }

    public List<Tweet> getTweetsByCategoryId(long categoryId) {
        return tweetDAO.getTweetsByCategoryId(categoryId);
    }

    public Tweet getLatestTweet() {
        return tweetDAO.getLatestTweet();
    }

    /**
     *
     * @param olderThanDate - Delete any tweets that are OLDER than this date.
     */
    public void deleteOldTweetsByDate(DateTime olderThanDate) {
        List<Tweet> oldTweets = tweetDAO.getTweetsOlderThanDate(olderThanDate);

        if (oldTweets.size() > 0) {
            tweetDAO.deleteTweetList(oldTweets);
            Log.d("Tweet Cleanup:", "Deleting old tweets!");
        }
    }

    /**
     * This class is used as the response handler for the call to twitter. When the response is received
     * all tweets are written to the database.
     */
    private class TweetResponseHandler extends JsonHttpResponseHandler {

        private OnFeedLoaded listener;

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            tweets = Tweet.fromJson(response);

            if (tweets != null) {
                tweetDAO.saveTweetList(tweets);
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            listener.onFeedLoaded();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("ERROR Status code:" + String.valueOf(statusCode), throwable.getMessage());
            super.onFailure(statusCode, headers, throwable, errorResponse);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            //TODO: add code here to show an error dialog.
        }

        public void setOnFeedLoadedListener(OnFeedLoaded listener) {
            this.listener = listener;
        }
    }

}
