package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import wholesalefactory.co.R;
import wholesalefactory.co.model.TabAdapter;
import wholesalefactory.co.tablayouts.Attribute_Layout;
import wholesalefactory.co.tablayouts.Images_Layout;
import wholesalefactory.co.tablayouts.Info_Layout;

public class EditProduct extends AppCompatActivity {
    TabAdapter adapters;
    TabLayout tabLayouts;
    ViewPager viewPagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        viewPagers = (ViewPager)findViewById(R.id.edit_product_view);
        tabLayouts = (TabLayout)findViewById(R.id.edit_product_tab);
        adapters = new TabAdapter(getSupportFragmentManager());

        adapters.addFragment(new Info_Layout(), "Info");
        adapters.addFragment(new Images_Layout(), "Images");
        adapters.addFragment(new Attribute_Layout(), "Attribute");
        viewPagers.setAdapter(adapters);
        tabLayouts.setupWithViewPager(viewPagers);

    }
}
