package com.example.mycarhystory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class UjSzervizFragment extends Fragment {

    public Button btUjelvetes;
    public Button btUjmentes;

    public Button editUjDatum;
    public EditText editJavitasNeve;
    public Spinner editKategoria;
    public EditText editAr;
    public EditText editKm;
    public Spinner ujJavitasHelye;
    public Spinner ujJavitasTipus;


    public UjSzervizFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uj_szerviz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btUjmentes = (Button) view.findViewById(R.id.btUjmentes);
        btUjmentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Mentés")
                        .setMessage("Biztos menti az adatokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveDate();
                                Navigation.findNavController(view).navigate(R.id.ujSzervizToSzervizFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });
        btUjelvetes = (Button) view.findViewById(R.id.btUjelvetes);
        btUjelvetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Elvetés")
                        .setMessage("Biztos elveti a módosításokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Navigation.findNavController(view).navigate(R.id.ujSzervizToSzervizFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });

        editUjDatum = (Button) view.findViewById(R.id.editUjDatum);
        editUjDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editJavitasNeve = (EditText) view.findViewById(R.id.editJavitasNeve);
        editKategoria = (Spinner) view.findViewById(R.id.editKategoria);
        editAr = (EditText) view.findViewById(R.id.editAr);
        editKm = (EditText) view.findViewById(R.id.editKm);
        ujJavitasHelye = (Spinner) view.findViewById(R.id.ujJavitasHelye);
        ujJavitasTipus = (Spinner) view.findViewById(R.id.ujJavitasTipus);
    }

    public void saveDate () {
        String sor = "";
        sor += editUjDatum.getText()+";";
        sor += editJavitasNeve.getText()+";";
        sor += editKategoria.getSelectedItem().toString()+";";
        sor += editAr.getText()+";";
        sor += editKm.getText()+";";
        sor += ujJavitasHelye.getSelectedItemId()+";";
        sor += ujJavitasTipus.getSelectedItem()+"";

        MainActivity.dataHandler.addNewServiceToCar(MainActivity.choosenOwnerID, HomeFragment.choosenCarID, sor);

        MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID).refreshCarData();
    }

    public void showDatePickerDialog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.date_picker, null);
        final DatePicker picker = tesztLayout.findViewById(R.id.datePicker);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editUjDatum.setText(picker.getYear()+"/"+(picker.getMonth()+1)+"/"+picker.getDayOfMonth());
            }
        });

        alert.setView(tesztLayout);
        alert.show();
    }
}