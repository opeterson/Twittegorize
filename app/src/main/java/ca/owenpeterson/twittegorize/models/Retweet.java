package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

/**
 * Created by owen on 7/26/15.
 */
@Table(name="Retweets")
public class Retweet extends BaseTweet {

    @Column(name = "User", onUniqueConflict = Column.ConflictAction.REPLACE, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private RetweetedUser retweetedUser;

    public Retweet() {
        super();
    }

    public Retweet(long tweetId, String body, DateTime createdDate, RetweetedUser user) {
        super(tweetId, body, createdDate);
    }

    public RetweetedUser getRetweetedUser() {
        return retweetedUser;
    }

    public void setRetweetedUser(RetweetedUser retweetedUser) {
        this.retweetedUser = retweetedUser;
    }
}
