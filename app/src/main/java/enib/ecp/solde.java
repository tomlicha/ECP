package enib.ecp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class solde extends Fragment {

    Context thiscontext;

    public TextView mTextView;
    public String json_string;
    JSONObject jsonObject;
    JSONArray jsonArray;
    TextView resultat;
    TextView idcard;
    String serial_number;
    String json_url_card1;
    int new_balance;
    EditText ET_NEW_BALANCE;
    String method="getClientData";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        thiscontext = inflater.getContext();
        View rootView = inflater.inflate(R.layout.activity_solde, container, false);
        mTextView = (TextView) rootView.findViewById(R.id.solde);
        ET_NEW_BALANCE=(EditText)rootView.findViewById(R.id.new_balance);
        Button button = (Button) rootView.findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                json_url_card1="http://192.168.1.1:8080/products/client_update_balance.php";
                method="SetNewBalance";
                new_balance=Integer.parseInt(ET_NEW_BALANCE.getText().toString());
                solde.BackgroundTask backgroundTask = new solde.BackgroundTask(thiscontext);
                backgroundTask.execute(serial_number,method);
                //finish();
                //Toast.makeText(getApplicationContext(),"Top up successful",Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(),"Rechargement effectué",Toast.LENGTH_LONG).show();

            }
        });

        if (rootView==null){Log.d("rootview","root view est nul\n"); }
        return rootView;
    }








    protected void new_intent_solde(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            json_url_card1="http://192.168.1.1:8080/products/json_get_data_client.php";
            method="getClientData";
            BackgroundTask backgroundTask = new solde.BackgroundTask(getActivity());
            backgroundTask.execute(ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)),method);
            if (json_string==null){
                serial_number=ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                Toast.makeText(getContext(),"Données en cours d'aquisition, recommencer svp",Toast.LENGTH_LONG).show();
            }
            else{
                try {
                    Acheter.serial_number=ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                    Toast.makeText(getContext(),"carte detectée, id = "+ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)),Toast.LENGTH_LONG).show();
                    jsonObject= new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    String id;
                    float balance;
                    JSONObject JO = jsonArray.getJSONObject(0);
                    id = JO.getString("id");
                    balance = BigDecimal.valueOf(JO.getDouble("balance")).floatValue();
                    idcard=(TextView)getView().findViewById(R.id.tx_id);
                    resultat = (TextView)getView().findViewById(R.id.tx_balance);
                    resultat.setText(""+balance+" €");
                    idcard.setText(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

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

    class BackgroundTask extends AsyncTask<String, Void, String> {
        Context ctx;
        String json_url;
        String JSON_STRING;



        BackgroundTask(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {
            json_url = json_url_card1;

        }

        @Override
        protected void onPostExecute(String result) {
            //  TextView testjson=(TextView)findViewById(R.id.testJson);
            //testjson.setText(result);
            json_string=result;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String NFCCARD = params[0];
            String method = params[1];
            if (method.equals("getClientData")) {
                try {
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(NFCCARD, "UTF-8") + "&" +
                            URLEncoder.encode("new_balance", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(new_balance),"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_STRING);
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (method.equals("SetNewBalance")) {
                try {
                    Log.d("toto",json_url);
                    URL url = new URL(json_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(serial_number, "UTF-8") + "&" +
                            URLEncoder.encode("new_balance", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(new_balance), "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    return stringBuilder.toString().trim();
                }catch(MalformedURLException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }


            }

            return null;
        }

    }


}