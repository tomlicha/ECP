package enib.ecp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.concurrent.ExecutionException;

public class Nouveau_produit extends AppCompatActivity {
    String majorite;
    EditText ET_NAME,ET_PRICE;
    String name;
    String price;
    Object result=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_produit);
        ET_NAME=(EditText)findViewById(R.id.new_product_name);
        ET_PRICE=(EditText)findViewById(R.id.new_product_price);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.majorite_non:
                if (checked)
                    majorite="0";
                break;
            case R.id.majorite_oui:
                if (checked)
                    majorite="1";
                break;
        }
    }
    public void add_product(View view) throws ExecutionException, InterruptedException {
        name=ET_NAME.getText().toString();
        price=ET_PRICE.getText().toString();
        String method="new_product";
        BDD backgroundTask = new BDD(this);
        backgroundTask.execute(method,name,price,majorite);
        GetData backgroundTask2 = new GetData(this);
        String result =backgroundTask2.execute().get();
        Intent intent = new Intent(Nouveau_produit.this,Appli.class);
        intent.putExtra("json_data",result);
        Appli.previousActivity=("Nouveau_produit");
        Log.d("json data in",result);
        startActivity(intent);
        finish();
    }
}
