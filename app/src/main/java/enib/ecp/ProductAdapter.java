package enib.ecp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom on 27/04/2017.
 */

public class ProductAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public float shopping_cart;
    public ProductAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductHolder productHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
            convertView.setTag (productHolder);
        }
        if(productHolder==null){
            productHolder=new ProductHolder();
            productHolder.tx_name=(TextView)convertView.findViewById(R.id.tx_name);
            productHolder.tx_price=(TextView)convertView.findViewById(R.id.tx_price);
            productHolder.tx_quantite=(TextView)convertView.findViewById(R.id.tx_quantite);
            productHolder.moins=(Button)convertView.findViewById(R.id.moins);
            productHolder.plus=(Button)convertView.findViewById(R.id.plus);
            final Products products= (Products) this.getItem(position);
            productHolder.tx_name.setText(products.getName());
            productHolder.tx_price.setText(products.getPrice());
            productHolder.tx_quantite.setText(Integer.toString(products.getQuantite()));
            final ProductHolder finalProductHolder = productHolder;
            finalProductHolder.result=0;
            productHolder.moins.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(products.getQuantite()>0) {
                        products.setQuantite(products.getQuantite() - 1);
                        shopping_cart = shopping_cart - Float.parseFloat(products.getPrice());
                        finalProductHolder.result = finalProductHolder.result - shopping_cart;
                        finalProductHolder.tx_quantite.setText(Integer.toString(products.getQuantite()));
                        Acheter.shoppingCart.setText(Float.toString(shopping_cart));
                        Log.d("moins", Float.toString(shopping_cart));
                    }
                }
            });
            productHolder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    products.setQuantite(products.getQuantite()+1);
                    shopping_cart=shopping_cart+Float.parseFloat(products.getPrice());
                    finalProductHolder.result=finalProductHolder.result+shopping_cart;
                    finalProductHolder.tx_quantite.setText(Integer.toString(products.getQuantite()));
                    Acheter.shoppingCart.setText(Float.toString(shopping_cart));
                    Log.d("plus",Float.toString(shopping_cart));
                }
            });
            convertView.setTag(productHolder);


        }
        else {
            productHolder = (ProductHolder)convertView.getTag();
        }





        return convertView;
    }

    public void add(Products object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }
    public class ProductHolder{
        TextView tx_name, tx_price, tx_quantite;
        Button plus, moins;
        float result=0;
    }
}
