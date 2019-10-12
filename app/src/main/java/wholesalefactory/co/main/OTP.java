package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import wholesalefactory.co.welcome.WelcomeActivity;
import wholesalefactory.co.model.ConnectivityReceiver;
import wholesalefactory.co.model.VolleySingleton;
import wholesalefactory.co.pref.SharedPrefManager;
import wholesalefactory.co.pref.User;

public class OTP extends AppCompatActivity {

    Button otp_verify;
    String token, otp_numbers, mobile,otp;
    EditText otp_number;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    CheckBox termsandcondition;
    private Dialog myDialog;
    CheckBox t_check,l_check;
    LinearLayout btnFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            token = bundle.getString("token");
            mobile = bundle.getString("mobile");
            otp = bundle.getString("otp");
        }

        termsandcondition = (CheckBox) findViewById(R.id.termsandcondition);
        otp_number = (EditText) findViewById(R.id.otp);
        otp_verify = (Button) findViewById(R.id.otp_verify);
        otp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termsandcondition.isChecked()) {
                    checkConnection();
                } else {
                    ShowPopup(view);
                    termsandcondition.setChecked(true);
                }
            }
        });
        showNotification(OTP.this);
        myDialog=new Dialog(OTP.this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            attemptOTP();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    private void attemptOTP() {
        otp_numbers = otp_number.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(otp_numbers)) {
            otp_number.setError("Invalid OTP");
            focusView = otp_number;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
        else {
            Authenticate();
        }
    }

    public void Authenticate() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getBoolean("status")) {

                                JSONObject message = obj.getJSONObject("user");
                                String token = message.getString("token");
                                String category=message.getString("category");
                                User user = new User(token, mobile);

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();
                                if(category.equals("0")) {
                                         Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                                         startActivity(intent);
                                         finish();
                                   }
                                else  {
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else if (!obj.getBoolean("status")) {

                                String error = obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
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
                params.put("token", token);
                params.put("otp", otp_numbers);
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
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OTP.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        OTP.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void ShowPopup(View v) {

        myDialog.setContentView(R.layout.privacy);
        myDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        t_check=(CheckBox)myDialog.findViewById(R.id.t_check);
        l_check=(CheckBox)myDialog.findViewById(R.id.l_check);
        btnFollow=(LinearLayout)myDialog.findViewById(R.id.CONFIRM_add);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(t_check.isChecked() && l_check.isChecked()){
                    myDialog.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        myDialog.show();
    }

    public void showNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Your OTP is :"+otp)
                .setContentTitle("Welcome to Wholesale Factory");

        notificationManager.notify(notificationId, mBuilder.build());
    }
}
