package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Owen on 3/10/2015.
 *
 * Model class used to store a user in the database
 */
@Table(name = "Users")
public class User extends BaseUser {

    @Column(name = "profileBgImageUrl")
    private String profileBgImageUrl;

    @Column(name = "numTweets")
    private int numTweets;

    @Column(name = "followersCount")
    private int followersCount;

    @Column(name = "friendsCount")
    private int friendsCount;

    public User() {
        super();
    }

    public User(String name, long userId, String screenName, String profileBgImageUrl, String profileImageUrl, int numTweets, int followersCount, int friendsCount) {
        super(name, userId, screenName, profileImageUrl);
        this.profileBgImageUrl = profileBgImageUrl;
        this.numTweets = numTweets;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBgImageUrl;
    }

    public int getNumTweets() {
        return numTweets;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setProfileBgImageUrl(String profileBgImageUrl) {
        this.profileBgImageUrl = profileBgImageUrl;
    }

    public void setNumTweets(int numTweets) {
        this.numTweets = numTweets;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

}
