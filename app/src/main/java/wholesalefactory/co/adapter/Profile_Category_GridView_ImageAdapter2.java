package wholesalefactory.co.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import wholesalefactory.co.R;
import wholesalefactory.co.model.Profile_Category_Grid_Model2;

public class Profile_Category_GridView_ImageAdapter2 extends ArrayAdapter<Profile_Category_Grid_Model2> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Profile_Category_Grid_Model2> mGridData = new ArrayList<Profile_Category_Grid_Model2>();

    public Profile_Category_GridView_ImageAdapter2(Context mContext, int layoutResourceId, ArrayList<Profile_Category_Grid_Model2> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Profile_Category_Grid_Model2> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.cat_sub_name);
            holder.url=(TextView)row.findViewById(R.id.cat_sub_url);
            holder.id=(TextView)row.findViewById(R.id.cat_sub_id);
            holder.imageView = (ImageView) row.findViewById(R.id.cat_sub_img);
            holder.prices=(TextView)row.findViewById(R.id.cat_sub_price);
            holder.desc=(TextView)row.findViewById(R.id.cat_sub_desc);

            holder.category=(TextView)row.findViewById(R.id.category);
            holder.subcategory = (TextView) row.findViewById(R.id.subcategory);
            holder.productcategory=(TextView)row.findViewById(R.id.productcategory);
            holder.productsubcategory=(TextView)row.findViewById(R.id.productsubcategory);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Profile_Category_Grid_Model2 item = mGridData.get(position);
        holder.titleTextView.setText(item.getNames());
        holder.url.setText(item.getPro_img());
        holder.id.setText(item.getId());
        holder.prices.setText(item.getPrice());
        holder.desc.setText(item.getMin_qty());
        holder.category.setText(item.getCategory());
        holder.subcategory.setText(item.getSubcategory());
        holder.productcategory.setText(item.getProductcategory());
        holder.productsubcategory.setText(item.getProductsubcategory());

        Glide.with(mContext)
                .load(item.getPro_img())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .crossFade()
                .skipMemoryCache(true)
                .placeholder(R.drawable.no_image_available)
                .animate(R.anim.bounce)
                .into(holder.imageView);
        return row;
    }
    private static class ViewHolder {
        TextView titleTextView,category,subcategory,productcategory,productsubcategory;
        TextView url,id,prices,desc;
        ImageView imageView;
    }
}
