package ca.owenpeterson.twittegorize.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Owen on 3/17/2015.
 */
@Table(name = "Categories")
public class Category extends Model {

    @Column(name = "name")
    private String categoryName;

    public Category() {
        super();
    }

    public Category(String name) {
        super();
        this.categoryName = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String toString() {
        return categoryName;
    }
}
