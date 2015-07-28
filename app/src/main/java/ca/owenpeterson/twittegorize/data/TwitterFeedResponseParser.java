package ca.owenpeterson.twittegorize.data;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.owenpeterson.twittegorize.models.Retweet;
import ca.owenpeterson.twittegorize.models.RetweetedUser;
import ca.owenpeterson.twittegorize.models.Tweet;
import ca.owenpeterson.twittegorize.models.User;

/**
 * Created by owen on 7/27/15.
 */
public class TwitterFeedResponseParser {

    private JSONArray response;

    public TwitterFeedResponseParser(JSONArray response) {
        this.response = response;
    }

    public Map<String, List> parseResponse(JSONArray response) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Retweet> retweets = new ArrayList<>();
        ArrayList<RetweetedUser> retweetedUsers = new ArrayList<>();
        Map<String, List> resultMap = new HashMap<String, List>();

        return resultMap;
    }




}
