package ca.owenpeterson.twittegorize.data;

import java.util.List;

import ca.owenpeterson.twittegorize.models.User;

/**
 * Created by Owen on 3/19/2015.
 *
 * Handles database tasks related to Twitter Users.
 */
public class TwitterUserManager {

    private UserDAO userDAO;
    private UserCategoryDAO userCategoryDAO;

    public TwitterUserManager() {
        userDAO = new UserDAO();
        userCategoryDAO = new UserCategoryDAO();
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public List<Long> getUserIdsInCategory(long categoryId) {
        return userCategoryDAO.getUserIdsInCategory(categoryId);
    }

    public void addUserToCategory(long categoryId, long userId) {
        userCategoryDAO.addUserToCategory(categoryId, userId);
    }

    public void removeUserFromCategory(long categoryId, long userId) {
        userCategoryDAO.removeUserFromCategory(categoryId, userId);
    }

    public void removeUserCategoryEntries(long categoryId) {
        userCategoryDAO.removeUserCategoryEntries(categoryId);
    }

    public void addUserIdListToCategory(long categoryId, List<Long> usersToAdd) {
        userCategoryDAO.addUserIdListToCategory(categoryId, usersToAdd);
    }

    public void removeUserIdListFromCategory(long categoryId, List<Long> usersToRemove) {
        userCategoryDAO.removeUserIdListFromCategory(categoryId, usersToRemove);
    }
}
