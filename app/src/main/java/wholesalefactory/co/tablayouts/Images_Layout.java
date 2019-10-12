package wholesalefactory.co.tablayouts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import wholesalefactory.co.R;
import wholesalefactory.co.api.URLs;
import wholesalefactory.co.model.VolleySingleton;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Images_Layout extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    Intent intent ;
    public  static final int RequestPermissionCode  = 1 ;
    String image,key="0";
    private ImageView img1,img2,img3;
    int count = 0 ;
    public static final String PREFS_GAME ="saveproductid";
    public static final String GAME_SCORE= "pro_id";
    static final String SHARED_PREF_NAME = "wholesalepref";
    static final String KEY_ID = "token";
    String tokens,sc;
    ImageView show_pro_img;

    public Images_Layout() {
    }

    public static Images_Layout newInstance(String param1, String param2) {
        Images_Layout fragment = new Images_Layout();
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
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        EnableRuntimePermission();

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_GAME ,Context.MODE_PRIVATE);
        sc  = sp.getString(GAME_SCORE,"");

        show_pro_img=(ImageView)view.findViewById(R.id.show_pro_img);
        DisplayProduct();

         img1=(ImageView)view.findViewById(R.id.img1);
         img1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(intent, 7);
             }
         });

        img2=(ImageView)view.findViewById(R.id.img2);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        img3=(ImageView)view.findViewById(R.id.img3);
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmps=Bitmap.createScaledBitmap(bmp, 150, 150, false);
        bmps.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ++count;
            if(count == 1) {
                image = getStringImage(bitmap);
                img1.setImageDrawable(null);
                img1.setImageBitmap(bitmap);
                key="1";
                UploadImages();
            }
            if(count == 2) {
                image = getStringImage(bitmap);
                img2.setImageDrawable(null);
                img2.setImageBitmap(bitmap);
                key="2";
                UploadImages();
            }
            if(count == 3) {
                image = getStringImage(bitmap);
                img3.setImageDrawable(null);
                img3.setImageBitmap(bitmap);
                key="3";
                UploadImages();
            }
            if(count == 4) {
                Toast.makeText(getActivity(), "Update Product details", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void UploadImages(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_updateproductimage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")){
                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("product_id", sc);
                params.put("key", key);
                params.put("image", image);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    public void DisplayProduct(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_productdetails,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("status")){
                                JSONObject productobj=obj.getJSONObject("product");
                                JSONArray arrayimg=productobj.getJSONArray("images");
                                for(int i = 0; i < arrayimg.length() ; i++){
                                    JSONObject imgobj= arrayimg.getJSONObject(0);
                                    String product_id = imgobj.getString("product_id");
                                    String image = imgobj.getString("image");
                                    show_pro_img.setImageDrawable(null);
                                    Picasso.get()
                                            .load(image)
                                            .fit().centerCrop()
                                            .into(show_pro_img);

                                    JSONObject imgobj1= arrayimg.getJSONObject(1);
                                    String product_id1 = imgobj1.getString("product_id");
                                    String image1 = imgobj1.getString("image");
                                    show_pro_img.setImageDrawable(null);
                                    Picasso.get()
                                            .load(image1)
                                            .fit().centerCrop()
                                            .into(img1);

                                    JSONObject imgobj2= arrayimg.getJSONObject(2);
                                    String product_id2 = imgobj2.getString("product_id");
                                    String image2 = imgobj2.getString("image");
                                    show_pro_img.setImageDrawable(null);
                                    Picasso.get()
                                            .load(image2)
                                            .fit().centerCrop()
                                            .into(img2);

                                    JSONObject imgobj3= arrayimg.getJSONObject(3);
                                    String product_id3 = imgobj3.getString("product_id");
                                    String image3 = imgobj3.getString("image");
                                    show_pro_img.setImageDrawable(null);
                                    Picasso.get()
                                            .load(image3)
                                            .fit().centerCrop()
                                            .into(img3);
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
                        Toast.makeText(getActivity(),"Check again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", tokens);
                params.put("product_id", sc);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }
}
