package wholesalefactory.co.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import wholesalefactory.co.R;
import wholesalefactory.co.main.BottomProductView;
import wholesalefactory.co.main.Vendor_Profile_With_Products;

public class DownPageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils2> sliderImg;
    private ImageLoader imageLoader;
    SliderUtils2 utils;
    public DownPageAdapter(List sliderImg,Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout2, null);

        utils = sliderImg.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView ids= (TextView) view.findViewById(R.id.ids);
        TextView names= (TextView) view.findViewById(R.id.names);
        names.setText(utils.getName());

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getSliderImageUrl(), ImageLoader.getImageListener(imageView, R.drawable.ic_launcher_background, R.drawable.ic_launcher_background));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =sliderImg.get(position).getId();
                Intent intent=new Intent(context, BottomProductView.class);
                intent.putExtra("pro_id",id);
                intent.putExtra("name",sliderImg.get(position).getName());
                intent.putExtra("image",sliderImg.get(position).getSliderImageUrl());
                context.startActivity(intent);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

}
