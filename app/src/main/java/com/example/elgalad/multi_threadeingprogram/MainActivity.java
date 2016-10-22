package com.example.elgalad.multi_threadeingprogram;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createFile(View view) {

        String filename = "numbers.txt";
        File file = new File(this.getFilesDir(), filename);
        OutputStreamWriter outputStream;
        String string = "";

        try {
            outputStream = new OutputStreamWriter(this.openFileOutput(filename, Context.MODE_PRIVATE));
            for (int i = 1; i <= 10; i++) {
                string = Integer.toString(i);
                outputStream.write(string + "\n");
                Thread.sleep(250);
            }
            outputStream.close();
            Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadFile(View view) {


        FileInputStream inputStream;
        mainListView = (ListView) findViewById(R.id.mainListView);

        try {
            String message;
            List<String> list = new ArrayList<>();
            inputStream = openFileInput("numbers.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message);
                String temp = stringBuffer.toString();
                list.add(i, temp);
                Thread.sleep(250);
                i++;
            }
            listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, list);

            mainListView.setAdapter(listAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearFile(View view) {
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "File Cleared", Toast.LENGTH_LONG).show();
    }
}
