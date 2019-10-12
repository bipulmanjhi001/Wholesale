package wholesalefactory.co.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.CartModel;
import wholesalefactory.co.model.FooterBarLayout;
import wholesalefactory.co.model.VolleySingleton;

public class Cart extends AppCompatActivity {

    ImageView back_from_cart;
    TextView cart_title,grand_total,View_price;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;
    ImageView delete_all;
    RecyclerView recyclerView;
    ArrayList<CartModel> cartModels;
    CartRecyclerAdapter adapter;
    ArrayList<String> ids = new ArrayList<String>();
    ProgressBar probar;
    LinearLayout cart_payment;
    int count=0;
    FooterBarLayout cart_payment_proceed;
    Dialog myDialog;
    String idss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        back_from_cart=(ImageView)findViewById(R.id.back_from_cart);
        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Cart.this, Home.class);
                startActivity(openMain);
                finish();
            }
        });
        cart_title=(TextView)findViewById(R.id.cart_title);
        cart_title.setText("My Cart (0)");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cartModels = new ArrayList<CartModel>();
        cart_payment_proceed=(FooterBarLayout)findViewById(R.id.cart_payment_proceed);
        delete_all=(ImageView)findViewById(R.id.delete_all);
        grand_total=(TextView)findViewById(R.id.grand_total);
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAll();
            }
        });
        myDialog=new Dialog(Cart.this);
        View_price=(TextView)findViewById(R.id.View_price);
        View_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                View_price.startAnimation(shake);
            }
        });

        probar=(ProgressBar)findViewById(R.id.probar);
        cart_payment=(LinearLayout)findViewById(R.id.cart_payment);
        cart_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Cart.this, Order_Details.class);
                startActivity(openMain);
            }
        });

        ShowProduct();
    }
    public class CartRecyclerAdapter  extends RecyclerView.Adapter<CartRecyclerAdapter.MyViewHolder> {

        private ArrayList<CartModel> cartModels =new ArrayList<CartModel>();
        private ArrayList<String> cartids =new ArrayList<String>();
        int possses;
        Cart cart;

        public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
            public TextView cart_qty,product_id;
            public TextView cart_name,product_size;
            public TextView cart_total_price,delete;
            public ImageView cart_image,bin;
            public LinearLayout parentLinearLayout;

            public MyViewHolder(View view) {
                super(view);
                cart_qty= (TextView) view.findViewById(R.id.qty_cart);
                cart_name=(TextView)view.findViewById(R.id.cart_product);
                cart_total_price=(TextView)view.findViewById(R.id.cart_price);
                cart_image=(ImageView) view.findViewById(R.id.get_cart_image);
                product_size=(TextView)view.findViewById(R.id.product_size);
                product_id=(TextView)view.findViewById(R.id.product_id);
                bin=(ImageView)view.findViewById(R.id.bin_img);
                bin.setOnClickListener(this);
                delete=(TextView)view.findViewById(R.id.text_remove);
                delete.setOnClickListener(this);
                parentLinearLayout=(LinearLayout)view.findViewById(R.id.parentLinearLayout);
            }
            @Override
            public void onClick(View v) {
                possses = getAdapterPosition();
                if (possses!= RecyclerView.NO_POSITION) {
                    idss=cartids.get(possses);
                    ShowPopup(v);
                }
            }
        }
        public CartRecyclerAdapter(Cart cartActivity, ArrayList<CartModel> cartModelss, ArrayList<String> idss) {
            this.cartModels = cartModelss;
            this.cartids=idss;
            this.cart=cartActivity;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            CartModel cartModel=cartModels.get(position);
            holder.cart_qty.setText(cartModel.getCart_qty());
            holder.cart_name.setText(cartModel.getCart_name());
            holder.cart_total_price.setText(cartModel.getCart_total_price());
            holder.product_size.setText(cartModel.getCart_size());
            holder.product_id.setText(cartModel.getProduct_id());

            Glide.with(cart)
                    .load(cartModel.getCart_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .crossFade()
                    .skipMemoryCache(true)
                    .animate(R.anim.bounce)
                    .into(holder.cart_image);
        }
        @Override
        public int getItemCount() {
            return cartModels.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_cart_details,parent, false);
            return new MyViewHolder(v);
        }
    }
    public void ShowProduct(){
        probar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_viewcart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array=obj.getJSONArray("cart");
                            try {
                                cartModels.clear();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            for(int i=0; i < array.length(); i++){

                                JSONObject object= array.getJSONObject(i);
                                String id=object.getString("id");
                                ids.add(id);
                                String price=object.getString("price");
                                String quantity=object.getString("quantity");
                                String name=object.getString("name");
                                String image=object.getString("image");
                                CartModel cartModel=new CartModel();
                                cartModel.setCart_image(image);
                                cartModel.setCart_name(name);
                                cartModel.setCart_qty(quantity);
                                cartModel.setProduct_id(id);
                                cartModel.setCart_total_price(price);
                                cartModels.add(cartModel);
                                count++;
                                cart_payment_proceed.setVisibility(View.VISIBLE);
                            }
                            try{
                                probar.setVisibility(View.GONE);
                                adapter = new CartRecyclerAdapter(Cart.this,cartModels,ids);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.setAdapter(adapter);
                                CartCount();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                                setContentView(R.layout.no_added_product);
                                back_from_cart = (ImageView) findViewById(R.id.back_from_cart);
                                back_from_cart.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Cart.this, Home.class);
                                        startActivity(intent);
                                        Cart.this.finish();
                                    }
                                });
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
        probar.setVisibility(View.GONE);
        CartAmount();
    }

    public void DeleteAll(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_deletecart,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            ShowProduct();
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
    public void CartAmount(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cartamount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String amt=obj.getString("cartamount");
                            grand_total.setText(amt);

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
    public void ShowPopup(View v) {
        LinearLayout comment_cancels, comment_done;
        myDialog.setContentView(R.layout.remove_single_item);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        comment_done = (LinearLayout) myDialog.findViewById(R.id.comment_done);
        try {
            comment_cancels = (LinearLayout) myDialog.findViewById(R.id.comment_cancels);
            comment_cancels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myDialog.dismiss();
                }
            });
            comment_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteProduct();
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
        public void DeleteProduct(){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_deletecart,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                                ShowProduct();
                                myDialog.dismiss();

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
                    params.put("id",idss);
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

    public void CartCount(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cartcount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Integer count=obj.getInt("cartcount");
                            cart_title.setText("My Cart"+"("+String.valueOf(count)+")");

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
