package wholesalefactory.co.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.MyBounceInterpolator;
import wholesalefactory.co.model.VolleySingleton;

public class OneProduct_display extends AppCompatActivity {

    ImageView back_from_productdetails,product_cart;
    TextView product_name,title_product_name;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens,pro_id,catname,productcategory_id,subcategory_id,image,category,category_pro_url;
    ImageView show_image;
    ImageView whishlist_off,whishlist_on;
    CardView img_a1,img_a2,img_a3,img_a4;
    ImageView select_image3,select_image4,select_image2,select_image1;
    ArrayList<String> showImages= new ArrayList<String>();
    ProgressBar progress_dailog2;
    TextView qty_value,chargesvalue,product_details;
    Button view_price;
    EditText Qty;
    String qtys;
    LinearLayout product_details_show,pro_add_descs,footerbar_product_details;
    TextView cart_counts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pro_id = bundle.getString("pro_id");
            catname = bundle.getString("name");
            subcategory_id = bundle.getString("subcategory_id");
            productcategory_id = bundle.getString("productcategory_id");
            image = bundle.getString("image");
            category = bundle.getString("category");
            category_pro_url = bundle.getString("category_pro_url");
            show_image = (ImageView) findViewById(R.id.show_image);
            Picasso.get()
                    .load(image)
                    .fit().centerCrop()
                    .into(show_image);
        }

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        progress_dailog2=(ProgressBar)findViewById(R.id.progress_dailog2);
        qty_value=(TextView)findViewById(R.id.qty_value);
        chargesvalue=(TextView)findViewById(R.id.updated_price);
        pro_add_descs=(LinearLayout)findViewById(R.id.pro_add_descs);
        Qty=(EditText)findViewById(R.id.Qty);
        back_from_productdetails = (ImageView) findViewById(R.id.back_from_productdetails);
        footerbar_product_details=(LinearLayout)findViewById(R.id.footerbar_product_details);
        footerbar_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qtys = Qty.getText().toString();
                boolean cancel = false;
                View focusView = null;

                if (TextUtils.isEmpty(qtys)) {
                    Qty.setError(getString(R.string.error_field_required));
                    focusView = Qty;
                    cancel = true;
                }
                if (cancel) {
                    focusView.requestFocus();

                } else {
                    AddToCart();
                }
            }
        });

        back_from_productdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(OneProduct_display.this, Products_With_Profile.class);
                openMain.putExtra("pro_id", subcategory_id);
                openMain.putExtra("name", category);
                openMain.putExtra("sub_pro_id", productcategory_id);
                openMain.putExtra("category_pro_url", category_pro_url);
                startActivity(openMain);
                finish();
            }
        });

        product_cart = (ImageView) findViewById(R.id.product_cart);
        product_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(OneProduct_display.this, Cart.class);
                startActivity(openMain);
                finish();
            }
        });

        view_price=(Button)findViewById(R.id.view_price);
        product_details_show=(LinearLayout)findViewById(R.id.product_details_show);
        view_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_price.setVisibility(View.GONE);
                product_details_show.setVisibility(View.VISIBLE);
                pro_add_descs.setVisibility(View.VISIBLE);
            }
        });

        product_name = (TextView) findViewById(R.id.product_name);
        product_name.setText(catname);
        title_product_name = (TextView) findViewById(R.id.title_product_name);
        title_product_name.setText(category);
        whishlist_off = (ImageView) findViewById(R.id.whishlist_off);
        whishlist_on = (ImageView) findViewById(R.id.whishlist_on);
        product_details = (TextView) findViewById(R.id.product_details);
        cart_counts=(TextView)findViewById(R.id.product_cart_count);

        whishlist_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_off.setVisibility(View.GONE);
                whishlist_on.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(OneProduct_display.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_on.startAnimation(myAnim);
            }
        });

        whishlist_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_off.setVisibility(View.VISIBLE);
                whishlist_on.setVisibility(View.GONE);
                final Animation myAnim = AnimationUtils.loadAnimation(OneProduct_display.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_off.startAnimation(myAnim);
            }
        });

        img_a1 = (CardView) findViewById(R.id.img_a1);
        img_a2 = (CardView) findViewById(R.id.img_a2);
        img_a3 = (CardView) findViewById(R.id.img_a3);
        img_a4 = (CardView) findViewById(R.id.img_a4);
        select_image3 = (ImageView) findViewById(R.id.select_image3);
        select_image4 = (ImageView) findViewById(R.id.select_image4);
        select_image1 = (ImageView) findViewById(R.id.select_image1);
        select_image2 = (ImageView) findViewById(R.id.select_image2);

        DisplayProduct();

        try {
            select_image4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (showImages.get(3) != null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(3))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image4.setVisibility(View.VISIBLE);
                            img_a4.setVisibility(View.VISIBLE);
                        } else if (showImages.get(3) == null) {
                            select_image4.setVisibility(View.GONE);
                            img_a4.setVisibility(View.GONE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });

            select_image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (showImages.get(2) != null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(2))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image3.setVisibility(View.VISIBLE);
                            img_a3.setVisibility(View.VISIBLE);
                        } else if (showImages.get(2) == null) {
                            select_image3.setVisibility(View.GONE);
                            img_a3.setVisibility(View.GONE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });

            select_image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (showImages.get(1) != null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(1))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image2.setVisibility(View.VISIBLE);
                            img_a2.setVisibility(View.VISIBLE);
                        } else if (showImages.get(1) == null) {
                            select_image2.setVisibility(View.GONE);
                            img_a2.setVisibility(View.GONE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });

            select_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(showImages.get(0)!=null) {
                            Glide.with(getApplicationContext())
                                    .load(showImages.get(0))
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .skipMemoryCache(true)
                                    .animate(R.anim.bounce)
                                    .into(show_image);
                            select_image1.setVisibility(View.VISIBLE);
                            img_a1.setVisibility(View.VISIBLE);
                        }else if(showImages.get(0)==null){
                            select_image1.setVisibility(View.GONE);
                            img_a1.setVisibility(View.GONE);
                        }
                    }catch (IndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void DisplayProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")){
                                Object objs = null;
                                JSONObject productobj=obj.getJSONObject("product");
                                String description=productobj.getString("description");
                                String price=productobj.getString("price");
                                String actual_price=productobj.getString("actual_price");
                                String min_qty=productobj.getString("min_qty");
                                qty_value.setText(min_qty);
                                chargesvalue.setText(price);
                                product_details.setText(description);

                                JSONArray arrayimg=productobj.getJSONArray("images");
                                for(int i = 0; i < arrayimg.length() ; i++){
                                    JSONObject imgobj= arrayimg.getJSONObject(0);
                                    String product_id = imgobj.getString("product_id");
                                    String image = imgobj.getString("image");
                                    Picasso.get()
                                            .load(image)
                                            .fit().centerCrop()
                                            .into(select_image1);

                                    JSONObject imgobj1= arrayimg.getJSONObject(1);
                                    String image1 = imgobj1.getString("image");
                                    img_a2.setVisibility(View.VISIBLE);
                                    Picasso.get()
                                            .load(image1)
                                            .fit().centerCrop()
                                            .into(select_image2);

                                    JSONObject imgobj2= arrayimg.getJSONObject(2);
                                    String image2 = imgobj2.getString("image");
                                    img_a3.setVisibility(View.VISIBLE);
                                    Picasso.get()
                                            .load(image2)
                                            .fit().centerCrop()
                                            .into(select_image3);

                                    JSONObject imgobj3= arrayimg.getJSONObject(3);
                                    String image3 = imgobj3.getString("image");
                                    img_a4.setVisibility(View.VISIBLE);
                                    Picasso.get()
                                            .load(image3)
                                            .fit().centerCrop()
                                            .into(select_image4);
                                    JSONObject imgobj4= arrayimg.getJSONObject(i);
                                    String image4 = imgobj4.getString("image");
                                    showImages.add(image4);
                                    if(i >=2){
                                        img_a1.setVisibility(View.VISIBLE);
                                    }else {
                                        img_a1.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("product_id", pro_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        CartCount();
    }

    public void AddToCart() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_addtocart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            Qty.setText("");
                            CartCount();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("product_id", pro_id);
                params.put("quantity", qtys);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

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
}