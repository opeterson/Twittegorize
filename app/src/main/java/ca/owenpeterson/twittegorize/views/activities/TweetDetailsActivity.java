package ca.owenpeterson.twittegorize.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TweetService;
import ca.owenpeterson.twittegorize.models.DetailTweet;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.utils.OnDetailTweetLoaded;

public class TweetDetailsActivity extends ActionBarActivity {

    private ImageView imageProfile;
    private TextView textUserName;
    private TextView textScreenName;
    private TextView textTweetBody;
    private TextView textCreatedDate;
    private Button buttonReply;
    private Button buttonViewBrowser;
    private TweetLoadedListener tweetLoadedListener;
    private TweetService tweetService;
    private DetailTweet detailTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        long tweetId = getIntent().getLongExtra("tweetId", 0);

        tweetService = new TweetService(TweetDetailsActivity.this);
        tweetLoadedListener = new TweetLoadedListener();

        //call tweetservice for tweet by id with listener
        TweetLoadedListener listener = new TweetLoadedListener();
        DetailTweetResponseHandler handler = new DetailTweetResponseHandler(listener);
        TwitterApplication.getRestClient().getTweetById(tweetId, handler);

        imageProfile = (ImageView) findViewById(R.id.image_details_profile);
        textUserName = (TextView) findViewById(R.id.text_details_tweet_name);
        textScreenName = (TextView) findViewById(R.id.text_details_tweet_screen_name);
        textTweetBody = (TextView) findViewById(R.id.text_details_tweet_body);
        textCreatedDate = (TextView) findViewById(R.id.text_details_date_created);
        buttonReply = (Button) findViewById(R.id.button_details_reply);
        buttonViewBrowser = (Button) findViewById(R.id.button_details_view_in_browser);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private class TweetLoadedListener implements OnDetailTweetLoaded {
        @Override
        public void onDetailTweetLoaded(DetailTweet detailTweet) {
            String imageURL = detailTweet.getUser().getProfileImageUrl();
            //imageURL = StringUtils.replace(imageURL,".png", "_bigger.png");

            Log.d(this.getClass().getName(), imageURL);

            String name = detailTweet.getUser().getName();
            String screenName = detailTweet.getUser().getScreenName();
            String tweetBody = detailTweet.getBody();
            String createdDate = detailTweet.getCreatedDate();

            Picasso.with(TweetDetailsActivity.this).load(imageURL).noFade().fit().into(imageProfile);
            textUserName.setText(name);
            textScreenName.setText("@" + screenName);
            textTweetBody.setText(tweetBody);
            textCreatedDate.setText(createdDate);
        }
    }

    private class DetailTweetResponseHandler extends JsonHttpResponseHandler {

        private OnDetailTweetLoaded listener;

        public DetailTweetResponseHandler(OnDetailTweetLoaded listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            //build the detailTweet here.
            detailTweet = DetailTweet.fromJson(response);
            listener.onDetailTweetLoaded(detailTweet);
            super.onSuccess(statusCode, headers, response);
        }

        public void setListener(OnDetailTweetLoaded listener) {
            this.listener = listener;
        }
    }
}
