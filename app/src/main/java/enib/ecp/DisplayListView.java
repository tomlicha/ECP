package enib.ecp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DisplayListView extends AppCompatActivity {
    String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProductAdapter productAdapter;
    ListView listView;
    TextView shoppingCart;
    int pushed;
    Button acheter;
    String method;
    String serial_number="toto";
    //String test ="0";
   // private VariableChangeListener variableChangeListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acheter);
        pushed=0;
        listView=(ListView)findViewById(R.id.listview);
        productAdapter = new ProductAdapter(this,R.layout.row_layout);
        shoppingCart=(TextView)findViewById(R.id.shoppingCart);
        listView.setAdapter(productAdapter);
        acheter=(Button)findViewById(R.id.acheter);
        json_string=getIntent().getExtras().getString("json_data");
        // setVariableChangeListener(variableChangeListener);
        //this.variableChangeListener.onVariableChanged(productAdapter.shopping_cart);
        try {
            jsonObject= new JSONObject(json_string);
            jsonArray = jsonObject.getJSONArray("server_response");
            int count=0;
            String name, price, majority;
            while(count<100){
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

    }
    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
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


    public void majpanier(View view) {
        if (pushed == 0) {
            shoppingCart.setText(Float.toString(productAdapter.shopping_cart));
            acheter.setText("ACHETER");
            pushed=pushed+1;
        } else if (pushed==1){
            if(!"toto".equals(serial_number)){
                Log.d("test","pushed");
                Log.d("appel",serial_number);
                Log.d("appel",Float.toString(productAdapter.shopping_cart));
                method="update_client_balance_buy";
                BDD backgroundTask = new BDD(this);
                backgroundTask.execute(method,Float.toString(productAdapter.shopping_cart),serial_number,null);
                finish();
            } else {
                Log.d("probleme de carte","coucou");
                Toast.makeText(this,"No card detected",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
