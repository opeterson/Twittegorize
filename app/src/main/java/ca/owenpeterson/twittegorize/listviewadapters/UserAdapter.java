package ca.owenpeterson.twittegorize.listviewadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import ca.owenpeterson.twittegorize.R;
import ca.owenpeterson.twittegorize.data.TwitterUserManager;
import ca.owenpeterson.twittegorize.models.User;
import ca.owenpeterson.twittegorize.views.activities.CategoryUserView;

/**
 * Created by Owen on 3/19/2015.
 *
 * ArrayAdapter class used to display a user in the ListView of the CategoryUserView class.
 * This list is used to add/remove users from a category
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Context context;
    private TextView nameView;
    private TextView screenNameView;
    private CheckBox checkBox;
    private List<Long> userIdsInCategory;
    private TwitterUserManager userManager;
    private long categoryId;
    private List<User> users;

    public UserAdapter(Context context, int resource, List<User> objects, long categoryId) {
        super(context, resource, objects);
        this.context = context;
        this.categoryId = categoryId;
        this.users = objects;
        userManager = new TwitterUserManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.modify_category_user_item, null);
        }

        User user = getItem(position);
        userIdsInCategory = userManager.getUserIdsInCategory(categoryId);

        String userName = user.getName();
        String screenName = user.getScreenName();

        checkBox = (CheckBox) view.findViewById(R.id.checkbox_in_category);

        if (StringUtils.isNotBlank(userName)) {
            nameView = (TextView) view.findViewById(R.id.text_user_name);
            nameView.setText(userName);
        }

        if (StringUtils.isNotBlank(screenName)) {
            screenNameView = (TextView) view.findViewById(R.id.text_screen_name);
            screenNameView.setText("@" + screenName);
        }

        long userId = user.getId();

        //if the user is already in the current category, check the checkbox next to thier name.
        if (userIdsInCategory.contains(userId)) {
            checkBox.setChecked(true);
        }

        return view;
    }

    //these overrides were required in order for the list to maintain its state when scrolling.
    //Otherwise it was showing different users as checked or unchecked when they shouldn't be.
    @Override

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
