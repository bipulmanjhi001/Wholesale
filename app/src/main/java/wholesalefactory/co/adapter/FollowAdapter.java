package wholesalefactory.co.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import wholesalefactory.co.R;
import wholesalefactory.co.model.FollowModel;

public class FollowAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<FollowModel> mylist = new ArrayList<FollowModel>();

    public FollowAdapter(ArrayList<FollowModel> itemArray, Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView shop_name,user_id,location;
        private ImageView image;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {
                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.follow_list, null);
                view.user_id = (TextView) convertView.findViewById(R.id.follow_user_id);
                view.location = (TextView) convertView.findViewById(R.id.follow_location);
                view.shop_name = (TextView) convertView.findViewById(R.id.follow_shop_name);
                view.image = (ImageView) convertView.findViewById(R.id.follow_image);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.user_id.setTag(position);
            view.user_id.setText(mylist.get(position).getUser_id());
            view.location.setText(mylist.get(position).getLocation());
            view.shop_name.setText(mylist.get(position).getShop_name());

            try {
                Glide.with(mContext).load(mylist.get(position).getImage())
                        .error(R.drawable.no_image_available)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(view.image);
            }catch (IllegalArgumentException e){
                e.printStackTrace();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}