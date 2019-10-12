package wholesalefactory.co.main;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.JsonObject;
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
import wholesalefactory.co.adapter.OrderAdapter;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.CartModel;
import wholesalefactory.co.model.OrderModel;
import wholesalefactory.co.model.VolleySingleton;

public class Order_Details extends AppCompatActivity {
    Dialog myDialog;
    String rating;
    LinearLayout CONFIRM_add;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;
    ArrayList<OrderModel> orderModels;
    TextView s_name,s_address,s_pin,s_landmark,s_state,s_distic,s_gst;
    TextView profile_name,order_price;
    CircleImageView profile_image;
    String cartamount,name,img,gstin;
    OrderAdapter orderAdapter;
    ProgressBar progressBar;
    ListView orderlist;
    ImageView back_from_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        s_name=(TextView)findViewById(R.id.s_name);
        s_address=(TextView)findViewById(R.id.s_address);
        s_pin=(TextView)findViewById(R.id.s_pin);
        s_landmark=(TextView)findViewById(R.id.s_landmark);
        s_state=(TextView)findViewById(R.id.s_state);
        s_distic=(TextView)findViewById(R.id.s_distic);
        s_gst=(TextView)findViewById(R.id.s_gst);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);

        CartSummery();

        profile_image=(CircleImageView)findViewById(R.id.profile_image);
        profile_name=(TextView)findViewById(R.id.profile_name);
        order_price=(TextView)findViewById(R.id.order_price);
        orderModels=new ArrayList<OrderModel>();
        orderlist=(ListView)findViewById(R.id.orderlist);
        myDialog = new Dialog(Order_Details.this);
        CONFIRM_add=(LinearLayout)findViewById(R.id.CONFIRM_add);
        CONFIRM_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup(view);
            }
        });
    }
    public void CartSummery(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cartsummary,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject summary= obj.getJSONObject("summary");
                            JSONArray cart=summary.getJSONArray("cart");
                            for(int i=0; i < cart.length(); i++){

                                JSONObject getvalue=cart.getJSONObject(i);
                                String price=getvalue.getString("price");
                                String discount=getvalue.getString("discount");
                                String quantity=getvalue.getString("quantity");
                                String name=getvalue.getString("name");
                                String image=getvalue.getString("image");
                                String shop_name=getvalue.getString("shop_name");

                                OrderModel orderModel=new OrderModel(image,shop_name,name,quantity,price,discount);
                                orderModels.add(orderModel);
                            }
                            progressBar.setVisibility(View.GONE);
                            orderAdapter=new OrderAdapter(orderModels,Order_Details.this);
                            orderlist.setAdapter(orderAdapter);
                            orderAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(orderlist);

                            cartamount=summary.getString("cartamount");
                            order_price.setText(cartamount);
                            JSONObject profile=summary.getJSONObject("profile");
                            name=profile.getString("name");
                            profile_name.setText(name);
                            img=profile.getString("photo");

                            try {
                                Glide.with(getApplicationContext()).load(img)
                                        .error(R.drawable.no_image_available)
                                        .centerCrop()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(profile_image);
                            }catch (IllegalArgumentException e){
                                e.printStackTrace();
                            }

                            JSONObject shop=summary.getJSONObject("shop");
                            String shop_name=shop.getString("shop_name");
                            s_name.setText(shop_name);
                            String address=shop.getString("address");
                            s_address.setText(address);
                            String pincode=shop.getString("pincode");
                            s_pin.setText(pincode);
                            String landmark=shop.getString("landmark");
                            s_landmark.setText(landmark);
                            String state=shop.getString("state");
                            s_state.setText(state);
                            String district=shop.getString("district");
                            s_distic.setText(district);
                            gstin=shop.getString("gstin");
                            s_gst.setText(gstin);

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
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void ShowPopup(View v) {
        TextView txtclose,staff_rate_name,staff_rate_phone,staff_total;
        Button btnFollow;
        myDialog.setContentView(R.layout.custom_rateus);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);

        CircleImageView staff_img = (CircleImageView) myDialog.findViewById(R.id.staff_rate_img);
        staff_rate_name =(TextView) myDialog.findViewById(R.id.staff_rate_name);
        staff_rate_phone =(TextView) myDialog.findViewById(R.id.staff_rate_phone);

        staff_total =(TextView) myDialog.findViewById(R.id.staff_total);
        staff_rate_name.setText(name);
        staff_rate_phone.setText(gstin);
        staff_total.setText(cartamount);

        try {
            Glide.with(getApplicationContext())
                    .load(img)
                    .into(staff_img);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rating();
                myDialog.dismiss();
            }
        });
        SmileRating smileRating = (SmileRating)myDialog.findViewById(R.id.smile_rating);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, "Angry");
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                switch (smiley) {
                    case SmileRating.BAD:
                        rating="2";
                        break;
                    case SmileRating.GOOD:
                        rating="4";
                        break;
                    case SmileRating.GREAT:
                        rating="5";
                        break;
                    case SmileRating.OKAY:
                        rating="3";
                        break;
                    case SmileRating.TERRIBLE:
                        rating="1";
                        break;
                }
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
