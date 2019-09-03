package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wholesalefactory.co.R;

public class ChatActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "wholesalepref";
    private static final String KEY_ID = "token";
    String tokens;
    ImageView back_from_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followus);

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
