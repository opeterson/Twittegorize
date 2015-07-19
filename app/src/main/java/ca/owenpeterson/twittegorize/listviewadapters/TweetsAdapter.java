package ca.owenpeterson.twittegorize.listviewadapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.User;
import ca.owenpeterson.twittegorize.utils.JodaDateUtils;

/**
 * Created by Owen on 3/10/2015.
 *
 * ArrayAdapter used to display a Tweet in the listView of the TwitterFeedFragment
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> {

    private ImageView imageView;
    private TextView nameView;
    private TextView bodyView;
    private TextView screenNameView;
    protected TextView tweetAgeView;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.tweet_item, null);
        }

        Tweet tweet = getItem(position);
        User user = tweet.getUser();

        imageView = (ImageView) view.findViewById(R.id.image_profile);
        String imageURL = user.getProfileImageUrl();
        Picasso.with(this.getContext()).load(imageURL).noFade().fit().into(imageView);

        screenNameView = (TextView) view.findViewById(R.id.tweet_screen_name);
        String screenName = user.getScreenName();
        screenNameView.setText("@" + screenName);


        nameView = (TextView) view.findViewById(R.id.tweet_name);
        String name = user.getName();
        nameView.setText(name);

        bodyView = (TextView) view.findViewById(R.id.tweet_body);
        String tweetBody = tweet.getBody();
        bodyView.setText(Html.fromHtml(tweetBody));


        tweetAgeView = (TextView) view.findViewById(R.id.tweet_age);
        String dateString = JodaDateUtils.formatDate(tweet.getCreatedDate());
        String age = getFormattedTweetAge(dateString);
        tweetAgeView.setText(age);

        return view;
    }

    public String getFormattedTweetAge(String dateString) {

        String formattedAge;
        DateTime tweetDate = JodaDateUtils.parseDateTime(dateString);
        DateTime now = new DateTime();

        Seconds secondsBetween = Seconds.secondsBetween(tweetDate, now);
        int seconds = secondsBetween.getSeconds();

        if (seconds < 60) {
            formattedAge = String.valueOf(seconds) + "s";
        } else {
            Minutes minutesBetween = Minutes.minutesBetween(tweetDate, now);
            int minutes = minutesBetween.getMinutes();

            if (minutes < 60) {
                formattedAge = String.valueOf(minutes) + "m";
            } else {
                Hours hoursBetween = Hours.hoursBetween(tweetDate, now);
                int hours = hoursBetween.getHours();

                if (hours < 24) {
                    formattedAge = String.valueOf(hours) + "h";

                } else {
                    Days daysBetween = Days.daysBetween(tweetDate, now);
                    int days = daysBetween.getDays();
                    formattedAge = String.valueOf(days) + "d";
                }
            }
        }

        return formattedAge;
    }
}
