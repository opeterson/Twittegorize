package ca.owenpeterson.twittegorize.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TweetManager;
import ca.owenpeterson.twittegorize.listviewadapters.TweetsAdapter;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.utils.AppConstants;
import ca.owenpeterson.twittegorize.listeners.OnFeedLoaded;
import ca.owenpeterson.twittegorize.listeners.OnQueryComplete;
import ca.owenpeterson.twittegorize.applicationpersistence.SettingsManager;
import ca.owenpeterson.twittegorize.views.activities.TweetDetailsActivity;

/**
 * Created by Owen on 3/14/2015.
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        settingsManager = new SettingsManager(getActivity());
        int themeId = settingsManager.getCurrentTheme();

        if (themeId != -1) {
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), themeId);

            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

            rootView =  localInflater.inflate(R.layout.fragment_twitter_feed, container, false);

        } else {
            rootView = inflater.inflate(R.layout.fragment_twitter_feed, container, false);
        }

        tweetManager = new TweetManager(getActivity());

        Bundle argBundle = getArguments();
        this.categoryId = argBundle.getLong(AppConstants.Strings.CATEGORY_ID);

        tweetDBLoader = new AsyncTweetDBLoader(getActivity());
        queryListener = new OnQueryComplete() {
            @Override
            public void onQueryComplete() {
                Tweet latestTweet = tweetManager.getLatestTweet();
                latestTweetId = latestTweet.getTweetId();

                setLatestTweetId(latestTweetId);
                initFeedItems(tweets);
            }
        };


        tweetDBLoader.setOnQueryCompleteListener(queryListener);
        tweetDBLoader.execute();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initFeedItems(List<Tweet> tweets) {

        tweetsListView = (ListView) rootView.findViewById(R.id.tweet_list_view);
        tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
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
                        tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
                        tweetsListView.setAdapter(tweetsAdapter);
                    }
                };

                AsyncTweetDBLoader dbLoader = new AsyncTweetDBLoader(getActivity());
                dbLoader.setOnQueryCompleteListener(listener);
                dbLoader.execute();
            }
        };

        tweetManager.putNewTweetsToDatabase(getLatestTweetId(), listener);
    }

    public long getLatestTweetId() {
        return latestTweetId;
    }

    private void setLatestTweetId(long id) {
        this.latestTweetId = id;
    }

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Tweet selectedTweet = tweets.get(position);
            long tweetId = selectedTweet.getTweetId();
            Intent intent = new Intent(getActivity(), TweetDetailsActivity.class);
            intent.putExtra("tweetId", tweetId);
            startActivity(intent);
        }
    }

    private class AsyncTweetDBLoader extends AsyncTask<Void, Void, List<Tweet>> {

        private OnQueryComplete listener;
        private Context context;
        private ProgressDialog dialog;

        public AsyncTweetDBLoader(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Retrieving Your Tweets");
            dialog.show();
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
            TwitterFeedFragment.this.tweets = tweets;
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            listener.onQueryComplete();
        }

        public void setOnQueryCompleteListener(OnQueryComplete listener) {
            this.listener = listener;
        }
    }
}
