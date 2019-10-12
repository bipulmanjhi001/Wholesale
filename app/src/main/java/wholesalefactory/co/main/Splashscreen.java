package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ConnectivityReceiver;
import wholesalefactory.co.model.VolleySingleton;
import wholesalefactory.co.model.wholesaleApplication;

public class Splashscreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    static final String SHARED_PREF_NAME = "wholesalepref";
    static final String KEY_ID = "token";
    String tokens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        checkConnection();

    }
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if(tokens !=null && !tokens.isEmpty()){
                            Check();
                        }else {
                            Intent intent=new Intent(Splashscreen.this,Login.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                }
            };
            timer.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        wholesaleApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void Check() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_currentlogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                Intent intent=new Intent(Splashscreen.this, Home.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(!obj.getBoolean("status") && obj.getString("category").equals("0")) {

                                Intent intent=new Intent(Splashscreen.this,CategoryList.class);
                                startActivity(intent);
                                finish();
                            }
                             else if(!obj.getBoolean("status") && obj.getString("message").equals("User not logged in")){
                                Intent intent=new Intent(Splashscreen.this,Login.class);
                                startActivity(intent);
                                finish();
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
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Splashscreen.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Splashscreen.this.finish();
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

