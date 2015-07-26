package ca.owenpeterson.twittegorize.applicationpersistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.utils.AppConstants;

/**
 * Created by Owen on 4/4/2015.
 *
 * Uses Shared Preferences to persist settings for the application.
 * Provides the ability to get setting values from shared preferences.
 */
public class SettingsManager extends ActionBarActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SettingsManager(Context context) {

        //a context seems to be required in order to use shared preferences.
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(AppConstants.Strings.SETTINGS_PREFERENCES_ID, MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void setThemeDark() {
        editor.putInt(AppConstants.Strings.THEME, R.style.Dark);
        editor.commit();

    }

    public void setThemeLight() {
        editor.putInt(AppConstants.Strings.THEME, R.style.AppTheme);
        editor.commit();
    }

    public int getCurrentTheme() {
        return sharedPreferences.getInt(AppConstants.Strings.THEME, -1);
    }

    public void setAlwaysLogout(boolean alwaysLogout) {
        editor.putBoolean(AppConstants.Strings.ALWAYS_LOGOUT, alwaysLogout);
        editor.commit();
    }

    public boolean getAlwaysLogout() {
        boolean alwaysLogout = sharedPreferences.getBoolean(AppConstants.Strings.ALWAYS_LOGOUT, false);
        return alwaysLogout;
    }

    public void setDefaultCategoryIndex(int defaultIndex) {
        editor.putInt(AppConstants.Strings.DEFAULT_CATEGORY_INDEX, defaultIndex);
        editor.commit();
    }

    public int getDefaultCategoryIndex() {
        int defaultIndex = sharedPreferences.getInt(AppConstants.Strings.DEFAULT_CATEGORY_INDEX, -1);
        return defaultIndex;
    }

    public void setOpenTwitterDefault(boolean openTwitterDefault) {
        editor.putBoolean(AppConstants.Strings.OPEN_TWITTER_DEFAULT, openTwitterDefault);
        editor.commit();
    }

    public boolean getOpenTwitterDefault() {
        boolean openTwitterDefault = sharedPreferences.getBoolean(AppConstants.Strings.OPEN_TWITTER_DEFAULT, false);
        return openTwitterDefault;
    }
}
