package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Owen on 3/10/2015.
 *
 * Model class used to store a user in the database
 */
@Table(name = "Users")
public class User extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "userId", unique = true)
    private long userId;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileBgImageUrl")
    private String profileBgImageUrl;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

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
        this.name = name;
        this.userId = userId;
        this.screenName = screenName;
        this.profileBgImageUrl = profileBgImageUrl;
        this.profileImageUrl = profileImageUrl;
        this.numTweets = numTweets;
        this.followersCount = followersCount;
        this.friendsCount = friendsCount;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBgImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileBgImageUrl(String profileBgImageUrl) {
        this.profileBgImageUrl = profileBgImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
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
