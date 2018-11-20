package enib.ecp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static enib.ecp.Appli.json_string;

public class Modifier_produit extends Fragment {


    FloatingActionButton ajouter_produit = null;
    Context thiscontext;
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProductAdapterModifier productAdaptermodifier;
    ListView listView;
    TextView shoppingCart;
    Button acheter;
    Switch aSwitch=null;
    int i=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        thiscontext = inflater.getContext();

        View rootView = inflater.inflate(R.layout.activity_view_list_modifier, container, false);
        // pushed=0;
        listView=(ListView) rootView.findViewById(R.id.listview);
        productAdaptermodifier = new ProductAdapterModifier(thiscontext,R.layout.raw_layout_modifierty);
        shoppingCart=(TextView) rootView.findViewById(R.id.shoppingCart);
        acheter=(Button)rootView.findViewById(R.id.acheter);
        aSwitch = (Switch) rootView.findViewById(R.id.switcher);

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
                productAdaptermodifier.add(products);
                count++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView.setAdapter(productAdaptermodifier);
        ajouter_produit = (FloatingActionButton) rootView.findViewById(R.id.ajouter_produit);
        ajouter_produit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // getActivity().finish();
                Intent intent = new Intent(thiscontext, Nouveau_produit.class);
                startActivity(intent);
            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                // Do something in response to button click

                if (isChecked) {
                    productAdaptermodifier.visible=true;
                    listView.setAdapter(productAdaptermodifier);
                    Log.d("boutonon"+ Integer.toString(i), Boolean.toString(isChecked));

                }

                if (!isChecked) {

                    productAdaptermodifier.visible=false;
                    listView.setAdapter(productAdaptermodifier);
                    Log.d("boutonoff", Boolean.toString(isChecked));
                }
            }
        });



        return rootView;
    }

}







