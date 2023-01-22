package com.example.mycarhystory.dataStructure;

import android.util.Log;

import java.util.ArrayList;

public class CarData {
    private String uniqueFilePath;

    public String marka;
    public int evjarat;
    public int alvazszam;
    public String motorkod;
    public int kmAllas;
    public float uzemanyagszint;
    public String olajTipus;
    public String legutobbiSzerviz;
    public String rendszam;

    public ArrayList<Service> szervizkonyv = new ArrayList<>();
    public ArrayList<FuelData> tankolasok = new ArrayList<>();

    public JelzesData jelzesData = null;

    public CarData (ArrayList<String> sorok, String uniqueFileID) {
        this.uniqueFilePath = uniqueFileID;
        setData(sorok.get(0));

        for (int i = 1; i<sorok.size(); i++) {
            szervizkonyv.add(new Service(sorok.get(i)));
        }
    }

    public void loadJelzes (ArrayList<String> jelzesek) {
        if (jelzesek.size()>0){
            jelzesData = new JelzesData(jelzesek.get(0));
        }
    }

    public void loadTankolasok (ArrayList<String> tankoltAdatok) {
        for (int i = 0; i<tankoltAdatok.size(); i++) {
            tankolasok.add(new FuelData(tankoltAdatok.get(i)));
        }
    }

    private void setData (String sor) {
        String[] plusz = sor.split(";");

        marka = plusz[0];
        evjarat = Integer.parseInt(plusz[1]);
        alvazszam = Integer.parseInt(plusz[2]);
        motorkod = plusz[3];
        kmAllas = Integer.parseInt(plusz[4]);
        uzemanyagszint = Float.parseFloat(plusz[5]);
        olajTipus = plusz[6];
        legutobbiSzerviz = plusz[7];
        rendszam = plusz[8];

        Log.d("TESZT", sor);
    }

    public String getUniqueFilePath() {
        return uniqueFilePath;
    }

    public String getWritableDate () {
        String adatok = marka+";"+evjarat+";"+alvazszam+";"+motorkod+";"+kmAllas+";"+uzemanyagszint+";"+olajTipus+";"+legutobbiSzerviz+";"+rendszam;

        for (int i = 0; i<szervizkonyv.size(); i++) {
            adatok += "\n"+szervizkonyv.get(i).getWritableDate();
        }

        return adatok;
    }

    public String getExportableData () {
        return marka+";"+evjarat+";"+alvazszam+";"+motorkod+";"+kmAllas+";"+uzemanyagszint+";"+olajTipus+";"+legutobbiSzerviz+";"+rendszam;
    }

    public void tankolas (String sor) {
        tankolasok.add(new FuelData(sor));
    }

    public String getFuelFilePath () {
        String[] plusz = uniqueFilePath.split("\\.");
        return plusz[0]+"fuelFile.txt";
    }

    public String getLegutobbiSzerviz() {
        if(legutobbiSzerviz.equals("-1/-1/-1"))
            return "Nincs adat";

        return legutobbiSzerviz;
    }

    public void refreshCarData () {
        String[] plusz = legutobbiSzerviz.split("/");
        int y = Integer.parseInt(plusz[0]);
        int m = Integer.parseInt(plusz[1]);
        int d = Integer.parseInt(plusz[2]);

        for (int i = 0; i<szervizkonyv.size(); i++) {
            if (szervizkonyv.get(i).ev>y) {
                y = szervizkonyv.get(i).ev;
                m = szervizkonyv.get(i).ho;
                d = szervizkonyv.get(i).nap;
            }else if (szervizkonyv.get(i).ev==y){
                if (szervizkonyv.get(i).ho>m) {
                    m = szervizkonyv.get(i).ho;
                    d = szervizkonyv.get(i).nap;
                }else if (szervizkonyv.get(i).ho==m){
                    if (szervizkonyv.get(i).nap>d){
                        d = szervizkonyv.get(i).nap;
                    }
                }
            }
        }

        for (int i = 0; i<szervizkonyv.size(); i++) {
            if (szervizkonyv.get(i).kmAllas>kmAllas) {
                kmAllas = szervizkonyv.get(i).kmAllas;
            }
        }

        legutobbiSzerviz = y+"/"+m+"/"+d;
    }
}
