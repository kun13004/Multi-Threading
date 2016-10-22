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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ListView mainListView;
    public ArrayAdapter<String> listAdapter;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }



    // Method when the Create button is clicked
    public void createFile(View view) {

        //Create a new background thread
        new createFileTask().execute();
    }


    /*
    This is a private class that extends the
    AsyncTask abstract class. This creates a
    background thread for the program to do
    without having to influence the GUI directly.
    Methods provided by this abstract class
    include doInBackground, onProgressUpdate,
    and onPostExecute.
     */
    private class createFileTask extends AsyncTask<Void, Integer, Void> {

        @Override
        /*
        This is the doInBackground method where
        anything we want done behind the scenes
        on the background thread is executed.
         */
        protected Void doInBackground(Void... params) {

            /*
            We need to create a new file called
            numbers.txt for us to write data to
             */
            String filename = "numbers.txt";
            File file = new File(getApplicationContext().getFilesDir(), filename);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            /*
            In this try/catch block we loop
            from the numbers 1 to 10 and write
            them to the file.
             */
            try {
                FileOutputStream fOut = openFileOutput(filename, Context.MODE_PRIVATE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fOut);

                for (int i = 1; i <= 10; i++) {
                    String string = Integer.toString(i) + "\n";
                    outputStreamWriter.write(string);
                    Thread.sleep(250);
                    publishProgress(i * 10);
                }

                // Close up when done
                outputStreamWriter.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        /*
        This is the onProgressUpdate method
        where we can send updates to the GUI
        when necessary. In this case we are
        sending updates to the progress bar.
         */
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


        /*
        This is the onPostExecute method
        where we take what we have done
        during the background thread and
        execute it on the GUI. In this
        case we are only showing that the
        file was created.
         */
        protected void onPostExecute(Void params) {
            Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
        }
    }





    // Method when the Load button is clicked
    public void loadFile(View view) {

        //Create a new background thread
        new loadFileTask().execute();
    }



    /*
    This is a private class that extends the
    AsyncTask abstract class. This creates a
    background thread for the program to do
    without having to influence the GUI directly.
    Methods provided by this abstract class
    include doInBackground, onProgressUpdate,
    and onPostExecute.
     */
    private class loadFileTask extends AsyncTask<Void, Integer, List<String>> {


        @Override
        /*
        This is the doInBackground method where
        anything we want done behind the scenes
        on the background thread is executed.
         */
        protected List<String> doInBackground(Void... params) {


            mainListView = (ListView) findViewById(R.id.mainListView);
            List<String> list = new ArrayList<>();


            /*
            In this try/catch block we are using
            bufferedReader to read each line from
            the file one at a time. After each line
            the thread will sleep to simulate things
            going on. Then the progress bar will be
            updated.
             */
            try {
                String message;
                FileInputStream inputStream = openFileInput("numbers.txt");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                int i = 0;
                while ((message = bufferedReader.readLine()) != null) {
                    list.add(i, message);
                    Thread.sleep(250);
                    i++;
                    publishProgress(i * 10);
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "No File", Toast.LENGTH_LONG).show();
            }
            return list;
        }


        /*
        This is the onProgressUpdate method
        where we can send updates to the GUI
        when necessary. In this case we are
        sending updates to the progress bar.
         */
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


        /*
        This is the onPostExecute method
        where we take what we have done
        during the background thread and
        execute it on the GUI. In this case
        we are displaying the contents of the
        list on the main ListView.
         */
        protected void onPostExecute(List<String> list) {
            listAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.simplerow, list);
            mainListView.setAdapter(listAdapter);
        }
    }


    /*
    This is the method for when the
    Clear button is clicked. The method
    calls the clear method for the
    listAdapter and then notifies that
    the data was changed. We are also
    notified that the load was cleared.
     */
    public void clearFile(View view) {
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
        deleteFile("numbers.txt");
        Toast.makeText(getApplicationContext(), "Load Cleared", Toast.LENGTH_LONG).show();
    }
}
