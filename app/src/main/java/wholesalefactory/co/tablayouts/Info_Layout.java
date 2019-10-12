package wholesalefactory.co.tablayouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class Info_Layout extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static final String PREFS_GAME ="saveproductid";
    public static final String GAME_SCORE= "pro_id";
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
    String sc;
    LinearLayout show_sub_sub;
    TextView sub_text,cat_text;
    CardView sub_view;
    CardView cat_show;

    public Info_Layout() {
    }

    public static Info_Layout newInstance(String param1, String param2) {
        Info_Layout fragment = new Info_Layout();
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_GAME ,Context.MODE_PRIVATE);
        sc  = sp.getString(GAME_SCORE,"");

        show_pro_img=(ImageView)view.findViewById(R.id.my_avatar);
        product_name=(EditText)view.findViewById(R.id.product_name);
        Price=(EditText)view.findViewById(R.id.Price);
        max_order=(EditText)view.findViewById(R.id.max_order);

        Actual_Price=(EditText)view.findViewById(R.id.Actual_Price);
        Description=(EditText)view.findViewById(R.id.Description);
        show_sub_sub=(LinearLayout)view.findViewById(R.id.show_sub_sub);
        sub_text=(TextView)view.findViewById(R.id.sub_text);

        sub_view=(CardView)view.findViewById(R.id.sub_view);
        cat_text=(TextView)view.findViewById(R.id.cat_text);
        cat_show=(CardView)view.findViewById(R.id.cat_show);

        spinner3=(Spinner)view.findViewById(R.id.spinner3);
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        spinner=(Spinner)view.findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(cat_show.getVisibility() == View.VISIBLE) {
                    ids = categoryIds.get(i).toString();
                }
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
                if(sub_view.getVisibility() == View.VISIBLE) {
                    subid = subcategoryIds.get(i).toString();
                }
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
                if(show_sub_sub.getVisibility() == View.VISIBLE) {
                    subsubid = subsubcategoryIds.get(i).toString();
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
        DisplayProduct();
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
    public void DisplayProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")){
                                JSONObject productobj=obj.getJSONObject("product");
                                String id = productobj.getString("id");
                                String name = productobj.getString("name");
                                product_name.setText(name);
                                String description = productobj.getString("description");
                                Description.setText(description);
                                String price = productobj.getString("price");
                                Price.setText(price);
                                String actual_price = productobj.getString("actual_price");
                                Actual_Price.setText(actual_price);
                                String min_qty = productobj.getString("min_qty");
                                max_order.setText(min_qty);
                                String subcategory_id = productobj.getString("subcategory_id");
                                ids = productobj.getString("subcategory_id");
                                if(productobj.has("subcategory_id") && subcategory_id.equals("0"))
                                {
                                    cat_text.setVisibility(View.VISIBLE);
                                    cat_show.setVisibility(View.VISIBLE);
                                    sub_text.setVisibility(View.VISIBLE);
                                    sub_view.setVisibility(View.VISIBLE);
                                    show_sub_sub.setVisibility(View.VISIBLE);

                                }
                                String productcategory_id = productobj.getString("productcategory_id");
                                subid = productobj.getString("productcategory_id");
                                if(productobj.has("productcategory_id") && productcategory_id.equals("0"))
                                {
                                    cat_text.setVisibility(View.VISIBLE);
                                    cat_show.setVisibility(View.VISIBLE);
                                    sub_text.setVisibility(View.VISIBLE);
                                    sub_view.setVisibility(View.VISIBLE);
                                    show_sub_sub.setVisibility(View.VISIBLE);
                                }
                                String subproductcategory_id = productobj.getString("subproductcategory_id");
                                subsubid = productobj.getString("subproductcategory_id");
                                if(productobj.has("subproductcategory_id") && subproductcategory_id.equals("0"))
                                {
                                    cat_text.setVisibility(View.VISIBLE);
                                    cat_show.setVisibility(View.VISIBLE);
                                    sub_text.setVisibility(View.VISIBLE);
                                    sub_view.setVisibility(View.VISIBLE);
                                    show_sub_sub.setVisibility(View.VISIBLE);
                                }

                                JSONArray arrayimg=productobj.getJSONArray("images");
                                for(int i = 0; i < arrayimg.length() ; i++){
                                    JSONObject imgobj= arrayimg.getJSONObject(0);
                                    String product_id = imgobj.getString("product_id");
                                    String image = imgobj.getString("image");
                                    Log.d("image",image);
                                    show_pro_img.setImageDrawable(null);
                                    Picasso.get()
                                            .load(image)
                                            .fit().centerCrop()
                                            .into(show_pro_img);
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
                        Toast.makeText(getActivity(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("product_id", sc);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        Category_List();
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
            UpdateProduct();
        }
    }
    public void UpdateProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updateproduct,
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
                            save_product.setVisibility(View.VISIBLE);
                            DisplayProduct();
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
                params.put("product_id", sc);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
