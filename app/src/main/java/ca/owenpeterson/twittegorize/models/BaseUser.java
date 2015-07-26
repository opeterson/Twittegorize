package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by owen on 7/26/15.
 *
 * A model class used to hold the common properties of a User
 */
public abstract class BaseUser extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "userId", unique = true)
    private long userId;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    public BaseUser() {}

    public BaseUser(String name, long userId, String screenName, String profileImageUrl) {
        this.name = name;
        this.userId = userId;
        this.screenName = screenName;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
