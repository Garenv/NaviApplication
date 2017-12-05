package com.mancj.example;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.sql.DatabaseMetaData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity{

    public void jumpToMap(View view)
    {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

}
