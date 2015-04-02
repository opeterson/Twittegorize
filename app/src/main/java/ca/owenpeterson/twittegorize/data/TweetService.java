package ca.owenpeterson.twittegorize.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.TweetComparator;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.utils.OnFeedLoaded;

/**
 * Created by Owen on 3/15/2015.
 *
 * Consider renaming this to TweetManager
 */
public class TweetService {
    private Context context;
    private ArrayList<Tweet> tweets;
    private TweetComparator comparator = new TweetComparator();
    private ProgressDialog dialog;

    public TweetService(Context context) {
        this.context = context;
    }

    public void putTweetsToDatabase(OnFeedLoaded listener) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Collecting Tweets");
        dialog.show();
        TweetResponseHandler responseHandler = new TweetResponseHandler();
        responseHandler.setOnFeedLoadedListener(listener);
        TwitterApplication.getRestClient().getHomeTimeline(responseHandler);
    }

    public void putNewTweetsToDatabase(long tweetId, OnFeedLoaded listener) {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Collecting Tweets");
        dialog.show();
        TweetResponseHandler responseHandler = new TweetResponseHandler();
        responseHandler.setOnFeedLoadedListener(listener);
        TwitterApplication.getRestClient().getNewTweets(tweetId, responseHandler);
    }

    //try to make this an async task eventually
    public List<Tweet> getAllTweets() {
//        ProgressDialog dialog;
//        dialog = new ProgressDialog(context);
//        dialog.setMessage("Collecting Tweets");
//        dialog.show();

        List<Tweet> tweets = new Select().from(Tweet.class).orderBy("tweetId").execute();

        Collections.sort(tweets, comparator);

//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
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

//    public DetailTweet getTweetById(long tweetId) {
//        DetailTweet detailTweet;
//        DetailTweetResponseHandler detailTweetResponseHandler = new DetailTweetResponseHandler();
//        //DetailTweetLoadedListener detailTweetLoadedListener = new DetailTweetLoadedListener();
//        //detailTweetResponseHandler.setOnFeedLoadedListener(detailTweetLoadedListener);
//        TwitterApplication.getRestClient().getTweetById(tweetId, detailTweetResponseHandler);
//
//        detailTweet = detailTweetResponseHandler.getDetailTweet();
//
//        return detailTweet;
//    }
//
//
//    private class DetailTweetResponseHandler extends JsonHttpResponseHandler {
//
//        private DetailTweet detailTweet;
//
//        @Override
//        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            //build the detailTweet here.
//            detailTweet = DetailTweet.fromJson(response);
//            super.onSuccess(statusCode, headers, response);
//        }
//
//        public DetailTweet getDetailTweet() {
//            return detailTweet;
//        }
//
//    }

    private class TweetResponseHandler extends JsonHttpResponseHandler {

        private OnFeedLoaded listener;

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            tweets = Tweet.fromJson(response);

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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            listener.onFeedLoaded();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            //not sure yet. Log message?
            Log.e("ERROR! Status code:" + String.valueOf(statusCode), throwable.getMessage());
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.e("ERROR Status code:" + String.valueOf(statusCode), throwable.getMessage());
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("ERROR Status code:" + String.valueOf(statusCode), throwable.getMessage());
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        public void setOnFeedLoadedListener(OnFeedLoaded listener) {
            this.listener = listener;
        }
    }

}
