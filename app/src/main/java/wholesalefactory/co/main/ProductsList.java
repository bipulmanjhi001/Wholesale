package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ProductModel;
import wholesalefactory.co.model.VolleySingleton;

public class ProductsList extends AppCompatActivity {

    String ids;
    ListView product_list;
    ProgressBar progressBar;
    ArrayList<ProductModel> productModels;
    ProductAdapter productAdapter;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;
    TextView back;
    RelativeLayout footer_bar;
    String pro_select_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ids = bundle.getString("id");
        }
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        product_list=(ListView)findViewById(R.id.product_list);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        product_list.setDivider(null);
        product_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        productModels = new ArrayList<ProductModel>();
        footer_bar=(RelativeLayout)findViewById(R.id.footer_bar);
        footer_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendSelectedproductId();
            }
        });
        back=(TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsList.this, CategoryList.class);
                startActivity(intent);
                finish();
            }
        });
        CallProduct();
    }

    public class ProductAdapter extends BaseAdapter {
        private Context mContext;
        ArrayList<ProductModel> mylist = new ArrayList<ProductModel>();
        JSONObject jObjectData;

        public ProductAdapter(ArrayList<ProductModel> itemArray, Context mContext) {
            super();
            this.mContext = mContext;
            mylist = itemArray;
        }
        @Override
        public int getCount() {
            return mylist.size();
        }
        @Override
        public String getItem(int position) {
            return mylist.get(position).toString();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {
            private TextView id,name;
            private RadioButton checkBox;
            private CircleImageView att_img;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder view;
            LayoutInflater inflator = null;
            if (convertView == null) {
                view = new ViewHolder();
                try {
                    inflator = ((Activity) mContext).getLayoutInflater();
                    convertView = inflator.inflate(R.layout.product_list, null);
                    view.id = (TextView) convertView.findViewById(R.id.product_id);
                    view.name = (TextView) convertView.findViewById(R.id.product_name);
                    view.checkBox=(RadioButton)convertView.findViewById(R.id.product_check);
                    view.att_img=(CircleImageView)convertView.findViewById(R.id.product_img);

                    convertView.setTag(view);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            try {
                view.id.setTag(position);
                view.id.setText(mylist.get(position).getId());
                view.name.setText(mylist.get(position).getName());
                view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(view.checkBox.isChecked()){
                            footer_bar.setVisibility(View.VISIBLE);
                            try {
                                jObjectData = new JSONObject();
                                jObjectData.put("id", mylist.get(position).getId());
                            } catch (Exception e) {
                            }
                            if (pro_select_id.length() > 2) {
                                pro_select_id = pro_select_id.concat(jObjectData.toString());
                                Log.d("pr_id",pro_select_id);
                            } else {
                                pro_select_id = jObjectData.toString();
                                Log.d("pr_id",pro_select_id);
                            }
                        }
                    }
                });

                Glide.with(mContext)
                        .load(mylist.get(position).getUrl())
                        .into(view.att_img);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
    private void CallProduct(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_SUBCATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {
                                JSONArray userJson = obj.getJSONArray("category");
                                for (int i = 0; i < userJson.length(); i++) {
                                    JSONObject itemslist = userJson.getJSONObject(i);
                                    String id = itemslist.getString("id");
                                    String name = itemslist.getString("name");
                                    String photo = itemslist.getString("image");

                                    ProductModel productModel = new ProductModel(id, name, photo,false);
                                    productModels.add(productModel);
                                }
                            }else {
                                Toast.makeText(ProductsList.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            progressBar.setVisibility(View.GONE);
                            productAdapter = new ProductAdapter(productModels,ProductsList.this);
                            product_list.setAdapter(productAdapter);
                            productAdapter.notifyDataSetChanged();
                        }catch (NullPointerException e){
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
                params.put("token",tokens);
                params.put("category_id",ids);
                return params;
            }
        };

        VolleySingleton.getInstance(ProductsList.this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductsList.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ProductsList.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void SendSelectedproductId() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_setsubcategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")) {

                                Intent intent = new Intent(ProductsList.this, Home.class);
                                startActivity(intent);
                                finish();

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
                params.put("subcategory", pro_select_id);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
