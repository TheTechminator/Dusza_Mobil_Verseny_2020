package com.example.mycarhystory;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.mycarhystory.dataStructure.DataHandler;
import com.example.mycarhystory.fileHandling.FileHandler;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static DataHandler dataHandler;
    public static int choosenOwnerID = 0;

    public static int firstShown = 0;

    public static FileHandler fileHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;

        fileHandler = new FileHandler(context);

        if(!fileHandler.isFileExisting("rootFile.txt"))
            fileHandler.writeToFile("rootFile.txt", "István");


        dataHandler = new DataHandler(fileHandler, "rootFile.txt");
        dataHandler.loadDataFromFile();

        setContentView(R.layout.activity_main);
    }
}
