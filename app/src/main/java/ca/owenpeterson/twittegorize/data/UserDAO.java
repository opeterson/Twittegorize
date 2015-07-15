package ca.owenpeterson.twittegorize.data;

import com.activeandroid.query.Select;

import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.models.User;

/**
 * Created by owen on 7/14/15.
 */
public class UserDAO {

    public List<User> getAllUsers() {
        List<User> users = Collections.emptyList();
        users = new Select().from(User.class).orderBy("Id ASC").execute();
        return users;
    }
}
