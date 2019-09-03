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
import wholesalefactory.co.model.Top_Sallers_Model;

public class Top_Sallers_Adapter extends ArrayAdapter<Top_Sallers_Model> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Top_Sallers_Model> mGridData = new ArrayList<Top_Sallers_Model>();

    public Top_Sallers_Adapter(Context mContext, int layoutResourceId, ArrayList<Top_Sallers_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Top_Sallers_Model> mGridData) {
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
            holder.url=(TextView)row.findViewById(R.id.feature_url);
            holder.id=(TextView)row.findViewById(R.id.feature_id1);
            holder.imageView = (ImageView) row.findViewById(R.id.img_feature1);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        Top_Sallers_Model item = mGridData.get(position);
        holder.url.setText(item.getImage());
        holder.id.setText(item.getId());

        Picasso.with(mContext)
                .load(item.getImage())
                .fit().centerCrop()
                .into(holder.imageView);

        return row;
    }
    private static class ViewHolder {
        TextView url,id;
        ImageView imageView;
    }
}
