package com.example.mycarhystory.dataStructure;

public class FuelData {

    public float mennyiseg;
    public int ar;
    public String datum;

    public FuelData (String sor) {
        String[] plusz = sor.split(";");

        mennyiseg = Float.parseFloat(plusz[0]);
        ar = Integer.parseInt(plusz[1]);
        datum = plusz[2];
    }

    public String getWritableDate () {
        return mennyiseg+";"+ar+";"+datum;
    }

}
