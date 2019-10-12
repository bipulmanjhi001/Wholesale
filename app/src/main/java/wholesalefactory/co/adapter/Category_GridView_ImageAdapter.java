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
import wholesalefactory.co.model.Category_Grid_Model;

public class Category_GridView_ImageAdapter extends ArrayAdapter<Category_Grid_Model> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Category_Grid_Model> mGridData = new ArrayList<Category_Grid_Model>();

    public Category_GridView_ImageAdapter(Context mContext, int layoutResourceId, ArrayList<Category_Grid_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Category_Grid_Model> mGridData) {
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
            holder.titleTextView = (TextView) row.findViewById(R.id._grid_item_title);
            holder.url=(TextView)row.findViewById(R.id._grid_item_url);
            holder.id=(TextView)row.findViewById(R.id._grid_item_id);
            holder.imageView = (ImageView) row.findViewById(R.id._grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Category_Grid_Model item = mGridData.get(position);
        holder.titleTextView.setText(item.getName());
        holder.url.setText(item.getImage());
        holder.id.setText(item.getId());

        Picasso.get()
                .load(item.getImage())
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
