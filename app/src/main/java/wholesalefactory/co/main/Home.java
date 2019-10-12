package wholesalefactory.co.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.text.TextUtils;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.bottomdrawer.ChatActivity;
import wholesalefactory.co.bottomdrawer.FollowUs;
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
    TextView txtName,title,phones,cart_counts;
    DrawerLayout drawer;
    private View navHeader;
    private static final String TAG_HOME = "Home";
    private static final String TAG_ORDER = "Order";
    Toolbar toolbar;
    NavigationView navigationView;
    CircleImageView profilepic;
    Intent intent;
    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens,phoness,pic,name="Wholesale Factory";
    LinearLayout follow,chats;
    Class fragmentClass;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

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
        phoness = tokes.getString("role",null);
        Log.d("token",tokens);

        SharedPreferences prefs = getSharedPreferences("myprofile", MODE_PRIVATE);
        name = prefs.getString("userName", "");
         phoness = prefs.getString("mobile", "");

        if(TextUtils.isEmpty(name)) {
            phones.setText("+91 - " + phoness);
            txtName.setText(name);
        }else {
            txtName.setText(name);
            phones.setText("+91 - " + phoness);
        }
        title= (TextView)findViewById(R.id.title);
        profilepic=(CircleImageView)navHeader.findViewById(R.id.imageView);
        cart_counts=(TextView)findViewById(R.id.cart_counts);
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
        follow=(LinearLayout)findViewById(R.id.follows);
        chats=(LinearLayout)findViewById(R.id.chats);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.dashboard, new Homescreen()).commit();
                title.setText(TAG_HOME);

            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this, FollowUs.class);
                startActivity(intent);
            }
        });
        chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Home.this, ChatActivity.class);
                startActivity(intent);
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
        CartAmount();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentClass = Homescreen.class;
            title.setText(TAG_HOME);

        } else if (id == R.id.nav_order) {

            fragmentClass= Orders.class;
            title.setText(TAG_ORDER);

        } else if (id == R.id.nav_returns) {

            fragmentClass= Returns.class;
            title.setText("Returns");

        } else if (id == R.id.nav_about) {

            fragmentClass= About.class;
            title.setText("About W/F");

        } else if (id == R.id.nav_sell) {

            fragmentClass= Sell.class;
            title.setText("Sell on W/F");

        }  else if (id == R.id.nav_policies) {

            fragmentClass= Policies.class;
            title.setText("Policies");

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard, fragment).commit();
        item.setChecked(true);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void CartAmount(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_cartcount,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Integer count=obj.getInt("cartcount");
                            cart_counts.setText(String.valueOf(count));

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
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        SetImage();
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Home.this.finish();
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
