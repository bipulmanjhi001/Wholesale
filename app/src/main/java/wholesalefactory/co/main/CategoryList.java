package wholesalefactory.co.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.Category_GridView_ImageAdapter;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ConnectionDetector;
import wholesalefactory.co.model.ExpandableHeightGridView;
import wholesalefactory.co.model.VolleySingleton;
import wholesalefactory.co.pojo.Category_Grid_Model;
import org.json.JSONException;

public class CategoryList extends AppCompatActivity {

    ExpandableHeightGridView mGridView;
    private Category_GridView_ImageAdapter category_gridView_imageAdapter;
    private ArrayList<Category_Grid_Model> mGridData;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    ProgressBar progressBar;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        mGridView=(ExpandableHeightGridView)findViewById(R.id.grid_imagelist);
        mGridView.setExpanded(true);

        if (isInternetPresent) {

            mGridData = new ArrayList<Category_Grid_Model>();
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            UPDATE();

            mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                    Category_Grid_Model item = (Category_Grid_Model) parent.getItemAtPosition(position);
                    Intent intent = new Intent(CategoryList.this, Home.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            setContentView(R.layout.activity_offline);
            LinearLayout offline_more=(LinearLayout)findViewById(R.id.offline_more);
            offline_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
        }
    }

    private void UPDATE() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject c = new JSONObject(response);
                            if(c.getBoolean("status")){
                            JSONArray array = c.getJSONArray("category");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");
                                String image = object.getString("image");

                                Category_Grid_Model _grid_model = new Category_Grid_Model();
                                _grid_model.setId(id);
                                _grid_model.setName(name);
                                _grid_model.setImage(image);
                                mGridData.add(_grid_model);
                            }
                            }else {
                                Toast.makeText(getApplicationContext(), c.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        category_gridView_imageAdapter = new Category_GridView_ImageAdapter(CategoryList.this, R.layout.image_library, mGridData);
                        mGridView.setAdapter(category_gridView_imageAdapter);
                        category_gridView_imageAdapter.setGridData(mGridData);
                        progressBar.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check connection again.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CategoryList.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CategoryList.this.finish();
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
