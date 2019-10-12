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
import wholesalefactory.co.model.New_Arrivales_Model;

public class New_Arrivales_Adapter extends ArrayAdapter<New_Arrivales_Model> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<New_Arrivales_Model> mGridData = new ArrayList<New_Arrivales_Model>();

    public New_Arrivales_Adapter(Context mContext, int layoutResourceId, ArrayList<New_Arrivales_Model> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<New_Arrivales_Model> mGridData) {
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
            holder.url=(TextView)row.findViewById(R.id.new_arrival_url);
            holder.id=(TextView)row.findViewById(R.id.new_arrival_id);
            holder.name=(TextView)row.findViewById(R.id.new_arrival_name);
            holder.imageView = (ImageView) row.findViewById(R.id.new_arrival_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        New_Arrivales_Model item = mGridData.get(position);
        holder.url.setText(item.getImage());
        holder.id.setText(item.getId());
        holder.name.setText(item.getName());

        Picasso.get()
                .load(item.getImage())
                .fit().centerCrop()
                .into(holder.imageView);

        return row;
    }
    private static class ViewHolder {
        TextView url,id,name;
        ImageView imageView;
    }
}
