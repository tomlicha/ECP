package enib.ecp;

import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static enib.ecp.Appli.json_string;


public class Acheter extends Fragment {
    FloatingActionButton ajouter_produit = null;
    Context thiscontext;

    // String json_string = "toto";
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProductAdapter productAdapter;
    ListView listView;
    static TextView shoppingCart;
    int pushed;
    Button acheter;
    String method;
    static String serial_number="toto";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = inflater.getContext();
        View rootView = inflater.inflate(R.layout.activity_acheter, container, false);
        pushed=0;


        listView=(ListView) rootView.findViewById(R.id.listview);
        productAdapter = new ProductAdapter(thiscontext,R.layout.row_layout);
        listView.setAdapter(productAdapter);

        shoppingCart=(TextView) rootView.findViewById(R.id.shoppingCart);

        acheter=(Button)rootView.findViewById(R.id.acheter);
        try {
            jsonObject= new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count=0;
            String name, price, majority;
            while(count<jsonArray.length()){
                JSONObject JO = jsonArray.getJSONObject(count);
                name = JO.getString("name");
                price = JO.getString("price");
                majority = JO.getString("majorite");
                Products products =new Products(name, price, majority);
                productAdapter.add(products);
                count++;


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button majpanier = (Button) rootView.findViewById(R.id.acheter);
        majpanier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                    if(!"toto".equals(serial_number)){
                        Log.d("test","pushed");
                        Log.d("appel",serial_number);
                        Log.d("appel",Float.toString(productAdapter.shopping_cart));
                        method="update_client_balance_buy";
                        BDD backgroundTask = new BDD(thiscontext);
                        backgroundTask.execute(method,Float.toString(productAdapter.shopping_cart),serial_number,null);
                        //finish();
                    } else {
                        Log.d("probleme de carte","coucou");
                        Toast.makeText(thiscontext,"Aucune carte détectée, veuillez présenter votre carte",Toast.LENGTH_LONG).show();
                    }

            }
        });

        return rootView;
    }



    protected void new_intent_acheter(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            serial_number=ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            Log.d("test",serial_number);
        }
    }


    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


}







