package wholesalefactory.co.drawer;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.Home;
import wholesalefactory.co.model.VolleySingleton;

public class Profile extends AppCompatActivity {

    ImageView back_from_profile;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    private String tokens;
    TextView p_name,p_email,p_phone,p_address,pin_address,p_landmark,p_state,p_distic;
    TextView s_name,s_type,s_address,s_pin,s_landmark,s_state,s_distic,p_sign;
    LinearLayout contain_pro_data;
    CircleImageView profilepic;
    String pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        back_from_profile=(ImageView)findViewById(R.id.back_from_profile);
        back_from_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Profile.this, Home.class);
                startActivity(openMain);
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        p_name=(TextView)findViewById(R.id.p_name);
        p_email=(TextView)findViewById(R.id.p_email);
        p_phone=(TextView)findViewById(R.id.p_phone);

        p_address=(TextView)findViewById(R.id.p_address);
        pin_address=(TextView)findViewById(R.id.pin_address);
        p_landmark=(TextView)findViewById(R.id.p_landmark);

        p_state=(TextView)findViewById(R.id.p_state);
        p_distic=(TextView)findViewById(R.id.p_distic);
        s_name=(TextView)findViewById(R.id.s_name);

        s_type=(TextView)findViewById(R.id.s_type);
        s_address=(TextView)findViewById(R.id.s_address);
        s_pin=(TextView)findViewById(R.id.s_pin);

        s_landmark=(TextView)findViewById(R.id.s_landmark);
        s_state=(TextView)findViewById(R.id.s_state);
        s_distic=(TextView)findViewById(R.id.s_distic);
        p_sign=(TextView)findViewById(R.id.p_sign);
        contain_pro_data=(LinearLayout)findViewById(R.id.contain_pro_data);

        SetImage();
        profilepic=(CircleImageView)findViewById(R.id.profile_img);
    }
    public void ProfileShow(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userJson = obj.getJSONObject("userdetails");

                            if(obj.getBoolean("status")) {
                                contain_pro_data.setVisibility(View.VISIBLE);
                                    JSONObject itemslist = userJson.getJSONObject("profile");
                                    String name = itemslist.getString("name");
                                    p_name.setText(name);
                                    String email = itemslist.getString("email");
                                    p_email.setText(email);
                                    String mobile = itemslist.getString("mobile");
                                    p_phone.setText(mobile);

                                SharedPreferences prefs = getSharedPreferences(
                                        "myprofile", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("userName", name);
                                editor.putString("mobile", mobile);
                                editor.apply();

                                String addresss = itemslist.getString("address");
                                p_address.setText(addresss);
                                String pincode = itemslist.getString("pincode");
                                pin_address.setText(pincode);
                                String landmark = itemslist.getString("landmark");
                                p_landmark.setText(landmark);

                                String state = itemslist.getString("state");
                                p_state.setText(state);
                                String district = itemslist.getString("district");
                                p_distic.setText(district);

                                JSONObject shoplist = userJson.getJSONObject("shop");
                                String shop_name = shoplist.getString("shop_name");
                                s_name.setText(shop_name);
                                String address = shoplist.getString("address");
                                s_address.setText(address);

                                String business_type = shoplist.getString("business_type");
                                s_type.setText(business_type);

                                String pincodes = shoplist.getString("pincode");
                                s_pin.setText(pincodes);
                                String landmarks = shoplist.getString("landmark");
                                s_landmark.setText(landmarks);

                                String states = shoplist.getString("state");
                                s_state.setText(states);
                                String districts = shoplist.getString("district");
                                s_distic.setText(districts);

                            }else {
                                Toast.makeText(getApplicationContext(),"No city added",Toast.LENGTH_SHORT).show();
                            }
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

    public void SetImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_setpic,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            pic = obj.getString("photo");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(pic != null) {
                            Glide.with(getApplicationContext())
                                    .load(pic)
                                    .error(R.drawable.no_image_available)
                                    .into(profilepic);
                        }else {
                            profilepic.setBackground(getResources().getDrawable(R.drawable.ic_profle));
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
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        ProfileShow();
    }
    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Profile.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
