package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.CategoryManager;
import ca.owenpeterson.twittegorize.models.Category;
import ca.owenpeterson.twittegorize.utils.AppConstants;
import ca.owenpeterson.twittegorize.utils.SettingsManager;

public class SettingsActivity extends BaseActivity {
    private Switch themeSwitch;
    private Switch logoutSwitch;
    private SettingsManager settingsManager;
    private SwitchListener switchListener;
    private Spinner categorySpinner;
    private ArrayAdapter<Category> categoryAdapter;
    private CategoryManager categoryManager;
    private boolean themeChanged = false;
    private long previousCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent previousIntent = getIntent();
        long lastId = previousIntent.getLongExtra(AppConstants.Strings.CATEGORY_ID, -1);
        setPreviousCategoryId(lastId);

        settingsManager = new SettingsManager(this);
        themeSwitch = (Switch) findViewById(R.id.switch_theme);
        switchListener = new SwitchListener();
        themeSwitch.setOnCheckedChangeListener(switchListener);

        logoutSwitch = (Switch) findViewById(R.id.switch_logout);
        logoutSwitch.setOnCheckedChangeListener(switchListener);

        categoryManager = new CategoryManager();
        categorySpinner = (Spinner) findViewById(R.id.spinner_categories_settings);
        populateCategorySpinner();

    }

    @Override
    protected void onResume() {
        super.onResume();

        int themeId = settingsManager.getCurrentTheme();

        if (themeId != -1) {
          if (themeId == R.style.Dark) {
              themeSwitch.setChecked(true);
          } else {
              themeSwitch.setChecked(false);
          }
        }

        boolean alwaysLogout = settingsManager.getAlwaysLogout();
        logoutSwitch.setChecked(alwaysLogout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AppConstants.Strings.THEME_CHANGED, themeChanged);

        if (themeChanged) {

            long categoryId = getPreviousCategoryId();
            int themeId = settingsManager.getCurrentTheme();
            bundle.putLong(AppConstants.Strings.CATEGORY_ID, categoryId);
            bundle.putInt(AppConstants.Strings.THEME, themeId);
        }

        intent.putExtras(bundle);

        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            switch(id) {
                case R.id.switch_theme:
                    themeChanged = true;
                    if (isChecked) {
                        settingsManager.setThemeDark();
                    } else {
                        settingsManager.setThemeLight();
                    }
                case R.id.switch_logout:
                    if (isChecked) {
                        settingsManager.setAlwaysLogout(true);
                    } else {
                        settingsManager.setAlwaysLogout(false);
                    }
            }
        }
    }

    public long getPreviousCategoryId() {
        return previousCategoryId;
    }

    public void setPreviousCategoryId(long previousCategoryId) {
        this.previousCategoryId = previousCategoryId;
    }

    private void populateCategorySpinner() {
        List<Category> categories = categoryManager.getAllCategories();

        if (categories != null) {
            categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,categories);
            categorySpinner.setAdapter(categoryAdapter);
        }
    }
}
