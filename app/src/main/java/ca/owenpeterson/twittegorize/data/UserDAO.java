package ca.owenpeterson.twittegorize.data;

import com.activeandroid.query.Select;

import java.util.List;

import ca.owenpeterson.twittegorize.models.User;

/**
 * Used to perform operations for users on the database.
 *
 * Created by owen on 7/14/15.
 */
public class UserDAO {

    public UserDAO(){}

    public List<User> getAllUsers() {
        List<User> users;
        users = new Select().from(User.class).orderBy("Id ASC").execute();
        return users;
    }
}
