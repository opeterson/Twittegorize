package ca.owenpeterson.twittegorize.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.HashMap;
import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TwitterUserManager;
import ca.owenpeterson.twittegorize.listviewadapters.UserAdapter;
import ca.owenpeterson.twittegorize.models.User;

/**
 * Displays a list of users that are associated with the current category. Allows the user of the app
 * to add and remove users from the category.
 * TODO: Figure out how to also allow the user to hit the checkbox to toggle instead of the listview item
 */
public class CategoryUserView extends BaseActivity {

    private TwitterUserManager userManager;
    private UserAdapter userAdapter;
    private ListView userListView;
    private List<User> users;
    private HashMap<Integer, Boolean> checkBoxStates = new HashMap<>();
    private UserClickListener userClickListener;
    private long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_user_view);

        userManager = new TwitterUserManager();
        userClickListener = new UserClickListener();

        categoryId = getIntent().getLongExtra("categoryId", 0);

        users = userManager.getAllUsers();

        userAdapter = new UserAdapter(this, R.layout.modify_category_user_item, users, categoryId);
        userListView = (ListView) findViewById(R.id.list_view_users);
        userListView.setAdapter(userAdapter);
        userListView.setOnItemClickListener(userClickListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_user_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public class UserClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_in_category);
            User user = users.get(position);
            long userId = user.getId();

            if (checkBox.isChecked()) {
                checkBoxStates.put(position, false);
                checkBox.setChecked(false);

                //remove the user from the category
                userManager.removeUserFromCategory(categoryId, userId);

            } else {
                checkBoxStates.put(position, true);
                checkBox.setChecked(true);

                //add the user to the category
                userManager.addUserToCategory(categoryId, userId);
            }
        }
    }
}
