package enib.ecp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

/**
 * Created by Landry on 16/05/2017.
 */

public class modifier extends AppCompatActivity {


    EditText ET_NAME,ET_PRICE;
    String name;
    String price;
    String majority;
    String majorite_mod;
    String name_mod;
    String price_mod;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier);

        name=getIntent().getExtras().getString("nom");
        price=getIntent().getExtras().getString("prix");
        majority=getIntent().getExtras().getString("majorite");

        majorite_mod=majority;

        final CheckBox maj_oui = (CheckBox)findViewById(R.id.maj_oui);
        final CheckBox maj_non = (CheckBox)findViewById(R.id.maj_non);

        ET_NAME = (EditText) findViewById(R.id.mod_product_name);
        ET_PRICE = (EditText) findViewById(R.id.mod_product_price);

        ET_NAME.setText(name);
        ET_PRICE.setText(price);

        if (majority.equals("0")){
            maj_non.setChecked(true);
            maj_oui.setChecked(false);
        }
        if (majority.equals("1")){
            maj_oui.setChecked(true);
            maj_non.setChecked(false);
        }


        maj_oui.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (majorite_mod.equals("0")) {
                        majorite_mod = "1";
                        maj_non.setChecked(false);
                    }
                }


            }
        });

        maj_non.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (majorite_mod.equals("1")) {
                        majorite_mod = "0";
                        maj_oui.setChecked(false);
                    }
                }

            }
        });


    }

    public void modifier(View view) throws ExecutionException, InterruptedException {
        name_mod=ET_NAME.getText().toString();
        price_mod=ET_PRICE.getText().toString();

        Log.d("nom",name_mod);
        Log.d("prix",price_mod);
        Log.d("m",majorite_mod);
        Log.d("name",name);
        String method="delete_product";//mettre la méthode pour supprimer un produit
        BDD backgroundTask2 = new BDD(this);
        backgroundTask2.execute(method,name);
        method="new_product";
        BDD backgroundTask = new BDD(this);
        backgroundTask.execute(method,name_mod,price_mod,majorite_mod);
        GetData backgroundtask3=new GetData(this);
        Appli.previousActivity="Nouveau_produit";
        String result =backgroundtask3.execute().get();
        Intent intent=new Intent(modifier.this,Appli.class);
        intent.putExtra("json_data",result);
        startActivity(intent);
        finish();
    }


}
