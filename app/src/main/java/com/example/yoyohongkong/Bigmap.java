package com.example.yoyohongkong;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class Bigmap extends ActionBarActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView lvLeftMenu;
    private String[] lvs = {"Your Award", "Area Tsim Sha Tsui", "Area CityU", "Area Cheung Chau", "Area ShaTin",
            "Developer Settings",
            "  Reset(for demo)", "  Set Area1 all pass(for demo)", "  Set Area2 all pass(for demo)",
            "  Set Area3 all pass(for demo)", "  Set Area4 all pass(for demo)"};
    private ArrayAdapter arrayAdapter;


    private int q1_flag, q2_flag, q3_flag, q4_flag, q5_flag;
    private ImageButton ib1;
    private ImageButton ib2;
    private ImageButton ib3;
    private ImageButton ib4;

    private ImageView map;
    private int goto_flag = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.HomeTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bigmap);

        initView();

    }


    @Override
    protected void onStart() {
        super.onStart();
        goto_flag = 0;

        updateList();

    }

    @Override
    protected void onResume() {

        super.onResume();
        goto_flag = 0;

        updateList();

    }

    private void updateList() {
        int i = 0;
        loadPreference("1");
        if (q1_flag == 1) i++;
        if (q2_flag == 1) i++;
        if (q3_flag == 1) i++;
        if (q4_flag == 1) i++;
        if (q5_flag == 1) i++;
        lvs[1] = "Area Tsim Sha Tsui (" + i + "/5)";
        i = 0;
        loadPreference("2");
        if (q1_flag == 1) i++;
        if (q2_flag == 1) i++;
        if (q3_flag == 1) i++;
        if (q4_flag == 1) i++;
        if (q5_flag == 1) i++;
        lvs[2] = "Area CityU (" + i + "/5)";
        i = 0;
        loadPreference("3");
        if (q1_flag == 1) i++;
        if (q2_flag == 1) i++;
        if (q3_flag == 1) i++;
        if (q4_flag == 1) i++;
        if (q5_flag == 1) i++;
        lvs[3] = "Area Cheung Chau (" + i + "/5)";
        i = 0;
        loadPreference("4");
        if (q1_flag == 1) i++;
        if (q2_flag == 1) i++;
        if (q3_flag == 1) i++;
        if (q4_flag == 1) i++;
        if (q5_flag == 1) i++;
        lvs[4] = "Area ShaTin (" + i + "/5)";
        lvLeftMenu.setAdapter(arrayAdapter);
    }

    private void initView() {
        /*ib1 = (ImageButton) findViewById(R.id.ImageButton1);
        ib2 = (ImageButton) findViewById(R.id.ImageButton2);
        ib3 = (ImageButton) findViewById(R.id.ImageButton3);
        ib4 = (ImageButton) findViewById(R.id.ImageButton4);*/
        map = (ImageView) findViewById(R.id.map);


        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        lvLeftMenu = (ListView) findViewById(R.id.lv_left_menu);

        /*ib1.setOnClickListener(mylistener);
        ib2.setOnClickListener(mylistener);
        ib3.setOnClickListener(mylistener);
        ib4.setOnClickListener(mylistener);*/


        toolbar.setTitle("YoYo Hong Kong");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        arrayAdapter = new ArrayAdapter(this, R.layout.drawer_list_item, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);

        lvLeftMenu.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        award();
                        break;
                    case 1:
                        area_tst();
                        break;
                    case 2:
                        area_cityu();
                        break;
                    case 3:
                        area_cc();
                        break;
                    case 4:
                        area_st();
                        break;
                    case 6:
                        reset();
                        break;
                    case 7://set area
                        setArea("1");
                        break;
                    case 8://set area
                        setArea("2");
                        break;
                    case 9://set area
                        setArea("3");
                        break;
                    case 10://set area
                        setArea("4");
                        break;
                }

            }

        });

        /*map.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goto_flag == 1) {
                    goto_flag=0;
                    area_st();
                } else if (goto_flag == 2) {
                    goto_flag=0;
                    area_tst();
                } else if (goto_flag == 3) {
                    goto_flag=0;
                    area_cc();
                }else {
                    goto_flag=0;
                    area_cityu();
                }

            }
        });*/

        map.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int x = (int) event.getX();
                int y = (int) event.getY();
                ImageView imageView = ((ImageView) v);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                int pixel = bitmap.getPixel(x, y);

                String Hex = Integer.toHexString(pixel);

                //Toast.makeText(Bigmap.this, Hex, Toast.LENGTH_SHORT).show();

                if (Hex.equals("ff60d4a3") && goto_flag != 1) {
                    goto_flag = 1;
                    area_st();

                } else if (Hex.equals("ff6063d4") && goto_flag != 1) {
                    goto_flag = 1;
                    area_tst();

                } else if (Hex.equals("ffed1c61") && goto_flag != 1) {
                    goto_flag = 1;
                    area_cc();
                }else if (Hex.equals("ffa936f2") && goto_flag != 1) {
                    goto_flag = 1;
                    area_cityu();
                }


                return true;
            }
        });


    }

    public void loadPreference(String index) {
        SharedPreferences preferences = getSharedPreferences(index, MODE_PRIVATE);
        q1_flag = preferences.getInt("Q1", 0);
        q2_flag = preferences.getInt("Q2", 0);
        q3_flag = preferences.getInt("Q3", 0);
        q4_flag = preferences.getInt("Q4", 0);
        q5_flag = preferences.getInt("Q5", 0);

    }

    private void setArea(String index) {
        SharedPreferences preferences = getSharedPreferences(index, MODE_PRIVATE);
        preferences.edit().putInt("Q1", 1).commit();
        preferences.edit().putInt("Q2", 1).commit();
        preferences.edit().putInt("Q3", 1).commit();
        preferences.edit().putInt("Q4", 1).commit();
        preferences.edit().putInt("Q5", 1).commit();
        updateList();
    }

