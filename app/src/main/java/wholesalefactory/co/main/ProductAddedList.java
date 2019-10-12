package wholesalefactory.co.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.ProductAdapter;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ProductModel;
import wholesalefactory.co.model.ProductUpdateModel;
import wholesalefactory.co.model.VolleySingleton;

public class ProductAddedList extends AppCompatActivity {

    ImageView back_from_update;
    ListView product_list;
    ProgressBar progressBar;
    ArrayList<ProductUpdateModel> productUpdateModels;
    ProductAdapter productAdapter;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;

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
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        product_list=(ListView)findViewById(R.id.product_added_list);
        progressBar=(ProgressBar)findViewById(R.id.progressBar_added);
        product_list.setDivider(null);
        product_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        productUpdateModels = new ArrayList<ProductUpdateModel>();

        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selected =((TextView)v.findViewById(R.id._item_id_pro)).getText().toString();
                Intent intent=new Intent(ProductAddedList.this,EditProduct.class);
                intent.putExtra("productid",selected);
                startActivity(intent);
                finish();

            }
        });
        CallProduct();
    }

    public class ProductAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<ProductUpdateModel> mylist = new ArrayList<ProductUpdateModel>();

        public ProductAdapter(ArrayList<ProductUpdateModel> itemArray, Context mContext) {
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
            private TextView id,name;
            private CircleImageView att_img;
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
                    convertView = inflator.inflate(R.layout.product_update_list, null);
                    view.id = (TextView) convertView.findViewById(R.id._item_id_pro);
                    view.name = (TextView) convertView.findViewById(R.id._grid_item_title_name);
                    view.att_img=(CircleImageView)convertView.findViewById(R.id._grid_item_image_);

                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText(mylist.get(position).getId());
                view.name.setText(mylist.get(position).getName());
                Glide.with(mContext)
                        .load(mylist.get(position).getUrl())
                        .into(view.att_img);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
    private void CallProduct(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_userproducts,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("products");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    ProductUpdateModel productModel = new ProductUpdateModel(id, name, photo);
                                    productUpdateModels.add(productModel);
                                }
                            }else {
                                Toast.makeText(ProductAddedList.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            progressBar.setVisibility(View.GONE);
                            productAdapter = new ProductAdapter(productUpdateModels,ProductAddedList.this);
                            product_list.setAdapter(productAdapter);
                            productAdapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token",tokens);
                return params;
            }
        };

        VolleySingleton.getInstance(ProductAddedList.this).addToRequestQueue(stringRequest);
    }
}
