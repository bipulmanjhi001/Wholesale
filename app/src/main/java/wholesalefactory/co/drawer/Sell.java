package wholesalefactory.co.drawer;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.ProductAddedList;
import wholesalefactory.co.model.VolleySingleton;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Sell extends Fragment implements View.OnClickListener {

     static final String ARG_PARAM1 = "param1";
     static final String ARG_PARAM2 = "param2";
     String mParam1;
     String mParam2;
     LinearLayout edit_products;
     Spinner spinner,spinner2,spinner3;
     static final String SHARED_PREF_NAME = "wholesalepref";
     static final String KEY_ID = "token";
     String tokens,ids,subid,subsubid;
     ArrayList<String> categoryNames=new ArrayList<String>();
     ArrayList<String> categoryIds=new ArrayList<String>();
     ArrayList<String> subcategoryNames=new ArrayList<String>();
     ArrayList<String> subcategoryIds=new ArrayList<String>();
     ArrayList<String> subsubcategoryNames=new ArrayList<String>();
     ArrayList<String> subsubcategoryIds=new ArrayList<String>();
     LinearLayout save_product;
     ImageView show_pro_img;
     String product_names,Prices,max_orders,Actual_Prices,Descriptions;
     EditText product_name,Price,max_order,Actual_Price,Description;
     String image;
     public  static final int RequestPermissionCode  = 1 ;

    public Sell() {
    }

    public static Sell newInstance(String param1, String param2) {
        Sell fragment = new Sell();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_sell, container, false);
        EnableRuntimePermission();
        show_pro_img=(ImageView)view.findViewById(R.id.my_avatar);
        product_name=(EditText)view.findViewById(R.id.product_name);
        Price=(EditText)view.findViewById(R.id.Price);
        max_order=(EditText)view.findViewById(R.id.max_order);
        Actual_Price=(EditText)view.findViewById(R.id.Actual_Price);
        Description=(EditText)view.findViewById(R.id.Description);

        edit_products=(LinearLayout)view.findViewById(R.id.edit_products);
        edit_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ProductAddedList.class);
                startActivity(intent);
            }
        });

        spinner3=(Spinner)view.findViewById(R.id.spinner3);
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    ids = categoryIds.get(i).toString();
                    if (!subcategoryIds.isEmpty()) {
                        subcategoryIds.clear();
                        subcategoryNames.clear();
                    }
                    SubCategory_List();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner2=(Spinner)view.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               try{
                subid=subcategoryIds.get(i).toString();
                if(!subsubcategoryIds.isEmpty()){
                    subsubcategoryIds.clear();
                    subsubcategoryNames.clear();
                }
                SubSubCategory_List();

            }catch (NullPointerException e){
                e.printStackTrace();
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    subsubid = subsubcategoryIds.get(i).toString();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_product=(LinearLayout)view.findViewById(R.id.save_product);
        save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save_product.setVisibility(View.GONE);
                attemptSave();
            }
        });

        show_pro_img.setOnClickListener(this);
        Category_List();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void SubCategory_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productcategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")){
                                JSONArray array = c.getJSONArray("productcategory");

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    subcategoryNames.add(name);
                                    subcategoryIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), c.getString("message"), Toast.LENGTH_SHORT).show();
                                subsubcategoryIds.clear();
                                subsubcategoryNames.clear();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                (getActivity(), android.R.layout.simple_spinner_item, subcategoryNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner2.setAdapter(spinnerArrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("subcategory_id", ids);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void  Category_List() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("subcategory");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    categoryNames.add(name);
                                    categoryIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                subid="0";
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        try {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryNames);
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerArrayAdapter);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void SubSubCategory_List(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productsubcategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("productsubcategory");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    subsubcategoryNames.add(name);
                                    subsubcategoryIds.add(id);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                subsubid="0";
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                (getActivity(), android.R.layout.simple_spinner_item, subsubcategoryNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner3.setAdapter(spinnerArrayAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("subcategory_id",ids);
                params.put("productcategory_id",subid);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap selectedImage = (Bitmap) extras.get("data");
                        image=getStringImage(selectedImage);
                        show_pro_img.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        try {
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            image = encodeImage(selectedImage);
                            show_pro_img.setImageBitmap(selectedImage);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String image = Base64.encodeToString(b, Base64.DEFAULT);

        return image;
    }

    public void attemptSave() {
        product_names = product_name.getText().toString();
        Prices = Price.getText().toString();
        max_orders = max_order.getText().toString();
        Actual_Prices = Actual_Price.getText().toString();
        Descriptions = Description.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(product_names)) {
            product_name.setError(getString(R.string.error_field_required));
            focusView = product_name;
            cancel = true;
        }
        if (TextUtils.isEmpty(Prices)) {
            Price.setError(getString(R.string.error_field_required));
            focusView = Price;
            cancel = true;
        }
        if (TextUtils.isEmpty(max_orders)) {
            max_order.setError(getString(R.string.error_field_required));
            focusView = max_order;
            cancel = true;
        }
        if (TextUtils.isEmpty(Actual_Prices)) {
            Actual_Price.setError(getString(R.string.error_field_required));
            focusView = Actual_Price;
            cancel = true;
        }
        if (TextUtils.isEmpty(Descriptions)) {
            Description.setError(getString(R.string.error_field_required));
            focusView = Description;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            AddProduct();
        }
    }

    public void AddProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_addproduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            product_name.setText("");
                            Description.setText("");
                            Price.setText("");
                            Actual_Price.setText("");
                            max_order.setText("");
                            image="";
                            save_product.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("subcategory_id", ids);
                params.put("productcategory_id", subid);
                params.put("productsubcategory_id", subsubid);
                params.put("name", product_names);
                params.put("description", Descriptions);
                params.put("price", Prices);
                params.put("actual_price", Actual_Prices);
                params.put("min_qty", max_orders);
                params.put("image", image);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {
        if (view == show_pro_img) {
            selectImage(getActivity());
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Log.d( "Permission","CAMERA permission allows us to Access CAMERA app");
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
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
}