package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import wholesalefactory.co.R;
import wholesalefactory.co.drawer.Profile;

public class FollowUs extends AppCompatActivity {

    ImageView back_from_follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_us);

        back_from_follow=(ImageView)findViewById(R.id.back_from_follow);
        back_from_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(FollowUs.this, Home.class);
                startActivity(openMain);
                finish();
            }
        });
    }
}
