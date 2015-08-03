package ca.owenpeterson.twittegorize.data;

import org.joda.time.DateTime;

import java.util.List;

import ca.owenpeterson.twittegorize.models.Retweet;

/**
 * This class is used to combine business logic and database CRUD operations to manage Retweets.
 *
 * Created by owen on 8/3/15.
 */
public class RetweetManager {

    private RetweetDAO retweetDAO;

    public RetweetManager() {
        retweetDAO = new RetweetDAO();
    }

    public void deleteOldRetweetsByDate(DateTime olderThanDate) {
        List<Retweet> retweetsToDelete = retweetDAO.getRetweetsOlderThanDate(olderThanDate);
        if (retweetsToDelete != null) {
            retweetDAO.deleteRetweetList(retweetsToDelete);
        }
    }
}
