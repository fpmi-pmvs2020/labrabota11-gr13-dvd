package com.task.fbresult;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_CODE = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startSignInWindow();
        }else{
            configureScreen();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if(response == null)
            finish();
        else{
            configureScreen();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configureScreen(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        auth.addAuthStateListener(param->{
            if(auth.getCurrentUser()!=null)
                configureMenuOf(navigationView);
        });
    }

    private void startSignInWindow() {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        //providers.add(new AuthUI.IdpConfig.PhoneBuilder().build());
        providers.add(new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                SIGN_IN_CODE);
    }

    private void configureMenuOf(NavigationView navigationView){
        View headerView = navigationView.getHeaderView(0);
        TextView tvPersonTitle = headerView.findViewById(R.id.tvPersonTitle);
        tvPersonTitle.setText(auth.getCurrentUser().getDisplayName());
        TextView tvEmailTitle = headerView.findViewById(R.id.tvEmailTitle);
        tvEmailTitle.setText(auth.getCurrentUser().getEmail());

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_logout);
        menuItem.setOnMenuItemClickListener(item -> {
            auth.signOut();
            startSignInWindow();
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}