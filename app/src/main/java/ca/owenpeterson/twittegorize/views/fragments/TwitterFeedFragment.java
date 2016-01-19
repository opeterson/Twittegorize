package ca.owenpeterson.twittegorize.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.applicationpersistence.SettingsManager;
import ca.owenpeterson.twittegorize.data.TweetDAO;
import ca.owenpeterson.twittegorize.data.TweetManager;
import ca.owenpeterson.twittegorize.listeners.OnFeedLoaded;
import ca.owenpeterson.twittegorize.listeners.OnQueryComplete;
import ca.owenpeterson.twittegorize.listviewadapters.TweetsAdapter;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.utils.AppConstants;
import ca.owenpeterson.twittegorize.views.activities.TweetDetailsActivity;

/**
 * Created by Owen on 3/14/2015.
 *
 * This fragment is used to display the list of tweets for the currently selected category.
 */
public class TwitterFeedFragment extends Fragment {

    private View rootView;
    private ListView tweetsListView;
    private TweetsAdapter tweetsAdapter;
    private long categoryId;
    private TweetManager tweetManager;
    private List<Tweet> tweets;
    private long latestTweetId;
    private OnFeedLoaded listener;
    private OnQueryComplete queryListener;
    private ItemClickListener itemClickListener;
    private AsyncTweetDBLoader tweetDBLoader;
    private SettingsManager settingsManager;
    private Context context;
    private boolean openTwitterDefault;
    private Button newTweetsButton;
    private RelativeLayout buttonLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //set the context to a class level variable so that the context can persist across
        //fragment detatchments.
        //bug fix for screen rotation crash.
        this.context = getActivity();

        settingsManager = new SettingsManager(context);
        int themeId = settingsManager.getCurrentTheme();
        openTwitterDefault = settingsManager.getOpenTwitterDefault();

        if (themeId != -1) {
            final Context contextThemeWrapper = new ContextThemeWrapper(context, themeId);

            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

            rootView =  localInflater.inflate(R.layout.fragment_twitter_feed, container, false);

        } else {
            rootView = inflater.inflate(R.layout.fragment_twitter_feed, container, false);
        }

        tweetManager = new TweetManager(context);

        Bundle argBundle = getArguments();
        this.categoryId = argBundle.getLong(AppConstants.Strings.CATEGORY_ID);

        tweetDBLoader = new AsyncTweetDBLoader(context, false);
        queryListener = new OnQueryComplete() {
            @Override
            public void onQueryComplete() {
                latestTweetId = getLatestTweetIdFromDatabase();

                setLatestTweetId(latestTweetId);
                initFeedItems(tweets);
            }
        };

