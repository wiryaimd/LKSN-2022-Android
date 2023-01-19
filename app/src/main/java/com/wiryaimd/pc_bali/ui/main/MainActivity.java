package com.wiryaimd.pc_bali.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.wiryaimd.pc_bali.BaseApplication;
import com.wiryaimd.pc_bali.R;
import com.wiryaimd.pc_bali.ui.main.fragment.CartFragment;
import com.wiryaimd.pc_bali.ui.main.fragment.MenuFragment;
import com.wiryaimd.pc_bali.ui.main.fragment.OrdersFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView botnav;

    private BaseApplication baseApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        botnav = findViewById(R.id.main_botnav);

        baseApplication = (BaseApplication) getApplication();

        toolbar.setTitle("Esemka Restaurant - Table " + baseApplication.getTableCodeModel().getNumber());

        botnav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.mainmenu_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.mainmenu_orders:
                        fragment = new OrdersFragment();
                        break;
                    default:
                        fragment = new MenuFragment();
                        break;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, fragment);
                ft.commit();
                return true;
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MenuFragment());
        ft.commit();
    }
}