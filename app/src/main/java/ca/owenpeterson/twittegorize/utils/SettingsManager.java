package ca.owenpeterson.twittegorize.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import ca.owenpeterson.twittegorize.R;

/**
 * Created by Owen on 4/4/2015.
 */
public class SettingsManager extends Activity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    
    public SettingsManager() {
        sharedPreferences = getSharedPreferences(AppConstants.Strings.SETTINGS_PREFERENCES_ID, MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
}
