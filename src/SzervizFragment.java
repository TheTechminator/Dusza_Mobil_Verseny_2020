package com.example.mycarhystory;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mycarhystory.dataStructure.Service;

import java.util.ArrayList;

public class SzervizFragment extends Fragment {

    public View view;

    public LinearLayout layoutSzurok;
    public Button btSzuro;
    public LinearLayout serviceData;
    public Button btUjSzerviz;

    public boolean btSzuroVisible = false;

    public static int choosenServiceID;


    public CheckBox modCheck;
    public CheckBox tipusCheck;
    public CheckBox kategoriaCheck;
    public Spinner spinnerMod;
    public Spinner spinnerTipus;
    public Spinner spinnerKategoria;

    public int szerOszAr;
    public TextView viewOsszkoltseg;

    public SzervizFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_szerviz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutSzurok = (LinearLayout) view.findViewById(R.id.layoutSzurok);

        viewOsszkoltseg = (TextView) view.findViewById(R.id.viewOsszkoltseg);

        modCheck = (CheckBox) view.findViewById(R.id.modCheck);
        tipusCheck = (CheckBox) view.findViewById(R.id.tipusCheck);
        kategoriaCheck = (CheckBox) view.findViewById(R.id.kategoriaCheck);
        spinnerMod = (Spinner) view.findViewById(R.id.spinnerMod);
        spinnerTipus = (Spinner) view.findViewById(R.id.spinnerTipus);
        spinnerKategoria = (Spinner) view.findViewById(R.id.spinnerKategoria);

        btSzuro = (Button) view.findViewById(R.id.btSzuro);
        btSzuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kattBtSzuro();
            }
        });

        btUjSzerviz = (Button) view.findViewById(R.id.btUjSzerviz);
        btUjSzerviz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.szervizFragmentToUjSzervizFragment);
            }
        });

        serviceData = (LinearLayout) view.findViewById(R.id.serviceData);

        loadLayout();

        this.view = view;
    }

    public void kattBtSzuro () {
        if (btSzuroVisible) {
            layoutSzurok.setVisibility(View.GONE);
            btSzuroVisible = false;
            btSzuro.setText("Szűrő beállítása");
            loadLayout();
        }else {
            layoutSzurok.setVisibility(View.VISIBLE);
            btSzuroVisible = true;
            btSzuro.setText("Szűrő mentése");
        }
    }

    public boolean megjelenhet (Service service) { //A szűrési adatok alapján eldönti hogy megjelenhet e az adott adat

        if (modCheck.isChecked()) {
            if(service.gyari != spinnerMod.getSelectedItemId()) {
                return false;
            }
        }

        if (tipusCheck.isChecked()){
            if (!service.javitasTipusa.equals(spinnerTipus.getSelectedItem())) {
                return false;
            }
        }

        if(kategoriaCheck.isChecked()) {
            if(!service.kategoria.equals(spinnerKategoria.getSelectedItem().toString())) {
                return false;
            }
        }

        return true;
    }

    public void loadLayout(){ //Adatok betöltése
        serviceData.removeAllViews();

        ArrayList<Service> services = MainActivity.dataHandler.getServicesByCar(MainActivity.choosenOwnerID, HomeFragment.choosenCarID);

        szerOszAr = 0;

        for (int i = 0; i<services.size(); i++){
            if (megjelenhet(services.get(i))) {
                serviceData.addView(createView(services.get(i).datum, i)); //Felteszi az elemek a kijelzőre
                szerOszAr += services.get(i).ar;
            }
        }

        viewOsszkoltseg.setText("Összköltség: "+szerOszAr+" Ft");
    }

    public Button createView(String s, int id){ //Elkészíti a buttont amiben a service dátum van
        Button btn = new Button(MainActivity.context);
        btn.setId(id);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 20;
        btn.setLayoutParams(params);
        btn.setBackgroundResource(R.drawable.button_bg);
        btn.setTextColor(Color.parseColor("#041E42"));

        btn.requestFocus();
        btn.setClickable(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosenServiceID = v.getId();
                showChoosenServiceData();
            }
        });

        btn.setText(s);
        return btn;
    }

    public void showChoosenServiceData () {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.context);
        alert.setTitle("Servíz adatok");
        alert.setMessage("A kiválasztott szervizelés adatai");

        LinearLayout tesztLayout = (LinearLayout) LayoutInflater.from(MainActivity.context).inflate(R.layout.service_layout, null);
        displayServiceData(tesztLayout);

        alert.setView(tesztLayout);
        alert.show();
    }

    public void displayServiceData (LinearLayout tesztLayout) {
        TextView tvServiceDatum = (TextView) tesztLayout.findViewById(R.id.tvServiceDatum);
        TextView tvServiceJavNev = (TextView) tesztLayout.findViewById(R.id.tvServiceJavNev);
        TextView tvServiceKategoria = (TextView) tesztLayout.findViewById(R.id.tvServiceKategoria);
        TextView tvServiceAr = (TextView) tesztLayout.findViewById(R.id.tvServiceAr);
        TextView tvServiceKm = (TextView) tesztLayout.findViewById(R.id.tvServiceKm);
        TextView tvServiceHely = (TextView) tesztLayout.findViewById(R.id.tvServiceHely);
        TextView tvServiceTipus = (TextView) tesztLayout.findViewById(R.id.tvServiceTipus);

        Service service = MainActivity.dataHandler.tulajdonosok.get(MainActivity.choosenOwnerID).autoAdatok.get(HomeFragment.choosenCarID).szervizkonyv.get(choosenServiceID);

        tvServiceDatum.setText(service.datum);
        tvServiceJavNev.setText(service.javitasNeve);
        tvServiceKategoria.setText(service.kategoria);
        tvServiceAr.setText(service.ar+" Ft");
        tvServiceKm.setText(service.kmAllas+" km");
        tvServiceTipus.setText(service.javitasTipusa);

        if (service.gyari == 1)
            tvServiceHely.setText("Márkaszervíz");
        else
            tvServiceHely.setText("Magán úton történt");
    }
}