package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.Profile_Category_GridView_ImageAdapter2;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ExpandableHeightGridView;
import wholesalefactory.co.model.Profile_Category_Grid_Model2;
import wholesalefactory.co.model.VolleySingleton;

public class Vendor_Profile_With_Products_For_Main extends AppCompatActivity {

    Profile_Category_GridView_ImageAdapter2 profile_category_gridView_imageAdapter;
    ExpandableHeightGridView expandableHeightGridView;
    ProgressBar progressBar;
    ArrayList<Profile_Category_Grid_Model2> products_with_profileModels;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens,user_ids,shop_name;;
    CircleImageView profile_product_img, pro_img;
    TextView min_qty, profile_product_shop_name, profile_product_name, profile_product_location;
    ImageView back_from_productdetails, product_cart;
    TextView title_product_name;
    String _id,pro_id,name,sub_pro_id,category_pro_url;
    TextView cart_counts;
    Button Follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__profile__with__products__for__main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _id=bundle.getString("_id");
            pro_id=bundle.getString("pro_id");
            name=bundle.getString("name");
            sub_pro_id=bundle.getString("sub_pro_id");
            category_pro_url=bundle.getString("category_pro_url");
        }

        expandableHeightGridView = (ExpandableHeightGridView) findViewById(R.id.load_sub_category_list);
        expandableHeightGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Profile_Category_Grid_Model2 profile_category_grid_model2=(Profile_Category_Grid_Model2) adapterView.getItemAtPosition(i);
                Intent intent=new Intent(Vendor_Profile_With_Products_For_Main.this,Profile_Product_One_For_Main.class);
                intent.putExtra("id",profile_category_grid_model2.getId());
                intent.putExtra("_id",_id);
                intent.putExtra("pro_id",pro_id);
                intent.putExtra("name",name);
                intent.putExtra("subcategory_id",sub_pro_id);
                intent.putExtra("productcategory_id",profile_category_grid_model2.getProductcategory());
                intent.putExtra("image",profile_category_grid_model2.getPro_img());
                intent.putExtra("category",profile_category_grid_model2.getCategory());
                intent.putExtra("category_pro_url",category_pro_url);
                startActivity(intent);
                finish();

            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        products_with_profileModels = new ArrayList<Profile_Category_Grid_Model2>();
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        min_qty = (TextView) findViewById(R.id.min_qty);
        profile_product_img = (CircleImageView) findViewById(R.id.profile_product_img);
        pro_img = (CircleImageView) findViewById(R.id.pro_img);
        profile_product_shop_name = (TextView) findViewById(R.id.profile_product_shop_name);
        profile_product_name = (TextView) findViewById(R.id.profile_product_name);
        profile_product_location = (TextView) findViewById(R.id.profile_product_location);
        cart_counts=(TextView)findViewById(R.id.product_cart_count);
        product_cart = (ImageView) findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vendor_Profile_With_Products_For_Main.this, Cart.class);
                startActivity(intent);
                finish();
            }
        });
        back_from_productdetails = (ImageView) findViewById(R.id.back_from_productdetails);
        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Vendor_Profile_With_Products_For_Main.this, Products_With_Profile.class);
                intent.putExtra("pro_id",pro_id);
                intent.putExtra("name",name);
                intent.putExtra("sub_pro_id",sub_pro_id);
                intent.putExtra("category_pro_url",category_pro_url);
                startActivity(intent);
                finish();
            }
        });
        title_product_name = (TextView) findViewById(R.id.title_product_name);
        Follow=(Button)findViewById(R.id.Follow);
        Follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Follow();
            }
        });
        VendorProfile();
    }

    private void VendorProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_userlistings,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {
                                JSONObject jsonObject = obj.getJSONObject("result");
                                JSONObject dashobj = jsonObject.getJSONObject("user");

                                user_ids = dashobj.getString("user_id");
                                String name = dashobj.getString("name");
                                profile_product_name.setText(name);
                                shop_name = dashobj.getString("shop_name");
                                profile_product_shop_name.setText(shop_name);
                                String location = dashobj.getString("location");
                                profile_product_location.setText(location);
                                String title = shop_name.concat(" " + location);
                                title_product_name.setText(title);
                                String image = dashobj.getString("image");

                                Picasso.get()
                                        .load(image)
                                        .fit().centerCrop()
                                        .into(profile_product_img);

                                Picasso.get()
                                        .load(image)
                                        .fit().centerCrop()
                                        .into(pro_img);

                                JSONArray toparr = jsonObject.getJSONArray("products");
                                for (int j = 0; j < toparr.length(); j++) {
                                    try {
                                        JSONObject gettop = toparr.getJSONObject(j);
                                        String pro_img = gettop.getString("image");
                                        String id = gettop.getString("id");
                                        String names = gettop.getString("name");
                                        String price = gettop.getString("price");
                                        String actual_price = gettop.getString("actual_price");
                                        String min_qty = gettop.getString("min_qty");
                                        String category = gettop.getString("category");
                                        String subcategory = gettop.getString("subcategory");
                                        String productcategory = gettop.getString("productcategory");
                                        String productsubcategory = gettop.getString("productsubcategory");

                                        Profile_Category_Grid_Model2 profile_category_grid_model = new Profile_Category_Grid_Model2();
                                        profile_category_grid_model.setPro_img(pro_img);
                                        profile_category_grid_model.setId(id);
                                        profile_category_grid_model.setNames(names);
                                        profile_category_grid_model.setPrice(price);
                                        profile_category_grid_model.setActual_price(actual_price);
                                        profile_category_grid_model.setMin_qty(min_qty);
                                        profile_category_grid_model.setCategory(category);
                                        profile_category_grid_model.setSubcategory(subcategory);
                                        profile_category_grid_model.setProductcategory(productcategory);
                                        profile_category_grid_model.setProductsubcategory(productsubcategory);
                                        products_with_profileModels.add(profile_category_grid_model);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        profile_category_gridView_imageAdapter = new Profile_Category_GridView_ImageAdapter2(Vendor_Profile_With_Products_For_Main.this, R.layout.profile_category_gridview2, products_with_profileModels);
                        expandableHeightGridView.setAdapter(profile_category_gridView_imageAdapter);
                        profile_category_gridView_imageAdapter.setGridData(products_with_profileModels);
                        expandableHeightGridView.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("user_id", _id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        CartCount();
    }

    public void CartCount(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cartcount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Integer count=obj.getInt("cartcount");
                            cart_counts.setText(String.valueOf(count));

                        } catch (JSONException e) {
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
                params.put("token", tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void Follow(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_follow,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
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
                params.put("token", tokens);
                params.put("user_id", user_ids);
                params.put("shop_name", shop_name);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}