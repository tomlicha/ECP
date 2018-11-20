package enib.ecp;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class GetData extends AsyncTask<String, Void, String> {
    Context ctx;
    String json_url;
    String JSON_STRING;
    static String json_string;
    static String result;


    GetData(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        json_url = "http://192.168.1.1:8080/products/json_get_data.php";

    }

    @Override
    protected void onPostExecute(String result) {
        //  TextView testjson=(TextView)findViewById(R.id.testJson);
        //testjson.setText(result);
        json_string=result;
        printresult();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(json_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while ((JSON_STRING = bufferedReader.readLine()) != null) {
                stringBuilder.append(JSON_STRING + "\n");
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



        return null;
    }
public void printresult(){
    result=json_string;
}
}