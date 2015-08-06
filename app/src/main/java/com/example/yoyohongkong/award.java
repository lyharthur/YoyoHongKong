package com.example.yoyohongkong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class award extends Activity {

    private int q1_flag, q2_flag, q3_flag, q4_flag, q5_flag;
    private ImageView a, b, c, d;
    private Button share;
    private File imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award);
        initView();
        checkImage();

    }

    private void initView() {
        a = (ImageView) findViewById(R.id.imageview1);
        b = (ImageView) findViewById(R.id.imageview2);
        c = (ImageView) findViewById(R.id.imageview3);
        d = (ImageView) findViewById(R.id.imageview4);
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(new mylistener());
    }

    private class mylistener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.share:
                    publishStory();
                    break;


            }

        }
    }

    private void checkImage() {
        loadPreference("1");
        if (q1_flag == 1 && q2_flag == 1 && q3_flag == 1 && q4_flag == 1 && q5_flag == 1)
            a.setImageResource(R.drawable.bread1);
        else
            a.setImageResource(R.drawable.none1);

        loadPreference("2");
        if (q1_flag == 1 && q2_flag == 1 && q3_flag == 1 && q4_flag == 1 && q5_flag == 1)
            b.setImageResource(R.drawable.bread2);
        else
            b.setImageResource(R.drawable.none2);

        loadPreference("3");
        if (q1_flag == 1 && q2_flag == 1 && q3_flag == 1 && q4_flag == 1 && q5_flag == 1)
            c.setImageResource(R.drawable.bread3);
        else
            c.setImageResource(R.drawable.none3);

        loadPreference("4");
        if (q1_flag == 1 && q2_flag == 1 && q3_flag == 1 && q4_flag == 1 && q5_flag == 1)
            d.setImageResource(R.drawable.bread4);
        else
            d.setImageResource(R.drawable.none4);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_award, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadPreference(String index) {
        SharedPreferences preferences = getSharedPreferences(index, MODE_PRIVATE);
        q1_flag = preferences.getInt("Q1", 0);
        q2_flag = preferences.getInt("Q2", 0);
        q3_flag = preferences.getInt("Q3", 0);
        q4_flag = preferences.getInt("Q4", 0);
        q5_flag = preferences.getInt("Q5", 0);

    }

    public Bitmap takeScreenshot() {

        View rootView = findViewById(android.R.id.content).getRootView();

        rootView.setDrawingCacheEnabled(true);

        return rootView.getDrawingCache();

    }

    public String saveBitmap(Bitmap bitmap) {


        imagePath = new File(Environment.getExternalStorageDirectory().getPath() + "/myPic/screenshot.png");
        if (!imagePath.exists()){
            imagePath.mkdir();
        }
        //Toast.makeText(award.this, Environment.getExternalStorageDirectory().getPath() + "/myPic/screenshot.png", Toast.LENGTH_SHORT).show();

        Uri screenshotUri = Uri.fromFile(imagePath);
        //Toast.makeText(award.this, String.valueOf(screenshotUri), Toast.LENGTH_SHORT).show();

        FileOutputStream fos;

        try {

            fos = new FileOutputStream(imagePath);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();

            fos.close();

        } catch (FileNotFoundException e) {

            Log.e("GREC", e.getMessage(), e);

        } catch (IOException e) {

            Log.e("GREC", e.getMessage(), e);

        }


        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("image/png");

        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);

        startActivity(Intent.createChooser(intent, "Share"));


        return imagePath.getPath();

    }

    public void publishStory() {

        Bitmap screenshot = takeScreenshot();

        String path = saveBitmap(screenshot);


    }

}
