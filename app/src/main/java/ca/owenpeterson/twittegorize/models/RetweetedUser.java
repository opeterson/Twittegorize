package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Table;

/**
 * Created by owen on 7/26/15.
 *
 * This model class is used to store Users that are in tweets which have been retweeted in the
 * database. They are kept separate from the users that are actually followed.
 */
@Table(name = "RetweetedUsers")
public class RetweetedUser extends BaseUser {

    public RetweetedUser() {
        super();
    }

    public RetweetedUser(String name, long userId, String screenName, String profileImageUrl) {
        super(name, userId, screenName, profileImageUrl);
    }
}
