package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.CategoryManager;
import ca.owenpeterson.twittegorize.models.Category;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.utils.AppConstants;
import ca.owenpeterson.twittegorize.utils.SettingsManager;
import ca.owenpeterson.twittegorize.views.fragments.NavigationDrawerFragment;
import ca.owenpeterson.twittegorize.views.fragments.TwitterFeedFragment;


public class CategoryViewSelector extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private TwitterFeedFragment currentFragment;
    private String fragmentName = "CUSTOM_FRAGMENT";
    private CategoryManager categoryManager;
    private final int RETURN_TO_TWEET_LIST = 2;
    private SettingsManager settingsManager;
    private long lastCategoryId = 0;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private String[] drawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryManager = new CategoryManager();

        setContentView(R.layout.activity_category_view_selector);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //create a fragment object palceholder
        TwitterFeedFragment fragment = null;

        // update the main content by replacing fragments

        //create a bundle object that will be used to pass arguments to the fragment
        //such as a category id.
        Bundle bundle = new Bundle();

        switch(position) {
            case 0:
                //load all the tweets by default.
                bundle.putLong("categoryId", position);
                fragment = new TwitterFeedFragment();
                fragment.setArguments(bundle);
                setCurrentFragment(fragment);
                break;
            case 1:
                Intent intent = new Intent(this, CategoryManagerView.class);
                //may have to change this to be for result again, so that you can use the onActivityResult method
                //which will prevent refreshing of the fragment.
                startActivityForResult(intent, RETURN_TO_TWEET_LIST);
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Help not yet available", Toast.LENGTH_LONG).show();
                break;
            default:
                openFragmentForCategory(position);
                break;

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, currentFragment, fragmentName)
                .commitAllowingStateLoss();
    }

    private void openFragmentForCategory(int position) {
        List<Category> categories = categoryManager.getAllCategories();

        //there are three default items in the list right now.
        //figure out a better way to handle this. Maybe an app constant.
        int index = position - 3;

        Category selectedCategory = categories.get(index);

        long id = selectedCategory.getId();

        Bundle bundle = new Bundle();
        TwitterFeedFragment fragment = null;

        bundle.putLong("categoryId", id);

        bundle.putString("categoryName", selectedCategory.getCategoryName());

        fragment = new TwitterFeedFragment();
        fragment.setArguments(bundle);
        setCurrentFragment(fragment);
        setLastCategoryId(id);
    }

    /**
     * This method attaches a title to the fragment window when an item is clicked. This will also
     * require the use of the category names from the category manager.
     * @param number
     */
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.text_category_default_feed);
                break;
            case 2:
                mTitle = "";
                break;
            case 3:
                mTitle = getString(R.string.text_category_help);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_category_view_selector, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                long lastCategoryId = getLastCategoryId();
                intent.putExtra(AppConstants.Strings.CATEGORY_ID, lastCategoryId);
                startActivityForResult(intent, RETURN_TO_TWEET_LIST);
                break;
            case R.id.action_refresh:
                currentFragment.refreshFeed();
                break;
            case R.id.action_logout:
                TwitterApplication.getRestClient().clearAccessToken();
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This is required so that the refreshFeed method of any fragment can be called.
     *
     * In the future it might make more sense to make an interface.
     * @param currentFragment
     */
    public void setCurrentFragment(TwitterFeedFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RETURN_TO_TWEET_LIST:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        boolean themeChanged = bundle.getBoolean(AppConstants.Strings.THEME_CHANGED);

                        if (themeChanged) {
                            long previousCategoryId = bundle.getLong(AppConstants.Strings.CATEGORY_ID);
                            int themeId = bundle.getInt(AppConstants.Strings.THEME);

                            //setTheme(themeId);

                            rebuildFragment(previousCategoryId);

                        }
                    }
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void rebuildFragment(long lastCategoryId) {

        if (lastCategoryId != -1) {

            Bundle bundle = new Bundle();
            TwitterFeedFragment fragment = null;

            bundle.putLong("categoryId", lastCategoryId);

            fragment = new TwitterFeedFragment();
            fragment.setArguments(bundle);
            setCurrentFragment(fragment);
            setLastCategoryId(lastCategoryId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, currentFragment, fragmentName)
                    .commitAllowingStateLoss();

        }

    }

    public long getLastCategoryId() {
        return lastCategoryId;
    }

    public void setLastCategoryId(long lastCategoryId) {
        this.lastCategoryId = lastCategoryId;
    }
}
