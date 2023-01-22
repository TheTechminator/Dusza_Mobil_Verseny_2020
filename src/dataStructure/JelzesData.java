package com.example.mycarhystory.dataStructure;

import android.util.Log;

public class JelzesData {
    public String kategoria;
    public int mikorJelezzen; //0 = km mulva, 1 = adott napon
    public String datum;
    public int kmMulva;
    public int lastCarkm;

    public JelzesData (String sor) {
        if(sor.length()>0) {
            String[] plusz = sor.split(";");

            kategoria = plusz[0];
            mikorJelezzen = Integer.parseInt(plusz[1]);

            if (mikorJelezzen == 0) {
                kmMulva = Integer.parseInt(plusz[2]);
            } else {
                datum = plusz[2];
            }

            lastCarkm = Integer.parseInt(plusz[3]);
        }
        Log.d("TESZT",sor);
    }

    public String getWritableData () {
        String adatok = kategoria+";";
        adatok += mikorJelezzen+";";

        if (mikorJelezzen == 0) {
            adatok += kmMulva;
        } else {
            adatok += datum;
        }

        return adatok+";"+lastCarkm;
    }
}
