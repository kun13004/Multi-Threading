package com.example.elgalad.multi_threadeingprogram;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ListView mainListView;
    public ArrayAdapter<String> listAdapter;
    public ProgressBar progressBar;
    public File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }




    public void createFile(View view) {
        new createFileTask().execute();
    }

    private class createFileTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String filename = "numbers.txt";
            file = new File(getBaseContext().getFilesDir(), filename);
            FileOutputStream outputStream;
            String string;

            try {
                outputStream = new FileOutputStream(filename);
                for (int i = 1; i <= 10; i++) {
                    string = i + "\n";
                    outputStream.write(string.getBytes());
                    Thread.sleep(250);
                }
                outputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(Void params) {
            Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
        }
    }






    public void loadFile(View view) {
        new loadFileTask().execute();
    }

    private class loadFileTask extends AsyncTask<Void, Integer, List<String>> {



        @Override
        protected List<String> doInBackground(Void... params) {
            FileInputStream inputStream;
            mainListView = (ListView) findViewById(R.id.mainListView);
            List<String> list = new ArrayList<>();

            try {
                String message;
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
                    publishProgress(i * 10);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        protected void onPostExecute(List<String> list) {
            listAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.simplerow, list);
            mainListView.setAdapter(listAdapter);
        }
    }

    public void clearFile(View view) {
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(), "File Cleared", Toast.LENGTH_LONG).show();
    }
}
