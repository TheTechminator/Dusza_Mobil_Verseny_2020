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
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddNewCarFragment extends Fragment {

    public Button btMentes;
    public Button btElvetes;

    public EditText editMarka;
    public EditText editEvjarat;
    public EditText editAlvaz;
    public EditText editKod;
    public EditText editKm;
    public EditText editUzemanyag;
    public EditText editOlaj;
    public EditText editLegutobbi;
    public EditText editRendszam;


    public AddNewCarFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_new_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btMentes = (Button) view.findViewById(R.id.btMentes);


        btMentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Mentés")
                        .setMessage("Biztos menti az adatokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                kattMentes();
                                Navigation.findNavController(view).navigate(R.id.AddNewCarToHomeFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });

        btElvetes = (Button) view.findViewById(R.id.btElvetes);
        btElvetes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                new AlertDialog.Builder(MainActivity.context)
                        .setTitle("Elvetés")
                        .setMessage("Biztos elveti a módosításokat?")
                        .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Navigation.findNavController(view).navigate(R.id.AddNewCarToHomeFragment);
                            }
                        })
                        .setNegativeButton("Nem", null)
                        .show();
            }
        });

        editMarka = (EditText) view.findViewById(R.id.editMarka);
        editEvjarat = (EditText) view.findViewById(R.id.editEvjarat);
        editAlvaz = (EditText) view.findViewById(R.id.editAlvaz);
        editKod = (EditText) view.findViewById(R.id.editKod);
        editKm = (EditText) view.findViewById(R.id.editKm);
        editUzemanyag = (EditText) view.findViewById(R.id.editUzemanyag);
        editOlaj = (EditText) view.findViewById(R.id.editOlaj);
        editLegutobbi = (EditText) view.findViewById(R.id.editLegutobbi);
        editRendszam = (EditText) view.findViewById(R.id.editRendszam);
    }

    private void kattMentes () {
        String adatok = "";

        adatok += editMarka.getText()+";";
        adatok += editEvjarat.getText()+";";
        adatok += editAlvaz.getText()+";";
        adatok += editKod.getText()+";";
        adatok += editKm.getText()+";";
        adatok += editUzemanyag.getText()+";";
        adatok += editOlaj.getText()+";";
        adatok += editLegutobbi.getText()+";";
        adatok += editRendszam.getText();

        MainActivity.dataHandler.addNewCarToOwner(MainActivity.choosenOwnerID, adatok);
    }
}