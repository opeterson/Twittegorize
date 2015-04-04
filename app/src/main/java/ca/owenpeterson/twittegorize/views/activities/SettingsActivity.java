package ca.owenpeterson.twittegorize.views.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.utils.SettingsManager;

public class SettingsActivity extends ActionBarActivity {
    private Switch themeSwitch;
    private SettingsManager settingsManager;
    private SwitchListener switchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);
        themeSwitch = (Switch) findViewById(R.id.switch_theme);
        switchListener = new SwitchListener();
        themeSwitch.setOnCheckedChangeListener(switchListener);
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

    private class SwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int id = buttonView.getId();

            switch(id) {
                case R.id.switch_theme:
                    if (isChecked) {
                        settingsManager.setThemeDark();
                    } else {
                        settingsManager.setThemeLight();
                    }
            }
        }
    }
}
