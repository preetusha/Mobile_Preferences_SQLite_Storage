package com.example.prabh.preference_sql_storage;



        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.OutputStreamWriter;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;

public class Preferences extends AppCompatActivity {

    EditText name, author, desc;
    SharedPreferences pref;
    private static int Counter = 0;
    private static final String dataFile = "dataFile.txt";

    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        name = (EditText) findViewById(R.id.bookName);
        author = (EditText) findViewById(R.id.bookAuthor);
        desc = (EditText) findViewById(R.id.desc);

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Counter = pref.getInt("Counter", 0);

    }

    public void onResume()
    {
        super.onResume();
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Counter = pref.getInt("Counter", 0);
    }

    public void saveData(View view) {
        String Name = name.getText().toString();
        String Author = author.getText().toString();
        String Desc = desc.getText().toString();

        String dateTime = date.format(Calendar.getInstance().getTime());

        Counter += 1;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Counter", Counter);
        editor.commit();

        try {
            OutputStreamWriter timeStamp = new OutputStreamWriter(openFileOutput(dataFile,Context.MODE_APPEND));
            String timeOutput = "\nSave Preference "+Counter+", "+dateTime;
            timeStamp.write(timeOutput);
            timeStamp.close();
        } catch (FileNotFoundException e) {
            Log.e("Error", "Output file not Found");
        } catch (IOException i){
            Log.i("Error", "File write failed");
        }


        Intent in = new Intent(this,MainActivity.class);
        startActivity(in);
//       Toast.makeText(PreferencesView.this, "The date is: "+dateTime, Toast.LENGTH_LONG).show();
    }

    public void cancelData(View view) {
        Preferences.this.finish();
    }
}