/*
    private View.OnClickListener mylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ImageButton1:
                    area_tst();
                    break;
                case R.id.ImageButton2:
                    area_cityu();
                    break;
                case R.id.ImageButton3:
                    area_cc();
                    break;
                case R.id.ImageButton4:
                    area_st();
                    break;

            }

        }


    };
*/

    private void reset() {
        int i;
        for (i = 1; i <= 4; i++) {
            SharedPreferences preferences = getSharedPreferences(String.valueOf(i), MODE_PRIVATE);
            preferences.edit().putInt("Q1", 0).commit();
            preferences.edit().putInt("Q2", 0).commit();
            preferences.edit().putInt("Q3", 0).commit();
            preferences.edit().putInt("Q4", 0).commit();
            preferences.edit().putInt("Q5", 0).commit();
        }
        updateList();

    }

    private void award() {
        Intent intent = new Intent(this, award.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void area_tst() {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("zoom", 15);
        bundle.putInt("area", 1);

        bundle.putDouble("latitude1", 22.293137);
        bundle.putDouble("longitude1", 114.173026);
        bundle.putString("Q1", "Avenue of Stars");

        bundle.putDouble("latitude2", 22.297509);
        bundle.putDouble("longitude2", 114.168289);
        bundle.putString("Q2", "Harbour city");

        bundle.putDouble("latitude3", 22.293480);
        bundle.putDouble("longitude3", 114.172020);
        bundle.putString("Q3", "Hong Kong Museum of Art");

        bundle.putDouble("latitude4", 22.293614);
        bundle.putDouble("longitude4", 114.169386);
        bundle.putString("Q4", "Hong Kong Clock Tower");

        bundle.putDouble("latitude5", 22.293669);
        bundle.putDouble("longitude5", 114.168694);
        bundle.putString("Q5", "Ferry Pier");


        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void area_cityu() {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("zoom", 16);
        bundle.putInt("area", 2);

        bundle.putDouble("latitude1", 22.336278);
        bundle.putDouble("longitude1", 114.173023);
        bundle.putString("Q1", "City Express");

        bundle.putDouble("latitude2", 22.337524);
        bundle.putDouble("longitude2", 114.174025);
        bundle.putString("Q2", "Festival Walk");

        bundle.putDouble("latitude3", 22.339759);
        bundle.putDouble("longitude3", 114.170618);
        bundle.putString("Q3", "Student Residence of CityU");

        bundle.putDouble("latitude4", 22.337611);
        bundle.putDouble("longitude4", 114.172915);
        bundle.putString("Q4", "CityU AC3");

        bundle.putDouble("latitude5", 22.335992);
        bundle.putDouble("longitude5", 114.173614);
        bundle.putString("Q5", "City University of Hong Kong");


        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void area_cc() {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("zoom", 15);
        bundle.putInt("area", 3);

        bundle.putDouble("latitude1", 22.200278);
        bundle.putDouble("longitude1", 114.017542);
        bundle.putString("Q1", "Cheung Po Tsai Cave");

        bundle.putDouble("latitude2", 22.208530);
        bundle.putDouble("longitude2", 114.028061);
        bundle.putString("Q2", "Cheung Chau Public Pier");

        bundle.putDouble("latitude3", 22.201438);
        bundle.putDouble("longitude3", 114.018956);
        bundle.putString("Q3", "Sai Wan Tin Hau Temple");

        bundle.putDouble("latitude4", 22.212385);
        bundle.putDouble("longitude4", 114.027881);
        bundle.putString("Q4", "Pak Tai Temple");

        bundle.putDouble("latitude5", 22.205835);
        bundle.putDouble("longitude5", 114.027709);
        bundle.putString("Q5", "Kate Dessert");

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void area_st() {
        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("zoom", 13);
        bundle.putInt("area", 4);

        bundle.putDouble("latitude1", 22.421505);
        bundle.putDouble("longitude1", 114.207837);
        bundle.putString("Q1", "Chinese University of Hong Kong");

        bundle.putDouble("latitude2", 22.390711);
        bundle.putDouble("longitude2", 114.202264);
        bundle.putString("Q2", "Shing Mun River");

        bundle.putDouble("latitude3", 22.387616);
        bundle.putDouble("longitude3", 114.184768);
        bundle.putString("Q3", "Buddhas monastery");

        bundle.putDouble("latitude4", 22.400984);
        bundle.putDouble("longitude4", 114.205179);
        bundle.putString("Q4", "Sha Tin Racecourse");

        bundle.putDouble("latitude5", 22.377268);
        bundle.putDouble("longitude5", 114.185285);
        bundle.putString("Q5", "Hong Kong Heritage Museum");

        intent.putExtras(bundle);
        startActivity(intent);
    }


}