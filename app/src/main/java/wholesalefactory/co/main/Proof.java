package wholesalefactory.co.main;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import wholesalefactory.co.R;

public class Proof extends AppCompatActivity {

    RadioButton gst_c,shop_c,udhyog_c;
    ImageView back_from_gst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proof);
        gst_c=(RadioButton)findViewById(R.id.gst_c);
        gst_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gst_c.isChecked()){
                    Intent openMain = new Intent(Proof.this,Buyer_Verification.class);
                    startActivity(openMain);
                    finish();
                    shop_c.setChecked(false);
                    udhyog_c.setChecked(false);
                }
            }
        });
        shop_c=(RadioButton)findViewById(R.id.shop_c);
        shop_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shop_c.isChecked()){
                    Intent openMain = new Intent(Proof.this,Shop_Buyer_Verification.class);
                    startActivity(openMain);
                    finish();
                    udhyog_c.setChecked(false);
                    gst_c.setChecked(false);
                }
            }
        });
        udhyog_c=(RadioButton)findViewById(R.id.udhyog_c);
        udhyog_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(udhyog_c.isChecked()){
                    Intent openMain = new Intent(Proof.this,Udhyog_Buyer_Verification.class);
                    startActivity(openMain);
                    finish();
                    shop_c.setChecked(false);
                    gst_c.setChecked(false);
                }
            }
        });
        back_from_gst=(ImageView)findViewById(R.id.back_from_gst);
        back_from_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openMain = new Intent(Proof.this,Home.class);
                startActivity(openMain);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Proof.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_launcher_round);
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Proof.this.finish();
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
