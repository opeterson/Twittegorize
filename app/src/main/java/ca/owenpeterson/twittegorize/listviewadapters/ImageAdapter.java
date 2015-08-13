package ca.owenpeterson.twittegorize.listviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import ca.owenpeterson.twittegorize.R;


/**
 * Created by Owen on 3/30/2015.
 *
 * Used by the tweet detail activity to create a listview of image URLS for the specific tweet.
 */
public class ImageAdapter extends ArrayAdapter<URL> {

    private TextView imageURL;
    private ImageView imageView;
    private Context context;

    public ImageAdapter(Context context, List<URL> images) {
        super(context, 0, images);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.plain_image_item, null);
        }

        imageURL = (TextView) view.findViewById(R.id.text_details_image_item);
        imageView = (ImageView) view.findViewById(R.id.list_item_image);

        URL url = getItem(position);

        String stringUrl = url.toString();
        imageURL.setText(stringUrl);

        Picasso.with(view.getContext()).load(stringUrl).into(imageView);
        return view;
    }
}
