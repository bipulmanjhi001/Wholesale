package wholesalefactory.co.tablayouts;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import wholesalefactory.co.R;

import static android.content.Context.MODE_PRIVATE;

public class Attribute_Layout extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    public static final String PREFS_GAME ="saveproductid";
    public static final String GAME_SCORE= "pro_id";

    LinearLayout linearLayout2;
    String data="",valueList3,valueList4;
    Boolean i = false;
    JSONObject jObjectData;
    TableLayout linear;
    static final String SHARED_PREF_NAME = "wholesalepref";
    static final String KEY_ID = "token";
    String tokens;
    Button add_more;

    public Attribute_Layout() {

    }

    public static Attribute_Layout newInstance(String param1, String param2) {
        Attribute_Layout fragment = new Attribute_Layout();
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
        View view = inflater.inflate(R.layout.fragment_attribute, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);
        Log.d("sc",tokens);

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_GAME ,Context.MODE_PRIVATE);
        String sc  = sp.getString(GAME_SCORE,"");
        Log.d("sc",sc);
        jObjectData = new JSONObject();

       /* linear=(TableLayout)view.findViewById(R.id.linear);
        add_more=(Button)view.findViewById(R.id.add_more);
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cust_layout();
            }
        });*/

        return view;
    }

    private void cust_layout(){
        linearLayout2 = new LinearLayout(getActivity());
        linearLayout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

        EditText tv3 = new EditText(getActivity());
        tv3.setHint("SIZE");
        tv3.setWidth(200);
        tv3.setGravity(Gravity.CENTER_VERTICAL);
        tv3.setHeight(35);
        tv3.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        tv3.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        tv3.setTextSize(12);
        tv3.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv3.setInputType(InputType.TYPE_CLASS_TEXT);
        tv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                valueList3 = s.toString();
            }
        });

        EditText tv4 = new EditText(getActivity());
        tv4.setHint("LIMIT");
        tv4.setWidth(200);
        tv4.setGravity(Gravity.CENTER_VERTICAL);
        tv4.setHeight(35);
        tv4.setHintTextColor(getResources().getColor(R.color.colorPrimary));
        tv4.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        tv4.setTextSize(12);
        tv4.setTextColor(getResources().getColor(R.color.colorPrimary));
        tv4.setInputType(InputType.TYPE_CLASS_TEXT);
        tv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                valueList4 = s.toString();
                i=true;
                try {
                    jObjectData.put("SIZE", valueList3);
                    jObjectData.put("LIMIT", valueList4);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if (data.length() > 95) {
                    data = data.concat(jObjectData.toString());
                    Log.d("json",data);
                } else {
                    data = jObjectData.toString();
                    Log.d("json",data);
                }
            }
        });

        linearLayout2.addView(tv3);
        linearLayout2.addView(tv4);
        linear.addView(linearLayout2);
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

}