package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TweetService;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.rest.TwitterClient;
import ca.owenpeterson.twittegorize.utils.OnFeedLoaded;


public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    private static final int RETURN_TO_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        TweetService tweetService = new TweetService(this);

        Tweet latestTweet = tweetService.getLatestTweet();

        long id = 0;

        if (null != latestTweet) {
            id = latestTweet.getId();
        }

        //String date = latestTweet.getCreatedDate();
        //DateTime latestTweetDate = JodaDateUtils.parseToDateTime(date);
        //DateTime today = new DateTime();

        //Intent i = new Intent(getBaseContext(), CategoryViewSelector.class);
        //startActivityForResult(i, RETURN_TO_LOGIN);

        OnFeedLoaded listener = new OnFeedLoaded() {
            @Override
            public void onFeedLoaded() {
                Intent i = new Intent(getBaseContext(), CategoryViewSelector.class);
                startActivityForResult(i, RETURN_TO_LOGIN);
            }
        };

        //TweetService tweetService = new TweetService(this);
        //tweetService.putTweetsToDatabase(listener);

        if (id != 0) {
            tweetService.putNewTweetsToDatabase(id, listener);
        } else {
            tweetService.putTweetsToDatabase(listener);
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
                    finish();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
