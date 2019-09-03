package wholesalefactory.co.drawer;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.ProductAdapter;
import wholesalefactory.co.adapter.Top_Sallers_Adapter;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.Proof;
import wholesalefactory.co.model.ExpandableHeightGridView;
import wholesalefactory.co.model.ProductModel;
import wholesalefactory.co.model.SliderUtils;
import wholesalefactory.co.model.Top_Sallers_Model;
import wholesalefactory.co.model.UpdateMeeDialog;
import wholesalefactory.co.model.ViewPagerAdapter;
import wholesalefactory.co.model.VolleySingleton;
import static android.content.Context.MODE_PRIVATE;

public class Homescreen  extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Dialog myDialog;
    public static String PACKAGE_NAME;
    private LinearLayout verify_a1;

    Boolean off_welcome=true;
    LocationTrack locationTrack;
    private String versionName;
    private OnFragmentInteractionListener mListener;
    private Double longitude,latitude;
    private TextView locationtext;
    private ListView Type,Type2;

    private ArrayList state_names = new ArrayList();
    private ArrayList state_ids = new ArrayList();
    private ArrayList city_names = new ArrayList();
    private ArrayList city_ids = new ArrayList();
    private EditText address1,Landmark,pincode,name,email;
    private TextView city,Other;

    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    private String tokens,city_id,stateid;
    private String names,addresss,emails,citys,Landmarks,Others,pincodes;

    private ExpandableHeightGridView mGridView;
    private ExpandableHeightGridView top_sallers;
    private ProductAdapter productAdapter;
    private ArrayList<ProductModel> mGridData;
    private ImageView[] dots;
    private ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private List<SliderUtils> sliderImg;
    private SliderUtils sliderUtils;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView submit_proof;
    String bannerbottom;
    protected LocationManager locationManager;
    private Top_Sallers_Adapter top_sallers_adapter;
    private ArrayList<Top_Sallers_Model> top_sallers_models;
    String top_saller_img;

    public Homescreen() {
    }

    public static Homescreen newInstance(String param1, String param2) {
        Homescreen fragment = new Homescreen();
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
        View view = inflater.inflate(R.layout.content_homescreen, container, false);
        myDialog=new Dialog(getActivity());
            ShowPopup(view);

        final PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
                PACKAGE_NAME = getActivity().getPackageName();
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                versionName = null;
            }
        }
        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        Log.d("token",tokens);

        UPDATE();
        locationtext=(TextView)view.findViewById(R.id.location);
        verify_a1=(LinearLayout)view.findViewById(R.id.verify_a1);
        verify_a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(getActivity(), Proof.class);
                startActivity(openMain);
            }
        });

        locationTrack = new LocationTrack(getActivity());
        mGridView=(ExpandableHeightGridView)view.findViewById(R.id.grid_imagelist_pro);
        top_sallers=(ExpandableHeightGridView)view.findViewById(R.id.grid_top_sellers);
        mGridView.setExpanded(true);
        top_sallers.setExpanded(true);
        mGridData = new ArrayList<ProductModel>();
        top_sallers_models=new ArrayList<Top_Sallers_Model>();

           /* mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Category_Grid_Model item = (Category_Grid_Model) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("id", item.getId());
                    startActivity(intent);
                }
            });*/

        submit_proof=(ImageView)view.findViewById(R.id.submit_proof);
        sliderImg = new ArrayList<>();
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout)view.findViewById(R.id.layoutDots);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                try {
                    for (int i = 0; i < dotscount; i++) {
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                    }
                    dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    private void CallProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_dashboardtop,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONObject dashobj=obj.getJSONObject("dashboard");
                                JSONArray sliearr=dashobj.getJSONArray("banner");

                                for(int j = 0; j < sliearr.length(); j++){
                                    sliderUtils = new SliderUtils();
                                    try {
                                        JSONObject getslide = sliearr.getJSONObject(j);
                                        sliderUtils.setSliderImageUrl(getslide.getString("image"));
                                        sliderImg.add(sliderUtils);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                JSONArray userJson = dashobj.getJSONArray("subcategory");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    ProductModel productModel = new ProductModel(id, name, photo,false);
                                    mGridData.add(productModel);
                                }
                                bannerbottom=dashobj.getString("bannerbottom");
                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        viewPagerAdapter = new ViewPagerAdapter(sliderImg, getActivity());
                        viewPager.setAdapter(viewPagerAdapter);
                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for(int i = 0; i < dotscount; i++){

                            dots[i] = new ImageView(getActivity());
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(8, 0, 8, 0);
                            sliderDotspanel.addView(dots[i], params);

                        }
                        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                        productAdapter = new ProductAdapter(getActivity(), R.layout.product_list, mGridData);
                        mGridView.setAdapter(productAdapter);
                        productAdapter.setGridData(mGridData);
                        mGridView.setVisibility(View.VISIBLE);
                        Picasso.with(getActivity())
                                .load(bannerbottom)
                                .fit().centerCrop()
                                .into(submit_proof);

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
                params.put("token",tokens);
                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        TopSallers();
    }

    private void TopSallers(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_dashboardmiddle,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONObject dashobj=obj.getJSONObject("dashboard");
                                JSONArray toparr=dashobj.getJSONArray("topsellers");

                                for(int j = 0; j < toparr.length(); j++){
                                    try {
                                        JSONObject gettop = toparr.getJSONObject(j);
                                        top_saller_img= gettop.getString("image");
                                        Top_Sallers_Model top_sallers_model=new Top_Sallers_Model();
                                        top_sallers_model.setImage(top_saller_img);
                                        top_sallers_models.add(top_sallers_model);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                              /*  JSONArray userJson = dashobj.getJSONArray("subcategory");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    ProductModel productModel = new ProductModel(id, name, photo,false);
                                    mGridData.add(productModel);
                                }
                                bannerbottom=dashobj.getString("bannerbottom");*/

                            }else {
                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        top_sallers_adapter = new Top_Sallers_Adapter(getActivity(), R.layout.to_sellers, top_sallers_models);
                        top_sallers.setAdapter(top_sallers_adapter);
                        top_sallers_adapter.setGridData(top_sallers_models);
                        top_sallers.setVisibility(View.VISIBLE);
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
                params.put("token",tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        locationTrack.stopListener();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void ShowPopup(View v) {
        LinearLayout btnFollow;
        Button add_state;
        myDialog.setContentView(R.layout.custom_address);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        address1 = (EditText) myDialog.findViewById(R.id.address);
        city = (TextView) myDialog.findViewById(R.id.city);
        Landmark = (EditText) myDialog.findViewById(R.id.Landmark);
        Other = (TextView) myDialog.findViewById(R.id.Other);
        pincode = (EditText) myDialog.findViewById(R.id.pincode);
        name = (EditText) myDialog.findViewById(R.id.name);
        email = (EditText) myDialog.findViewById(R.id.email);

        btnFollow=(LinearLayout)myDialog.findViewById(R.id.CONFIRM_add);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                names = name.getText().toString();
                emails = email.getText().toString();
                addresss = address1.getText().toString();
                citys = city.getText().toString();
                Landmarks = Landmark.getText().toString();
                Others = Other.getText().toString();
                pincodes = pincode.getText().toString();
                attemptUpdate();
            }
        });
        add_state=(Button)myDialog.findViewById(R.id.add_state);
        add_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showState(view);
            }
        });
        myDialog.show();
    }

    private void attemptUpdate() {
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(names)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }
        if (TextUtils.isEmpty(emails)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        }
        if (TextUtils.isEmpty(addresss)) {
            address1.setError(getString(R.string.error_field_required));
            focusView = address1;
            cancel = true;
        }
        if (TextUtils.isEmpty(pincodes)) {
            pincode.setError(getString(R.string.error_field_required));
            focusView = pincode;
            cancel = true;
        }
        if (TextUtils.isEmpty(citys)) {
            city.setError(getString(R.string.error_field_required));
            focusView = city;
            cancel = true;
        }
        if (TextUtils.isEmpty(Landmarks)) {
            Landmark.setError(getString(R.string.error_field_required));
            focusView = Landmark;
            cancel = true;
        }
        if (TextUtils.isEmpty(Others)) {
            Other.setError(getString(R.string.error_field_required));
            focusView = Other;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            UpdteProfile();
        }
    }
    private void UpdteProfile() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updateprofile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Toast.makeText(getActivity(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                myDialog.dismiss();

                            } else {
                                Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Check details again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("name", names);
                params.put("email", emails);
                params.put("address", addresss);
                params.put("pincode", pincodes);
                params.put("landmark", Landmarks);
                params.put("district",city_id);
                params.put("state", stateid);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GETVERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject getversion = obj.getJSONObject("version");
                                String version_code = getversion.getString("version_code");
                                String version_name = getversion.getString("version_name");

                                if (version_name.equals(versionName)) {
                                    if(off_welcome) {
                                        off_welcome=false;
                                    }

                                } else {

                                    UpdateMeeDialog updateMeeDialog = new UpdateMeeDialog();
                                    updateMeeDialog.showDialogAddRoute(getActivity(), PACKAGE_NAME);
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
                        Toast.makeText(getActivity(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        GetState();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationTrack.stopListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationTrack.stopListener();
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
                                Toast.makeText(getActivity(),"No state added",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Check again..", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        CallProduct();
    }

    private void showState(View view) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.state_list_dialog);
        Type= (ListView) dialog.findViewById(R.id.state_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, state_names){
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
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.city_list_dialog);
        Type2= (ListView) dialog.findViewById(R.id.city_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, city_names){
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
                dialog.dismiss();
            }
        });
        dialog.show();
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
                                Toast.makeText(getActivity(),"No city added",Toast.LENGTH_SHORT).show();
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    public class LocationTrack extends Service implements LocationListener {

        private final Context mContext;
        boolean checkGPS = false;
        boolean checkNetwork = false;
        boolean canGetLocation = false;
        Location loc;
        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 60000;


        public LocationTrack(Context mContext) {
            this.mContext = mContext;
            getLocation();
        }

        private Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
                checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (!checkGPS && !checkNetwork) {
                    Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT).show();
                } else {
                    this.canGetLocation = true;
                    if (checkGPS) {

                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                        }

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d( "Coordinates",latitude+""+longitude);

                                try {
                                    Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
                                    List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
                                    if (addresses.isEmpty()) {
                                        locationtext.setText("Location");
                                    } else {
                                        if (addresses.size() > 0) {
                                            locationtext.setText(addresses.get(0).getLocality());
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                        if (locationManager == null) {
                            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return loc;
        }

        public void stopListener() {
            if (locationManager != null) {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(LocationTrack.this);
            }
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {
            location.setAccuracy(100);
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            Log.e("latitude", latitude + "");
            Log.e("longitude", longitude + "");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }
}