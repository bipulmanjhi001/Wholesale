package wholesalefactory.co.drawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.Home;
import wholesalefactory.co.main.Login;
import wholesalefactory.co.model.VolleySingleton;
import wholesalefactory.co.pref.SharedPrefManager;

public class Profile extends AppCompatActivity {

    ImageView back_from_profile;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    private String tokens;
    TextView p_name,p_email,p_phone,p_address,pin_address,p_landmark,p_state,p_distic;
    TextView s_name,s_type,s_address,s_pin,s_landmark,s_state,s_distic,p_sign;
    LinearLayout contain_pro_data,shop_pro_visible;
    CircleImageView profilepic;
    String pic,imageBase;
    public  static final int RequestPermissionCode  = 1 ;
    Intent intent;
    ImageButton logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        EnableRuntimePermission();
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
        shop_pro_visible=(LinearLayout)findViewById(R.id.shop_pro_visible);

        SetImage();
        profilepic=(CircleImageView)findViewById(R.id.profile_img);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        logout=(ImageButton)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
                finish();
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
            }
        });
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
                                    JSONObject itemslist = userJson.getJSONObject("profile");
                                    String name = itemslist.getString("name");
                                    p_name.setText(name);
                                    if(!name.isEmpty()) {
                                        p_name.setVisibility(View.VISIBLE);
                                    }
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
                                if(!district.isEmpty()){
                                    contain_pro_data.setVisibility(View.VISIBLE);
                                }

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
                                if(!districts.isEmpty()){
                                    shop_pro_visible.setVisibility(View.VISIBLE);
                                }

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
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageBase = getStringImage(bitmap);
            profilepic.setBackgroundResource(android.R.color.transparent);
            profilepic.setImageBitmap(bitmap);
            UploadImage();
        }
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Profile.this, Manifest.permission.CAMERA)) {
            Log.d( "Permission","CAMERA permission allows us to Access CAMERA app");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d( "Permission","Permission Granted, Now your application can access CAMERA.");
                } else {
                    Log.d( "Permission","Permission Canceled, Now your application cannot access CAMERA.");
                }
                break;
        }
    }

    public void UploadImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updatephoto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("photo", imageBase);
                params.put("token", tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
