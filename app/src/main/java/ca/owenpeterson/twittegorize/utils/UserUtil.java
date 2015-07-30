package ca.owenpeterson.twittegorize.utils;

import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import ca.owenpeterson.twittegorize.models.RetweetedUser;
import ca.owenpeterson.twittegorize.models.User;

/**
 * Created by owen on 7/29/15.
 */
public class UserUtil {

    private UserUtil(){
        //don't allow instantiation.
    }

    public static User queryOrCreateUser(JSONObject jsonObject) {
        User jsonUser = createUserFromJson(jsonObject);
        long userId = jsonUser.getUserId();

        //TODO: create a method in the TwitterUserManager class that handles this instead.
        User existingUser = new Select().from(User.class).where("userId = ?", userId).executeSingle();

        if (existingUser != null) {
            return existingUser;
        } else {
            jsonUser.save();
            return jsonUser;
        }
    }

    public static RetweetedUser queryOrCreateRetweetedUser(JSONObject jsonObject) {

        RetweetedUser jsonUser = createRetweetedUserFromJson(jsonObject);
        long userId = jsonUser.getUserId();

        //TODO: create a method in the TwitterUserManager class that handles this instead.
        RetweetedUser existingUser = new Select().from(RetweetedUser.class).where("userId = ?", userId).executeSingle();

        if (existingUser != null) {
            return existingUser;
        } else {
            jsonUser.save();
            return jsonUser;
        }
    }

    private static User createUserFromJson(JSONObject json) {
        User u = new User();
        try {
            u.setName(json.getString("name"));
            u.setUserId(json.getLong("id"));
            u.setScreenName(json.getString("screen_name"));
            u.setProfileBgImageUrl(json.getString("profile_background_image_url"));
            u.setProfileImageUrl(json.getString("profile_image_url"));
            u.setNumTweets(json.getInt("statuses_count"));
            u.setFollowersCount(json.getInt("followers_count"));
            u.setFriendsCount(json.getInt("friends_count"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }


    private static RetweetedUser createRetweetedUserFromJson(JSONObject json) {
        RetweetedUser retweetedUser = new RetweetedUser();
        try {
            retweetedUser.setName(json.getString("name"));
            retweetedUser.setUserId(json.getLong("id"));
            retweetedUser.setScreenName(json.getString("screen_name"));
            retweetedUser.setProfileImageUrl(json.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retweetedUser;
    }
}
