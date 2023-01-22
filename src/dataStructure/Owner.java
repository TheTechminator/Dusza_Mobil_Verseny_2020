package com.example.mycarhystory.dataStructure;

import android.util.Log;

import com.example.mycarhystory.fileHandling.FileHandler;

import java.util.ArrayList;

public class Owner {
    public String nev;
    public ArrayList<CarData> autoAdatok = new ArrayList<>();

    public Owner (String sor, FileHandler fileHandler) {
        String[] plusz = sor.split(";");
        nev = plusz[0];

        for (int i = 1; i<plusz.length; i++){
            String[] fuelPlusz = plusz[i].split("\\.");
            autoAdatok.add(new CarData(fileHandler.readFromFile(plusz[i]), plusz[i]));
            autoAdatok.get(autoAdatok.size()-1).loadTankolasok(fileHandler.readFromFile(fuelPlusz[0]+"fuelFile.txt"));
            autoAdatok.get(autoAdatok.size()-1).loadJelzes(fileHandler.readFromFile(fuelPlusz[0]+"alertFile.txt"));
        }

        Log.d("TESZT", nev);
    }
}
