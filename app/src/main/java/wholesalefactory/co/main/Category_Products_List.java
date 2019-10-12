package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.Category_ProductAdpater;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.Category_ProductModel;
import wholesalefactory.co.model.VolleySingleton;

public class Category_Products_List extends AppCompatActivity {

    TextView cat_pro_title;
    ListView category_product;
    ImageView back_cat_pro;
    Category_ProductAdpater category_productAdpater;
    ArrayList<Category_ProductModel> category_productModels;
    ProgressBar progress_product;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens,pro_id,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category__products__list);
        cat_pro_title=(TextView)findViewById(R.id.cat_pro_title);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pro_id = bundle.getString("pro_id");
            name=bundle.getString("name");
            cat_pro_title.setText(name);
        }

        category_product=(ListView)findViewById(R.id.category_product);
        category_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected =((TextView)view.findViewById(R.id.category_pro_id)).getText().toString();
                String selectedimg=((TextView)view.findViewById(R.id.category_pro_url)).getText().toString();
                Intent intent=new Intent(Category_Products_List.this,Products_With_Profile.class);
                intent.putExtra("pro_id",pro_id);
                intent.putExtra("sub_pro_id",selected);
                intent.putExtra("name",name);
                intent.putExtra("category_pro_url",selectedimg);
                startActivity(intent);
                finish();
            }
        });
        category_productModels = new ArrayList<Category_ProductModel>();
        progress_product=(ProgressBar)findViewById(R.id.progress_product);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        back_cat_pro=(ImageView)findViewById(R.id.back_cat_pro);
        back_cat_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Category_Products_List.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        CallProductList();
    }

    private void CallProductList(){
        progress_product.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productcategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("productcategory");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    Category_ProductModel productModel = new Category_ProductModel(id, name, photo);
                                    category_productModels.add(productModel);
                                }
                            }else {
                                Toast.makeText(Category_Products_List.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            progress_product.setVisibility(View.GONE);
                            category_productAdpater = new Category_ProductAdpater(Category_Products_List.this,R.layout.category_product_list,category_productModels);
                            category_product.setAdapter(category_productAdpater);
                            category_productAdpater.notifyDataSetChanged();
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
                params.put("subcategory_id",pro_id);
                return params;
            }
        };

        VolleySingleton.getInstance(Category_Products_List.this).addToRequestQueue(stringRequest);
    }
}
