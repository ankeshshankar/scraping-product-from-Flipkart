package com.example.getprice;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class MainActivity extends AppCompatActivity  {

    ProgressDialog prodiag;
    String productName;
    String[] headers = {"Product Name","Price"};
    String[][] name_price;
    ArrayList<String> p_name = new ArrayList<>();
    ArrayList<String> p_price = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Spinner productType = findViewById(R.id.productType);
        //productType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productType.setAdapter(adapter);*/
    }


    public void showDetail(View view) {
        EditText product = findViewById(R.id.productName);
        productName = String.valueOf(product.getText());
        GetData data = new GetData();
        data.execute();
    }
    public void putData(){
        TableView<String[]> head = findViewById(R.id.productData);
        head.setVisibility(View.VISIBLE);

        head.setColumnCount(2);
        head.setHeaderAdapter(new SimpleTableHeaderAdapter(MainActivity.this,headers));
        name_price = new String[p_name.size()][2];
        for (int i = 0; i < p_price.size() && i < p_name.size(); i++) {
            name_price[i][0] = p_name.get(i);
            name_price[i][1] = p_price.get(i);
        }
        head.setDataAdapter(new SimpleTableDataAdapter(MainActivity.this,name_price));
        p_name.clear();
        p_price.clear();
    }



    @SuppressLint("StaticFieldLeak")
    private class GetData extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            prodiag=new ProgressDialog(MainActivity.this);
            prodiag.setMessage("loading");
            prodiag.setIndeterminate(false);
            prodiag.show();
            Toast.makeText(getApplicationContext(),"Best price you will get on Flipkart !!",Toast.LENGTH_LONG).show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            String url = "https://www.flipkart.com/search?q=" + productName + "";
            Document document;
            try {
                document = Jsoup.connect(url).get();
                Elements name = document.select("._3wU53n"); //get name
                Elements price = document.select("._1vC4OE._2rQ-NK"); //Get price
                Elements name1 = document.select("._2cLu-l"); //get name
                Elements price1 = document.select("._1vC4OE"); //Get price
                for (int i = 0; i < price.size() && i < name.size(); i++) {
                    p_name.add(name.get(i).text());
                    p_price.add(price.get(i).text());
                }
                for (int i = 0; i < price1.size() && i < name1.size(); i++) {
                    p_name.add(name1.get(i).text());
                    p_price.add(price1.get(i).text());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            prodiag.dismiss();
            super.onPostExecute(aVoid);
            putData();
        }
    }
}
