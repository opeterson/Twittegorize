package ca.owenpeterson.twittegorize.views.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import ca.owenpeterson.twittegorize.applicationpersistence.SettingsManager;

/**
 * Created by Owen on 4/5/2015.
 */
public class BaseActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SettingsManager settingsManager = new SettingsManager(this);
        int themeId = settingsManager.getCurrentTheme();

        if (themeId != -1 ) {
            setTheme(themeId);
        }
        super.onCreate(savedInstanceState);
    }

}
