package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import wholesalefactory.co.R;

public class ProductAddedList extends AppCompatActivity {
    ImageView back_from_update;
    TextView summery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_added_list);

        back_from_update=(ImageView)findViewById(R.id.back_from_update);
        back_from_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent intent=new Intent(ProductAddedList.this,Home.class);
             startActivity(intent);
             finish();
            }
        });

        summery=(TextView)findViewById(R.id.summery);
        summery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProductAddedList.this,EditProduct.class);
                startActivity(intent);
            }
        });
    }
}
