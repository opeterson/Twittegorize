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
 * Created by Owen on 3/30/2015.
 */
public class ImageAdapter extends ArrayAdapter<URL> {

    private TextView imageURL;

    public ImageAdapter(Context context, List<URL> images) {
        super(context, 0, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.plain_image_item, null);
        }

        imageURL = (TextView) view.findViewById(R.id.text_details_image_item);

        URL url = getItem(position);
        String stringURL = url.toString();
        imageURL.setText(stringURL);
        return view;
    }
}
