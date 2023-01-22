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
import android.widget.TextView;
import com.example.mycarhystory.dataStructure.FuelData;

import java.util.ArrayList;

public class TankolasFragment extends Fragment {

    public Button btTankolasElvetes;
    public Button btTankolasMentes;

    public LinearLayout layoutTankolasiElozmenyek;

    public EditText editTankolasMennyiseg;
    public EditText editTankolasAr;
    public Button editTankolasDatum;

    public TankolasFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tankolas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTankolasMennyiseg = (EditText) view.findViewById(R.id.editTankolasMennyiseg);
        editTankolasAr = (EditText) view.findViewById(R.id.editTankolasAr);
        editTankolasDatum = (Button) view.findViewById(R.id.editTankolasDatum);
        editTankolasDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btTankolasElvetes = (Button) view.findViewById(R.id.btTankolasElvetes);
        btTankolasElvetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Elvetés")
                        .setMessage("Biztos elveti a módosításokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Navigation.findNavController(view).navigate(R.id.tankolasToCarFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });

        btTankolasMentes = (Button) view.findViewById(R.id.btTankolasMentes);
        btTankolasMentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Mentés")
                        .setMessage("Biztos menti az adatokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveDate();
                                Navigation.findNavController(view).navigate(R.id.tankolasToCarFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });

        layoutTankolasiElozmenyek = (LinearLayout) view.findViewById(R.id.layoutTankolasiElozmenyek);
        showTankolasok();
    }

    public void showTankolasok () {

        ArrayList<FuelData> fData = MainActivity.dataHandler.getFuelDataByCar(MainActivity.choosenOwnerID, HomeFragment.choosenCarID);

        for (int i = 0; i<fData.size(); i++) {
            LinearLayout ideiglenes = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.layout_tankolas_sor, null);

            TextView tv1, tv2, tv3;
            tv1 = (TextView) ideiglenes.findViewById(R.id.tvTankolDatum);
            tv2 = (TextView) ideiglenes.findViewById(R.id.tvTankolMennyi);
            tv3 = (TextView) ideiglenes.findViewById(R.id.tvTankolAr);

            tv1.setText(fData.get(i).datum+"");
            tv2.setText(fData.get(i).mennyiseg+" liter");
            tv3.setText(fData.get(i).ar+" Ft");

            layoutTankolasiElozmenyek.addView(ideiglenes);
        }
    }

    public void saveDate () {
        String sor = "";
        sor += editTankolasMennyiseg.getText()+";";
        sor += editTankolasAr.getText()+";";
        sor += editTankolasDatum.getText()+"";

        MainActivity.dataHandler.refuelTheCar(MainActivity.choosenOwnerID, HomeFragment.choosenCarID, sor);
    }

    public void showDatePickerDialog () {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.date_picker, null);
        final DatePicker picker = tesztLayout.findViewById(R.id.datePicker);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTankolasDatum.setText(picker.getYear()+"/"+(picker.getMonth()+1)+"/"+picker.getDayOfMonth());
            }
        });

        alert.setView(tesztLayout);
        alert.show();
    }
}