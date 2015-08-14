package ca.owenpeterson.twittegorize.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.listeners.OnDetailTweetLoaded;
import ca.owenpeterson.twittegorize.listviewadapters.ImageAdapter;
import ca.owenpeterson.twittegorize.listviewadapters.LinkAdapter;
import ca.owenpeterson.twittegorize.models.DetailTweet;
import ca.owenpeterson.twittegorize.rest.TwitterApplication;
import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

/**
 * View used to display the selected activity with more detail, including links and image URLS.
 * In the future I may want to make it so that the image displays automatically using the URL.
 * I may also want it to show the other replies to the selected tweet, though perhaps the button
 * to view the tweet on twitter is good enough for that.
 */
public class TweetDetailsActivity extends BaseActivity {

    private ImageView imageProfile;
    private TextView textUserName;
    private TextView textScreenName;
    private TextView textTweetBody;
    private TextView textCreatedDate;
    private Button buttonReply;
    private Button buttonViewBrowser;
    private TweetLoadedListener tweetLoadedListener;
    private ListView urlsListView;
    private ListView imagesListView;
    private DetailTweet detailTweet;
    private ButtonClickHandler buttonClickHandler;
    private LinkAdapter linkAdapter;
    private ImageAdapter imageAdapter;
    private LinearLayout entitiesLayout;
    private LinearLayout linkLayout;
    private LinearLayout imageLayout;
    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        long tweetId = getIntent().getLongExtra("tweetId", 0);

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
        entitiesLayout = (LinearLayout) findViewById(R.id.pane_entities);
        linkLayout = (LinearLayout) findViewById(R.id.subpane_links);
        imageLayout = (LinearLayout) findViewById(R.id.subpane_images);
        buttonLayout = (LinearLayout) findViewById(R.id.pane_button_bar);
        entitiesLayout.setVisibility(LinearLayout.GONE);
        linkLayout.setVisibility(LinearLayout.GONE);
        imageLayout.setVisibility(LinearLayout.GONE);



        buttonClickHandler = new ButtonClickHandler();

        buttonReply = (Button) findViewById(R.id.button_details_reply);
        buttonReply.setOnClickListener(buttonClickHandler);

        buttonViewBrowser = (Button) findViewById(R.id.button_details_view_in_browser);
        buttonViewBrowser.setOnClickListener(buttonClickHandler);
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

    private class ButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch(id) {
                case R.id.button_details_reply:
                    Toast.makeText(TweetDetailsActivity.this, "This feature is not available yet.", Toast.LENGTH_LONG).show();
                    break;
                case R.id.button_details_view_in_browser:
                    handleOpenLinkExternally();
                    break;
            }
        }
    }

    private void handleOpenLinkExternally() {
        String tweetId = String.valueOf(detailTweet.getTweetId());
        String screenName = String.valueOf(detailTweet.getUser().getScreenName());

        String url = "https://twitter.com/" + screenName + "/status/" + tweetId;
        Intent browser = new Intent (Intent.ACTION_VIEW, Uri.parse(url) );
        startActivity(browser);
    }

    private class TweetLoadedListener implements OnDetailTweetLoaded {
        @Override
        public void onDetailTweetLoaded(DetailTweet detailTweet) {
            String imageURL = detailTweet.getUser().getProfileImageUrl();

            //add a setting for this, high quality images, hide images, etc
            imageURL = StringUtils.replace(imageURL, "_normal", "_bigger");

            String name = detailTweet.getUser().getName();
            String screenName = detailTweet.getUser().getScreenName();
            String tweetBody = detailTweet.getBody();
            DateTime createdDate = detailTweet.getCreatedDate();
            List<URL> urls = detailTweet.getUrls();
            List<URL> images = detailTweet.getImages();

            Picasso.with(TweetDetailsActivity.this).load(imageURL).noFade().fit().into(imageProfile);
            textUserName.setText(name);
            textScreenName.setText("@" + screenName);
            textTweetBody.setText(tweetBody);

            String formattedDate = JodaDateUtils.formatDate(createdDate);
            textCreatedDate.setText(formattedDate);

            Boolean hasLinks = urls.size() > 0;
            Boolean hasImages = images.size() > 0;

            if (hasLinks || hasImages) {
                entitiesLayout.setVisibility(LinearLayout.VISIBLE);
                LayoutInflater inflater = (LayoutInflater) TweetDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (hasLinks) {
                    LinearLayout urlLayout = (LinearLayout) findViewById(R.id.urls_layout);
                    for (URL url : urls) {
                        View urlItemView = inflater.inflate(R.layout.plain_url_item, null);
                        TextView urlTextView = (TextView) urlItemView.findViewById(R.id.text_details_url_item);
                        urlTextView.setText(url.toString());
                        urlLayout.addView(urlItemView);
                    }
                    linkLayout.setVisibility(LinearLayout.VISIBLE);
                }

                if(hasImages) {
                    LinearLayout imageViewLayout = (LinearLayout) findViewById(R.id.images_layout);

                    for (URL detailImageUrl : images) {
                        View imageItemView = inflater.inflate(R.layout.plain_image_item, null);
                        TextView imageUrlText = (TextView) imageItemView.findViewById(R.id.text_details_image_item);
                        ImageView imageView = (ImageView) imageItemView.findViewById(R.id.details_image_item);

                        imageUrlText.setText(detailImageUrl.toString());
                        Picasso.with(imageItemView.getContext()).load(detailImageUrl.toString()).into(imageView);
                        imageViewLayout.addView(imageItemView);
                    }
                    imageLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }

            buttonLayout.setVisibility(LinearLayout.VISIBLE);

        }
    }

    private class DetailTweetResponseHandler extends JsonHttpResponseHandler {

        private OnDetailTweetLoaded listener;

        public DetailTweetResponseHandler(OnDetailTweetLoaded listener) {
            this.listener = listener;
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            detailTweet = DetailTweet.fromJson(response);
            listener.onDetailTweetLoaded(detailTweet);
            super.onSuccess(statusCode, headers, response);
        }

        public void setListener(OnDetailTweetLoaded listener) {
            this.listener = listener;
        }
    }
}
