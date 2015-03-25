package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TweetService;
import ca.owenpeterson.twittegorize.utils.OnFeedLoaded;

public class TweetDetailsActivity extends ActionBarActivity {

    private ImageView imageProfile;
    private TextView textUserName;
    private TextView textScreenName;
    private TextView textTweetBody;
    private TextView textCreatedDate;
    private Button buttonReply;
    private Button buttonViewBrowser;
    private TweetLoadedHandler tweetLoadedHandler;
    private TweetService tweetService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        long tweetId = getIntent().getLongExtra("tweetId", 0);

        tweetService = new TweetService(TweetDetailsActivity.this);
        tweetLoadedHandler = new TweetLoadedHandler();

        //call tweetservice for tweet by id with listener

        imageProfile = (ImageView) findViewById(R.id.image_details_profile);
        textUserName = (TextView) findViewById(R.id.text_details_tweet_name);
        textScreenName = (TextView) findViewById(R.id.text_details_tweet_screen_name);
        textTweetBody = (TextView) findViewById(R.id.text_details_tweet_body);
        textCreatedDate = (TextView) findViewById(R.id.text_details_date_created);
        buttonReply = (Button) findViewById(R.id.button_details_reply);
        buttonViewBrowser = (Button) findViewById(R.id.button_details_view_in_browser);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                break;
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TweetLoadedHandler implements OnFeedLoaded {
        @Override
        public void onFeedLoaded() {

        }
    }
}
