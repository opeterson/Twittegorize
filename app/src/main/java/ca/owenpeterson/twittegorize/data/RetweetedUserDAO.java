package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;

import java.util.List;

import ca.owenpeterson.twittegorize.models.RetweetedUser;

/**
 * Created by owen on 7/28/15.
 */
public class RetweetedUserDAO {
    public RetweetedUserDAO() {
    }

    public boolean saveRetweetedUserList(List<RetweetedUser> retweetedUsers) {
        boolean success = false;

        ActiveAndroid.beginTransaction();
        try {
            for (RetweetedUser ru : retweetedUsers) {
                ru.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return success;
    }
}
