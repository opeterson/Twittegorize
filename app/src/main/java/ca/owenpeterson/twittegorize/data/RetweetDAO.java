package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import org.joda.time.DateTime;

import java.util.List;

import ca.owenpeterson.twittegorize.models.Retweet;

/**
 * Created by owen on 7/28/15.
 */
public class RetweetDAO {
    public RetweetDAO() {
    }

    public boolean saveRetweetList(List<Retweet> retweets) {
        boolean success = false;

        ActiveAndroid.beginTransaction();
        try {
            for (Retweet rt : retweets) {
                rt.save();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return success;
    }

    public boolean deleteRetweetList(List<Retweet> retweets) {
        boolean success = false;

        ActiveAndroid.beginTransaction();
        try {
            for (Retweet rt : retweets) {
                rt.getRetweetedUser().delete();
                rt.delete();
            }
            ActiveAndroid.setTransactionSuccessful();
            success = true;
        }
        finally {
            ActiveAndroid.endTransaction();
        }

        return success;
    }

    public List<Retweet> getRetweetsOlderThanDate(DateTime olderThanDate) {
        Long dateInMillis = olderThanDate.getMillis();
        return new Select().from(Retweet.class).where("createdDate <= " + dateInMillis).execute();
    }
}
