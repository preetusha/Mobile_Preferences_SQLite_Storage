package com.example.prabh.preference_sql_storage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends Activity {

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int Count = sharedPref.getInt("Counter", 0);
        String date = sharedPref.getString("Date", "");

        String data = "";
        try {
            InputStream input = openFileInput("dataFile.txt");
            InputStreamReader in = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(in);
            String read;
            StringBuilder sb = new StringBuilder();

            while((read = br.readLine()) != null){
                sb.append(read+"\n");
            }
            input.close();
            data = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("Error","Output file not Found");
        } catch (IOException i){
            Log.i("Error","File read failed");
        }

        TextView textRead = (TextView) findViewById(R.id.outputData);
        textRead.setText(data);
//        Toast.makeText(MainActivity.this,"Count is:"+Count, Toast.LENGTH_LONG).show();
    }

    public void openPreferences(View view) {
        Intent intent = new Intent(this,Preferences.class);
        startActivity(intent);
    }

    public void openSql(View view) {
        Intent intent = new Intent(this,sqlite.class);
        startActivity(intent);
    }

    public void close(View view) {
        MainActivity.this.finish();
    }

    public void clearContents(View view) {
        //deleteFile("dataFile.txt");
        TextView textRead = (TextView) findViewById(R.id.outputData);
        textRead.setText("");
    }
}
