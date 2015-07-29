package ca.owenpeterson.twittegorize.data;

import com.activeandroid.ActiveAndroid;

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
}
