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
import wholesalefactory.co.model.OrderModel;

public class OrderAdapter  extends BaseAdapter {
    private Context mContext;
    ArrayList<OrderModel> mylist = new ArrayList<OrderModel>();

    public OrderAdapter(ArrayList<OrderModel> itemArray, Context mContext) {
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
        private TextView shop_name,name,quantity,price,discount;;
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
                convertView = inflator.inflate(R.layout.order_list, null);
                view.name = (TextView) convertView.findViewById(R.id.order_name);
                view.quantity = (TextView) convertView.findViewById(R.id.order_quantity);
                view.shop_name = (TextView) convertView.findViewById(R.id.order_shop_name);
                view.image = (ImageView) convertView.findViewById(R.id.order_image);
                view.price = (TextView) convertView.findViewById(R.id.order_price);
                view.discount = (TextView) convertView.findViewById(R.id.order_discount);
                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.name.setTag(position);
            view.name.setText(mylist.get(position).getName());
            view.quantity.setText(mylist.get(position).getQuantity());
            view.price.setText(mylist.get(position).getPrice());
            view.discount.setText(mylist.get(position).getDiscount());
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