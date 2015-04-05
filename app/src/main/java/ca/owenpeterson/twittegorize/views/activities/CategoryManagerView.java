package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.CategoryManager;
import ca.owenpeterson.twittegorize.data.TwitterUserManager;
import ca.owenpeterson.twittegorize.models.Category;

public class CategoryManagerView extends BaseActivity {

    //creating category items
    private EditText inputCategoryName;
    private Button buttonAddNewCategory;
    private CategoryManager categoryManager;

    //deleting category items
    private Spinner spinnerCategories;
    private Button buttonDelete;
    private ArrayAdapter<Category> categoryNameAdapter;

    //modifying category user items
    private Spinner spinnerModifyCategories;
    private Button buttonModifyUsers;

    private ButtonClickHandler buttonClickHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_manager_view);

        categoryManager = new CategoryManager();
        buttonClickHandler = new ButtonClickHandler();

        inputCategoryName = (EditText) findViewById(R.id.input_category_name);
        buttonAddNewCategory = (Button) findViewById(R.id.button_add_category);
        buttonAddNewCategory.setOnClickListener(buttonClickHandler);

        spinnerCategories = (Spinner) findViewById(R.id.spinner_categories);
        spinnerModifyCategories = (Spinner) findViewById(R.id.spinner_categories_user_management);
        populateCategorySpinners();

        buttonDelete = (Button) findViewById(R.id.button_delete_category);
        buttonDelete.setOnClickListener(buttonClickHandler);

        buttonModifyUsers = (Button) findViewById(R.id.button_manage_category_users);
        buttonModifyUsers.setOnClickListener(buttonClickHandler);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_manager_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch(id) {
                case R.id.button_add_category:
                    handleCreateNewCategory();
                    break;
                case R.id.button_delete_category:
                    handleDeleteCategory();
                    break;
                case R.id.button_manage_category_users:
                    handleCategoryUserModify();
                    break;
            }
        }
    }

    private void handleCategoryUserModify() {
        Category selectedCategory;
        selectedCategory = (Category) spinnerModifyCategories.getSelectedItem();
        long categoryId = selectedCategory.getId();
        Intent intent = new Intent(this, CategoryUserView.class);
        intent.putExtra("categoryId", categoryId);
        startActivity(intent);
    }

    private void handleDeleteCategory() {
        Category selectedCategory;

        selectedCategory = (Category) spinnerCategories.getSelectedItem();

        if (selectedCategory != null) {
            //selectedCategory.delete();
            long categoryId = selectedCategory.getId();
            categoryManager.removeCategory(categoryId);

            //this works for now but I may want to do this asynchronously (if its not already)
            TwitterUserManager twitterUserManager = new TwitterUserManager();
            twitterUserManager.removeUserCategoryEntries(categoryId);

            populateCategorySpinners();
        }


    }

    private void handleCreateNewCategory() {
        String catName = inputCategoryName.getText().toString();
        boolean success;
        if (StringUtils.isNotBlank(catName)) {
            success = categoryManager.addCategory(catName);

            if (success) {
                //TODO: Add a dialog
                inputCategoryName.setText("");
                populateCategorySpinners();
            }
        }
    }

    private void populateCategorySpinners() {
        List<Category> categories = categoryManager.getAllCategories();

        if (categories != null) {
            categoryNameAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,categories);
            spinnerCategories.setAdapter(categoryNameAdapter);
            spinnerModifyCategories.setAdapter(categoryNameAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }




}
