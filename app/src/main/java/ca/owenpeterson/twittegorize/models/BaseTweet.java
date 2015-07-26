package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import org.joda.time.DateTime;

/**
 * Created by owen on 7/26/15.
 *
 * This class is used to contain the common properties between a Tweet and a Retweet.
 */
public abstract class BaseTweet extends Model {

    @Column(name = "tweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;

    @Column(name = "body")
    private String body;

    private boolean favorited;
    private boolean retweeted;

    @Column(name = "createdDate")
    private DateTime createdDate;

    @Column(name = "User", onUniqueConflict = Column.ConflictAction.REPLACE, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    public BaseTweet(){}

    public BaseTweet(long tweetId, String body, DateTime createdDate, User user) {
        this.tweetId = tweetId;
        this.body = body;
        this.createdDate = createdDate;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return tweetId;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setTweetId(long tweetId) {
        this.tweetId = tweetId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
