package com.aidanbunch.arrosocial.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.aidanbunch.arrosocial.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class CentralActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavBar;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_central);

        setUpActionBar();

        chipNavBar = findViewById(R.id.chipNav);

        chipNavBar.setItemSelected(R.id.homeNav, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.chipContainer, new HomeFragment()).commit();

        chipNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch(i) {
                    case R.id.homeNav:
                        fragment = new HomeFragment();
                        break;
                    case R.id.msgsNav:
                        fragment = new MessagesFragment();
                        break;
                    case R.id.searchNav:
                        fragment = new SearchFragment();
                        break;
                    case R.id.settingsNav:
                        fragment = new SettingsFragment();
                        break;
                }
                if(fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.chipContainer, fragment).commit();
                }
            }
        });

    }

    public void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}