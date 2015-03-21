package ca.owenpeterson.twittegorize.data;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.owenpeterson.twittegorize.models.User;
import ca.owenpeterson.twittegorize.models.UserCategory;

/**
 * Created by Owen on 3/19/2015.
 */
public class TwitterUserManager {

    public TwitterUserManager() {

    }

    /**
     * TODO: wrap all selects in transactions
     * TODO: attempt to create async tasks for DB operations that only involve the DB. (No Twitter Fetches)
     * TODO: exception handling
     */

    public List<User> getAllUsers() {
        List<User> users = Collections.emptyList();
        users = new Select().from(User.class).orderBy("Id ASC").execute();
        return users;
    }

    /**
     * Provides a list of primary keys for users in a specific category
     * @param categoryId
     * @return
     */
    public List<Long> getUserIdsInCategory(long categoryId) {
        List<UserCategory> userCategories = Collections.emptyList();
        userCategories = new Select().from(UserCategory.class).where("categoryId = ?", categoryId).execute();
        List<Long> ids = new ArrayList<Long>();

        for (UserCategory uc :  userCategories) {
            Long id = uc.getUserId();
            ids.add(id);
        }

        return ids;
    }

    public void addUserToCategory(long categoryId, long userId) {
        UserCategory userCategory = new UserCategory();
        userCategory.setCategoryId(categoryId);
        userCategory.setUserId(userId);
        userCategory.save();
    }

    public void removeUserFromCategory(long categoryId, long userId) {
        UserCategory userCategory = new Select().from(UserCategory.class).where("userId = ?", userId).and("categoryId = ?", categoryId).executeSingle();
        if (null != userCategory) {
            userCategory.delete();
        }
    }


    public void addUserIdListToCategory(long categoryId, List<Long> usersToAdd) {
        for (Long userId : usersToAdd) {
            UserCategory userCategory = new UserCategory();
            userCategory.setCategoryId(categoryId);
            userCategory.setUserId(userId);
            userCategory.save();
        }
    }

    public void removeUserIdListFromCategory(long categoryId, List<Long> usersToRemove) {

//        List<UserCategory> matches = new Select().from(UserCategory.class).where("categoryId = ?", categoryId).and("userId in ?", usersToRemove.toArray()).execute();
//
//        for (UserCategory uc : matches) {
//            uc.delete();
//        }

        //this needs work. get all of the usercategories in one query instead of each time.
        for (Long userId : usersToRemove) {
            UserCategory userCategory = new Select().from(UserCategory.class).where("Id = ?", userId).and("categoryId = ?", categoryId).executeSingle();
            if (null != userCategory) {
                userCategory.delete();
            }
        }
    }
}