        newTweetsButton = (Button) rootView.findViewById(R.id.new_tweets_button);
        newTweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadTweetsFromDatabase();
            }
        });
        buttonLayout = (RelativeLayout) rootView.findViewById(R.id.new_tweets_button_layout);
        buttonLayout.setVisibility(View.GONE);

        tweetDBLoader.setOnQueryCompleteListener(queryListener);
        tweetDBLoader.execute();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        final long latestTweetIdBeforeUpdate = getLatestTweetIdFromDatabase();

        //TODO: Check to see if there are any new tweets first!
        //TODO: Convert this whole concept to a service that checks for new tweets in the background.
        if (latestTweetId != 0) {
            OnFeedLoaded listener = new OnFeedLoaded() {
                @Override
                public void onFeedLoaded() {
                    long latestTweetIdSinceUpdate = getLatestTweetIdFromDatabase();
                    //TODO: THis is not working correctly. More testing required.
                    if (latestTweetIdSinceUpdate > latestTweetIdBeforeUpdate) {
                        buttonLayout.setVisibility(View.VISIBLE);
                        buttonLayout.postDelayed(new Runnable() { public void run() { buttonLayout.setVisibility(View.GONE); } }, 5000);
                    }
                }
            };

            tweetManager.putNewTweetsToDatabase(latestTweetIdBeforeUpdate, listener);
        }
    }

    private void initFeedItems(List<Tweet> tweets) {

        tweetsListView = (ListView) rootView.findViewById(R.id.tweet_list_view);
        tweetsAdapter = new TweetsAdapter(context, tweets);
        tweetsListView.setAdapter(tweetsAdapter);

        itemClickListener = new ItemClickListener();
        tweetsListView.setOnItemClickListener(itemClickListener);
    }

    public void refreshFeed() {
        listener = new OnFeedLoaded() {
            @Override
            public void onFeedLoaded() {
                OnQueryComplete listener = new OnQueryComplete() {
                    @Override
                    public void onQueryComplete() {
                        tweetsAdapter = new TweetsAdapter(context, tweets);
                        tweetsListView.setAdapter(tweetsAdapter);
                    }
                };

                AsyncTweetDBLoader dbLoader = new AsyncTweetDBLoader(context, true);
                dbLoader.setOnQueryCompleteListener(listener);
                dbLoader.execute();
            }
        };

        tweetManager.putNewTweetsToDatabase(getLatestTweetId(), listener);
    }

    public void reloadTweetsFromDatabase() {
        List<Tweet> tweets = tweetManager.getAllTweets();
        initFeedItems(tweets);
    }

    public long getLatestTweetId() {
        return latestTweetId;
    }

    private void setLatestTweetId(long id) {
        this.latestTweetId = id;
    }

    private long getLatestTweetIdFromDatabase() {
        Tweet latestTweet = tweetManager.getLatestTweet();
        latestTweetId = latestTweet.getTweetId();
        return latestTweetId;
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Tweet selectedTweet = tweets.get(position);
            Long tweetId;

            if (selectedTweet.isRetweeting()) {
                tweetId = selectedTweet.getRetweet().getTweetId();
            } else {
                tweetId = selectedTweet.getTweetId();
            }

            if (openTwitterDefault) {
                /**
                 * TODO: move this funtcion to a separate class of some kind
                 * because it is repeated in the tweetDetails view.
                 */
                String screenName;
                if (selectedTweet.isRetweeting()) {
                    screenName = String.valueOf(selectedTweet.getRetweet().getRetweetedUser().getScreenName());
                } else {
                    screenName = String.valueOf(selectedTweet.getUser().getScreenName());
                }

                String url = "https://twitter.com/" + screenName + "/status/" + tweetId;
                Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse(url) );
                startActivity(browser);
            } else {
                Intent intent = new Intent(context, TweetDetailsActivity.class);
                intent.putExtra("tweetId", tweetId);
                startActivity(intent);
            }
        }
    }

    /**
     * Async task used to contact the database and get all tweet items that are associaed with the
     * selected category. Uses a listener to then display all of the tweets in the list once
     * the query to the database is complete.
     */
    private class AsyncTweetDBLoader extends AsyncTask<Void, Void, List<Tweet>> {

        private OnQueryComplete listener;
        private Context localContext;
        private Boolean isRefreshAction;
        private ProgressDialog dialog;

        public AsyncTweetDBLoader(Context context, Boolean isRefreshAction) {
            this.localContext = context;
            this.isRefreshAction = isRefreshAction;
        }

        @Override
        protected void onPreExecute() {
            if (isRefreshAction) {
                dialog = new ProgressDialog(localContext);
                dialog.setMessage("Retrieving Your Tweets");
                dialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected List<Tweet> doInBackground(Void... params) {
            List<Tweet> tweets;

            if (categoryId == 0) {
                tweets = tweetManager.getAllTweets();
            } else {
                tweets = tweetManager.getTweetsByCategoryId(categoryId);
            }

            return tweets;
        }

        @Override
        protected void onPostExecute(List<Tweet> tweets) {
            super.onPostExecute(tweets);

            //set the tweets on the parent class.
            TwitterFeedFragment.this.tweets = tweets;
            if (isRefreshAction && dialog.isShowing()) {
                dialog.dismiss();
            }

            listener.onQueryComplete();
        }

        public void setOnQueryCompleteListener(OnQueryComplete listener) {
            this.listener = listener;
        }
    }
}
