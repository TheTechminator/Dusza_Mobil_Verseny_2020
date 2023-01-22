package com.example.mycarhystory.dataStructure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.mycarhystory.MainActivity;
import com.example.mycarhystory.fileHandling.FileHandler;

import java.util.ArrayList;

public class DataHandler {

    public ArrayList<Owner> tulajdonosok = new ArrayList<>();
    public FileHandler fileHandler;
    public String rootFilePath;

    public DataHandler (FileHandler fileHandler, String rootFilePath) {
        this.fileHandler = fileHandler;
        this.rootFilePath = rootFilePath;
    }

    public void loadDataFromFile () {
        ArrayList<String> tulajAdatok = fileHandler.readFromFile(rootFilePath);
        System.out.println(tulajAdatok.get(0));

        for (int i = 0; i<tulajAdatok.size(); i++) {
            tulajdonosok.add(new Owner(tulajAdatok.get(i), fileHandler));
        }
    }

    public void addNewCarToOwner (int ownerID, String carData) {
        ArrayList<String> autoAdatok = new ArrayList<>();
        autoAdatok.add(carData);

        String filePath = "";
        do {
            filePath = ""+(int)(Math.random()*1000000)+".txt";
        }while (fileHandler.isFileExisting(filePath));

        CarData auto = new CarData(autoAdatok, filePath);
        tulajdonosok.get(ownerID).autoAdatok.add(auto);
        saveNewCarInFile(auto);
        refreshFuelFile(ownerID,tulajdonosok.get(ownerID).autoAdatok.size()-1);
        refreshAlertFile(ownerID,tulajdonosok.get(ownerID).autoAdatok.size()-1);
    }

    public ArrayList<CarData> getCarsByOwner (int ownerID) {
        return tulajdonosok.get(ownerID).autoAdatok;
    }

    public void removeCarFromOwner (int ownerID, int carID) {
        fileHandler.deleteFile(tulajdonosok.get(ownerID).autoAdatok.get(carID).getFuelFilePath());
        fileHandler.deleteFile(tulajdonosok.get(ownerID).autoAdatok.get(carID).getUniqueFilePath());
        tulajdonosok.get(ownerID).autoAdatok.remove(carID);
        refresRootFile();
    }

    public void saveNewCarInFile (CarData auto) {
        fileHandler.writeToFile(auto.getUniqueFilePath(), auto.getWritableDate());
        refresRootFile();
    }

    public void addNewServiceToCar (int ownerID, int carID, String data) {
        ArrayList<String> serviceData = new ArrayList<>();
        serviceData.add(data);
        tulajdonosok.get(ownerID).autoAdatok.get(carID).szervizkonyv.add(new Service(data));
        refreshCarFile(tulajdonosok.get(ownerID).autoAdatok.get(carID));
    }

    public void refreshCarFile (CarData auto) {
        fileHandler.writeToFile(auto.getUniqueFilePath(), auto.getWritableDate());
    }

    public ArrayList<Service> getServicesByCar (int ownerID, int carID) {
        return tulajdonosok.get(ownerID).autoAdatok.get(carID).szervizkonyv;
    }

    public void refuelTheCar (int ownerID, int carID, String fuelData) {
        tulajdonosok.get(ownerID).autoAdatok.get(carID).tankolas(fuelData);
        refreshFuelFile(ownerID, carID);
    }

    public ArrayList<FuelData> getFuelDataByCar (int ownerID, int carID) {
        return tulajdonosok.get(ownerID).autoAdatok.get(carID).tankolasok;
    }

    public void refreshFuelFile (int ownerID, int carID) {
        String[] plusz = tulajdonosok.get(ownerID).autoAdatok.get(carID).getUniqueFilePath().split("\\.");

        ArrayList<FuelData> tankolasok = tulajdonosok.get(ownerID).autoAdatok.get(carID).tankolasok;

        String adatok = "";

        if(tankolasok.size()>0) {
            adatok = tankolasok.get(0).getWritableDate();
            for (int i = 1; i < tankolasok.size(); i++) {
                adatok += "\n"+tankolasok.get(i).getWritableDate();
            }
        }

        fileHandler.writeToFile(plusz[0]+"fuelFile.txt", adatok);

        fileHandler.debugExistingFiles();

    }

    public void refreshAlertFile (int ownerID, int carID) {
        String[] plusz = tulajdonosok.get(ownerID).autoAdatok.get(carID).getUniqueFilePath().split("\\.");

        JelzesData jelzesData = tulajdonosok.get(ownerID).autoAdatok.get(carID).jelzesData;

        String adatok = "";
        if(jelzesData != null) {
            adatok = jelzesData.getWritableData();
        }

        fileHandler.writeToFile(plusz[0]+"alertFile.txt", adatok);
    }

    public void refreshCarKMAllas (int ownerID, int carID, int kmallas) {
        tulajdonosok.get(ownerID).autoAdatok.get(carID).kmAllas = kmallas;
        refreshCarFile(tulajdonosok.get(ownerID).autoAdatok.get(carID));
    }

    public void refresRootFile () {
        String adatok = tulajdonosok.get(0).nev;
        for (int i = 0; i<tulajdonosok.get(0).autoAdatok.size(); i++) {
            adatok += ";"+tulajdonosok.get(0).autoAdatok.get(i).getUniqueFilePath();
        }

        for (int k = 1; k<tulajdonosok.size(); k++) {
            adatok += "\n"+tulajdonosok.get(k).nev;
            for (int i = 0; i<tulajdonosok.get(k).autoAdatok.size(); i++) {
                adatok += ";"+tulajdonosok.get(k).autoAdatok.get(i).getUniqueFilePath();
            }
        }

        fileHandler.writeToFile(rootFilePath, adatok);
    }

    public String getCarDataToExport (int ownerID, int carID) {
        String adatok = "";

        CarData c = tulajdonosok.get(ownerID).autoAdatok.get(carID);

        adatok += "Az autó adatai: Márka;Évjárat;Alvázszám;Motorkód;Kilóméteróra állás;Üzemanyagszint;Olajtípus;Legutóbbi szervíz dátuma;Rendszám\n";
        adatok += c.getExportableData()+"\n\n";

        adatok += "Szervízkönyv előzmények: Dátum;Javítás neve;Kategória;Ár;Kilóméteróra állás;Javítás helye;Javítás típusa\n";
        for(int i = 0; i<c.szervizkonyv.size(); i++) {
            adatok += c.szervizkonyv.get(i).getExportableData()+"\n";
        }

        adatok += "\nTankolási előzmények: Mennyiség;Ár;Dátum\n";
        for(int i = 0; i<c.tankolasok.size(); i++) {
            adatok += c.tankolasok.get(i).getWritableDate()+"\n";
        }

        return adatok;
    }
}
