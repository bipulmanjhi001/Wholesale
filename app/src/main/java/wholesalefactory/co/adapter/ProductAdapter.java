package wholesalefactory.co.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import wholesalefactory.co.R;
import wholesalefactory.co.model.ProductModel;
import wholesalefactory.co.pojo.Category_Grid_Model;

public class ProductAdapter extends ArrayAdapter<ProductModel> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<ProductModel> mGridData = new ArrayList<ProductModel>();

    public ProductAdapter(Context mContext, int layoutResourceId, ArrayList<ProductModel> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<ProductModel> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id._grid_item_title_pro);
            holder.url=(TextView)row.findViewById(R.id._grid_item_url_pro);
            holder.id=(TextView)row.findViewById(R.id._grid_item_id_pro);
            holder.imageView = (ImageView) row.findViewById(R.id._grid_item_image_pro);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        ProductModel item = mGridData.get(position);
        holder.titleTextView.setText(item.getName());
        holder.url.setText(item.getUrl());
        holder.id.setText(item.getId());

        Picasso.with(mContext)
                .load(item.getUrl())
                .fit().centerCrop()
                .into(holder.imageView);

        return row;
    }
    private static class ViewHolder {
        TextView titleTextView;
        TextView url,id;
        ImageView imageView;
    }
}
