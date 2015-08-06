package com.example.yoyohongkong;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {

    ImageButton button_start;
    ImageButton button_rule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        button_start = (ImageButton) findViewById(R.id.start);
        button_rule = (ImageButton) findViewById(R.id.rule);
        button_start.setOnClickListener(new mylistener());
        button_rule.setOnClickListener(new mylistener());

    }

    private class mylistener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.start:
                    gotomap();
                    break;
                case R.id.rule:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("How to play")
                            .setMessage(R.string.dialog_message).show();
                    break;

            }

        }
    }

    private void gotomap() {
        Intent intent = new Intent(this, Bigmap.class);
        startActivity(intent);
    }

    ;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
