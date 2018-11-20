package enib.ecp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tom on 27/04/2017.
 */

public class ProductAdapterModifier extends ArrayAdapter {
    List list = new ArrayList();
    public boolean visible=false;
    Context ctx;

    String method="delete_product";

    public ProductAdapterModifier(Context context, int resource) {
        super(context, resource);
        ctx =context;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        ProductHolder productHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.raw_layout_modifierty, parent, false);
            convertView.setTag (productHolder);
        }
        if (productHolder == null) {
            productHolder = new ProductHolder();
            productHolder.delete = (ImageButton) convertView.findViewById(R.id.delete);
            productHolder.tx_name = (TextView) convertView.findViewById(R.id.tx_name);
            productHolder.tx_price = (TextView) convertView.findViewById(R.id.tx_price);
            productHolder.tx_majority = (TextView) convertView.findViewById(R.id.tx_majority);
            if(visible==true){      productHolder.delete.setBackgroundResource(R.drawable.poubelle);     }
            else{productHolder.delete.setBackgroundResource(R.drawable.modifier);}
            final Products products = (Products) this.getItem(position);
            productHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    Log.d("listener delete",products.getName());
                    BDD backgroundTask = new BDD(ctx);
                    if(visible==true) {
                        GetData backgroundTask2 = new GetData(ctx);
                        String result = null;
                        backgroundTask.execute(method, products.getName());
                        try {
                            result = backgroundTask2.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        intent=new Intent(ctx,Appli.class);
                        intent.putExtra("json_data",result);
                        ((Activity)ctx).finish();
                        ctx.startActivity(intent);
                    }
                    else {
                        ((Activity)ctx).finish();
                        intent=new Intent(ctx,modifier.class);
                        Bundle extras = new Bundle();
                        extras.putString("nom",products.getName());
                        extras.putString("prix",products.getPrice());
                        extras.putString("majorite",products.getMajority());
                        intent.putExtras(extras);
                        ctx.startActivity(intent);
                    }
                }
            });
            productHolder.tx_name.setText(products.getName());
            productHolder.tx_price.setText(products.getPrice());
            productHolder.tx_majority.setText(products.getMajority());
            convertView.setTag(productHolder);


        } else {
            productHolder = (ProductHolder) convertView.getTag();
        }


        return convertView;
    }
    // Intent intent = new Intent(ProductAdapterModifier.this, Modifier_Produit.class);
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

    public class ProductHolder {
        TextView tx_name, tx_price, tx_majority;
        ImageButton delete;
    }
}