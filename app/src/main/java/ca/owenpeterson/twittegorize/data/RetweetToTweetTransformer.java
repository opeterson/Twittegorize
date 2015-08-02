package ca.owenpeterson.twittegorize.data;

import ca.owenpeterson.twittegorize.models.Retweet;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.User;

/**
 * Transforms a Retweet object into a Tweet object.
 *
 * Created by owen on 8/2/15.
 */
public class RetweetToTweetTransformer {
    private Retweet retweet;
    private RetweetedUserToUserTransformer transformer;

    public RetweetToTweetTransformer(Retweet retweet) {
        this.retweet = retweet;
    }

    public Tweet transform() {
        Tweet tweet = new Tweet();
        transformer = new RetweetedUserToUserTransformer(retweet.getRetweetedUser());
        User retweetedUser = transformer.transform();
        tweet.setUser(retweetedUser);
        tweet.setBody(retweet.getBody());
        tweet.setCreatedDate(retweet.getCreatedDate());
        tweet.setTweetId(retweet.getTweetId());
        return tweet;
    }
}
