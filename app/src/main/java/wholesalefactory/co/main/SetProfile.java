package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.VolleySingleton;

public class SetProfile extends AppCompatActivity {

    ImageView back_from_upload,show_img;
    Button click_bill,add_state,click_logo;
    public  static final int RequestPermissionCode  = 1 ;
    String imageBase,logos;
    Intent intent;
    LinearLayout visible_bill,visible_logo;
    EditText shop_name,Business_Type,shop_address,shop_pincode,shop_Landmark,reg_no;
    TextView Other,city;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    private String tokens,city_id,stateid;
    private String names,addresss,emails,citys,Landmarks,Others,pincodes,reg_nos;
    private ListView Type,Type2;
    private ArrayList state_names = new ArrayList();
    private ArrayList state_ids = new ArrayList();
    private  ArrayList city_names = new ArrayList();
    private ArrayList city_ids = new ArrayList();
    LinearLayout CONFIRM_add;
    Dialog dialog,dialog2;
    Bitmap bitmap;
    Boolean checkCam=false;
    CircleImageView logo_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        EnableRuntimePermission();
        back_from_upload=(ImageView)findViewById(R.id.back_from_upload);
        show_img=(ImageView)findViewById(R.id.show_img);
        visible_logo=(LinearLayout)findViewById(R.id.visible_logo);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        logo_image=(CircleImageView)findViewById(R.id.logo_image);

        visible_bill=(LinearLayout)findViewById(R.id.visible_bill);
        back_from_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SetProfile.this, Proof.class));
            }
        });
        click_bill=(Button)findViewById(R.id.click_bill);
        click_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        click_logo=(Button)findViewById(R.id.click_logo);
        click_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        dialog=new Dialog(SetProfile.this);
        dialog2=new Dialog(SetProfile.this);
        shop_name = (EditText)findViewById(R.id.shop_name);
        Business_Type = (EditText)findViewById(R.id.Business_Type);
        shop_address = (EditText)findViewById(R.id.shop_address);
        shop_pincode = (EditText)findViewById(R.id.shop_pincode);
        shop_Landmark = (EditText)findViewById(R.id.shop_Landmark);
        reg_no = (EditText)findViewById(R.id.reg_no);
        Other = (TextView)findViewById(R.id.Other);
        city = (TextView)findViewById(R.id.city);
        add_state=(Button)findViewById(R.id.add_states);
        add_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showState(view);
            }
        });

        GetState();

        CONFIRM_add=(LinearLayout)findViewById(R.id.CONFIRM_add);
        CONFIRM_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptShopUpdate();
            }
        });
    }

    private void attemptShopUpdate() {

        names = shop_name.getText().toString();
        emails = Business_Type.getText().toString();
        addresss = shop_address.getText().toString();
        citys = city.getText().toString();
        Landmarks = shop_Landmark.getText().toString();
        Others = Other.getText().toString();
        pincodes = shop_pincode.getText().toString();
        reg_nos=reg_no.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(names)) {
            shop_name.setError(getString(R.string.error_field_required));
            focusView = shop_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(emails)) {
            Business_Type.setError(getString(R.string.error_field_required));
            focusView = Business_Type;
            cancel = true;
        }
        if (TextUtils.isEmpty(addresss)) {
            shop_address.setError(getString(R.string.error_field_required));
            focusView = shop_address;
            cancel = true;
        }
        if (TextUtils.isEmpty(pincodes)) {
            shop_pincode.setError(getString(R.string.error_field_required));
            focusView = shop_pincode;
            cancel = true;
        }
        if (TextUtils.isEmpty(Landmarks)) {
            shop_Landmark.setError(getString(R.string.error_field_required));
            focusView = shop_Landmark;
            cancel = true;
        }
        if (TextUtils.isEmpty(citys)) {
            city.setError(getString(R.string.error_field_required));
            focusView = city;
            cancel = true;
        }
        if (TextUtils.isEmpty(Others)) {
            Other.setError(getString(R.string.error_field_required));
            focusView = Other;
            cancel = true;
        }
        if (TextUtils.isEmpty(reg_nos)) {
            reg_no.setError(getString(R.string.error_field_required));
            focusView = reg_no;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            UpdteProfile();
        }
    }
    private void UpdteProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updateshop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                shop_name.setText("");
                                Business_Type.setText("");
                                shop_address.setText("");
                                shop_pincode.setText("");
                                shop_Landmark.setText("");
                                reg_no.setText("");
                                Other.setText("");
                                city.setText("");
                                imageBase="";
                                show_img.setVisibility(View.GONE);
                                visible_logo.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(getApplicationContext(), "Check details again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check details again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                imageBase=getStringImageCam(bitmap);
                logos=getStringImage(bitmap);
                params.put("token", tokens);
                params.put("shop_name", names);
                params.put("business_type", emails);
                params.put("address", addresss);
                params.put("pincode", pincodes);
                params.put("landmark", Landmarks);
                params.put("district",city_id);
                params.put("state", stateid);
                params.put("gstin",reg_nos);
                params.put("image",imageBase);
                params.put("logo",logos);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void GetState() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_state,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("state");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_type = itemslist.getString("name");
                                    String item_id = itemslist.getString("id");
                                    state_names.add(item_type);
                                    state_ids.add(item_id);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),"No state added",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Check again..", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void showState(View view) {
        dialog.setContentView(R.layout.state_list_dialog);
        Type= (ListView) dialog.findViewById(R.id.state_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, state_names){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };

        Type.setAdapter(adapter);
        Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Other.setText(state_names.get(position).toString());
                stateid=state_ids.get(position).toString();
                City_List();

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showCity() {
        dialog2.setContentView(R.layout.city_list_dialog);
        Type2= (ListView) dialog2.findViewById(R.id.city_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1, city_names){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.WHITE);

                return view;
            }
        };
        Type2.setAdapter(adapter);
        Type2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city.setText(city_names.get(position).toString());
                city_id=city_ids.get(position).toString();
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

    public void City_List(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_district,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                JSONArray userJson = obj.getJSONArray("district");
                                for (int i = 0; i < userJson.length(); i++) {

                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String item_type = itemslist.getString("name");
                                    city_names.add(item_type);
                                    String item_id = itemslist.getString("id");
                                    city_ids.add(item_id);
                                }
                                showCity();
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
                params.put("state_id", stateid);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String getStringImageCam(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmps=Bitmap.createScaledBitmap(bmp, 300, 400, false);
        bmps.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            if (checkCam) {
                visible_bill.setVisibility(View.VISIBLE);
                show_img.setImageBitmap(bitmap);
            }
            if (!checkCam) {
                visible_logo.setVisibility(View.VISIBLE);
                logo_image.setImageBitmap(bitmap);
                click_logo.setVisibility(View.GONE);
                checkCam = true;
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(SetProfile.this, Manifest.permission.CAMERA)) {
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

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SetProfile.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SetProfile.this.finish();
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
