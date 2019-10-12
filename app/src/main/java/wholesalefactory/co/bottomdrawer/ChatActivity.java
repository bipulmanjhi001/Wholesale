package wholesalefactory.co.bottomdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wholesalefactory.co.R;
import wholesalefactory.co.main.Home;

public class ChatActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;
    ImageView back_from_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followus);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        back_from_chat=(ImageView)findViewById(R.id.back_from_chat);
        back_from_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
