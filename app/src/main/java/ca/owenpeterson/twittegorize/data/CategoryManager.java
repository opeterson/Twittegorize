package ca.owenpeterson.twittegorize.data;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import ca.owenpeterson.twittegorize.models.Category;

/**
 * Created by Owen on 3/17/2015.
 */
public class CategoryManager {

    /**
     * TODO: wrap all selects in transactions
     * TODO: attempt to create async tasks for DB operations that only involve the DB. (No Twitter Fetches)
     */


    public boolean addCategory(String categoryName) {
        long id;
        Category category = new Category();
        category.setCategoryName(categoryName);
        id = category.save();

        if (id > 0) {
            return true;
        }
        return false;
    }

    public void removeCategory(long categoryId) {
        Category category = null;
        category = new Select().from(Category.class).where("Id = ?", categoryId).executeSingle();
        category.delete();
    }

    public Category getCategoryById(long categoryId) {
        Category category = null;
        category = new Select().from(Category.class).where("Id = ?", categoryId).executeSingle();
        return category;
    }

    public List<String> getCategoryNameList() {
        List<String> names = new ArrayList<String>();
        List<Category> categories = new Select().from(Category.class).orderBy("Id ASC").execute();

        for(Category c : categories) {
            names.add(c.getCategoryName());
        }
        return names;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new Select().from(Category.class).orderBy("Id ASC").execute();
        return categories;
    }

    /**

//    return new Select()
//    .from(Student.class)
//    .innerJoin(StudentCourse.class).on("students.id = studentcourses.id")
//    .where("studentcourses.course = ?", courseId)
//    .execute();
     **/

}
