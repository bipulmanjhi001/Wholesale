package wholesalefactory.co.drawer;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.ProductAddedList;
import wholesalefactory.co.model.VolleySingleton;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Sell extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LinearLayout edit_products;
    private Spinner spinner,spinner2,spinner3;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    public String tokens,ids,subid,image1,subsubid;

    private ArrayList<String> categoryNames=new ArrayList<String>();
    private ArrayList<String> categoryIds=new ArrayList<String>();
    private ArrayList<String> subcategoryNames=new ArrayList<String>();
    private ArrayList<String> subcategoryIds=new ArrayList<String>();
    private ArrayList<String> subsubcategoryNames=new ArrayList<String>();
    private ArrayList<String> subsubcategoryIds=new ArrayList<String>();
    public  static final int RequestPermissionCode  = 1 ;
    private LinearLayout visible_img,save_product;
    private ImageView show_pro_img;
    private String product_names,Prices,max_orders,Actual_Prices,Descriptions;
    private EditText product_name,Price,max_order,Actual_Price,Description;
    private Button click_upload;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_sell, container, false);
        EnableRuntimePermission();

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
        visible_img=(LinearLayout)view.findViewById(R.id.visible_img);
        show_pro_img=(ImageView)view.findViewById(R.id.show_pro1_img);
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ids=categoryIds.get(i).toString();
                if(!subcategoryIds.isEmpty()){
                    subcategoryIds.clear();
                    subcategoryNames.clear();
                }
                SubCategory_List();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2=(Spinner)view.findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subid=subcategoryIds.get(i).toString();
                if(!subsubcategoryIds.isEmpty()){
                    subsubcategoryIds.clear();
                    subsubcategoryNames.clear();
                }
                SubSubCategory_List();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subsubid=subsubcategoryIds.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_product=(LinearLayout)view.findViewById(R.id.save_product);
        save_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSave();
            }
        });

        click_upload=(Button) view.findViewById(R.id.click_upload);
        click_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
                visible_img.setVisibility(View.VISIBLE);
            }
        });
        Category_List();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void SubCategory_List() {
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

    private void  Category_List() {
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
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                (getActivity(), android.R.layout.simple_spinner_item, categoryNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        spinner.setAdapter(spinnerArrayAdapter);
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

    private void SubSubCategory_List(){
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
                                    subsubid=subsubcategoryIds.get(0);
                                }
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("productcategory_id",subid);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmps=Bitmap.createScaledBitmap(bmp, 150, 150, false);
        bmps.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
             Bitmap bitmap = (Bitmap) data.getExtras().get("data");
             image1 = getStringImage(bitmap);
             Log.d("imgsss",image1);
             show_pro_img.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void attemptSave() {
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
    private void AddProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_addproduct,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();

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
                params.put("discount", Actual_Prices);
                params.put("min_qty", max_orders);
                params.put("image", image1);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}