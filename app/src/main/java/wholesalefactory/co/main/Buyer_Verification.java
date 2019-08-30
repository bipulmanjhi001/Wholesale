package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import wholesalefactory.co.R;

public class Buyer_Verification extends AppCompatActivity {

     RadioButton gst_06_on,gst_25_on;
     Button gst_25,gst_06;
     ImageView back_from_buyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer__verification);

        gst_25_on=(RadioButton)findViewById(R.id.gst_25_on);
        gst_06_on=(RadioButton)findViewById(R.id.gst_06_on);
        gst_06=(Button)findViewById(R.id.gst_06);
        gst_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gst_06_on.isChecked()) {
                    Intent intent = new Intent(Buyer_Verification.this, SetProfile.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        gst_25=(Button)findViewById(R.id.gst_25);
        gst_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gst_25_on.isChecked()) {
                    Intent intent = new Intent(Buyer_Verification.this, SetProfile.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        gst_06_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gst_06.setVisibility(View.VISIBLE);
                gst_25.setVisibility(View.GONE);
                gst_25_on.setChecked(false);
            }
        });

        gst_25_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gst_25.setVisibility(View.VISIBLE);
                gst_06.setVisibility(View.GONE);
                gst_06_on.setChecked(false);
            }
        });

        back_from_buyer=(ImageView)findViewById(R.id.back_from_buyer);
        back_from_buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(Buyer_Verification.this, Proof.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Buyer_Verification.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Buyer_Verification.this.finish();
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
