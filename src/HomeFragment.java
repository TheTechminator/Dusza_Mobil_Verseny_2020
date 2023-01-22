package com.example.mycarhystory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.mycarhystory.dataStructure.CarData;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public Button btn_addCar;

    public LinearLayout layoutAutok;

    public static int choosenCarID = -1;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_addCar = (Button) view.findViewById(R.id.btn_addCar);
        btn_addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.homeToAddNewCarFragment);
            }
        });


        layoutAutok = (LinearLayout) view.findViewById(R.id.layout_autok);
        loadLayout();

        Log.d("TESZT","Ujra");
        System.out.println("ujra");
    }

    public void loadLayout(){ //Adatok betöltése
        layoutAutok.removeAllViews();

        ArrayList<CarData> cars = MainActivity.dataHandler.getCarsByOwner(MainActivity.choosenOwnerID);

        for (int i = 0; i<cars.size(); i++){
            layoutAutok.addView(createView(cars.get(i).marka, i)); //Felteszi az elemek a kijelzőre
        }
    }

    public Button createView(String s, int id){ //Elkészíti a buttont amiben az autók neve van
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
                choosenCarID = v.getId();
                Navigation.findNavController(v).navigate(R.id.homeToCarFragment);
            }
        });

        btn.setLongClickable(true);
        btn.setOnLongClickListener(new LongClick());

        btn.setText(s);
        return btn;
    }

    public class LongClick implements View.OnLongClickListener{ //Lefut ha a felhasználó hossza nyomja az osztály/csoport nevét

        @Override
        public boolean onLongClick(final View v) {
            final int torolId = v.getId();

            new AlertDialog.Builder(MainActivity.context)
                    .setTitle("Törlés")
                    .setMessage("Biztos törli véglegesen a kiválasztott autót és adatait?")
                    .setPositiveButton("Igen",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.dataHandler.removeCarFromOwner(MainActivity.choosenOwnerID, torolId);
                            loadLayout();
                        }
                    })
                    .setNegativeButton("Nem", null)
                    .show();

            return false;
        }
    }
}