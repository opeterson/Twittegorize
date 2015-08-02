package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

/**
 * Created by Owen on 3/10/2015.
 *
 * Model class used to store basic information about a Tweet. Does not contain as much info as
 * the DetailTweet class.
 */
@Table(name = "Tweets")
public class Tweet extends BaseTweet {

    @Column(name = "retweet", onUniqueConflict = Column.ConflictAction.REPLACE, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Retweet retweet;

    @Column(name = "User", onUniqueConflict = Column.ConflictAction.REPLACE, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "Retweeting")
    private boolean isRetweeting;

    public Tweet() {
        super();
    }

    public Tweet(long tweetId, String body, DateTime createdDate, User user) {
        super(tweetId, body, createdDate);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Retweet getRetweet() {
        return retweet;
    }

    public void setRetweet(Retweet retweet) {
        this.retweet = retweet;
    }

    public boolean isRetweeting() {
        return isRetweeting;
    }

    public void setIsRetweeting(boolean isRetweeting) {
        this.isRetweeting = isRetweeting;
    }
}
