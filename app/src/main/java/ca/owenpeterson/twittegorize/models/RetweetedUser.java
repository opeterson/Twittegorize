package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.joda.time.DateTime;

/**
 * Created by owen on 7/26/15.
 *
 * This model class is used to store Users that are in tweets which have been retweeted in the
 * database. They are kept separate from the users that are actually followed.
 */
@Table(name = "RetweetedUsers")
public class RetweetedUser extends BaseUser {

    @Column(name = "createdDate")
    private DateTime createdDate;

    public RetweetedUser() {
        super();
    }

    public RetweetedUser(String name, long userId, String screenName, String profileImageUrl) {
        super(name, userId, screenName, profileImageUrl);
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }
}
