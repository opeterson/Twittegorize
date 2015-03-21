package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Owen on 3/10/2015.
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

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.userId = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileBgImageUrl = json.getString("profile_background_image_url");
            u.profileImageUrl = json.getString("profile_image_url");
            u.numTweets = json.getInt("statuses_count");
            u.followersCount = json.getInt("followers_count");
            u.friendsCount = json.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    public static User queryOrCreateUser(JSONObject jsonObject) {

        User jsonUser = fromJson(jsonObject);
        long userId = jsonUser.getUserId();
        User existingUser = new Select().from(User.class).where("userId = ?", userId).executeSingle();

        if (existingUser != null) {
            return existingUser;
        } else {
            jsonUser.save();
            return jsonUser;
        }
    }
}
