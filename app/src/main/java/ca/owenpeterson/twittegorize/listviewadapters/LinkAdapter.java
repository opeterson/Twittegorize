package ca.owenpeterson.twittegorize.listviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import ca.owenpeterson.twittegorize.R;

/**
 * Created by Owen on 3/26/2015.
 *
 * Used by the tweet detail activity to create a listview of URLS for the specific tweet.
 */
public class LinkAdapter extends ArrayAdapter<URL> {

    private TextView link;

    public LinkAdapter(Context context, List<URL> links) {
        super(context, 0, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.plain_url_item, null);
        }

        link = (TextView) view.findViewById(R.id.text_details_url_item);
        URL url = getItem(position);
        String text = url.toString();
        link.setText(text);

        return view;
    }
}
