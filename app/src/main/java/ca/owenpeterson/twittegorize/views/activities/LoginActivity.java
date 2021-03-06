package ca.owenpeterson.twittegorize.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.codepath.oauth.OAuthLoginActionBarActivity;

import org.joda.time.DateTime;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.applicationpersistence.SettingsManager;
import ca.owenpeterson.twittegorize.data.RetweetManager;
import ca.owenpeterson.twittegorize.data.TweetManager;
import ca.owenpeterson.twittegorize.listeners.OnFeedLoaded;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.rest.TwitterClient;

/**
 * Provides the ability for the user to log into thier twitter account using the OAuth2 protocol
 *
 * The OAuth2 is handled by a library that was part of a rest client template I got from this GitHub
 * project: https://github.com/codepath/android-rest-client-template
 *
 */
public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    private static final int RETURN_TO_LOGIN = 1;
    private SettingsManager settingsManager;
    private LinearLayout loginLayout;
    private LinearLayout noConnectionLayout;
    private TweetManager tweetManager;
    private RetweetManager retweetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginLayout = (LinearLayout) findViewById(R.id.pane_login);
        noConnectionLayout = (LinearLayout) findViewById(R.id.pane_no_connection);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!isConnected)
        {
            loginLayout.setVisibility(View.INVISIBLE);
            noConnectionLayout.setVisibility(View.VISIBLE);
        } else {
            loginLayout.setVisibility(View.VISIBLE);
            settingsManager = new SettingsManager(this);
        }
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    // OAuth authenticated successfully, launch primary authenticated activity
    // i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
        tweetManager = new TweetManager(this);
        retweetManager = new RetweetManager();

        Tweet latestTweet = tweetManager.getLatestTweet();

        long id = 0;
        if (null != latestTweet) {
            id = latestTweet.getId();
        }

        OnFeedLoaded listener = new OnFeedLoaded() {
            @Override
            public void onFeedLoaded() {
                Intent i = new Intent(getBaseContext(), CategoryViewSelector.class);
                startActivityForResult(i, RETURN_TO_LOGIN);
            }
        };

        if (id != 0) {
            //don't call twitter, just trigger the listener to go straight to the activity.
            listener.onFeedLoaded();
        } else {
            tweetManager.putTweetsToDatabase(listener);
        }
    }

    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }

    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RETURN_TO_LOGIN:
                if (resultCode == RESULT_OK) {
                    boolean alwaysLogout = settingsManager.getAlwaysLogout();

                    if (alwaysLogout) {
                        TwitterApplication.getRestClient().clearAccessToken();
                    }

                    finish();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
