package com.example.mycarhystory;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.DocumentsContract;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mycarhystory.dataStructure.CarData;
import com.example.mycarhystory.dataStructure.JelzesData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CarFragment extends Fragment {

    public TextView autoNeve;
    public TextView showEvjarat;
    public TextView showAlvaz;
    public TextView showMotor;
    public TextView showKm;
    public TextView showUzemanyag;
    public TextView showOlajtipus;
    public TextView showLegutolso;
    public TextView showRendszam;

    public Button btSzerviz;
    public Button btTankolas;
    public Button btAdatokExportalasa;

    public FloatingActionButton floatingActionButton;



    public Spinner serviceSetterKategoria;
    public Spinner serviceSetterTipus;
    public TextView tvserviceSetterDatum;
    public Button serviceSetterDatum;
    public TextView tvserviceSetterKM;
    public EditText serviceSetterKM;

    public CarFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btSzerviz = (Button) view.findViewById(R.id.btSzerviz);
        btSzerviz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.carToSzervizFragment);
            }
        });
        btTankolas = (Button) view.findViewById(R.id.btTankolas);
        btTankolas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.carToTankolasFragment);
            }
        });


        btAdatokExportalasa = (Button) view.findViewById(R.id.btAdatokExportalasa);
        btAdatokExportalasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Exportálás")
                        .setMessage("Válasszon ki egy mappát, ahova az adott autó adatait kívánja exportálni.")
                        .setNegativeButton("Mégsem", null)
                        .setPositiveButton("Oké",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                                    Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                    i.addCategory(Intent.CATEGORY_DEFAULT);
                                    startActivityForResult(Intent.createChooser(i, "Mappa kiválasztása"), 9999);
                                }
                            }
                        })
                        .show();
            }
        });

        autoNeve = (TextView) view.findViewById(R.id.autoNeve);
        showEvjarat = (TextView) view.findViewById(R.id.showEvjarat);
        showAlvaz = (TextView) view.findViewById(R.id.showAlvaz);
        showMotor = (TextView) view.findViewById(R.id.showMotor);
        showKm = (TextView) view.findViewById(R.id.showKm);
        showUzemanyag = (TextView) view.findViewById(R.id.showUzemanyag);
        showOlajtipus = (TextView) view.findViewById(R.id.showOlajtipus);
        showLegutolso = (TextView) view.findViewById(R.id.showLegutolso);
        showRendszam = (TextView) view.findViewById(R.id.showRendszam);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showServiceSetterDialog();
            }
        });

        displayCarData();

        createNotification();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9999 && resultCode != 0) {
            DocumentFile documentFile = DocumentFile.fromTreeUri(MainActivity.context, data.getData());
            DocumentFile documentFile1 = documentFile.createFile("text/plain", "exportaltAdatok");
            MainActivity.fileHandler.writeToExternalFile(documentFile1.getUri(), MainActivity.dataHandler.getCarDataToExport(MainActivity.choosenOwnerID, HomeFragment.choosenCarID));
        }
    }

    public void displayCarData () {
        CarData adatok = MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID);

        autoNeve.setText(adatok.marka+"");
        showEvjarat.setText(adatok.evjarat+"");
        showAlvaz.setText(adatok.alvazszam+"");
        showMotor.setText(adatok.motorkod+"");
        showKm.setText(adatok.kmAllas+" km");
        showUzemanyag.setText(adatok.uzemanyagszint+" liter");
        showOlajtipus.setText(adatok.olajTipus+"");
        showLegutolso.setText(adatok.getLegutobbiSzerviz()+"");
        showRendszam.setText(adatok.rendszam+"");
    }

    public void showServiceSetterDialog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.service_setter, null);

        serviceSetterKategoria = (Spinner) tesztLayout.findViewById(R.id.serviceSetterKategoria);
        serviceSetterTipus = (Spinner) tesztLayout.findViewById(R.id.serviceSetterTipus);
        tvserviceSetterDatum = (TextView) tesztLayout.findViewById(R.id.tvserviceSetterDatum);
        serviceSetterDatum = (Button) tesztLayout.findViewById(R.id.serviceSetterDatum);
        tvserviceSetterKM = (TextView) tesztLayout.findViewById(R.id.tvserviceSetterKM);
        serviceSetterKM = (EditText) tesztLayout.findViewById(R.id.serviceSetterKM);

        serviceSetterTipus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    tvserviceSetterDatum.setVisibility(View.VISIBLE);
                    serviceSetterDatum.setVisibility(View.VISIBLE);
                    tvserviceSetterKM.setVisibility(View.GONE);
                    serviceSetterKM.setVisibility(View.GONE);
                }else {
                    tvserviceSetterDatum.setVisibility(View.GONE);
                    serviceSetterDatum.setVisibility(View.GONE);
                    tvserviceSetterKM.setVisibility(View.VISIBLE);
                    serviceSetterKM.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        serviceSetterDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        alert.setMessage("Időszerű javítás figyelmeztetés hozzáadása");
        alert.setPositiveButton("Hozzáad", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String adatok = serviceSetterKategoria.getSelectedItem()+";";
                adatok += serviceSetterTipus.getSelectedItemId()+";";

                if (serviceSetterTipus.getSelectedItemId() == 0) {
                    adatok += serviceSetterKM.getText();
                } else {
                    adatok += serviceSetterDatum.getText();
                }

                adatok += ";"+MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID).kmAllas;

                MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID)
                        .autoAdatok.get(HomeFragment.choosenCarID).jelzesData = new JelzesData(adatok);
                MainActivity.dataHandler.refreshAlertFile(MainActivity.choosenOwnerID, HomeFragment.choosenCarID);
            }
        });
        alert.setNegativeButton("Mégse", null);

        alert.setView(tesztLayout);
        alert.show();
    }

    public void showDatePickerDialog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.date_picker, null);
        final DatePicker picker = tesztLayout.findViewById(R.id.datePicker);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serviceSetterDatum.setText(picker.getYear()+"/"+(picker.getMonth()+1)+"/"+picker.getDayOfMonth());
            }
        });

        alert.setView(tesztLayout);
        alert.show();
    }

    public void createNotification () {
        JelzesData jelzesData = MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID).jelzesData;
        CarData carData = MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID);

        boolean jelez = false;
        if (jelzesData != null) {
            if (jelzesData.mikorJelezzen == 0) {
                if (carData.kmAllas >= jelzesData.lastCarkm+jelzesData.kmMulva) {
                    jelez = true;
                }else {

                    if(MainActivity.firstShown == 0) {
                        final EditText editText = new EditText(MainActivity.context);

                        new AlertDialog.Builder(MainActivity.context)
                                .setTitle("Kilóméteróra állás")
                                .setMessage("Kérem adja meg, hogy hány kilóméter van az adott autójában.")
                                .setCancelable(false)
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("TESZT", editText.getText() + "");
                                        MainActivity.dataHandler.refreshCarKMAllas(MainActivity.choosenOwnerID, HomeFragment.choosenCarID, Integer.parseInt(editText.getText() + ""));
                                        displayCarData();
                                        createNotification();
                                    }
                                })
                                .show();
                        MainActivity.firstShown = 1;
                    }
                }
            }else {
                String date = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
                if (date.equals(jelzesData.datum)){
                    jelez = true;
                }
            }

            if (jelez) {
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Figyelmeztetés")
                        .setMessage("Szükségessé vált egy " + MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID)
                                .autoAdatok.get(HomeFragment.choosenCarID).jelzesData.kategoria + ".")
                        .setCancelable(false)
                        .setPositiveButton("Majdnem elfelejtettem", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID)
                                        .autoAdatok.get(HomeFragment.choosenCarID).jelzesData = null;
                                MainActivity.dataHandler.refreshAlertFile(MainActivity.choosenOwnerID, HomeFragment.choosenCarID);
                            }
                        })
                        .show();
            }
        }
    }

}