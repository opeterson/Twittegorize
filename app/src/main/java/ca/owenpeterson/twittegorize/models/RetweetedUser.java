package ca.owenpeterson.twittegorize.models;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by owen on 7/26/15.
 */
@Table(name = "RetweetedUsers")
public class RetweetedUser extends BaseUser {

    public RetweetedUser() {
        super();
    }

    public RetweetedUser(String name, long userId, String screenName, String profileImageUrl) {
        super(name, userId, screenName, profileImageUrl);
    }

    public static RetweetedUser fromJson(JSONObject json) {
        RetweetedUser u = new RetweetedUser();
        try {
            u.setName(json.getString("name"));
            u.setUserId(json.getLong("id"));
            u.setScreenName(json.getString("screen_name"));
            u.setProfileImageUrl(json.getString("profile_image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    /**
     * This method was necessary in order to handle a foreign key constraint on the database.
     * I didn't want to be attempting to insert users when they already exist.
     * This method queries the database for the userId first and then returns that user if they exist
     * otherwise the user is saved and the new user object is returned.
     * @param jsonObject
     * @return
     */
    public static RetweetedUser queryOrCreateUser(JSONObject jsonObject) {

        RetweetedUser jsonUser = fromJson(jsonObject);
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

}
