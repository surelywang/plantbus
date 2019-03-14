package com.example.surelywang.plantbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import static android.support.design.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Fragment fragment;
    //private FragmentManager fm;
    private FragmentManager fragmentManager;

    // Fragments
    final Fragment frameGallery = new GalleryFragment();
    final Fragment frameMove = new MoveFragment();
    final Fragment framePlant = new PlantFragment();
    final Fragment frameSettings = new SettingsFragment();
    Fragment active = frameGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*fm.beginTransaction().add(R.id.main_container, frameSettings, "4").hide(frameSettings).commit();
        fm.beginTransaction().add(R.id.main_container, framePlant, "3").hide(framePlant).commit();
        fm.beginTransaction().add(R.id.main_container, frameMove, "2").hide(frameMove).commit();
        fm.beginTransaction().add(R.id.main_container, frameGallery, "1").commit();
*/

        setupNavigationView();
    }

    private void setupNavigationView() {
        navigation = findViewById(R.id.bottom_navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.inflateMenu(R.menu.navigation);
        fragmentManager = getSupportFragmentManager();

        //Set Add Menu to be selected by default
        Menu menu = navigation.getMenu();
        menu.getItem(0).setChecked(true);
        fragment = new GalleryFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment).commit();


        // Remove labels and animation
        navigation.setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
        navigation.setItemHorizontalTranslationEnabled(false);
        navigation.setItemIconTintList(getResources().getColorStateList(R.color.colorNaviBottom));

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.gallery:
                        fragment = new GalleryFragment();
                        break;
                    case R.id.move:
                        fragment = new MoveFragment();
                        break;
                        //return true;
                    case R.id.plants:
                        fragment = new PlantFragment();
                        break;
                    case R.id.settings:
                        fragment = new SettingsFragment();
                        break;
                }
                //return false;

                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.rootLayout, fragment).commit();
                return true;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
