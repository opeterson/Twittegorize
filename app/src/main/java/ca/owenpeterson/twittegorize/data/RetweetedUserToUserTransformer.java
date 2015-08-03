package ca.owenpeterson.twittegorize.data;

import ca.owenpeterson.twittegorize.models.RetweetedUser;
import ca.owenpeterson.twittegorize.models.User;

/**
 * Transforms a Retweeted user into a regular User
 * Created by owen on 8/2/15.
 */
public class RetweetedUserToUserTransformer {
    private RetweetedUser retweetedUser;

    public RetweetedUserToUserTransformer(RetweetedUser retweetedUser) {
        this.retweetedUser = retweetedUser;
    }

    public User transform() {
        User user = new User();
        user.setProfileImageUrl(retweetedUser.getProfileImageUrl());
        user.setName(retweetedUser.getName());
        user.setScreenName(retweetedUser.getScreenName());
        user.setUserId(retweetedUser.getUserId());

        return user;
    }
}
