package ca.owenpeterson.twittegorize.listeners;

/**
 * Interface used to indicate that the TwitterFeedResponseParser class has finished parsing
 * the response from twitter.
 *
 * Created by owen on 7/29/15.
 */
public interface FeedResponseParsed {

    public void OnFeedResponseParsed();
}
