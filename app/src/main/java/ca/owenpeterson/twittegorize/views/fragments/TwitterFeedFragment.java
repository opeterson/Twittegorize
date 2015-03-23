package ca.owenpeterson.twittegorize.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TweetService;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.listviewadapters.TweetsAdapter;
import ca.owenpeterson.twittegorize.utils.AppConstants;
import ca.owenpeterson.twittegorize.utils.OnFeedLoaded;

/**
 * Created by Owen on 3/14/2015.
 */
public class TwitterFeedFragment extends Fragment {

    private View rootView;
    private ListView tweetsListView;
    private TweetsAdapter tweetsAdapter;
    private long categoryId;
    private TweetService tweetService;
    private List<Tweet> tweets;
    private long latestTweetId;
    private OnFeedLoaded listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_twitter_feed, container, false);

        tweetService = new TweetService(getActivity());

        Bundle argBundle = getArguments();
        this.categoryId = argBundle.getLong(AppConstants.Strings.CATEGORY_ID);

        if (categoryId == 0) {
            tweets = tweetService.getAllTweets();
        } else {
            //add a new method to the tweetmanager to get tweets by categoryId
            tweets = tweetService.getTweetsByCategoryId(categoryId);
        }

        //maybe get this from the database instead
        latestTweetId = tweets.get(0).getTweetId();
        setLatestTweetId(latestTweetId);
        initFeedItems(tweets);

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

        tweetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(getActivity(), TweetDetailsActivity.class);
                //startActivity(intent);
                Toast.makeText(getActivity(), "Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshFeed() {
        long latestTweetId = tweets.get(0).getTweetId();
        listener = new OnFeedLoaded() {
            @Override
            public void onFeedLoaded() {

                if (categoryId == 0) {
                    tweets = tweetService.getAllTweets();
                } else if (categoryId >= 4) {
                    tweets = tweetService.getTweetsByCategoryId(categoryId);
                }

                tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
                tweetsListView.setAdapter(tweetsAdapter);
            }
        };

        tweetService.putNewTweetsToDatabase(latestTweetId, listener);
    }


    public long getLatestTweetId() {
        return latestTweetId;
    }

    private void setLatestTweetId(long id) {
        this.latestTweetId = id;
    }

}
