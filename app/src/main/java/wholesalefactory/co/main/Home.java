package wholesalefactory.co.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.drawer.About;
import wholesalefactory.co.drawer.Homescreen;
import wholesalefactory.co.drawer.Orders;
import wholesalefactory.co.drawer.Policies;
import wholesalefactory.co.drawer.Profile;
import wholesalefactory.co.drawer.Returns;
import wholesalefactory.co.drawer.Sell;
import wholesalefactory.co.model.MyBounceInterpolator;
import wholesalefactory.co.model.VolleySingleton;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView whishlist_off,whishlist_on,check_notifications,home_prescription_cart;
    Animation shake;
    LinearLayout home;
    TextView txtName,title,phones;
    DrawerLayout drawer;
    private View navHeader;
    private static final String TAG_HOME = "Home";
    private static final String TAG_ORDER = "Order";
    Toolbar toolbar;
    NavigationView navigationView;
    public  static final int RequestPermissionCode  = 1 ;
    CircleImageView profilepic;
    String imageBase;
    Intent intent;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens,phoness,pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        EnableRuntimePermission();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        txtName = (TextView) navHeader.findViewById(R.id.name);
        phones = (TextView) navHeader.findViewById(R.id.phone);

        SharedPreferences tokes = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = tokes.getString(KEY_ID, null);
        phoness = tokes.getString(KEY_ID,null);

        SharedPreferences prefs = getSharedPreferences(
                "myprofile", MODE_PRIVATE);
        String name = prefs.getString("userName", "");
        String phone = prefs.getString("mobile", "");

        if(TextUtils.isEmpty("phone")) {
            phones.setText("+91 - " + phoness);
            txtName.setText("WholeSale Factory");
        }else {
            txtName.setText(name);
            phones.setText("+91 - " + phone);
        }

        title= (TextView)findViewById(R.id.title);
        profilepic=(CircleImageView)navHeader.findViewById(R.id.imageView);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });
        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Home.this, Profile.class);
                startActivity(openMain);
                finish();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, new Homescreen()).commit();
        Home.this.setTitle(TAG_HOME);

        whishlist_off=(ImageView)findViewById(R.id.whishlist_off);
        whishlist_on=(ImageView)findViewById(R.id.whishlist_on);
        whishlist_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_off.setVisibility(View.GONE);
                whishlist_on.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Home.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_on.startAnimation(myAnim);
            }
        });

        whishlist_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whishlist_on.setVisibility(View.GONE);
                whishlist_off.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(Home.this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
                myAnim.setInterpolator(interpolator);
                whishlist_off.startAnimation(myAnim);
            }
        });

        home=(LinearLayout)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.dashboard, new Homescreen()).commit();
                title.setText(TAG_HOME);

            }
        });

        check_notifications=(ImageView)findViewById(R.id.check_notifications);
        check_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_notifications.setBackgroundResource(R.drawable.ic_turn_notifications_on_button);
                shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                check_notifications.startAnimation(shake);
            }
        });

        home_prescription_cart=(ImageView)findViewById(R.id.home_prescription_cart);
        home_prescription_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Home.this, Cart.class);
                startActivity(openMain);
                finish();
            }
        });
        SetImage();
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Home.this.finish();
                    }
                });
        alertDialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new Homescreen()).commit();
            title.setText(TAG_HOME);

        } else if (id == R.id.nav_order) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new Orders()).commit();
            title.setText(TAG_ORDER);

        } else if (id == R.id.nav_returns) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new Returns()).commit();
            title.setText("Returns");

        } else if (id == R.id.nav_about) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new About()).commit();
            title.setText("About Us");

        } else if (id == R.id.nav_sell) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new Sell()).commit();
            title.setText("Sell");

        }  else if (id == R.id.nav_policies) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.dashboard, new Policies()).commit();
            title.setText("Policies");

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageBase=getStringImage(bitmap);
            profilepic.setBackgroundResource(android.R.color.transparent);
            profilepic.setImageBitmap(bitmap);
            UploadImage();
        }
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.CAMERA)) {
            Log.d( "Permission","CAMERA permission allows us to Access CAMERA app");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d( "Permission","Permission Granted, Now your application can access CAMERA.");
                } else {
                    Log.d( "Permission","Permission Canceled, Now your application cannot access CAMERA.");
                }
                break;
        }
    }

    public void UploadImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updatephoto,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("photo", imageBase);
                params.put("token", tokens);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void SetImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_setpic,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            pic = obj.getString("photo");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(pic != null) {
                            Glide.with(getApplicationContext())
                                    .load(pic)
                                    .error(R.drawable.no_image_available)
                                    .into(profilepic);
                        }else {
                            profilepic.setBackground(getResources().getDrawable(R.drawable.ic_profle));
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
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}
