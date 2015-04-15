package ca.owenpeterson.twittegorize.listeners;

import ca.owenpeterson.twittegorize.models.DetailTweet;

/**
 * Created by Owen on 3/25/2015.
 *
 * Used as a callback when opening the tweet details activity. When the user clicks on a tweet in
 * the list, a call is made to twitter to get the one tweet they clicked on. This interface is
 * used when the app receives a response from twitter.
 */
public interface OnDetailTweetLoaded {

    public void onDetailTweetLoaded(DetailTweet detailTweet);
}
