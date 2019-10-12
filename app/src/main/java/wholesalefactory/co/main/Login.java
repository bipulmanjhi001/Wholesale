package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueSdkScope;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.ConnectivityReceiver;
import wholesalefactory.co.model.VolleySingleton;
import wholesalefactory.co.pref.PrefManager;
import wholesalefactory.co.pref.SharedPrefManager;

public class Login extends AppCompatActivity {

    Button sign_in_button;
    EditText user_id;
    String username;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens, phone;
    private PrefManager prefManager;
    ITrueCallback sdkCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefManager = new PrefManager(this);
        user_id = (EditText) findViewById(R.id.user_id);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        phone = prefs.getString("phone", "");

        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrueSdkScope trueScope = new TrueSdkScope.Builder(Login.this, sdkCallback)
                        .consentMode(TrueSdkScope.CONSENT_MODE_POPUP )
                        .consentTitleOption( TrueSdkScope.SDK_CONSENT_TITLE_VERIFY )
                        .footerType( TrueSdkScope.FOOTER_TYPE_SKIP )
                        .build();

                TrueSDK.init(trueScope);
                TrueSDK.getInstance().isUsable();
               // TrueSDK.getInstance().getUserProfile();
                checkConnection();
            }
        });

        try {
            if (SharedPrefManager.getInstance(this).isLoggedIn()) {
                finish();
                 startActivity(new Intent(this, Home.class));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            attemptLogin();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    private void attemptLogin() {
        user_id.setError(null);
        username = user_id.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            user_id.setError(getString(R.string.error_field_required));
            focusView = user_id;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            Authenticate();
        }
    }

    public void Authenticate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("user");
                                String token = userJson.getString("token");
                                String otp = userJson.getString("otp");

                                Intent intent = new Intent(getApplicationContext(), OTP.class);
                                intent.putExtra("token", token);
                                intent.putExtra("mobile", username);
                                intent.putExtra("otp", otp);
                                startActivity(intent);
                                finish();

                            } else if (!obj.getBoolean("status")) {

                                String error = obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("mobile", username);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Login.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}