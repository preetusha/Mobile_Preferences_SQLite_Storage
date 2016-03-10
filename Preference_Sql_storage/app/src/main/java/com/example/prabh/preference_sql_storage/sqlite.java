package com.example.prabh.preference_sql_storage;



        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.EventLogTags;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.OutputStreamWriter;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;

public class sqlite extends AppCompatActivity {

    SQLiteDatabase storeMsgDB = null;
    private static int Count = 0;
    private static final String dataFile = "dataFile.txt";
    SharedPreferences pref;

    SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    EditText descMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);
        descMsg = (EditText) findViewById(R.id.description);

        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Count = pref.getInt("Counter", 0);
    }

    public void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Count = pref.getInt("Counter", 0);
    }
    public void onResume()
    {
        super.onResume();
        pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Count = pref.getInt("Counter", 0);
    }

    public void onSave(View view) {

        String dateTime = date.format(Calendar.getInstance().getTime());
        Count +=1;
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("Counter", Count);
        editor.commit();

        try{
            storeMsgDB = this.openOrCreateDatabase("Description", MODE_PRIVATE, null);
            storeMsgDB.execSQL("CREATE TABLE IF NOT EXISTS description "+ "(id integer primary key, message VARCHAR(255));");

            File db = getApplicationContext().getDatabasePath("Description.db");
            if(!db.exists()){

            }else{
                Toast.makeText(this,"Database Missing", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Log.e("Error", "Output file not Found");
        }

        String message  = descMsg.getText().toString();
        storeMsgDB.execSQL("INSERT INTO description (message) VALUES ('"+ message +"');");


        try {
            OutputStreamWriter timeStamp = new OutputStreamWriter(openFileOutput(dataFile, Context.MODE_APPEND));
            String timeOutput = "\nSQLite "+Count+", "+dateTime;
            timeStamp.write(timeOutput);
            timeStamp.close();
        } catch (FileNotFoundException e) {
            Log.e("Error", "Output file not Found");
        } catch (IOException i){
            Log.i("Error", "File write failed");
        }


        Intent in = new Intent(this,MainActivity.class);
        startActivity(in);
    }

    public void showData(View view) {
        Cursor cursor = storeMsgDB.rawQuery("SELECT * FROM description", null);
        int id = cursor.getColumnIndex("id");
        int msg = cursor.getColumnIndex("message");

        cursor.moveToFirst();

        String msgList = "";

        if(cursor !=null && (cursor.getCount()> 0)){

            do{
                String identification = cursor.getString(id);
                String message = cursor.getString(msg);

                msgList = msgList + message+"\n";
            }while(cursor.moveToNext());

            TextView setMessage = (TextView) findViewById(R.id.showDataEntry);
            setMessage.setText(msgList);
        }else{
            Toast.makeText(this,"No text saved", Toast.LENGTH_SHORT);
        }
    }

    public void cancelData(View view) {
        sqlite.this.finish();
    }

    public void clearData(View view) {
        TextView tv = (TextView) findViewById(R.id.showDataEntry);
        tv.setText("");
    }
}

