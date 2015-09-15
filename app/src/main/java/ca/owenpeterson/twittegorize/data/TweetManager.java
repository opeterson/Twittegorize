package ca.owenpeterson.twittegorize.data;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import ca.owenpeterson.twittegorize.listeners.FeedResponseParsed;
import ca.owenpeterson.twittegorize.listeners.OnFeedLoaded;
import ca.owenpeterson.twittegorize.models.Tweet;
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
    private ProgressDialog dialog;
    private TweetDAO tweetDAO;
    private TwitterClient twitterClient;

    public TweetManager(Context context) {
        this.context = context;
        tweetDAO = new TweetDAO();
        twitterClient = TwitterApplication.getRestClient();
    }

    public void putTweetsToDatabase(OnFeedLoaded listener) {
        TweetResponseHandler responseHandler = new TweetResponseHandler();
        responseHandler.setOnFeedLoadedListener(listener);
        twitterClient.getHomeTimeline(responseHandler);
    }

    public void putNewTweetsToDatabase(long tweetId, OnFeedLoaded listener) {
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
     * it is passed to the TwitterFeedResponseParser class which parses the response into individual entities
     * and writes those entities to the database.
     */
    private class TweetResponseHandler extends JsonHttpResponseHandler {

        private OnFeedLoaded listener;

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);

            TwitterFeedResponseParser feedResponseParser = new TwitterFeedResponseParser(response);
            feedResponseParser.setOnFeedResponseParsedListener(new FeedResponseParsed() {
                @Override
                public void OnFeedResponseParsed() {
                    listener.onFeedLoaded();
                }
            });

            feedResponseParser.execute();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("ERROR Status code:" + String.valueOf(statusCode), throwable.getMessage());
            super.onFailure(statusCode, headers, throwable, errorResponse);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Contacting twitter failed. Please try again later.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        public void setOnFeedLoadedListener(OnFeedLoaded listener) {
            this.listener = listener;
        }
    }

}
