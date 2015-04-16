package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Owen on 3/17/2015.
 *
 * Database model used to handle the many-to-many relationship between User and Category objects.
 */
@Table(name = "UserCategory")
public class UserCategory extends Model {

    @Column(name = "userId")
    private long userId;

    @Column(name = "categoryId")
    private long categoryId;

    public UserCategory() {
        super();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
