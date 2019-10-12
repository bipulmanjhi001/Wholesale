package wholesalefactory.co.bottomdrawer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.adapter.FollowAdapter;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.main.Home;
import wholesalefactory.co.model.FollowModel;
import wholesalefactory.co.model.VolleySingleton;

public class FollowUs extends AppCompatActivity {

    ImageView back_from_follow;
    ListView follow_list;
    ProgressBar progressBar;
    ArrayList<FollowModel> followModels;
    FollowAdapter followAdapter;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_us);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        back_from_follow=(ImageView)findViewById(R.id.back_from_follow);
        back_from_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(FollowUs.this, Home.class);
                startActivity(openMain);
                finish();
            }
        });
        follow_list=(ListView)findViewById(R.id.follow_list);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        followModels=new ArrayList<FollowModel>();
        CallList();
    }

    public void CallList(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_followlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            try {
                                followModels.clear();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            JSONArray array=obj.getJSONArray("followlist");
                            if(array != null && array.length() > 0 ) {

                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject itemslist2 = array.getJSONObject(j);
                                    String user_id = itemslist2.getString("user_id");
                                    String shop_name = itemslist2.getString("shop_name");
                                    String image = itemslist2.getString("image");
                                    String location = itemslist2.getString("location");

                                    FollowModel followModel = new FollowModel();
                                    followModel.setUser_id(user_id);
                                    followModel.setShop_name(shop_name);
                                    followModel.setImage(image);
                                    followModel.setLocation(location);
                                    followModels.add(followModel);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            progressBar.setVisibility(View.GONE);
                            followAdapter = new FollowAdapter(followModels, FollowUs.this);
                            follow_list.setAdapter(followAdapter);
                            followAdapter.notifyDataSetChanged();
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
                params.put("token", tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
