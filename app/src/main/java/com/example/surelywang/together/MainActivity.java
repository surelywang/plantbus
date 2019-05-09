package com.example.surelywang.together;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

//import com.example.surelywang.plantbus.R;

import static android.support.design.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Fragment fragment;
    //private FragmentManager fm;
    private FragmentManager fragmentManager;

    // Fragments
//    final Fragment frameGallery = new HomeFragment();
//    final Fragment frameMove = new TimeFragment();
//    final Fragment framePlant = new SummaryFragment();
//    final Fragment frameSettings = new SettingsFragment();
//    Fragment active = frameGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigationView();
    }

    private void setupNavigationView() {
        navigation = findViewById(R.id.bottom_navigation);
        navigation.inflateMenu(R.menu.navigation);
        fragmentManager = getSupportFragmentManager();

        //Set Add Menu to be selected by default
        Menu menu = navigation.getMenu();
        menu.getItem(0).setChecked(true);
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.rootLayout, fragment).commit();


        // Remove labels and animation
        navigation.setLabelVisibilityMode(LABEL_VISIBILITY_UNLABELED);
        navigation.setItemHorizontalTranslationEnabled(false);
        navigation.setItemIconTintList(getResources().getColorStateList(R.color.colorNaviBottom));
        //removeTextLabel();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.time:
                        fragment = new TimeFragment();
                        break;
                    case R.id.report:
                        fragment = new SummaryFragment();
                        break;
                    case R.id.network:
                        fragment = new NetworkFragment();
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

    private void removeTextLabel() {
        if (navigation == null) return;
        if (navigation instanceof MenuView.ItemView) {
            ViewGroup viewGroup = (ViewGroup) navigation;
            int padding = 0;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View v = viewGroup.getChildAt(i);
                if (v instanceof ViewGroup) {
                    padding = v.getHeight();
                    viewGroup.removeViewAt(i);
                }
            }
            viewGroup.setPadding(navigation.getPaddingLeft(), (viewGroup.getPaddingTop() + padding) / 2, navigation.getPaddingRight(), navigation.getPaddingBottom());
        }
    }

}
