package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.Map;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.Products_With_ProfileModel;
import wholesalefactory.co.model.Products_With_ProfileSubModel;
import wholesalefactory.co.model.VolleySingleton;

public class Products_With_Profile extends AppCompatActivity {

    ImageView back_from_product_profile, list_img;
    TextView title_from_product_profile, total_listing;
    ProgressBar profile_progressBar_added;
    ExpandableListView profile_product_added_list;

    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";

    String tokens, catname, username;
    String id, pro_id, sub_pro_id;
    String names, user_id, category_pro_url;
    String image;
    String location;
    String shop_name;
    CustomExpandableListAdapter customExpandableListAdapter;
    List<Products_With_ProfileSubModel> products_with_profileSubModels;
    List<Products_With_ProfileSubModel> products_with_profileSubModels2;
    HashMap<Products_With_ProfileModel, List<Products_With_ProfileSubModel>> addlist;
    ArrayList<Products_With_ProfileModel> products_with_profileModels;
    BakeryAdapter bakeryAdapter;
    int start=0,end=4,count=0,i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products__with__profile);
        list_img = (ImageView) findViewById(R.id.list_img);
        title_from_product_profile = (TextView) findViewById(R.id.title_from_product_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            pro_id = bundle.getString("pro_id");
            catname = bundle.getString("name");
            sub_pro_id = bundle.getString("sub_pro_id");
            category_pro_url = bundle.getString("category_pro_url");

            Picasso.get()
                    .load(category_pro_url)
                    .fit().centerCrop()
                    .into(list_img);
            title_from_product_profile.setText(catname);

        }
        products_with_profileModels=new ArrayList<Products_With_ProfileModel>();
        back_from_product_profile = (ImageView) findViewById(R.id.back_from_product_profile);
        back_from_product_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Products_With_Profile.this, Category_Products_List.class);
                intent.putExtra("pro_id", pro_id);
                intent.putExtra("name", catname);
                startActivity(intent);
                finish();

            }
        });

        total_listing = (TextView) findViewById(R.id.total_listing);
        profile_product_added_list = (ExpandableListView) findViewById(R.id.profile_product_added_list);
        profile_product_added_list .setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                expandableListView.expandGroup(groupPosition);
                return false;
            }
        });
        profile_product_added_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String ids=((TextView)view.findViewById(R.id.feature_id4)).getText().toString();
                Intent intent = new Intent(Products_With_Profile.this, OneProduct_display.class);
                intent.putExtra("pro_id", ids);
                intent.putExtra("subcategory_id", pro_id);
                intent.putExtra("image", image);
                intent.putExtra("name", "Loading");
                intent.putExtra("category", catname);
                intent.putExtra("productcategory_id", sub_pro_id);
                intent.putExtra("category_pro_url", category_pro_url);
                startActivity(intent);
            }
        });
        profile_progressBar_added = (ProgressBar) findViewById(R.id.profile_progressBar_added);
        addlist=new HashMap<Products_With_ProfileModel, List<Products_With_ProfileSubModel>>();

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        products_with_profileSubModels = new ArrayList<Products_With_ProfileSubModel>();
        products_with_profileSubModels2 = new ArrayList<Products_With_ProfileSubModel>();

        UPDATE();
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {

        private Context context;
        private ArrayList<Products_With_ProfileModel> expandableListTitle;
        private HashMap<Products_With_ProfileModel, List<Products_With_ProfileSubModel>> expandableListDetail;
        private int layoutResourceId;
        private int layoutsubResourceId;

        public CustomExpandableListAdapter(Context context, int layoutResourceId,
                                           ArrayList<Products_With_ProfileModel> expandableListTitle,
                                           int layoutsubResourceId, HashMap<Products_With_ProfileModel,
                                           List<Products_With_ProfileSubModel>> expandableListDetail) {
            this.context = context;
            this.layoutResourceId = layoutResourceId;
            this.expandableListTitle = expandableListTitle;
            this.layoutsubResourceId = layoutsubResourceId;
            this.expandableListDetail = expandableListDetail;
        }

        @Override
        public Object getChild(int listPosition, int expandedListPosition) {
            return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition))).get(expandedListPosition);
        }

        @Override
        public long getChildId(int listPosition, int expandedListPosition) {
            return expandedListPosition;
        }

        @Override
        public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder2 holder;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutsubResourceId, parent, false);
                holder = new ViewHolder2();
                holder.user_id = (TextView) row.findViewById(R.id.profile_product_id1);
                holder.name = (TextView) row.findViewById(R.id.profile_product_desc1);
                holder.image = (ImageView) row.findViewById(R.id.profile_product_img);

                row.setTag(holder);

            } else {
                holder = (ViewHolder2) row.getTag();
            }

            Products_With_ProfileSubModel item = Objects.requireNonNull(expandableListDetail.get(expandableListTitle.get(listPosition))).get(expandedListPosition);
            holder.user_id.setText(item.getId());
            holder.name.setText(item.getName());
            holder.image.setImageDrawable(null);
            Picasso.get()
                    .load(item.getImage())
                    .fit().centerCrop()
                    .into(holder.image);

            return row;
        }
        class ViewHolder2 {
            TextView user_id, name;
            ImageView image;
        }

        class ViewHolder {
            TextView user_id, name, shop_name, location, min_qty, deivery_date, cust_repeat;
            ImageView image;
            CircleImageView pro_img;
            LinearLayout profile_lay;
            RecyclerView sub_product_list;
            ProgressBar profile_subprogressBar_added;
        }

        @Override
        public int getChildrenCount(int listPosition) {
            return Objects.requireNonNull(this.expandableListDetail.get(this.expandableListTitle.get(listPosition))).size();
        }

        @Override
        public Object getGroup(int listPosition) {
            return this.expandableListTitle.get(listPosition);
        }

        @Override
        public int getGroupCount() {
            return this.expandableListTitle.size();
        }

        @Override
        public long getGroupId(int listPosition) {
            return listPosition;
        }

        @Override
        public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.user_id = (TextView) row.findViewById(R.id.profile_product_user_id);
                holder.name = (TextView) row.findViewById(R.id.profile_product_name);
                holder.shop_name = (TextView) row.findViewById(R.id.profile_product_shop_name);
                holder.location = (TextView) row.findViewById(R.id.profile_product_location);
                holder.image = (ImageView) row.findViewById(R.id.profile_product_img);
                holder.min_qty = (TextView) row.findViewById(R.id.min_qty);
                holder.deivery_date = (TextView) row.findViewById(R.id.deivery_date);
                holder.pro_img = (CircleImageView) row.findViewById(R.id.pro_img);
                holder.cust_repeat = (TextView) row.findViewById(R.id.cust_repeat);
                holder.profile_lay = (LinearLayout) row.findViewById(R.id.profile_lay);
                holder.sub_product_list = (RecyclerView) row.findViewById(R.id.sub_product_list);
                holder.profile_subprogressBar_added = (ProgressBar)row.findViewById(R.id.profile_subprogressBar_added);
                row.setTag(holder);

            } else {
                holder = (ViewHolder) row.getTag();
            }

            Products_With_ProfileModel item = expandableListTitle.get(listPosition);
            holder.user_id.setText(item.getUser_id());
            holder.name.setText(item.getName());
            holder.shop_name.setText(item.getShop_name());
            holder.location.setText(item.getLocation());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.sub_product_list.setLayoutManager(linearLayoutManager);

            try {
                holder.sub_product_list.setAdapter(bakeryAdapter);
                holder.profile_subprogressBar_added.setVisibility(View.GONE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            holder.min_qty.setText(" \u20B9 " + item.getMin_qty() + " min order");
            holder.image.setImageDrawable(null);

            Picasso.get()
                    .load(item.getImage())
                    .fit().centerCrop()
                    .into(holder.image);

            holder.pro_img.setImageDrawable(null);
            Picasso.get()
                    .load(item.getPro_img())
                    .fit().centerCrop()
                    .into(holder.pro_img);


            holder.shop_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Products_With_Profile.this, Vendor_Profile_With_Products_For_Main.class);
                    intent.putExtra("_id", item.getUser_id());
                    intent.putExtra("pro_id", pro_id);
                    intent.putExtra("name", catname);
                    intent.putExtra("sub_pro_id", sub_pro_id);
                    intent.putExtra("category_pro_url", category_pro_url);
                    startActivity(intent);
                }
            });

            holder.pro_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Products_With_Profile.this, Vendor_Profile_With_Products_For_Main.class);
                    intent.putExtra("_id", item.getUser_id());
                    intent.putExtra("pro_id", pro_id);
                    intent.putExtra("name", catname);
                    intent.putExtra("sub_pro_id", sub_pro_id);
                    intent.putExtra("category_pro_url", category_pro_url);
                    startActivity(intent);
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Products_With_Profile.this, Vendor_Profile_With_Products_For_Main.class);
                    intent.putExtra("_id", item.getUser_id());
                    intent.putExtra("pro_id", pro_id);
                    intent.putExtra("name", catname);
                    intent.putExtra("sub_pro_id", sub_pro_id);
                    intent.putExtra("category_pro_url", category_pro_url);
                    startActivity(intent);
                }
            });

            return row;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
            return true;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }

    private void UPDATE() {
        profile_progressBar_added.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_listings,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            Log.d("response",response);
                            if (c.getBoolean("status")) {
                                JSONArray array = c.getJSONArray("listings");

                                for ( i = 0; i < array.length(); i++) {
                                    Products_With_ProfileModel products_with_profileModel = new Products_With_ProfileModel();

                                    JSONObject object = array.getJSONObject(i);
                                    user_id = object.getString("user_id");
                                    username = object.getString("name");
                                    shop_name = object.getString("shop_name");
                                    location = object.getString("location");
                                    image = object.getString("image");

                                    products_with_profileModel.setUser_id(user_id);
                                    products_with_profileModel.setName(names);
                                    products_with_profileModel.setShop_name(shop_name);
                                    products_with_profileModel.setLocation(location);
                                    products_with_profileModel.setImage(image);

                                    JSONArray array2 = object.getJSONArray("products");

                                    for ( j = 0; j < array2.length(); j++) {

                                        JSONObject object2 = array2.getJSONObject(j);
                                        String id = object2.getString("id");
                                        String names = object2.getString("name");
                                        String min_qty = object2.getString("min_qty");
                                        String images = object2.getString("image");
                                        String  price = object2.getString("price");
                                        Products_With_ProfileSubModel products_with_profileSubModel=new Products_With_ProfileSubModel();
                                        products_with_profileSubModel.setId(id);
                                        products_with_profileSubModel.setName(names);
                                        Log.d("res", names);
                                        products_with_profileSubModel.setPrice(price);
                                        products_with_profileSubModel.setImage(images);
                                        products_with_profileSubModels.add(products_with_profileSubModel);
                                            products_with_profileModel.setMin_qty(min_qty);
                                            products_with_profileModel.setPro_img(images);

                                        count++;
                                    }
                                    try {

                                        profile_progressBar_added.setVisibility(View.GONE);
                                        products_with_profileModels.add(products_with_profileModel);
                                        addlist.put(products_with_profileModels.get(i),products_with_profileSubModels);

                                            try {
                                                customExpandableListAdapter = new CustomExpandableListAdapter(Products_With_Profile.this, R.layout.profile_product_list,products_with_profileModels,R.layout.profile_product_sublist,addlist);
                                                profile_product_added_list.setAdapter(customExpandableListAdapter);
                                                bakeryAdapter = new BakeryAdapter(Products_With_Profile.this, R.layout.profile_product_sublist, products_with_profileSubModels.subList(start, end));
                                                bakeryAdapter.notifyDataSetChanged();
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }

                                        Log.d("res", String.valueOf(products_with_profileSubModels.size()));


                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), c.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("subcategory_id", pro_id);
                params.put("productcategory_id", sub_pro_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public class BakeryAdapter extends RecyclerView.Adapter<BakeryHolder> {

        private final List<Products_With_ProfileSubModel> bakeries;
        private Context context;
        private int itemResource;

        public BakeryAdapter(Context context, int itemResource, List<Products_With_ProfileSubModel> bakeries) {
            this.bakeries = bakeries;
            this.context = context;
            this.itemResource = itemResource;
        }

        @Override
        public BakeryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(this.itemResource, parent, false);
            return new BakeryHolder(this.context, view);
        }

        @Override
        public void onBindViewHolder(BakeryHolder holder, int position) {

            Products_With_ProfileSubModel bakery = this.bakeries.get(position);
            holder.bindBakery(bakery);
        }

        @Override
        public int getItemCount() {

            return this.bakeries.size();
        }
    }

    public class BakeryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView bakeryLogo;
        private final TextView bakeryName;
        private final TextView profile_product_id1;
        private Products_With_ProfileSubModel bakery;
        private Context context;

        public BakeryHolder(Context context, View itemView) {

            super(itemView);
            this.context = context;
            this.bakeryLogo = (ImageView) itemView.findViewById(R.id.profile_product_img);
            this.bakeryName = (TextView) itemView.findViewById(R.id.profile_product_desc1);
            this.profile_product_id1 = (TextView) itemView.findViewById(R.id.profile_product_id1);
            itemView.setOnClickListener(this);
        }

        public void bindBakery(Products_With_ProfileSubModel bakery) {
            this.bakery = bakery;
            this.bakeryName.setText(bakery.getName());
            this.profile_product_id1.setText(bakery.getId());
            this.bakeryLogo.setImageDrawable(null);

            Picasso.get()
                    .load(this.bakery.getImage())
                    .fit().centerCrop()
                    .into(this.bakeryLogo);
        }

        @Override
        public void onClick(View v) {
            if (this.bakery != null) {
                Intent intent = new Intent(Products_With_Profile.this, OneProduct_display.class);
                intent.putExtra("pro_id", this.bakery.getId());
                intent.putExtra("subcategory_id", pro_id);
                intent.putExtra("image", this.bakery.getImage());
                intent.putExtra("name", this.bakery.getName());
                intent.putExtra("category", catname);
                intent.putExtra("productcategory_id", sub_pro_id);
                intent.putExtra("category_pro_url", category_pro_url);
                startActivity(intent);

            }
        }
    }
}
