package ca.owenpeterson.twittegorize.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;

import ca.owenpeterson.twittegorize.R;

/**
 * Created by Owen on 4/4/2015.
 */
public class SettingsManager extends ActionBarActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SettingsManager(Context context) {
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
}
