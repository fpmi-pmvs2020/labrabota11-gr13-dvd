package com.task.fbresult;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.task.fbresult.db.DBFillers;
import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.dialogs.AlertDialogBuilder;
import com.task.fbresult.model.Person;
import com.task.fbresult.service.AutoLoadingBroadcastReceiver;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.WebUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import lombok.var;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_CODE = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private String alertToken;


    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new Thread(() -> {
//            DBFillers.fillData();
//        }).start();

//        admin1@mail иванов в с
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() == null) {
            startSignInWindow();
        } else {
            configureScreen();
            configureAlertButton();
            tryRegisterUser();
        }
    }

    private void tryRegisterUser() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        WebUtils.registerUser(currentUser, handleToken, (e) -> {
            Log.d("web error", e.getMessage());
        });

    }
    Consumer<Map<String, String>> handleToken = (response) -> {
        String key = "key";
        if (response.containsKey(key)) {
            alertToken = response.get(key);
            startNotificationAlarm();
            configureAlertButton();
        }
    };

    private void configureAlertButton() {
        FloatingActionButton button = findViewById(R.id.alertFAB);
        button.setOnClickListener(this::alertButtonAction);
    }

    private void alertButtonAction(View view) {
        AlertDialog alertDialog = new AlertDialogBuilder(this)
                .build(getString(R.string.create_alert), null, () -> {
                }, alertToken);
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void startNotificationAlarm() {
        long time = System.currentTimeMillis() + 5000;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AutoLoadingBroadcastReceiver.class);
        intent.putExtra("token", alertToken);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pendingIntent);

        am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SIGN_IN_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (response == null)
                finish();
            else {
                configureScreen();
                tryRegisterUser();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configureScreen() {
        if (navigationView == null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_messages, R.id.nav_profile, R.id.nav_logout)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            auth.addAuthStateListener(param -> {
                if (auth.getCurrentUser() != null)
                    configureMenuOf(navigationView);
            });
        }
    }

    private void startSignInWindow() {
        List<AuthUI.IdpConfig> providers = new ArrayList<>();
        providers.add(new AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(false).build());
        startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                SIGN_IN_CODE);
    }

    private void configureMenuOf(NavigationView navigationView) {
        View headerView = navigationView.getHeaderView(0);
        TextView tvPersonTitle = headerView.findViewById(R.id.tvPersonTitle);
        tvPersonTitle.setText(auth.getCurrentUser().getDisplayName());
        TextView tvEmailTitle = headerView.findViewById(R.id.tvEmailTitle);
        tvEmailTitle.setText(auth.getCurrentUser().getEmail());

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_logout);
        menuItem.setOnMenuItemClickListener(item -> {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> {
                        //startSignInWindow();
                        finishAndRemoveTask();
                    });
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