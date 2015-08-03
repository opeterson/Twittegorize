package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.joda.time.DateTime;

import java.util.List;

import ca.owenpeterson.twittegorize.models.RetweetedUser;

/**
 * Class used to perform database operations specific to RetweetedUser
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

    public boolean deleteRetweetedUserList(List<RetweetedUser> retweetedUsers) {
        boolean success = false;

        ActiveAndroid.beginTransaction();
        try {
            for (RetweetedUser ru : retweetedUsers) {
                ru.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return success;
    }

    public List<RetweetedUser> getRetweetedUsersOlderThanDate(DateTime olderThanDate) {
        Long dateInMillis = olderThanDate.getMillis();
        return new Select().from(RetweetedUser.class).where("createdDate <= " + dateInMillis).execute();
    }
}
